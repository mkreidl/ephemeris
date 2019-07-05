package com.mkreidl.ephemeris.astrolabe;

import com.mkreidl.astro.solarsystem.SolarSystem;
import com.mkreidl.astro.time.Instant;
import com.mkreidl.ephemeris.Distance;
import com.mkreidl.ephemeris.Position;
import com.mkreidl.ephemeris.geometry.Angle;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Circle;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;
import com.mkreidl.ephemeris.solarsystem.Body;
import com.mkreidl.math.PhaseCartesian;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;


public class Planets extends AbstractPart
{
    private final Cartesian onUnitSphere = new Cartesian();
    private final Position position = new Position();
    private final SolarSystem solarSystem = SolarSystem.Companion.createFromMeeus();

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
        final Instant instant = Instant.ofEpochMilli( astrolabe.time.getTime() );
        solarSystem.compute( instant );
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
                Double.compare( solarSystem.getGeocentricDistance( bodyAdapter( o2 ) ),
                        solarSystem.getGeocentricDistance( bodyAdapter( o1 ) ) ) );
        //CHECK? onRecomputeListener.onRecomputeProjection();
    }

    void synchronize( Body body )
    {
        final Instant instant = Instant.ofEpochMilli( astrolabe.time.getTime() );
        solarSystem.computeSingle( instant, bodyAdapter( body ) );
        recompute( body );
        onRecomputeListener.onRecompute();
    }

    private static com.mkreidl.astro.solarsystem.Body bodyAdapter( Body body )
    {
        switch ( body )
        {
            case SUN:
                return com.mkreidl.astro.solarsystem.Body.SUN;
            case MOON:
                return com.mkreidl.astro.solarsystem.Body.MOON;
            case MERCURY:
                return com.mkreidl.astro.solarsystem.Body.MERCURY;
            case VENUS:
                return com.mkreidl.astro.solarsystem.Body.VENUS;
            case EARTH:
                return com.mkreidl.astro.solarsystem.Body.EARTH;
            case MARS:
                return com.mkreidl.astro.solarsystem.Body.MARS;
            case JUPITER:
                return com.mkreidl.astro.solarsystem.Body.JUPITER;
            case SATURN:
                return com.mkreidl.astro.solarsystem.Body.SATURN;
            case URANUS:
                return com.mkreidl.astro.solarsystem.Body.URANUS;
            case NEPTUNE:
                return com.mkreidl.astro.solarsystem.Body.NEPTUNE;
            case PLUTO:
                return com.mkreidl.astro.solarsystem.Body.PLUTO;
            default:
                return null;
        }
    }

    private void recompute( Body object )
    {
        final Equatorial.Sphe topocentric = topocentricPositions.get( object );
        getEphemerides( object, position );
        position.get( topocentric, Position.CoordinatesCenter.TOPOCENTRIC ).transform( onUnitSphere ).normalize();
        astrolabe.project( onUnitSphere, projectedPositions.get( object ) );
        final double apparentRadius = object.RADIUS_EQUATORIAL_M / topocentric.distance( Distance.m );
        astrolabe.project( topocentric, apparentRadius, apparentDisks.get( object ) );
        final Cartesian illuminationProjected = position.illuminationDirection();
        astrolabe.applyTangentialMap( onUnitSphere, illuminationProjected );
        illuminationProjected.z = position.getPhase( new Angle() ).getRadians();
        illuminationDirection.put( object, illuminationProjected );
    }

    private void getEphemerides( final Body body, final Position position )
    {
        final PhaseCartesian phaseBody = solarSystem.getTrueEclipticalGeocentric( bodyAdapter( body ) );
        final PhaseCartesian phaseEarth = solarSystem.getTrueEclipticalHeliocentric( com.mkreidl.astro.solarsystem.Body.EARTH );
        position.setGeocentricPhase( phaseBody, phaseEarth );
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
