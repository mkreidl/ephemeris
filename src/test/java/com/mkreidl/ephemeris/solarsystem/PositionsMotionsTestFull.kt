package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.TestUtil
import com.mkreidl.ephemeris.TestUtil.EphemerisData
import com.mkreidl.ephemeris.Time
import com.mkreidl.ephemeris.geometry.Angle
import com.mkreidl.ephemeris.solarsystem.meeus.*
import com.mkreidl.ephemeris.solarsystem.vsop87c.Earth
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
class PositionsMotionsTestFull(testname: String, private val body: Body, private val time: Time, private val expected: EphemerisData) {

    private val modelsMeeus = mapOf(
            Body.EARTH to ModelVsop87.XYZ(Earth.getCoefficients()),
            Body.SUN to ModelSun(),
            Body.MERCURY to ModelVsop87.LBR(Mercury.getCoefficients()),
            Body.VENUS to ModelVsop87.LBR(Venus.getCoefficients()),
            Body.MARS to ModelVsop87.LBR(Mars.getCoefficients()),
            Body.JUPITER to ModelVsop87.LBR(Jupiter.getCoefficients()),
            Body.SATURN to ModelVsop87.LBR(Saturn.getCoefficients()),
            Body.URANUS to ModelVsop87.LBR(Uranus.getCoefficients()),
            Body.NEPTUNE to ModelVsop87.LBR(Neptune.getCoefficients()),
            Body.MOON to ModelMoon(),
            Body.PLUTO to ModelPluto()
    )

    private val modelsVsop87 = mapOf(
            Body.EARTH to ModelVsop87.XYZ(Earth.getCoefficients()),
            Body.SUN to ModelSun(),
            Body.MERCURY to ModelVsop87.XYZ(com.mkreidl.ephemeris.solarsystem.vsop87c.Mercury.getCoefficients()),
            Body.VENUS to ModelVsop87.XYZ(com.mkreidl.ephemeris.solarsystem.vsop87c.Venus.getCoefficients()),
            Body.MARS to ModelVsop87.XYZ(com.mkreidl.ephemeris.solarsystem.vsop87c.Mars.getCoefficients()),
            Body.JUPITER to ModelVsop87.XYZ(com.mkreidl.ephemeris.solarsystem.vsop87c.Jupiter.getCoefficients()),
            Body.SATURN to ModelVsop87.XYZ(com.mkreidl.ephemeris.solarsystem.vsop87c.Saturn.getCoefficients()),
            Body.URANUS to ModelVsop87.XYZ(com.mkreidl.ephemeris.solarsystem.vsop87c.Uranus.getCoefficients()),
            Body.NEPTUNE to ModelVsop87.XYZ(com.mkreidl.ephemeris.solarsystem.vsop87c.Neptune.getCoefficients()),
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
        fun data(): Iterable<Array<Any>> {
            return TestUtil.solarSystemData(Arrays.asList(*Body.values()))
        }
    }
}
