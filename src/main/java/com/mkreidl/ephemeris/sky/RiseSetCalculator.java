package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Angle;
import com.mkreidl.ephemeris.geometry.Circle;
import com.mkreidl.ephemeris.geometry.Spherical;
import com.mkreidl.ephemeris.geometry.Stereographic;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;
import com.mkreidl.ephemeris.solarsystem.Body;

public abstract class RiseSetCalculator
{
    public enum EventType
    {
        RISE( -1 ), TRANSIT( 0 ), SET( 1 );
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

    public static RiseSetCalculator getDummy()
    {
        return new RiseSetCalculator()
        {
            @Override
            public boolean compute( long startTimeMs )
            {
                return false;
            }
        };
    }

    public static final double OPTICAL_HORIZON_DEG = -34.0 / 60;
    private static final double RAD_TO_SIDEREAL_MILLIS = Time.MILLIS_PER_SIDEREAL_DAY / ( 2 * Math.PI );

    protected final Equatorial.Sphe topocentric = new Equatorial.Sphe();
    protected final Spherical geographicLocation = new Spherical( Body.EARTH.RADIUS_MEAN_M, 0, 0 );
    protected final Time time = new Time();
    protected EventType mode = EventType.SET;

    LookupDirection lookupDirection = LookupDirection.FORWARD;
    double virtualHorizonDeg = OPTICAL_HORIZON_DEG;

    private final Circle horizon = new Circle();
    private Stereographic projection;
    private long startTimeMs;

    public abstract boolean compute( long startTimeMs );

    public long getTime()
    {
        return time.getTime();
    }

    private double computeHourAngle()
    {
        // Right ascension is the sidereal time at (upper) meridian transit (hourAngle == 0)
        final double siderealTime = time.getHourAngleOfVernalEquinox() + geographicLocation.lon;
        return Angle.standardize( siderealTime - topocentric.lon );
    }

    private double getOrbitRadius()
    {
        final double z = Math.sin( geographicLocation.lat > 0 ? topocentric.lat : -topocentric.lat );
        return Math.sqrt( ( 1 + z ) / ( 1 - z ) );
    }

    public void setEventType( EventType mode )
    {
        this.mode = mode;
    }

    public void setSearchDirection( LookupDirection lookupDirection )
    {
        this.lookupDirection = lookupDirection;
    }

    public void setGeographicLocation( Spherical geographicLocation )
    {
        setGeographicLocation( geographicLocation.lon, geographicLocation.lat );
    }

    public void setGeographicLocation( double lon, double lat )
    {
        geographicLocation.lon = lon;
        geographicLocation.lat = lat;
        projection = new Stereographic( geographicLocation.lat >= 0 ? 1 : -1 );
        updateHorizon();
    }

    void setStartTime( long startTimeMs )
    {
        this.startTimeMs = startTimeMs;
        time.setTime( startTimeMs );
    }

    boolean isCrossing()
    {
        return mode == EventType.TRANSIT || !completelyAboveHorizon() && !completelyBelowHorizon();
    }

    private boolean completelyAboveHorizon()
    {
        return Math.abs( Math.toDegrees( geographicLocation.lat + topocentric.lat ) ) >= 90 + virtualHorizonDeg;
    }

    private boolean completelyBelowHorizon()
    {
        return Math.abs( Math.toDegrees( geographicLocation.lat - topocentric.lat ) ) >= 90 - virtualHorizonDeg;
    }

    private void updateHorizon()
    {
        projection.project( geographicLocation, Math.toRadians( 90 - virtualHorizonDeg ), horizon );
    }

    boolean adjustTime()
    {
        updateHorizon();
        final double alpha;
        switch ( mode )
        {
            case RISE:
            case SET:
                alpha = mode.signum * computeHourAngleAtSet() - computeHourAngle();
                break;
            default:
                alpha = -computeHourAngle();
                break;
        }
        time.addMillis( (long)( alpha * RAD_TO_SIDEREAL_MILLIS ) );
        if ( lookupDirection == LookupDirection.FORWARD && time.getTime() < startTimeMs )
            time.addMillis( Time.MILLIS_PER_SIDEREAL_DAY );
        if ( lookupDirection == LookupDirection.BACKWARD && time.getTime() > startTimeMs )
            time.addMillis( -Time.MILLIS_PER_SIDEREAL_DAY );
        return !Double.isNaN( alpha ) && !Double.isInfinite( alpha );
    }

    private double computeHourAngleAtSet()
    {
        final double dist = horizon.distFromOrigin();
        final double rHor = horizon.r;
        final double rOrb = getOrbitRadius();
        return Math.acos( ( rHor * rHor - dist * dist - rOrb * rOrb ) / ( 2 * dist * rOrb ) );
    }
}
