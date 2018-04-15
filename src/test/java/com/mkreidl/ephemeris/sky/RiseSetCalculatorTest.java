package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.Time;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RiseSetCalculatorTest
{
    private static final double SUN_APPARENT_RADIUS_DEG =
            Math.toDegrees( SolarSystem.Body.SUN.RADIUS_MEAN_M / 1.5e11 );

    public static final double MUNICH_LON = 11.5;
    public static final double MUNICH_LAT = 68.3;
    private static final double OPTICAL_HORIZON_DEG = 34.0 / 60;

    private final Ephemerides ephemerides = new Ephemerides();
    private final SolarSystem solarSystem = new SolarSystem();
    private final Time time = new Time();

    private final RiseSetCalculator.PositionCalculator sunCalculator = ( time, geographicLocation, position ) ->
    {
        solarSystem.compute( time, SolarSystem.Body.EARTH );
        solarSystem.compute( time, SolarSystem.Body.SUN );
        solarSystem.getEphemerides( SolarSystem.Body.SUN, ephemerides );
        ephemerides.setTimeLocation( time, geographicLocation );
        ephemerides.get( position, Ephemerides.CoordinatesCenter.TOPOCENTRIC );
    };

    @Test
    public void testSunset() throws ParseException
    {
        final DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        time.setTime( dateFormat.parse( "2018-04-15 15:41:00" ).getTime() );
        final RiseSetCalculator sunRiseSet = new RiseSetCalculator( sunCalculator );
        sunRiseSet.setGeographicLocation( MUNICH_LON, MUNICH_LAT, -OPTICAL_HORIZON_DEG - SUN_APPARENT_RADIUS_DEG );
        sunRiseSet.compute( time, 1000 );
        System.out.println( dateFormat.format( new Date( time.getTime() ) ) );
    }

    @Test
    public void testSunrise() throws ParseException
    {
        final DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        time.setTime( dateFormat.parse( "2018-12-16 5:41:00" ).getTime() );
        final RiseSetCalculator sunRiseSet = new RiseSetCalculator( sunCalculator );
        sunRiseSet.setGeographicLocation( MUNICH_LON, MUNICH_LAT, -OPTICAL_HORIZON_DEG - SUN_APPARENT_RADIUS_DEG );
        sunRiseSet.setSearchDirection( -1 );
        sunRiseSet.setMode( RiseSetCalculator.Mode.RISE );
        sunRiseSet.compute( time, 15000 );
        System.out.println( dateFormat.format( new Date( time.getTime() ) ) );
    }
}
