package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Angle;
import com.mkreidl.ephemeris.geometry.Circle;
import com.mkreidl.ephemeris.geometry.Spherical;
import com.mkreidl.ephemeris.geometry.Stereographic;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;

public abstract class RiseSetCalculator
{
    private static final int MAX_ITERATION = 1000;
    private static final double RAD_TO_SIDEREAL_MILLIS = Time.MILLIS_PER_SIDEREAL_DAY / ( 2 * Math.PI );

    static final double OPTICAL_HORIZON_DEG = 34.0 / 60;

    public enum EventType
    {
        RISE, SET
    }

    public enum LookupDirection
    {
        FORWARD, BACKWARD
    }

    protected final Equatorial.Cart topocentric = new Equatorial.Cart();
    protected final Spherical geographicLocation = new Spherical( SolarSystem.Body.EARTH.RADIUS_MEAN_M, 0, 0 );
    protected final Time time = new Time();
    private final Circle horizon = new Circle();

    private EventType mode = EventType.SET;
    private double searchIncrement = 2 * Math.PI;
    private double oldCos = Double.NaN;

    public abstract long compute( long startTimeMs, long precisionMs );

    protected abstract void computeTopocentricPosition();

    protected abstract double shiftHorizonDeg();

    public void setEventType( EventType mode )
    {
        this.mode = mode;
    }

    public void setSearchDirection( LookupDirection lookupDirection )
    {
        this.searchIncrement = 2 * Math.PI * ( lookupDirection == LookupDirection.FORWARD ? 1 : -1 );
    }

    public void setGeographicLocation( Spherical geographicLocation )
    {
        this.geographicLocation.lon = geographicLocation.lon;
        this.geographicLocation.lat = geographicLocation.lat;
    }

    public long computeSingleStep( long millisSinceEpoch )
    {
        time.setTime( millisSinceEpoch );
        final double cosHourAngleAtSet = computeCosHourAngleAtSet();
        if ( cosHourAngleAtSet > 1 || cosHourAngleAtSet < -1 )
            throw new IllegalStateException(
                    "At the given time the horizon does not cross the declination coordinate line of the object in question." );
        else
            time.addMillis( computeIncrementMeetsHorizonAt( cosHourAngleAtSet, true ) );
        return time.getTime();
    }

    public long computeIterative( long millisSinceEpoch, long precisionMs )
    {
        time.setTime( millisSinceEpoch );
        long deltaMs = computeDeltaT( true );
        time.addMillis( deltaMs );
        for ( int n = 0; Math.abs( deltaMs ) >= precisionMs && n < MAX_ITERATION; ++n )
        {
            deltaMs = computeDeltaT( false );
            time.addMillis( deltaMs );
        }
        if ( Math.abs( deltaMs ) >= precisionMs )
            throw new IllegalStateException( "Unable to determine a rise/set event." );
        return time.getTime();
    }

    private long computeDeltaT( boolean fixSearchDirection )
    {
        double cosHourAngleAtSet = computeCosHourAngleAtSet();
        if ( cosHourAngleAtSet > 1 || cosHourAngleAtSet < -1 )
            return computeIncrementDoesNotMeetHorizon( cosHourAngleAtSet );
        else
            return computeIncrementMeetsHorizonAt( cosHourAngleAtSet, fixSearchDirection );
    }

    private long computeIncrementDoesNotMeetHorizon( double cosHourAngle )
    {
        final boolean isSet = cosHourAngle > 1 && oldCos < -1 && searchIncrement < 0
                || cosHourAngle < -1 && oldCos > 1 && searchIncrement > 0;
        final boolean isRise = cosHourAngle > 1 && oldCos < -1 && searchIncrement > 0
                || cosHourAngle < -1 && oldCos > 1 && searchIncrement < 0;
        if ( isRise && mode == EventType.RISE || isSet && mode == EventType.SET )
            searchIncrement /= -2;
        oldCos = cosHourAngle;
        return (long)( searchIncrement * RAD_TO_SIDEREAL_MILLIS );
    }

    private double computeCosHourAngleAtSet()
    {
        computeTopocentricPosition();
        projectHorizon();
        final double zNorm = topocentric.z / topocentric.length();
        final double z = geographicLocation.lat > 0 ? zNorm : -zNorm;
        final double rOrb = Math.sqrt( ( 1 + z ) / ( 1 - z ) );
        final double dist = horizon.distFromOrigin();
        final double rHor = horizon.r;
        return ( rHor * rHor - dist * dist - rOrb * rOrb ) / ( 2 * dist * rOrb );
    }

    private void projectHorizon()
    {
        final Stereographic projection = new Stereographic( geographicLocation.lat >= 0 ? 1 : -1 );
        projection.project( geographicLocation, Math.toRadians( 90 - shiftHorizonDeg() ), horizon );
    }

    private long computeIncrementMeetsHorizonAt( double cosHourAngleAtSet, boolean fixDirection )
    {
        double dt;
        switch ( mode )
        {
            case RISE:
                dt = -Math.acos( cosHourAngleAtSet ) - computeCurrentHourAngle();
                break;
            case SET:
                dt = Math.acos( cosHourAngleAtSet ) - computeCurrentHourAngle();
                break;
            default:
                dt = 0;
        }
        while ( fixDirection && dt * searchIncrement < 0 )
            dt += searchIncrement;
        return (long)( dt * RAD_TO_SIDEREAL_MILLIS );
    }

    private double computeCurrentHourAngle()
    {
        // Right ascension is the sidereal time at (upper) meridian transit (hourAngle == 0)
        final double siderealTime = time.getHourAngleOfVernalEquinox() + geographicLocation.lon;
        final double rightAscension = Math.atan2( topocentric.y, topocentric.x );
        return Angle.standardize( siderealTime - rightAscension );
    }
}
