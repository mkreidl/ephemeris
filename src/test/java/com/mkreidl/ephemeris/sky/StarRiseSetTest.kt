package com.mkreidl.ephemeris.sky

import com.mkreidl.ephemeris.sky.RiseSetCalculator.EventType
import com.mkreidl.math.Spherical3
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class StarRiseSetTest {

    @Test
    fun testPolarisRise() {
        val polaris = StarRiseSetCalculator("Polaris", MUNICH, EventType.RISE, RiseSetCalculator.LookupDirection.FORWARD)
        assertFalse(polaris.compute(0))
    }

    @Test
    fun testPolarisSet() {
        val polaris = StarRiseSetCalculator("Polaris", MUNICH, EventType.SET, RiseSetCalculator.LookupDirection.FORWARD)
        assertFalse(polaris.compute(0))
    }

    @Test
    fun testSiriusSet() {
        val startTime = dateFormat.parse("2018-05-07 12:00 +0200").time
        val eventTimeExpected = dateFormat.parse("2018-05-07 21:43 +0200").time
        val sirius = StarRiseSetCalculator("Sirius", MUNICH, EventType.SET, RiseSetCalculator.LookupDirection.FORWARD)
        assertTrue(sirius.compute(startTime))
        val eventTimeCalculated = sirius.time.epochMilli
        println("Expected: ${dateFormat.format(Date(eventTimeExpected))}; Actual: ${dateFormat.format(Date(eventTimeCalculated + 30_000))}")
        assertTrue(Math.abs(eventTimeCalculated - eventTimeExpected) < 30_000)
    }

    @Test
    fun testSiriusRise() {
        val startTime = dateFormat.parse("2018-05-08 00:00 +0200").time
        val eventTimeExpected = dateFormat.parse("2018-05-08 12:10 +0200").time
        val sirius = StarRiseSetCalculator("Sirius", MUNICH, EventType.RISE, RiseSetCalculator.LookupDirection.FORWARD)
        assertTrue(sirius.compute(startTime))
        val eventTimeCalculated = sirius.time.epochMilli
        println("Expected: ${dateFormat.format(Date(eventTimeExpected))}; Actual: ${dateFormat.format(Date(eventTimeCalculated + 30_000))}")
        assertTrue(Math.abs(eventTimeCalculated - eventTimeExpected) < 30_000)
    }

    companion object {
        private val MUNICH = Spherical3(1.0, Math.toRadians(11.576), Math.toRadians(48.137))
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm Z")
    }
}
