package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.Distance;
import com.mkreidl.ephemeris.Time;

public class PlanetRiseSetCalculator extends RiseSetCalculator
{
    private static final long PRECISION = 30000;
    private static final int MAX_ITERATION = 5;

    private final SolarSystem solarSystem;
    private final SolarSystem.Body body;
    private final Position position = new Position();

    private long timeMillisPrevious;

    private Boolean wasVisibleBefore;
    private Boolean isVisibleNow;
    private boolean isCrossingHorizon;

    public static RiseSetCalculator of( SolarSystem solarSystem, SolarSystem.Body body )
    {
        return new PlanetRiseSetCalculator( solarSystem, body );
    }

    private PlanetRiseSetCalculator( SolarSystem solarSystem, SolarSystem.Body body )
    {
        this.solarSystem = solarSystem;
        this.body = body;
    }

    @Override
    public boolean compute( long timeMillisStart )
    {
        super.setStartTime( timeMillisStart );
        isVisibleNow = null;
        for ( int n = 0; n < MAX_ITERATION; ++n )
        {
            timeMillisPrevious = time.getTime();
            computeTopocentricPosition();
            if ( !isCrossingHorizon || !adjustTimeToCrossingHorizon() )
                if ( mode == EventType.RISE && hasAppeared() || mode == EventType.SET && hasVanished() )
                    time.setTime( ( time.getTime() + timeMillisPrevious ) / 2 );
                else
                    return compute( searchOrbitCrossingHorizon() );
            if ( Math.abs( time.getTime() - timeMillisPrevious ) < PRECISION )
                return true;
        }
        return false;
    }

    @Override
    boolean adjustTimeToCrossingHorizon()
    {
        updateHorizon();
        return super.adjustTimeToCrossingHorizon();
    }

    private long searchOrbitCrossingHorizon()
    {
        long searchIncrement = 30 * Time.MILLIS_PER_SIDEREAL_DAY * ( lookupDirection == LookupDirection.FORWARD ? 1 : -1 );
        while ( !isCrossingHorizon )
        {
            timeMillisPrevious = time.getTime();
            time.addMillis( searchIncrement );
            computeTopocentricPosition();
        }
        while ( Math.abs( searchIncrement ) > Time.MILLIS_PER_HOUR )
        {
            if ( !isCrossingHorizon )
                timeMillisPrevious = time.getTime();
            searchIncrement /= 2;
            time.setTime( timeMillisPrevious + searchIncrement );
            computeTopocentricPosition();
        }
        return time.getTime();
    }

    private void computeTopocentricPosition()
    {
        wasVisibleBefore = isVisibleNow;
        solarSystem.compute( time, SolarSystem.Body.EARTH );
        solarSystem.compute( time, body );
        solarSystem.getEphemerides( body, position );
        position.setTimeLocation( time, geographicLocation );
        position.get( topocentric, Position.CoordinatesCenter.TOPOCENTRIC );
        final double apparentRadius = body.RADIUS_MEAN_M / topocentric.distance( Distance.m );
        virtualHorizonDeg = OPTICAL_HORIZON_DEG - Math.toDegrees( apparentRadius );
        isVisibleNow = topocentric.lat >= virtualHorizonDeg;
        isCrossingHorizon = isCrossingHorizon();
    }

    private boolean hasAppeared()
    {
        return wasVisibleBefore != null && isVisibleNow && !wasVisibleBefore;
    }

    private boolean hasVanished()
    {
        return wasVisibleBefore != null && !isVisibleNow && wasVisibleBefore;
    }
}