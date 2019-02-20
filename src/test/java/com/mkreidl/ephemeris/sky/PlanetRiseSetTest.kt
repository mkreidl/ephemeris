package com.mkreidl.ephemeris.sky

import com.mkreidl.ephemeris.solarsystem.Body
import com.mkreidl.ephemeris.solarsystem.SolarSystem
import com.mkreidl.ephemeris.time.Instant
import com.mkreidl.math.Spherical3
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat

abstract class PlanetRiseSetTest(
        private val body: Body,
        private val geographicLocation: Spherical3,
        private val startTime: String,
        private val eventTime: String,
        searchDirection: RiseSetCalculator.LookupDirection,
        eventType: RiseSetCalculator.EventType
) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm Z")
    private val calculator = PlanetRiseSetCalculator(SolarSystem.createFromMeeus(), body, eventType, searchDirection)

    @Test
    fun test() {
        val eventTimeExpected = dateFormat.parse(eventTime).time
        val startTime = dateFormat.parse(startTime).time

        calculator.geographicLocation = geographicLocation
        calculator.setStartTimeEpochMilli(startTime)

        println("=== ${body} ===")
        assertTrue(calculator.compute())
        assert(calculator, eventTimeExpected, PRECISION_MS)
    }

    companion object {
        val MUNICH = Spherical3(1.0, Math.toRadians(11.58198), Math.toRadians(48.13513))
        val SVOLVAER = Spherical3(1.0, Math.toRadians(14.561583), Math.toRadians(68.234764))
        val SYDNEY = Spherical3(1.0, Math.toRadians(151.2), Math.toRadians(-33.85))
        val VANCOUVER = Spherical3(1.0, Math.toRadians(-123.12244), Math.toRadians(49.28098))
        val FAR_NORTH = Spherical3(1.0, Math.toRadians(11.498888), Math.toRadians(77.170555)) // 11d29m56s 77d10m14s

        private const val PRECISION_MS: Long = 30_000

        fun assert(calculator: RiseSetCalculator, expected: Long, precision: Long) {
            println("Expected: ${Instant.ofEpochMilli(expected)}")
            println("Actual  : ${calculator.time}")
            Assert.assertTrue(Math.abs(expected - calculator.time.epochMilli) < precision)
        }
    }
}