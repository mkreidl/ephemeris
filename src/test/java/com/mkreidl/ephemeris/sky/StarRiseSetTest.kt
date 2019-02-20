package com.mkreidl.ephemeris.sky

import com.mkreidl.ephemeris.sky.RiseSetCalculator.EventType
import com.mkreidl.ephemeris.time.Instant
import com.mkreidl.math.Spherical3
import org.junit.Assert.*
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class StarRiseSetTest {

    @Test
    fun testPolarisRise() {
        val calculator = StarRiseSetCalculator("Polaris", EventType.RISE, RiseSetCalculator.LookupDirection.FORWARD)

        calculator.geographicLocation = MUNICH
        calculator.setStartTimeEpochMilli(0)
        assertFalse(calculator.compute())
    }

    @Test
    fun testPolarisSet() {
        val calculator = StarRiseSetCalculator("Polaris", EventType.SET, RiseSetCalculator.LookupDirection.FORWARD)

        calculator.geographicLocation = MUNICH
        calculator.setStartTimeEpochMilli(0)
        assertFalse(calculator.compute())
    }

    @Test
    fun testPolarisTransit() {
        val startTime = dateFormat.parse("2018-05-09 00:00 +0200").time
        val eventTimeExpected = dateFormat.parse("2018-05-09 12:59 +0200").time
        val calculator = StarRiseSetCalculator("Polaris", EventType.TRANSIT, RiseSetCalculator.LookupDirection.FORWARD)

        calculator.geographicLocation = MUNICH
        calculator.setStartTimeEpochMilli(startTime)
        assertTrue(calculator.compute())
        PlanetRiseSetTest.assert(calculator, eventTimeExpected, 40_000)
    }

    @Test
    fun testSiriusSet() {
        val startTime = dateFormat.parse("2018-05-07 12:00 +0200").time
        val eventTimeExpected = dateFormat.parse("2018-05-07 21:43 +0200").time
        val calculator = StarRiseSetCalculator("Sirius", EventType.SET, RiseSetCalculator.LookupDirection.FORWARD)

        calculator.geographicLocation = MUNICH
        calculator.setStartTimeEpochMilli(startTime)

        assertTrue(calculator.compute())
        PlanetRiseSetTest.assert(calculator, eventTimeExpected, 30_000)
    }

    @Test
    fun testSiriusRise() {
        val startTime = dateFormat.parse("2018-05-08 00:00 +0200").time
        val eventTimeExpected = dateFormat.parse("2018-05-08 12:10 +0200").time
        val calculator = StarRiseSetCalculator("Sirius", EventType.RISE, RiseSetCalculator.LookupDirection.FORWARD)

        calculator.geographicLocation = MUNICH
        calculator.setStartTimeEpochMilli(startTime)

        assertTrue(calculator.compute())
        PlanetRiseSetTest.assert(calculator, eventTimeExpected, 30_000)
    }

    @Test
    fun testSiriusTransit() {
        val startTime = dateFormat.parse("2018-05-08 00:00 +0200").time
        val eventTimeExpected = dateFormat.parse("2018-05-08 16:54 +0200").time
        val calculator = StarRiseSetCalculator("Sirius", EventType.TRANSIT, RiseSetCalculator.LookupDirection.FORWARD)

        calculator.geographicLocation = MUNICH
        calculator.setStartTimeEpochMilli(startTime)

        assertTrue(calculator.compute())
        PlanetRiseSetTest.assert(calculator, eventTimeExpected, 30_000)
    }

    companion object {
        private val MUNICH = Spherical3(1.0, Math.toRadians(11.576), Math.toRadians(48.137))
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm Z")
    }
}
