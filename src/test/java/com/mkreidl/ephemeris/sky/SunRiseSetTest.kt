package com.mkreidl.ephemeris.sky

import com.mkreidl.ephemeris.solarsystem.Body
import com.mkreidl.math.Spherical3
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class SunRiseSetTest(
        geographicLocation: Spherical3,
        startTime: String,
        searchDirection: RiseSetCalculator.LookupDirection,
        eventType: RiseSetCalculator.EventType,
        eventTime: String
) : PlanetRiseSetTest(
        Body.SUN,
        geographicLocation,
        startTime,
        eventTime,
        searchDirection,
        eventType
) {
    companion object {
        @JvmStatic
        @Parameters(name = "{0} {1} {2} {3} {4}")
        fun data() = listOf(
                // https://www.timeanddate.com/sun/germany/munich
                arrayOf(PlanetRiseSetTest.MUNICH, "2018-05-01 00:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-05-01 05:56 +0200"),
                arrayOf(PlanetRiseSetTest.MUNICH, "2018-05-01 00:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-05-01 20:27 +0200"),
                arrayOf(PlanetRiseSetTest.MUNICH, "2018-05-02 00:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-05-01 05:56 +0200"),
                arrayOf(PlanetRiseSetTest.MUNICH, "2018-05-02 00:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-05-01 20:27 +0200"),

                arrayOf(PlanetRiseSetTest.MUNICH, "2018-05-15 00:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-05-15 05:35 +0200"),
                arrayOf(PlanetRiseSetTest.MUNICH, "2018-05-15 00:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-05-15 20:46 +0200"),
                arrayOf(PlanetRiseSetTest.MUNICH, "2018-05-16 00:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-05-15 05:35 +0200"),
                arrayOf(PlanetRiseSetTest.MUNICH, "2018-05-16 00:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-05-15 20:46 +0200"),

                arrayOf(PlanetRiseSetTest.MUNICH, "2018-05-31 00:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-05-31 05:19 +0200"),
                arrayOf(PlanetRiseSetTest.MUNICH, "2018-05-31 00:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-05-31 21:05 +0200"),
                arrayOf(PlanetRiseSetTest.MUNICH, "2018-06-01 00:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-05-31 05:19 +0200"),
                arrayOf(PlanetRiseSetTest.MUNICH, "2018-06-01 00:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-05-31 21:05 +0200"),

                // https://www.timeanddate.com/sun/norway/svolvaer
                arrayOf(PlanetRiseSetTest.SVOLVAER, "2017-12-01 00:00 +0100", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2017-12-01 10:48 +0100"),
                arrayOf(PlanetRiseSetTest.SVOLVAER, "2017-12-01 00:00 +0100", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2017-12-01 12:53 +0100"),
                arrayOf(PlanetRiseSetTest.SVOLVAER, "2017-12-02 00:00 +0100", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2017-12-01 10:48 +0100"),
                arrayOf(PlanetRiseSetTest.SVOLVAER, "2017-12-02 00:00 +0100", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2017-12-01 12:53 +0100"),

                arrayOf(PlanetRiseSetTest.SVOLVAER, "2017-12-07 00:00 +0100", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2017-12-06 11:34 +0100"),
                arrayOf(PlanetRiseSetTest.SVOLVAER, "2017-12-07 00:00 +0100", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2017-12-06 12:11 +0100"),
                arrayOf(PlanetRiseSetTest.SVOLVAER, "2017-12-07 00:00 +0100", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-01-05 11:59 +0100"),
                arrayOf(PlanetRiseSetTest.SVOLVAER, "2017-12-07 00:00 +0100", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-01-05 12:16 +0100"),

                arrayOf(PlanetRiseSetTest.SVOLVAER, "2018-05-01 00:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-05-01 03:57 +0200"),
                arrayOf(PlanetRiseSetTest.SVOLVAER, "2018-05-01 00:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-05-01 22:04 +0200"),
                arrayOf(PlanetRiseSetTest.SVOLVAER, "2018-05-02 00:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-05-01 03:57 +0200"),
                arrayOf(PlanetRiseSetTest.SVOLVAER, "2018-05-02 00:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-05-01 22:04 +0200"),

                arrayOf(PlanetRiseSetTest.SVOLVAER, "2018-05-25 01:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-05-24 01:33 +0200"),
                arrayOf(PlanetRiseSetTest.SVOLVAER, "2018-05-25 01:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-05-25 00:43 +0200"),
                arrayOf(PlanetRiseSetTest.SVOLVAER, "2018-05-25 01:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-05-25 01:14 +0200"),
                arrayOf(PlanetRiseSetTest.SVOLVAER, "2018-05-25 01:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-07-19 00:53 +0200"),

                arrayOf(PlanetRiseSetTest.SVOLVAER, "2018-07-19 01:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-05-25 01:14 +0200"),
                arrayOf(PlanetRiseSetTest.SVOLVAER, "2018-07-19 01:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-07-19 00:53 +0200"),
                arrayOf(PlanetRiseSetTest.SVOLVAER, "2018-07-19 01:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-07-19 01:24 +0200"),
                arrayOf(PlanetRiseSetTest.SVOLVAER, "2018-07-19 01:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-07-20 00:34 +0200"),

                // https://www.timeanddate.com/sun/canada/vancouver
                arrayOf(PlanetRiseSetTest.VANCOUVER, "2018-05-01 00:00 -0700", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-05-01 05:51 -0700"),
                arrayOf(PlanetRiseSetTest.VANCOUVER, "2018-05-01 00:00 -0700", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-05-01 20:29 -0700"),
                arrayOf(PlanetRiseSetTest.VANCOUVER, "2018-05-02 00:00 -0700", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-05-01 05:51 -0700"),
                arrayOf(PlanetRiseSetTest.VANCOUVER, "2018-05-02 00:00 -0700", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-05-01 20:29 -0700"),

                // https://www.timeanddate.com/sun/australia/sydney
                arrayOf(PlanetRiseSetTest.SYDNEY, "2018-05-01 00:00 +1000", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-05-01 06:29 +1000"),
                arrayOf(PlanetRiseSetTest.SYDNEY, "2018-05-01 00:00 +1000", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-05-01 17:15 +1000"),
                arrayOf(PlanetRiseSetTest.SYDNEY, "2018-05-02 00:00 +1000", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-05-01 06:29 +1000"),
                arrayOf(PlanetRiseSetTest.SYDNEY, "2018-05-02 00:00 +1000", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-05-01 17:15 +1000"),

                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 00:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 00:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 01:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 01:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 02:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 02:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 03:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 03:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 04:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 04:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 05:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 05:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 06:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 06:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 07:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 07:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 08:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 08:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 09:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 09:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 10:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 10:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 11:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 11:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 12:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 12:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 13:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 13:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 14:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 14:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 15:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 15:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 16:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 16:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 17:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 17:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 18:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 18:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 19:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 19:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 20:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 20:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 21:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 21:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 22:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 22:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 23:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-05-14 23:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),

                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 02:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 02:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 03:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 03:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 04:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 04:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 05:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 05:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 06:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 06:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 07:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 07:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 08:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 08:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 09:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 09:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 10:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 10:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 11:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 11:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 12:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 12:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 13:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 13:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 14:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 14:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 15:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 15:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 16:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 16:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 17:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 17:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 18:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 18:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 19:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 19:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 20:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 20:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 21:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 21:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 22:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 22:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 23:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-22 23:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-23 00:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-23 00:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-08-22 00:45 +0200"),

                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-23 01:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-23 01:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-08-23 00:15 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-23 02:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-08-22 01:51 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-08-23 02:00 +0200", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-08-23 00:15 +0200"),

                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-10-21 02:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-10-21 10:18 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-10-21 01:00 +0200", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-10-21 15:37 +0200"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-10-29 01:00 +0100", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2018-10-29 11:21 +0100"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-10-29 01:00 +0100", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2018-10-29 12:33 +0100"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-10-29 14:00 +0100", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.RISE, "2019-02-13 11:39 +0100"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-10-29 13:00 +0100", RiseSetCalculator.LookupDirection.FORWARD, RiseSetCalculator.EventType.SET, "2019-02-13 13:20 +0100"),

                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-12-29 01:00 +0100", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.RISE, "2018-10-29 11:21 +0100"),
                arrayOf(PlanetRiseSetTest.FAR_NORTH, "2018-12-29 01:00 +0100", RiseSetCalculator.LookupDirection.BACKWARD, RiseSetCalculator.EventType.SET, "2018-10-29 12:33 +0100")
        )
    }
}