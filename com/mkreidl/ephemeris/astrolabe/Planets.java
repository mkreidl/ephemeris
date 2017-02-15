package com.mkreidl.ephemeris.astrolabe;

import org.eclipse.jdt.annotation.NonNull;

import com.mkreidl.ephemeris.*;
import com.mkreidl.ephemeris.geometry.*;
import com.mkreidl.ephemeris.sky.*;
import com.mkreidl.ephemeris.sky.coordinates.*;

import java.util.*;


public class Planets extends AbstractPart
{
    private final Cartesian onUnitSphere = new Cartesian();
    private final Equatorial.Sphe topocentric = new Equatorial.Sphe();
    private final Ephemerides ephem1 = new Ephemerides();
    private final Ephemerides ephem2 = new Ephemerides();

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
    }

    @Override
    protected void onChangeObserverParams()
    {
        ephem1.setTimeLocation( astrolabe.time, astrolabe.geographicLocation );
        ephem2.setTimeLocation( astrolabe.time, astrolabe.geographicLocation );
    }

    @Override
    protected void recalculate()
    {
        onChangeObserverParams();
        for ( SolarSystem.Body object : SolarSystem.Body.values() )
            recalculate( object );
        Collections.sort( sortedByDistance, distanceComparator );
    }

    public void synchronize( SolarSystem.Body body )
    {
        solarSystem.calculate( astrolabe.time, body );
        recalculate( body );
        onRecalculateListener.onRecalculate();
    }

    void recalculate( SolarSystem.Body object )
    {
        solarSystem.getEphemerides( object, ephem1 );
        ephem1.getTopocentric( topocentric ).transform( onUnitSphere ).normalize();
        astrolabe.camera.project( onUnitSphere, projectedPositions.get( object ) );
        final double apparentRadius = object.RADIUS_EQUATORIAL / topocentric.distance( Distance.m );
        astrolabe.camera.project( topocentric, apparentRadius, apparentDisks.get( object ) );
    }

    private final Comparator<SolarSystem.Body> distanceComparator = new Comparator<SolarSystem.Body>()
    {
        @Override
        public int compare( SolarSystem.Body o1, SolarSystem.Body o2 )
        {
            // Sort by decreasing distance if view is INNER,
            // (rationale: closest objects should be drawn on top = last)
            // Sort by increasing distance if view is OUTER,
            // (rationale: closest objects should be drawn first)
            solarSystem.getEphemerides( o1, ephem1 );
            solarSystem.getEphemerides( o2, ephem2 );
            final double d1 = ephem1.getTopocentric( topocentric ).distance( Distance.m );
            final double d2 = ephem2.getTopocentric( topocentric ).distance( Distance.m );
            return Double.compare( d2, d1 );
        }
    };
}
