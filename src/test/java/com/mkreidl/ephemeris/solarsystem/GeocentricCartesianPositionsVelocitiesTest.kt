package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.TestUtil
import com.mkreidl.ephemeris.TestUtil.EphemerisData
import com.mkreidl.ephemeris.Time
import com.mkreidl.ephemeris.geometry.Angle
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
class GeocentricCartesianPositionsVelocitiesTest(testname: String, private val body: Body, private val time: Time, private val expected: EphemerisData) {

    private val modelsMeeus = mapOf(
            Body.EARTH to EarthVsop87C(),
            Body.SUN to ModelSun(),
            Body.MERCURY to MercuryMeeus(),
            Body.VENUS to VenusMeeus(),
            Body.MARS to MarsMeeus(),
            Body.JUPITER to JupiterMeeus(),
            Body.SATURN to SaturnMeeus(),
            Body.URANUS to UranusMeeus(),
            Body.NEPTUNE to NeptuneMeeus(),
            Body.MOON to ModelMoon(),
            Body.PLUTO to ModelPluto()
    )

    private val modelsVsop87 = mapOf(
            Body.EARTH to EarthVsop87C(),
            Body.SUN to ModelSun(),
            Body.MERCURY to MercuryVsop87C(),
            Body.VENUS to VenusVsop87C(),
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
            Body.MOON -> 4 * Angle.MIN
            Body.PLUTO -> 0.8 * Angle.MIN
            else -> 0.58 * Angle.MIN
        }
        val solarSystem = FullSolarSystem(modelsMeeus)
        solarSystem.compute(time)
        test(solarSystem)
    }

    @Test
    fun testGeocentricCoordinatesVsop87() {
        tol = when (body) {
            Body.MOON -> 4 * Angle.MIN
            Body.PLUTO -> 0.8 * Angle.MIN  // = 48 sec
            else -> 0.2 * Angle.MIN  // = 12 sec
        }
        val solarSystem = FullSolarSystem(modelsVsop87)
        solarSystem.compute(time)
        test(solarSystem)
    }

    private fun test(solarSystem: FullSolarSystem) {
        val ecliptical = solarSystem.getTrueEclipticalGeocentric(body).position.toSpherical()
        val equatorial = solarSystem.getTrueEquatorialGeocentric(body).position.toSpherical()
        //val horizontal = solarSystem.getTrueEclipticalGeocentric(body).position.toSpherical()

        val longitude = Angle.Sexagesimal(Angle.Unit.DEGREES).setRadians(Angle.standardizePositive(ecliptical.lon))
        val latitude = Angle.Sexagesimal(Angle.Unit.DEGREES).setRadians(ecliptical.lat)

        val rightAscension = Angle.Sexagesimal(Angle.Unit.HOURS).setRadians(Angle.standardizePositive(equatorial.lon))
        val declination = Angle.Sexagesimal(Angle.Unit.DEGREES).setRadians(equatorial.lat)

        println("-------------------------------------")
        println("$time -- $body")
        println(time.julianDayNumber())
        println("-------------------------------------")

        println("Distance: " + solarSystem.getGeocentricDistance(body))
        println(String.format("%s  | ecliptical longitude |  %s", expected.longitude, longitude))
        println(String.format("%s  | ecliptical latitude  |  %s", expected.latitude, latitude))
        println(String.format("%s  | equatorial longitude |  %s", expected.rightAscension, rightAscension))
        println(String.format("%s  | equatorial latitude  |  %s", expected.declination, declination))
        println("expected: " + (if (expected.retrograde) "R" else "-") + "  |  actual: " + if (solarSystem.isRetrograde(body)) "R" else "-")
        println("Angular velocity [sec]: ${solarSystem.getAngularVelocity(body) * 3_600}")

        assertEquals(expected.longitude.radians, longitude.radians, tol)
        assertEquals(expected.latitude.radians, latitude.radians, tol)
        assertEquals(expected.rightAscension.radians, rightAscension.radians, tol)
        assertEquals(expected.declination.radians, declination.radians, tol)
        assertEquals(expected.retrograde, solarSystem.isRetrograde(body))
    }

    companion object {
        @JvmStatic
        @Parameters(name = "{0}")
        fun data(): Iterable<Array<Any>> = TestUtil.solarSystemData(Arrays.asList(*Body.values()))
    }
}
