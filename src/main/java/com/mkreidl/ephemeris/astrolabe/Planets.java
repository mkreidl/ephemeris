package com.mkreidl.ephemeris.astrolabe;

import com.mkreidl.ephemeris.Distance;
import com.mkreidl.ephemeris.Position;
import com.mkreidl.ephemeris.geometry.Angle;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Circle;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;
import com.mkreidl.ephemeris.solarsystem.Body;
import com.mkreidl.ephemeris.solarsystem.SolarSystem;
import com.mkreidl.ephemeris.solarsystem.SolarSystemMeeus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;


public class Planets extends AbstractPart
{
    private final Cartesian onUnitSphere = new Cartesian();
    private final Position position = new Position();
    private final SolarSystem solarSystem = new SolarSystemMeeus();

    private final Map<Body, Circle> apparentDisks = new EnumMap<>( Body.class );
    private final Map<Body, Equatorial.Sphe> topocentricPositions = new EnumMap<>( Body.class );
    private final Map<Body, Cartesian> projectedPositions = new EnumMap<>( Body.class );
    public final Map<Body, Cartesian> illuminationDirection = new EnumMap<>( Body.class );

    public List<Body> sortedByDistance = new ArrayList<>( Body.EXTRA_TERRESTRIAL );

    Planets( Astrolabe astrolabe )
    {
        super( astrolabe );
        for ( Body body : Body.values() )
        {
            projectedPositions.put( body, new Cartesian() );
            topocentricPositions.put( body, new Equatorial.Sphe() );
            apparentDisks.put( body, new Circle() );
            illuminationDirection.put( body, new Cartesian() );
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
        for ( Body object : Body.values() )
            recompute( object );
        Collections.sort( sortedByDistance, ( o1, o2 ) ->
                Double.compare( solarSystem.getGeocentricDistance( o2 ),
                        solarSystem.getGeocentricDistance( o1 ) ) );
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
        final Equatorial.Sphe topocentric = topocentricPositions.get( object );
        solarSystem.getEphemerides( object, position );
        position.get( topocentric, Position.CoordinatesCenter.TOPOCENTRIC ).transform( onUnitSphere ).normalize();
        astrolabe.project( onUnitSphere, projectedPositions.get( object ) );
        final double apparentRadius = object.RADIUS_EQUATORIAL_M / topocentric.distance( Distance.m );
        astrolabe.project( topocentric, apparentRadius, apparentDisks.get( object ) );
        final Cartesian illuminationProjected = position.illuminationDirection();
        astrolabe.applyTangentialMap( onUnitSphere, illuminationProjected );
        illuminationProjected.z = position.getPhase( new Angle() ).getRadians();
        illuminationDirection.put( object, illuminationProjected );
    }

    public List<Body> getSortedByDistance()
    {
        return sortedByDistance;
    }

    double getDeclination( Body planet )
    {
        return topocentricPositions.get( planet ).lat;
    }
}
