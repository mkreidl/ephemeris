package com.mkreidl.ephemeris;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.security.InvalidParameterException;
import java.util.Arrays;

@RunWith(Parameterized.class)
public class TimeTest
{

    public static final double toleranceJulianDayNumber = 1e-7;
    public static final double toleranceSiderealTime = 2.0 / 3600.0;
    public static Time julianDate;

    public double julianDayNumber;
    public double siderealTime;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        julianDate = new Time();
    }

    @Parameters
    public static Iterable<Object[]> data()
    {
        return Arrays
                .asList( new Object[][]
        {
                {
                        "1990.01.01 12:00:00", 2447893.0000000, "18:43:31"
                },
                {
                		"1800.01.01 12:00:00", 2378497.000000, "18:43:33"
                },
                {
                    	"2100.01.01 12:00:00", 2488070.0000000, "18:44:55"
                },
                {
                        "1990.01.01 18:00:00", 2447893.2500000, "00:44:30"
                },
                {
                        "1990.04.19 00:00:00", 2448000.5000000, "13:47:20"
                },
                {
                        "2000.01.01 12:00:00", Time.STD_EPOCH_DAY_NUMBER, "18:41:49"
                },
                {
                        "2006.01.14 16:30:00", 2453750.1875000, "00:05:59"
                },
                {
                        "2010.03.25 16:30:00", 2455281.1875000, "04:42:06"
                },
                {
                        "2016.01.22 13:03:01", 2457410.0437616, "21:08:19"
                },
                {
                        "2016.05.17 08:53:25", 2457525.8704282, "00:35:22"
                }
        } );
    }

    public TimeTest( String dateString, double JD, String siderealTime )
    {
        julianDate = Time.getAstronomicalTime( dateString );
        if ( julianDate == null )
            throw new InvalidParameterException( "String 'dateString' does not represent a valid date." );
        julianDayNumber = JD;
        final String[] siderealHHmmss = siderealTime.split( ":" );
        long hours = Long.parseLong( siderealHHmmss[ 0 ] );
        long mins = Long.parseLong( siderealHHmmss[ 1 ] );
        long secs = Long.parseLong( siderealHHmmss[ 2 ] );
        this.siderealTime = hours + mins / 60.0 + secs / 3600.0;
    }

    @Test
    public void testJulianDayNumber()
    {
        assertEquals( Time.STD_EPOCH_DAY_NUMBER, Time.J2000.julianDayNumber(), toleranceJulianDayNumber );
        assertEquals( julianDayNumber, julianDate.julianDayNumber(), toleranceJulianDayNumber );
        assertEquals( siderealTime, julianDate.getMeanSiderealTime(), toleranceSiderealTime );
    }

    @Test
    public void testSiderealTime()
    {
        assertEquals( siderealTime, julianDate.getMeanSiderealTime(), toleranceSiderealTime );
    }
}
