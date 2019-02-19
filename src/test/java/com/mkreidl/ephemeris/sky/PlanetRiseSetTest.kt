package com.mkreidl.ephemeris.sky

import com.mkreidl.ephemeris.solarsystem.Body
import com.mkreidl.math.Spherical3
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

abstract class PlanetRiseSetTest(
        val body: Body,
        geographicLocation: Spherical3,
        protected val startTime: String,
        searchDirection: RiseSetCalculator.LookupDirection,
        eventType: RiseSetCalculator.EventType,
        protected val eventTime: String
) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm Z")
    private val calculator = PlanetRiseSetCalculator(body, geographicLocation, eventType, searchDirection)

    @Test
    fun test() {
        val startTimeMs = dateFormat.parse(startTime).time
        calculator.compute(startTimeMs)
        val eventTimeCalculated = calculator.time.epochMilli
        val eventTimeExpected = dateFormat.parse(eventTime).time
        println("Expected: ${dateFormat.format(Date(eventTimeExpected))}; Actual: ${dateFormat.format(Date(eventTimeCalculated + 30_000))}")
        assertTrue(Math.abs(eventTimeCalculated - eventTimeExpected) < PRECISION_MS)
    }

    companion object {
        val MUNICH = Spherical3(1.0, Math.toRadians(11.58198), Math.toRadians(48.13513))
        val SVOLVAER = Spherical3(1.0, Math.toRadians(14.561583), Math.toRadians(68.234764))
        val SYDNEY = Spherical3(1.0, Math.toRadians(151.2), Math.toRadians(-33.85))
        val VANCOUVER = Spherical3(1.0, Math.toRadians(-123.12244), Math.toRadians(49.28098))
        val FAR_NORTH = Spherical3(1.0, Math.toRadians(11.498888), Math.toRadians(77.170555)) // 11d29m56s 77d10m14s

        private const val PRECISION_MS: Long = 30000
    }
}