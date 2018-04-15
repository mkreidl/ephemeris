package com.mkreidl.ephemeris.astrolabe;

import com.mkreidl.ephemeris.Distance;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Circle;
import com.mkreidl.ephemeris.sky.Ephemerides;
import com.mkreidl.ephemeris.sky.SolarSystem;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;


public class Planets extends AbstractPart
{
    private final Cartesian onUnitSphere = new Cartesian();
    private final Equatorial.Sphe topocentric = new Equatorial.Sphe();
    private final Ephemerides ephemerides = new Ephemerides();
    private final SolarSystem solarSystem = new SolarSystem();

    private final Map<SolarSystem.Body, String> planetNames = new EnumMap<>( SolarSystem.Body.class );
    private final Map<SolarSystem.Body, Circle> apparentDisks = new EnumMap<>( SolarSystem.Body.class );
    private final Map<SolarSystem.Body, Cartesian> projectedPositions = new EnumMap<>( SolarSystem.Body.class );

    public List<SolarSystem.Body> sortedByDistance;

    Planets( Astrolabe astrolabe )
    {
        super( astrolabe );
        for ( SolarSystem.Body body : SolarSystem.Body.values() )
        {
            projectedPositions.put( body, new Cartesian() );
            apparentDisks.put( body, new Circle() );
            planetNames.put( body, body.toString() );
        }
    }

    public void setPlanetNames( Map<SolarSystem.Body, String> planetNames )
    {
        this.planetNames.putAll( planetNames );
    }

    public String getName( SolarSystem.Body planet )
    {
        return planetNames.get( planet );
    }

    public Circle getApparentDisk( SolarSystem.Body object )
    {
        return apparentDisks.get( object );
    }

    Cartesian getPosition( SolarSystem.Body object )
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
        ephemerides.setTimeLocation( astrolabe.time, astrolabe.geographicLocation );
    }

    @Override
    protected void onRecomputeProjection()
    {
        onChangeViewParameters();
        for ( SolarSystem.Body object : SolarSystem.Body.values() )
            recompute( object );
        sortedByDistance = solarSystem.getSortedByDistanceDescending();
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
        solarSystem.getEphemerides( object, ephemerides );
        ephemerides.get( topocentric, Ephemerides.CoordinatesCenter.TOPOCENTRIC ).transform( onUnitSphere ).normalize();
        astrolabe.project( onUnitSphere, projectedPositions.get( object ) );
        final double apparentRadius = object.RADIUS_EQUATORIAL_M / topocentric.distance( Distance.m );
        astrolabe.project( topocentric, apparentRadius, apparentDisks.get( object ) );
    }

    public List<SolarSystem.Body> getSortedByDistance()
    {
        return sortedByDistance;
    }
}
