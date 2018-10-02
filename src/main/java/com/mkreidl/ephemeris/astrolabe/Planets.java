package com.mkreidl.ephemeris.astrolabe;

import com.mkreidl.ephemeris.Distance;
import com.mkreidl.ephemeris.Position;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Circle;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;
import com.mkreidl.ephemeris.solarsystem.Body;
import com.mkreidl.ephemeris.solarsystem.SolarSystem;
import com.mkreidl.ephemeris.solarsystem.SolarSystemMeeus;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;


public class Planets extends AbstractPart
{
    private final Cartesian onUnitSphere = new Cartesian();
    private final Equatorial.Sphe topocentric = new Equatorial.Sphe();
    private final Position position = new Position();
    private final SolarSystem solarSystem = new SolarSystemMeeus();

    private final Map<Body, Circle> apparentDisks = new EnumMap<>( Body.class );
    private final Map<Body, Cartesian> projectedPositions = new EnumMap<>( Body.class );

    public List<Body> sortedByDistance;

    Planets( Astrolabe astrolabe )
    {
        super( astrolabe );
        for ( Body body : Body.values() )
        {
            projectedPositions.put( body, new Cartesian() );
            apparentDisks.put( body, new Circle() );
        }
    }

    public Circle getApparentDisk( Body object )
    {
        return apparentDisks.get( object );
    }

    Cartesian getPosition( Body object )
    {
        return projectedPositions.get( object );
    }

    @Override
    protected void onSynchronize()
    {
        solarSystem.computeSync( astrolabe.time );
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
        for ( Body object : Body.values() )
            recompute( object );
        sortedByDistance = solarSystem.getSortedByDistanceDescending();
        //CHECK? onRecomputeListener.onRecomputeProjection();
    }

    void synchronize( Body body )
    {
        solarSystem.compute( astrolabe.time, body );
        recompute( body );
        onRecomputeListener.onRecompute();
    }

    private void recompute( Body object )
    {
        solarSystem.getEphemerides( object, position );
        position.get( topocentric, Position.CoordinatesCenter.TOPOCENTRIC ).transform( onUnitSphere ).normalize();
        astrolabe.project( onUnitSphere, projectedPositions.get( object ) );
        final double apparentRadius = object.RADIUS_EQUATORIAL_M / topocentric.distance( Distance.m );
        astrolabe.project( topocentric, apparentRadius, apparentDisks.get( object ) );
    }

    public List<Body> getSortedByDistance()
    {
        return sortedByDistance;
    }
}
