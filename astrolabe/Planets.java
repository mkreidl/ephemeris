package com.mkreidl.ephemeris.astrolabe;

import android.support.annotation.NonNull;

import com.mkreidl.ephemeris.Distance;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Circle;
import com.mkreidl.ephemeris.sky.Ephemerides;
import com.mkreidl.ephemeris.sky.SolarSystem;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;


public class Planets extends AbstractPart
{
    private final Cartesian onUnitSphere = new Cartesian();
    private final Equatorial.Sphe topocentric = new Equatorial.Sphe();
    private final Ephemerides ephemerides = new Ephemerides();

    private final EnumMap<SolarSystem.Body, Circle> apparentDisks = new EnumMap<>( SolarSystem.Body.class );
    private final EnumMap<SolarSystem.Body, Cartesian> projectedPositions = new EnumMap<>( SolarSystem.Body.class );

    public final SolarSystem solarSystem = new SolarSystem();
    public final List<SolarSystem.Body> sortedByDistance = new ArrayList<>();

    public Planets( @NonNull Astrolabe astrolabe )
    {
        super( astrolabe );
        for ( SolarSystem.Body body : SolarSystem.Body.values() )
        {
            if ( body != SolarSystem.Body.EARTH )  // don't draw earth (coordinates has no meaning on projection)
                sortedByDistance.add( body );
            projectedPositions.put( body, new Cartesian() );
            apparentDisks.put( body, new Circle() );
        }
    }

    Ephemerides getEphemerides( final SolarSystem.Body object, final Ephemerides output )
    {
        return solarSystem.getEphemerides( object, output );
    }

    public Circle getApparentDisk( SolarSystem.Body object )
    {
        return apparentDisks.get( object );
    }

    public Cartesian getPosition( SolarSystem.Body object )
    {
        return projectedPositions.get( object );
    }

    @Override
    protected void onSynchronize()
    {
        solarSystem.calculate( astrolabe.time );
        ephemerides.setTimeLocation( astrolabe.time, astrolabe.geographicLocation );
    }

    @Override
    protected void onChangeObserverParams()
    {
        ephemerides.setTimeLocation( astrolabe.time, astrolabe.geographicLocation );
    }

    @Override
    protected void recalculate()
    {
        for ( SolarSystem.Body object : SolarSystem.Body.values() )
        {
            solarSystem.getEphemerides( object, ephemerides );
            ephemerides.getTopocentric( topocentric ).transform( onUnitSphere ).normalize();
            astrolabe.camera.project( onUnitSphere, projectedPositions.get( object ) );
            final double apparentRadius = object.RADIUS_EQUATORIAL / topocentric.distance( Distance.m );
            astrolabe.camera.project( topocentric, apparentRadius, apparentDisks.get( object ) );
        }
        Collections.sort( sortedByDistance, distanceComparator );
    }

    private final Comparator<SolarSystem.Body> distanceComparator = new Comparator<SolarSystem.Body>()
    {
        private final Ephemerides ephem1 = new Ephemerides();
        private final Ephemerides ephem2 = new Ephemerides();

        @Override
        public int compare( SolarSystem.Body o1, SolarSystem.Body o2 )
        {
            // Sort by decreasing distance if view is INNER,
            // (rationale: closest objects should be drawn on top = last)
            // Sort by increasing distance if view is OUTER,
            // (rationale: closest objects should be drawn first)
            ephem1.setTimeLocation( astrolabe.time, astrolabe.geographicLocation );
            ephem2.setTimeLocation( astrolabe.time, astrolabe.geographicLocation );
            solarSystem.getEphemerides( o1, ephem1 );
            solarSystem.getEphemerides( o2, ephem2 );
            final double d1 = ephem1.getTopocentric( topocentric ).distance( Distance.m );
            final double d2 = ephem2.getTopocentric( topocentric ).distance( Distance.m );
            return Double.compare( d2, d1 );
        }
    };
}
