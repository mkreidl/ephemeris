package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.solarsystem.TestUtil.EphemerisData
import com.mkreidl.ephemeris.time.Instant
import com.mkreidl.math.Angle
import com.mkreidl.math.Sexagesimal
import com.mkreidl.math.times
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import java.util.*

/**
 * Parses txt test data produced by http://ephemeris.com/ephemeris.php
 */
@RunWith(Parameterized::class)
class SolarSystemGeocentricTest(testname: String, private val body: Body, private val instant: Instant, private val expected: EphemerisData) {

    private var tol = 0.0

    @Test
    fun testGeocentricCoordinatesMeeus() {
        tol = when (body) {
            Body.MOON -> 4.0 * Angle.minute
            Body.PLUTO -> 0.8 * Angle.minute
            else -> 0.8 * Angle.minute
        }.radians
        val solarSystem = SolarSystem.createFromMeeus()
        solarSystem.compute(instant)
        test(solarSystem)
    }

    @Test
    fun testGeocentricCoordinatesVsop87() {
        tol = when (body) {
            Body.MOON -> 4.0 * Angle.minute
            Body.PLUTO -> 0.8 * Angle.minute  // = 48 sec
            else -> 12.0 * Angle.second  // = 12 sec
        }.radians
        val solarSystem = SolarSystem.createFromVsop87C()
        solarSystem.compute(instant)
        test(solarSystem)
    }

    private fun test(solarSystem: SolarSystem) {
        val ecliptical = solarSystem.getTrueEclipticalGeocentric(body).position.spherical
        val equatorial = solarSystem.getTrueEquatorialGeocentric(body).position.spherical

        val longitude = Angle.ofRad(ecliptical.lon)
        val latitude = Angle.ofRad(ecliptical.lat)

        val rightAscension = Angle.ofRad(equatorial.lon)
        val declination = Angle.ofRad(equatorial.lat)

        println("-------------------------------------")
        println("$instant -- $body")
        println(instant.julianDayFraction)
        println("-------------------------------------")

        println("Distance: " + solarSystem.getGeocentricDistance(body))
        println("${Sexagesimal.of(expected.longitude.degreesNonNeg)}  | ecliptical longitude |  ${Sexagesimal.of(longitude.degreesNonNeg)}")
        println("${Sexagesimal.of(expected.latitude.degreesNonNeg)}  | ecliptical latitude  |  ${Sexagesimal.of(latitude.degreesNonNeg)}")
        println("${Sexagesimal.of(expected.rightAscension.hoursNonNeg)}  | equatorial longitude |  ${Sexagesimal.of(rightAscension.hoursNonNeg)}")
        println("${Sexagesimal.of(expected.declination.degreesNonNeg)}  | equatorial latitude  |  ${Sexagesimal.of(declination.degreesNonNeg)}")
        println("expected: ${r(expected.retrograde)}  |  actual: ${r(solarSystem.getTrueEclipticalGeocentric(body).retrograde)}")
        println("Angular velocity [sec]: ${solarSystem.getTrueEclipticalGeocentric(body).angularVelocity * 3_600}")

        assertEquals(expected.longitude.radians, longitude.radians, tol)
        assertEquals(expected.latitude.radians, latitude.radians, tol)
        assertEquals(expected.rightAscension.radians, rightAscension.radians, tol)
        assertEquals(expected.declination.radians, declination.radians, tol)
        assertEquals(expected.retrograde, solarSystem.getTrueEclipticalGeocentric(body).retrograde)
    }

    private fun r(isRetrograde: Boolean) = if (isRetrograde) "R" else "-"

    companion object {
        @JvmStatic
        @Parameters(name = "{0}")
        fun data() = TestUtil.solarSystemData(Arrays.asList(*Body.values()))
    }
}
