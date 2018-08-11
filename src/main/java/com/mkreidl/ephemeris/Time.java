package com.mkreidl.ephemeris;

import com.mkreidl.ephemeris.geometry.Angle;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

public class Time
{
    public static final Time J2000 = new Time( 946_728_000_000L );
    public static final double STD_EPOCH_DAY_NUMBER = 2451545.0;
    public static final double DAYS_PER_YEAR = 365.25;

    public static final double TDT_OFFSET = 1.5; // Terrestrial dynamical Time
    public static final double TDT_EPOCH_DAY_NUMBER = STD_EPOCH_DAY_NUMBER - TDT_OFFSET;


    public static final long MILLIS_PER_HOUR = 3_600_000;
    public static final long MILLIS_PER_DAY = MILLIS_PER_HOUR * 24;
    public static final long DAYS_PER_CENTURY = 36_525;
    public static final long DAYS_PER_MILLENNIUM = 365_250;
    public static final long MILLIS_PER_CENTURY = MILLIS_PER_DAY * DAYS_PER_CENTURY;

    public static final double SIDEREAL_PER_SOLAR = 1.00273790935;
    public static final double SOLAR_PER_SIDEREAL = 1.0 / SIDEREAL_PER_SOLAR;
    public static final long MILLIS_PER_SIDEREAL_DAY = (long)( MILLIS_PER_DAY * SOLAR_PER_SIDEREAL );

    private static final double[] GMST_COEFF_DAYS =
            {
                    1.0 / 38710000.0 * 1 / 360, 0.000387933 * 1 / 360,
                    36000.770053608 * 1 / 360, 100.46061837 * 1 / 360
            };

    private static final ZoneId UTC = ZoneId.of( "UTC" );
    public static final long SECONDS_PER_DAY = 24 * 3600;

    public static final long MILLIS_SINCE_EPOCH_AT_J2000 = 946_728_000_000L;
    public static final double JULIAN_YEARS_PER_MILLISECOND = 1.0 / ( MILLIS_PER_DAY * 365.25 );

    private long millisSinceEpoch;

    @Override
    public String toString()
    {
        return ZonedDateTime.ofInstant( Instant.ofEpochMilli( millisSinceEpoch ), UTC ).toString();
    }

    public Time()
    {
        millisSinceEpoch = System.currentTimeMillis();
    }

    public Time( long millisSinceEpoch )
    {
        this.millisSinceEpoch = millisSinceEpoch;
    }

    public long getTime()
    {
        return millisSinceEpoch;
    }

    public void setTime( long millisSinceEpoch )
    {
        this.millisSinceEpoch = millisSinceEpoch;
    }

    public double julianDayNumber()
    {
        return julianDayNumberSince( J2000 ) + STD_EPOCH_DAY_NUMBER;
    }

    public double julianDayNumberSince( Time instant )
    {
        return (double)( millisSinceEpoch - instant.millisSinceEpoch ) / MILLIS_PER_DAY;
    }

    public double julianYearsSinceJ2000()
    {
        return julianDayNumberSince( J2000 ) / DAYS_PER_YEAR;
    }

    public static double julianYearsSinceJ2000( long millisSinceEpoch )
    {
        return ( millisSinceEpoch - MILLIS_SINCE_EPOCH_AT_J2000 ) * JULIAN_YEARS_PER_MILLISECOND;
    }

    public double terrestrialDynamicalTime()
    {
        return julianDayNumberSince( J2000 ) + TDT_OFFSET;
    }

    /**
     * Calculate the Mean Sidereal Time for Greenwich at given date
     *
     * @return Greenwich Mean Sidereal Time in hours [h]
     */
    public double getHourAngleOfVernalEquinox()
    {
        return getSiderealTimeDayFraction() * 2 * Math.PI;
    }

    /**
     * Calculate the Mean Sidereal Time for Greenwich at given date
     *
     * @return Greenwich Mean Sidereal Time in hours [h]
     */
    public double getMeanSiderealTime()
    {
        return getSiderealTimeDayFraction() * 24;
    }

    /**
     * Calculate the Mean Sidereal Time for Greenwich at given date
     *
     * @return Greenwich Mean Sidereal Time in radians
     */
    public double getMeanSiderealTimeRadians()
    {
        return getSiderealTimeDayFraction() * 2 * Math.PI;
    }

    private double getSiderealTimeDayFraction()
    {
        final double midnightUT = midnightAtGreenwichSameDate();
        final double julianCenturyUT0 = ( midnightUT - J2000.millisSinceEpoch ) / MILLIS_PER_CENTURY;

        double daysBase = 0.0;
        for ( double c : GMST_COEFF_DAYS )
            daysBase = daysBase * julianCenturyUT0 + c;

        final double dayFraction = ( millisSinceEpoch - midnightUT ) / MILLIS_PER_SIDEREAL_DAY;
        double siderealTime = ( ( daysBase + dayFraction ) % 1 );
        if ( siderealTime < 0.0 )
            siderealTime += 1.0;
        return siderealTime;
    }

    public Angle getMeanSiderealTime( Angle siderealTime )
    {
        return siderealTime.set( getMeanSiderealTime(), Angle.Unit.HOURS );
    }

    public Angle getMeanSiderealTime( Angle longitude, Angle siderealTime )
    {
        double hours = getMeanSiderealTime() + longitude.get( Angle.Unit.HOURS );
        return siderealTime.set( standardize24( hours ), Angle.Unit.HOURS );
    }

    public Angle getMeanSolarTime( Angle longitude, Angle solarTime )
    {
        double hours = ( millisSinceEpoch - midnightAtGreenwichSameDate() ) / MILLIS_PER_HOUR + longitude.get( Angle.Unit.HOURS );
        return solarTime.set( standardize24( hours ), Angle.Unit.HOURS );
    }

    private static double standardize24( double hours )
    {
        return ( hours %= 24 ) < 0 ? hours + 24 : hours;
    }

    private double midnightAtGreenwichSameDate()
    {
        final Instant instant = Instant.ofEpochMilli( millisSinceEpoch );
        final ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant( instant, UTC );
        final ZonedDateTime midnightAtGreenwich = zonedDateTime
                .withHour( 0 ).withMinute( 0 ).withSecond( 0 ).withNano( 0 );
        return Instant.from( midnightAtGreenwich ).toEpochMilli();
    }

    public void addMillis( long millis )
    {
        millisSinceEpoch += millis;
    }

}
