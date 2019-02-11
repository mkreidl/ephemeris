package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.*
import com.mkreidl.ephemeris.TestUtil.EphemerisData
import com.mkreidl.ephemeris.solarsystem.meeus.*
import com.mkreidl.ephemeris.solarsystem.vsop87c.*
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
class SolarSystemGeocentricTest(testname: String, private val body: Body, private val time: Time, private val expected: EphemerisData) {

    private val modelsMeeus = mapOf(
            Body.MERCURY to MercuryMeeus(),
            Body.VENUS to VenusMeeus(),
            Body.EARTH to EarthMeeus(),
            Body.MARS to MarsMeeus(),
            Body.JUPITER to JupiterMeeus(),
            Body.SATURN to SaturnMeeus(),
            Body.URANUS to UranusMeeus(),
            Body.NEPTUNE to NeptuneMeeus(),
            Body.MOON to ModelMoon(),
            Body.PLUTO to ModelPluto()
    )

    private val modelsVsop87 = mapOf(
            Body.MERCURY to MercuryVsop87C(),
            Body.VENUS to VenusVsop87C(),
            Body.EARTH to EarthVsop87C(),
            Body.MARS to MarsVsop87C(),
            Body.JUPITER to JupiterVsop87C(),
            Body.SATURN to SaturnVsop87C(),
            Body.URANUS to UranusVsop87C(),
            Body.NEPTUNE to NeptuneVsop87C(),
            Body.MOON to ModelMoon(),
            Body.PLUTO to ModelPluto()
    )

    private var tol = 0.0

    @Test
    fun testGeocentricCoordinatesMeeus() {
        tol = when (body) {
            Body.MOON -> 4.0 * Angle.minute
            Body.PLUTO -> 0.8 * Angle.minute
            else -> 0.8 * Angle.minute
        }.radians
        val solarSystem = FullSolarSystem(modelsMeeus)
        solarSystem.compute(time)
        test(solarSystem)
    }

    @Test
    fun testGeocentricCoordinatesVsop87() {
        tol = when (body) {
            Body.MOON -> 4.0 * Angle.minute
            Body.PLUTO -> 0.8 * Angle.minute  // = 48 sec
            else -> 12.0 * Angle.second  // = 12 sec
        }.radians
        val solarSystem = FullSolarSystem(modelsVsop87)
        solarSystem.compute(time)
        test(solarSystem)
    }

    private fun test(solarSystem: FullSolarSystem) {
        val ecliptical = solarSystem.getTrueEclipticalGeocentric(body).position.toSpherical()
        val equatorial = solarSystem.getTrueEquatorialGeocentric(body).position.toSpherical()
        //val horizontal = solarSystem.getTrueEclipticalGeocentric(body).position.toSpherical()

        val longitude = Angle.reducePositive(ecliptical.lon)
        val latitude = ecliptical.lat

        val rightAscension = Angle.reducePositive(equatorial.lon)
        val declination = equatorial.lat

        println("-------------------------------------")
        println("$time -- $body")
        println(time.julianDayNumber())
        println("-------------------------------------")

        println("Distance: " + solarSystem.getGeocentricDistance(body))
        println(String.format("%s  | ecliptical longitude |  %s", expected.longitude, Sexagesimal.of(Angle.ofRad(longitude).degreesNonNeg)))
        println(String.format("%s  | ecliptical latitude  |  %s", expected.latitude, latitude))
        println(String.format("%s  | equatorial longitude |  %s", expected.rightAscension, rightAscension))
        println(String.format("%s  | equatorial latitude  |  %s", expected.declination, declination))
        println("expected: " + (if (expected.retrograde) "R" else "-") + "  |  actual: " + if (solarSystem.isRetrograde(body)) "R" else "-")
        println("Angular velocity [sec]: ${solarSystem.getAngularVelocity(body) * 3_600}")

        assertEquals(expected.longitude.radians, longitude, tol)
        assertEquals(expected.latitude.radians, latitude, tol)
        assertEquals(expected.rightAscension.radians, rightAscension, tol)
        assertEquals(expected.declination.radians, declination, tol)
        assertEquals(expected.retrograde, solarSystem.isRetrograde(body))
    }

    companion object {
        @JvmStatic
        @Parameters(name = "{0}")
        fun data() = TestUtil.solarSystemData(Arrays.asList(*Body.values()))
    }
}
