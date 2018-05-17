package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.geometry.Spherical;
import com.mkreidl.ephemeris.sky.RiseSetCalculator.EventType;
import com.mkreidl.ephemeris.sky.SolarSystem.Body;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

@RunWith( Parameterized.class )
public class SunRiseSetTest extends PlanetRiseSetTest
{
    private static final Object[][] EVENTS = {
            /*
            // https://www.timeanddate.com/sun/germany/munich
            {MUNICH, "2018-05-01 00:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-05-01 05:56 +0200"},
            {MUNICH, "2018-05-01 00:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-05-01 20:27 +0200"},
            {MUNICH, "2018-05-02 00:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-05-01 05:56 +0200"},
            {MUNICH, "2018-05-02 00:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-05-01 20:27 +0200"},

            {MUNICH, "2018-05-15 00:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-05-15 05:35 +0200"},
            {MUNICH, "2018-05-15 00:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-05-15 20:46 +0200"},
            {MUNICH, "2018-05-16 00:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-05-15 05:35 +0200"},
            {MUNICH, "2018-05-16 00:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-05-15 20:46 +0200"},

            {MUNICH, "2018-05-31 00:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-05-31 05:19 +0200"},
            {MUNICH, "2018-05-31 00:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-05-31 21:05 +0200"},
            {MUNICH, "2018-06-01 00:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-05-31 05:19 +0200"},
            {MUNICH, "2018-06-01 00:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-05-31 21:05 +0200"},
*/
            // https://www.timeanddate.com/sun/norway/svolvaer
//            {SVOLVAER, "2017-12-01 00:00 +0100", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2017-12-01 10:48 +0100"},
//            {SVOLVAER, "2017-12-01 00:00 +0100", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2017-12-01 12:53 +0100"},
//            {SVOLVAER, "2017-12-02 00:00 +0100", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2017-12-01 10:48 +0100"},
//            {SVOLVAER, "2017-12-02 00:00 +0100", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2017-12-01 12:53 +0100"},
//
//            {SVOLVAER, "2017-12-07 00:00 +0100", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2017-12-06 11:34 +0100"},
//            {SVOLVAER, "2017-12-07 00:00 +0100", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2017-12-06 12:11 +0100"},
            {SVOLVAER, "2017-12-07 00:00 +0100", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-01-05 11:59 +0100"},
            {SVOLVAER, "2017-12-07 00:00 +0100", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-01-05 12:16 +0100"},
//
//            {SVOLVAER, "2018-05-01 00:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-05-01 03:57 +0200"},
//            {SVOLVAER, "2018-05-01 00:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-05-01 22:04 +0200"},
//            {SVOLVAER, "2018-05-02 00:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-05-01 03:57 +0200"},
//            {SVOLVAER, "2018-05-02 00:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-05-01 22:04 +0200"},
//
//            {SVOLVAER, "2018-05-25 01:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-05-24 01:33 +0200"},
//            {SVOLVAER, "2018-05-25 01:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-05-25 00:43 +0200"},
//            {SVOLVAER, "2018-05-25 01:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-05-25 01:14 +0200"},
            {SVOLVAER, "2018-05-25 01:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-07-19 00:53 +0200"},

            {SVOLVAER, "2018-07-19 01:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-05-25 01:14 +0200"},
//            {SVOLVAER, "2018-07-19 01:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-07-19 00:53 +0200"},
//            {SVOLVAER, "2018-07-19 01:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-07-19 01:23 +0200"},
//            {SVOLVAER, "2018-07-19 01:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-07-20 00:34 +0200"},
/*
            // https://www.timeanddate.com/sun/canada/vancouver
            {VANCOUVER, "2018-05-01 00:00 -0700", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-05-01 05:51 -0700"},
            {VANCOUVER, "2018-05-01 00:00 -0700", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-05-01 20:29 -0700"},
            {VANCOUVER, "2018-05-02 00:00 -0700", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-05-01 05:51 -0700"},
            {VANCOUVER, "2018-05-02 00:00 -0700", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-05-01 20:29 -0700"},

            // https://www.timeanddate.com/sun/australia/sydney
            {SYDNEY, "2018-05-01 00:00 +1000", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-05-01 06:29 +1000"},
            {SYDNEY, "2018-05-01 00:00 +1000", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-05-01 17:15 +1000"},
            {SYDNEY, "2018-05-02 00:00 +1000", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-05-01 06:29 +1000"},
            {SYDNEY, "2018-05-02 00:00 +1000", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-05-01 17:15 +1000"},

            {FAR_NORTH, "2018-05-14 00:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"},
            {FAR_NORTH, "2018-05-14 00:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"},
            {FAR_NORTH, "2018-05-14 01:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"},
            {FAR_NORTH, "2018-05-14 01:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"},
            {FAR_NORTH, "2018-05-14 02:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"},
            {FAR_NORTH, "2018-05-14 02:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"},
            {FAR_NORTH, "2018-05-14 03:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"},
            {FAR_NORTH, "2018-05-14 03:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"},
            {FAR_NORTH, "2018-05-14 04:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"},
            {FAR_NORTH, "2018-05-14 04:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"},
            {FAR_NORTH, "2018-05-14 05:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"},
            {FAR_NORTH, "2018-05-14 05:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"},
            {FAR_NORTH, "2018-05-14 06:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"},
            {FAR_NORTH, "2018-05-14 06:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"},
            {FAR_NORTH, "2018-05-14 07:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"},
            {FAR_NORTH, "2018-05-14 07:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"},
            {FAR_NORTH, "2018-05-14 08:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"},
            {FAR_NORTH, "2018-05-14 08:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"},
            {FAR_NORTH, "2018-05-14 09:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"},
            {FAR_NORTH, "2018-05-14 09:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"},
            {FAR_NORTH, "2018-05-14 10:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"},
            {FAR_NORTH, "2018-05-14 10:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"},
            {FAR_NORTH, "2018-05-14 11:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"},
            {FAR_NORTH, "2018-05-14 11:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"},
            {FAR_NORTH, "2018-05-14 12:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"},
            {FAR_NORTH, "2018-05-14 12:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"},
            {FAR_NORTH, "2018-05-14 13:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"},
            {FAR_NORTH, "2018-05-14 13:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"},
            {FAR_NORTH, "2018-05-14 14:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"},
            {FAR_NORTH, "2018-05-14 14:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"},
            {FAR_NORTH, "2018-05-14 15:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"},
            {FAR_NORTH, "2018-05-14 15:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"},
            {FAR_NORTH, "2018-05-14 16:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"},
            {FAR_NORTH, "2018-05-14 16:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"},
            {FAR_NORTH, "2018-05-14 17:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"},
            {FAR_NORTH, "2018-05-14 17:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"},
            {FAR_NORTH, "2018-05-14 18:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"},
            {FAR_NORTH, "2018-05-14 18:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"},
            {FAR_NORTH, "2018-05-14 19:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"},
            {FAR_NORTH, "2018-05-14 19:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"},
            {FAR_NORTH, "2018-05-14 20:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"},
            {FAR_NORTH, "2018-05-14 20:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"},
            {FAR_NORTH, "2018-05-14 21:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"},
            {FAR_NORTH, "2018-05-14 21:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"},
            {FAR_NORTH, "2018-05-14 22:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"},
            {FAR_NORTH, "2018-05-14 22:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"},
            {FAR_NORTH, "2018-05-14 23:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"},
            {FAR_NORTH, "2018-05-14 23:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"},
            */
    };

    @Parameters( name = "{0} {1} {2} {3} {4}" )
    public static Iterable<Object[]> data()
    {
        return Arrays.asList( EVENTS );
    }

    public SunRiseSetTest( Spherical geographicLocation, String startTime, RiseSetCalculator.LookupDirection searchDirection, EventType eventType, String eventTime )
    {
        super( geographicLocation, startTime, searchDirection, eventType, eventTime );
    }

    @Override
    protected RiseSetCalculator getCalculator()
    {
        return PlanetRiseSetCalculator.of( new SolarSystem(), Body.SUN );
    }
}
