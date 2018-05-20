package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Angle;
import com.mkreidl.ephemeris.geometry.Circle;
import com.mkreidl.ephemeris.geometry.Spherical;
import com.mkreidl.ephemeris.geometry.Stereographic;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;

public abstract class RiseSetCalculator
{
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

    private static final double OPTICAL_HORIZON_DEG = 34.0 / 60;
    private static final double RAD_TO_SIDEREAL_MILLIS = Time.MILLIS_PER_SIDEREAL_DAY / ( 2 * Math.PI );

    protected final Equatorial.Sphe topocentric = new Equatorial.Sphe();
    protected final Spherical geographicLocation = new Spherical( SolarSystem.Body.EARTH.RADIUS_MEAN_M, 0, 0 );
    protected final Time time = new Time();
    protected EventType mode = EventType.SET;

    LookupDirection lookupDirection;

    private final Circle horizon = new Circle();
    private Stereographic projection;
    private long startTimeMs;

    public abstract boolean compute( long startTimeMs );

    public long getTime()
    {
        return time.getTime();
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
    }

    void setStartTime( long startTimeMs )
    {
        this.startTimeMs = startTimeMs;
        time.setTime( startTimeMs );
    }

    protected double virtualHorizonDeg()
    {
        return -OPTICAL_HORIZON_DEG;
    }

    boolean isCrossingHorizon()
    {
        return !completelyAboveHorizon() && !completelyBelowHorizon();
    }

    private boolean completelyAboveHorizon()
    {
        return Math.abs( Math.toDegrees( geographicLocation.lat + topocentric.lat ) ) >= 90 + virtualHorizonDeg();
    }

    private boolean completelyBelowHorizon()
    {
        return Math.abs( Math.toDegrees( geographicLocation.lat - topocentric.lat ) ) >= 90 - virtualHorizonDeg();
    }

    boolean adjustTimeToCrossingHorizon()
    {
        final double alpha = mode.signum * hourAngleAtSet() - currentHourAngle();
        time.addMillis( (long)( alpha * RAD_TO_SIDEREAL_MILLIS ) );
        if ( lookupDirection == LookupDirection.FORWARD && time.getTime() < startTimeMs )
            time.addMillis( Time.MILLIS_PER_SIDEREAL_DAY );
        if ( lookupDirection == LookupDirection.BACKWARD && time.getTime() > startTimeMs )
            time.addMillis( -Time.MILLIS_PER_SIDEREAL_DAY );
        return !Double.isNaN( alpha ) && !Double.isInfinite( alpha );
    }

    private double hourAngleAtSet()
    {
        projection.project( geographicLocation, Math.toRadians( 90 - virtualHorizonDeg() ), horizon );
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
