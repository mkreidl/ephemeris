package com.mkreidl.ephemeris.astrolabe;

import com.mkreidl.ephemeris.Distance;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Circle;
import com.mkreidl.ephemeris.sky.Position;
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
    private final Position position = new Position();

    private final EnumMap<SolarSystem.Body, Circle> apparentDisks = new EnumMap<>( SolarSystem.Body.class );
    private final EnumMap<SolarSystem.Body, Cartesian> projectedPositions = new EnumMap<>( SolarSystem.Body.class );

    final SolarSystem solarSystem = new SolarSystem();
    public final List<SolarSystem.Body> sortedByDistance = new ArrayList<>();

    public Planets( Astrolabe astrolabe )
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
        solarSystem.compute( astrolabe.time );
    }

    @Override
    protected void onChangeViewParameters()
    {
        position.setTimeLocation( astrolabe.time, astrolabe.geographicLocation );
    }

    @Override
    protected void onRecomputeProjection()
    {
        onChangeViewParameters();
        // Arrays.stream( SolarSystem.Body.values() ).forEach( this::recompute );
        for ( SolarSystem.Body object : SolarSystem.Body.values() )
            recompute( object );
        Collections.sort( sortedByDistance, distanceComparator );
        //CHECK? onRecomputeListener.onRecomputeProjection();
    }

    void synchronize( SolarSystem.Body body )
    {
        solarSystem.compute( astrolabe.time, body );
        recompute( body );
        onRecomputeListener.onRecompute();
    }

    private void recompute( SolarSystem.Body object )
    {
        solarSystem.getEphemerides( object, position );
        position.get( topocentric, Position.CoordinatesCenter.TOPOCENTRIC ).transform( onUnitSphere ).normalize();
        astrolabe.project( onUnitSphere, projectedPositions.get( object ) );
        final double apparentRadius = object.RADIUS_EQUATORIAL / topocentric.distance( Distance.m );
        astrolabe.project( topocentric, apparentRadius, apparentDisks.get( object ) );
    }

    private final Comparator<SolarSystem.Body> distanceComparator = ( o1, o2 ) ->
    {
        solarSystem.getEphemerides( o1, position );
        final double d1 = position.get( topocentric, Position.CoordinatesCenter.TOPOCENTRIC ).distance( Distance.m );
        solarSystem.getEphemerides( o2, position );
        final double d2 = position.get( topocentric, Position.CoordinatesCenter.TOPOCENTRIC ).distance( Distance.m );
        return Double.compare( d2, d1 );
    };
}
