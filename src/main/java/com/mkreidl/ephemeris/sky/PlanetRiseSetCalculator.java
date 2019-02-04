package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.Distance;
import com.mkreidl.ephemeris.Position;
import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.solarsystem.Body;
import com.mkreidl.ephemeris.solarsystem.SolarSystem;

public class PlanetRiseSetCalculator extends RiseSetCalculator
{
    private static final int MAX_ITERATION = 5;

    private final SolarSystem solarSystem;
    private final Body body;
    private final Position position = new Position();

    private long timeMillisPrevious;

    private Boolean wasVisibleBefore;
    private Boolean isVisibleNow;
    private boolean isCrossing;
    private long precisionMs = 1000;

    public static PlanetRiseSetCalculator of( SolarSystem solarSystem, Body body )
    {
        return new PlanetRiseSetCalculator( solarSystem, body );
    }

    private PlanetRiseSetCalculator( SolarSystem solarSystem, Body body )
    {
        this.solarSystem = solarSystem;
        this.body = body;
    }
    
    public void setPrecision( long precisionMs )
    {
      this.precisionMs = precisionMs;
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
            if ( !isCrossing || !adjustTime() )
                if ( mode == EventType.RISE && hasAppeared() || mode == EventType.SET && hasVanished() )
                    time.setTime( ( time.getTime() + timeMillisPrevious ) / 2 );
                else
                    return compute( searchOrbitCrossingHorizon() );
            if ( Math.abs( time.getTime() - timeMillisPrevious ) < precisionMs )
                return true;
        }
        return false;
    }

    private long searchOrbitCrossingHorizon()
    {
        long searchIncrement = 30 * Time.MILLIS_PER_SIDEREAL_DAY * ( lookupDirection == LookupDirection.FORWARD ? 1 : -1 );
        while ( !isCrossing )
        {
            timeMillisPrevious = time.getTime();
            time.addMillis( searchIncrement );
            computeTopocentricPosition();
        }
        while ( Math.abs( searchIncrement ) > Time.MILLIS_PER_HOUR )
        {
            if ( !isCrossing )
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
        solarSystem.compute( time, Body.EARTH );
        solarSystem.compute( time, body );
        solarSystem.getEphemerides( body, position );
        position.setTimeLocation( time, geographicLocation );
        position.get( topocentric, Position.CoordinatesCenter.TOPOCENTRIC );
        final double apparentRadius = body.getRADIUS_MEAN_M() / topocentric.distance( Distance.m );
        virtualHorizonDeg = OPTICAL_HORIZON_DEG - Math.toDegrees( apparentRadius );
        isVisibleNow = topocentric.lat >= virtualHorizonDeg;
        isCrossing = isCrossing();
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