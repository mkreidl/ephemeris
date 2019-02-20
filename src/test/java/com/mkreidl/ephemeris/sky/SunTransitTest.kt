package com.mkreidl.ephemeris.sky

import com.mkreidl.ephemeris.sky.RiseSetCalculator.EventType
import com.mkreidl.ephemeris.sky.RiseSetCalculator.LookupDirection
import com.mkreidl.ephemeris.solarsystem.Body
import com.mkreidl.ephemeris.solarsystem.SolarSystem
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat

class SunTransitTest {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z")

    private val searchStart = dateFormat.parse("2018-05-28 14:42:00 +0200").time
    private val nextTransit = dateFormat.parse("2018-05-29 13:11:04 +0200").time
    private val lastTransit = dateFormat.parse("2018-05-28 13:10:57 +0200").time

    @Test
    fun testForward() {
        val forward = PlanetRiseSetCalculator(SolarSystem.createFromMeeus(), Body.SUN, EventType.TRANSIT, LookupDirection.FORWARD)

        forward.geographicLocation = PlanetRiseSetTest.MUNICH
        forward.setStartTimeEpochMilli(searchStart)

        Assert.assertTrue(forward.compute())
        PlanetRiseSetTest.assert(forward, nextTransit, 1_500)
    }

    @Test
    fun testBackward() {
        val backward = PlanetRiseSetCalculator(SolarSystem.createFromMeeus(), Body.SUN, EventType.TRANSIT, LookupDirection.BACKWARD)

        backward.geographicLocation = PlanetRiseSetTest.MUNICH
        backward.setStartTimeEpochMilli(searchStart)

        Assert.assertTrue(backward.compute())
        PlanetRiseSetTest.assert(backward, lastTransit, 1_500)
    }
}
