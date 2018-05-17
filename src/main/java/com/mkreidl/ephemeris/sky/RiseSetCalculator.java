package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Angle;
import com.mkreidl.ephemeris.geometry.Circle;
import com.mkreidl.ephemeris.geometry.Spherical;
import com.mkreidl.ephemeris.geometry.Stereographic;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;

public abstract class RiseSetCalculator
{
    private static final long PRECISION = 30000;
    private static final int MAX_ITERATION = 1000;
    private static final double RAD_TO_SIDEREAL_MILLIS = Time.MILLIS_PER_SIDEREAL_DAY / ( 2 * Math.PI );

    static final double OPTICAL_HORIZON_DEG = 34.0 / 60;

    public enum EventType
    {
        RISE( -1 ), SET( 1 );
        private final int signum;

        EventType( int signum )
        {
            this.signum = signum;
        }
    }

    public enum LookupDirection
    {
        FORWARD, BACKWARD
    }

    protected final Equatorial.Sphe topocentric = new Equatorial.Sphe();
    protected final Spherical geographicLocation = new Spherical( SolarSystem.Body.EARTH.RADIUS_MEAN_M, 0, 0 );
    protected final Time time = new Time();
    private final Circle horizon = new Circle();

    private LookupDirection lookupDirection;
    private EventType mode = EventType.SET;
    private double oldCos = Double.NaN;
    private long searchIncrement = Time.MILLIS_PER_SIDEREAL_DAY;
    private long timeMillis, timeMillisStart;

    public abstract Long compute( long startTimeMs );

    protected abstract void computeTopocentricPosition();

    protected abstract double shiftHorizonDeg();

    public void setEventType( EventType mode )
    {
        this.mode = mode;
    }

    public void setSearchDirection( LookupDirection lookupDirection )
    {
        this.lookupDirection = lookupDirection;
        searchIncrement = Time.MILLIS_PER_SIDEREAL_DAY * ( lookupDirection == LookupDirection.FORWARD ? 1 : -1 );
    }

    public void setGeographicLocation( double lon, double lat )
    {
        geographicLocation.lon = lon;
        geographicLocation.lat = lat;
    }

    public void setGeographicLocation( Spherical geographicLocation )
    {
        this.geographicLocation.lon = geographicLocation.lon;
        this.geographicLocation.lat = geographicLocation.lat;
    }

    public Long computeIterative( long timeMillisStart )
    {
        this.timeMillisStart = timeMillisStart;
        this.timeMillis = timeMillisStart;
        time.setTime( timeMillisStart );
        while ( doesNotMeetHorizon() )
        {
            timeMillis += searchIncrement;
            time.setTime( timeMillis );
        }
        return computeIterativeMeetsHorizon();
    }

    private Long computeIterativeMeetsHorizon()
    {
        for ( int n = 0; n < MAX_ITERATION; ++n )
        {
            final long timeMillisPrevious = timeMillis;
            computeIncrement();
            time.setTime( timeMillis );
            if ( Math.abs( timeMillis - timeMillisPrevious ) < PRECISION )
                return time.getTime();
        }
        return null;
    }

    private boolean doesNotMeetHorizon()
    {
        computeTopocentricPosition();
        return completelyAboveHorizon() || completelyBelowHorizon();
    }

    protected boolean completelyAboveHorizon()
    {
        return Math.abs( Math.PI - geographicLocation.lat - topocentric.lat ) <= 0.5 * Math.PI;
    }

    protected boolean completelyBelowHorizon()
    {
        return Math.abs( geographicLocation.lat - topocentric.lat ) >= 0.5 * Math.PI;
    }

    private void computeIncrement()
    {
        computeTopocentricPosition();
        projectHorizon();
        final double alpha = mode.signum * hourAngleAtSet() - currentHourAngle();
        timeMillis += (long)( alpha * RAD_TO_SIDEREAL_MILLIS );
        if ( lookupDirection == LookupDirection.FORWARD && timeMillis < timeMillisStart )
            timeMillis += Time.MILLIS_PER_SIDEREAL_DAY;
        if ( lookupDirection == LookupDirection.BACKWARD && timeMillis > timeMillisStart )
            timeMillis -= Time.MILLIS_PER_SIDEREAL_DAY;
    }

    private void projectHorizon()
    {
        final Stereographic projection = new Stereographic( geographicLocation.lat >= 0 ? 1 : -1 );
        projection.project( geographicLocation, Math.toRadians( 90 - shiftHorizonDeg() ), horizon );
    }

    private double hourAngleAtSet()
    {
        final double z = Math.sin( geographicLocation.lat > 0 ? topocentric.lat : -topocentric.lat );
        final double rOrb = Math.sqrt( ( 1 + z ) / ( 1 - z ) );
        final double dist = horizon.distFromOrigin();
        final double rHor = horizon.r;
        return Math.acos( ( rHor * rHor - dist * dist - rOrb * rOrb ) / ( 2 * dist * rOrb ) );
    }

    private double currentHourAngle()
    {
        // Right ascension is the sidereal time at (upper) meridian transit (hourAngle == 0)
        final double siderealTime = time.getHourAngleOfVernalEquinox() + geographicLocation.lon;
        return Angle.standardize( siderealTime - topocentric.lon );
    }
}
