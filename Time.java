package com.mkreidl.ephemeris;

import com.mkreidl.ephemeris.geometry.*;

import java.text.*;
import java.util.*;


public class Time extends Date
{
    /**
     * Representing a Julian Date
     */
    public static final Time J2000 = new Time( 946728000000L );
    public static final double STD_EPOCH_DAY_NUMBER = 2451545.0;
    public static final double DAYS_PER_YEAR = 365.25;

    public static final double TDT_OFFSET = 1.5; // Terrestrial dynamical Time
    public static final double TDT_EPOCH_DAY_NUMBER = STD_EPOCH_DAY_NUMBER - TDT_OFFSET;

    public static final long MILLIS_PER_HOUR = 3600000;
    public static final long MILLIS_PER_DAY = MILLIS_PER_HOUR * 24;
    public static final long DAYS_PER_CENTURY = 36525;
    public static final long DAYS_PER_MILLENNIUM = 365250;
    public static final long MILLIS_PER_CENTURY = MILLIS_PER_DAY * DAYS_PER_CENTURY;

    public static final double SIDEREAL_PER_SOLAR = 1.00273790935;
    public static final double SOLAR_PER_SIDEREAL = 1.0 / SIDEREAL_PER_SOLAR;

    private static final long serialVersionUID = 1L;
    private static final SimpleDateFormat NASA_DATE_PARSER =
            new SimpleDateFormat( "yyyy.MM.dd HH:mm:ss", Locale.ENGLISH );
    private static final SimpleDateFormat VSOP_DATE_PARSER =
            new SimpleDateFormat( "dd/MM/yyyy HH", Locale.ENGLISH );
    private static final double[] GMST_COEFF_DAYS =
            {
                    1.0 / 38710000.0 * 1 / 360, 0.000387933 * 1 / 360,
                    36000.770053608 * 1 / 360, 100.46061837 * 1 / 360
            };
    private static final Calendar calUT0 = new GregorianCalendar( TimeZone.getTimeZone( "UT" ) );


    public static Time getAstronomicalTime( String dateString )
    {
        NASA_DATE_PARSER.setTimeZone( TimeZone.getTimeZone( "UCT" ) );
        try
        {
            return new Time( NASA_DATE_PARSER.parse( dateString ).getTime() );
        }
        catch ( ParseException e )
        {
        }
        VSOP_DATE_PARSER.setTimeZone( TimeZone.getTimeZone( "UCT" ) );
        try
        {
            return new Time( VSOP_DATE_PARSER.parse( dateString ).getTime() );
        }
        catch ( ParseException e )
        {
        }
        return null;
    }

    @Override
    public String toString()
    {
        return NASA_DATE_PARSER.format( getTime() );
    }

    public Time()
    {
        super();
    }

    public Time( long millisec )
    {
        super( millisec );
    }

    public double julianDayNumber()
    {
        return julianDayNumberSince( J2000 ) + STD_EPOCH_DAY_NUMBER;
    }

    public double julianDayNumberSince( Date date )
    {
        return (double)( getTime() - date.getTime() ) / MILLIS_PER_DAY;
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
    public double getMeanSiderealTime()
    {
        final double midnightUT = midnightAtGreenwichSameDate();
        final double julianCenturyUT0 = ( midnightUT - J2000.getTime() ) / MILLIS_PER_DAY / DAYS_PER_CENTURY;

        double daysBase = 0.0;
        for ( double c : GMST_COEFF_DAYS )
            daysBase = daysBase * julianCenturyUT0 + c;

        final double dayFraction = ( getTime() - midnightUT ) * SIDEREAL_PER_SOLAR / MILLIS_PER_DAY;
        double siderealTime = ( ( daysBase + dayFraction ) % 1 );
        if ( siderealTime < 0.0 )
            siderealTime += 1.0;

        return siderealTime * 24;
    }

    public Angle getMeanSiderealTime( Angle siderealTime )
    {
        return siderealTime.set( getMeanSiderealTime(), Angle.Unit.HOURS );
    }

    public Angle getMeanSiderealTime( Spherical observer )
    {
        return getMeanSiderealTime( observer, new Angle() );
    }

    public Angle getMeanSiderealTime( Spherical observer, Angle siderealTime )
    {
        final double hours = getMeanSiderealTime() + observer.lon * Angle.HOURS;
        return siderealTime.set( hours, Angle.Unit.HOURS ).standardizePositive();
    }

    public Angle getMeanSolarTime( Spherical observer, Angle solarTime )
    {
        final double hours = ( getTime() - midnightAtGreenwichSameDate() ) / 3.6e6 + observer.lon * Angle.HOURS;
        return solarTime.set( hours, Angle.Unit.HOURS ).standardizePositive();
    }

    public enum TimeDirection
    {
        PAST, FUTURE
    }

    /**
     * Adjust the coordinates in time at which an earth meridiane passes a
     * celestial meridiane.
     * The meridianes in question are given as hour angles [h] with
     * positive values eastward. Thereby, 0h for the earth meridiane means
     * Greenwich, 0h for the celestial meridiane means the vernal equinox.
     *
     * @param hourAngle      Hour angle relative to Greenwich [h]; the earth
     *                       meridiane in question [h]
     * @param rightAscension Right Ascension in [h]
     * @param when
     */
    public void adjustToTimeOfPassage( double hourAngle, double rightAscension, TimeDirection when )
    {
        double siderealHoursDelta = ( rightAscension - hourAngle - getMeanSiderealTime() ) % 24.0;
        if ( siderealHoursDelta > 0.0 && when == TimeDirection.PAST )
            siderealHoursDelta -= 24.0;
        if ( siderealHoursDelta < 0.0 && when == TimeDirection.FUTURE )
            siderealHoursDelta += 24.0;
        final double siderealMillisDelta = siderealHoursDelta * MILLIS_PER_HOUR;
        addSiderealMillis( siderealMillisDelta );
    }

    private double midnightAtGreenwichSameDate()
    {
        calUT0.setTime( this );
        calUT0.set( Calendar.HOUR_OF_DAY, 0 );
        calUT0.set( Calendar.MINUTE, 0 );
        calUT0.set( Calendar.SECOND, 0 );
        calUT0.set( Calendar.MILLISECOND, 0 );
        return calUT0.getTimeInMillis();
    }

    private void addSiderealMillis( double millis )
    {
        this.setTime( this.getTime() + (long)( millis * SOLAR_PER_SIDEREAL ) );
    }

}
