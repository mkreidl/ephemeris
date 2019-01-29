package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Time
import com.mkreidl.ephemeris.geometry.Spherical
import com.mkreidl.ephemeris.geometry.VSOP87File
import com.mkreidl.ephemeris.solarsystem.meeus.Earth
import com.mkreidl.ephemeris.solarsystem.meeus.Jupiter
import com.mkreidl.ephemeris.solarsystem.meeus.Mars
import com.mkreidl.ephemeris.solarsystem.meeus.Mercury
import com.mkreidl.ephemeris.solarsystem.meeus.Neptune
import com.mkreidl.ephemeris.solarsystem.meeus.Saturn
import com.mkreidl.ephemeris.solarsystem.meeus.Uranus
import com.mkreidl.ephemeris.solarsystem.meeus.Venus

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

import org.junit.Assert.assertEquals

@RunWith(Parameterized::class)
class MeeusTest(planet: VSOP87File.Planet, timeStr: String, dataSet: Vsop87AbstractTest.DataSet) : Vsop87AbstractTest(planet, timeStr, dataSet) {

    private val expectedPos = Spherical(dataSet.coordinates[2], dataSet.coordinates[0], dataSet.coordinates[1])
    private val expectedVel = Spherical(dataSet.coordinates[5], dataSet.coordinates[3], dataSet.coordinates[4])

    @Test
    fun testModel() {
        val model: ModelVsop87.LBR = when (planet) {
            VSOP87File.Planet.MER -> ModelVsop87.LBR(Mercury.getCoefficients())
            VSOP87File.Planet.VEN -> ModelVsop87.LBR(Venus.getCoefficients())
            VSOP87File.Planet.EAR -> ModelVsop87.LBR(Earth.getCoefficients())
            VSOP87File.Planet.MAR -> ModelVsop87.LBR(Mars.getCoefficients())
            VSOP87File.Planet.JUP -> ModelVsop87.LBR(Jupiter.getCoefficients())
            VSOP87File.Planet.SAT -> ModelVsop87.LBR(Saturn.getCoefficients())
            VSOP87File.Planet.URA -> ModelVsop87.LBR(Uranus.getCoefficients())
            VSOP87File.Planet.NEP -> ModelVsop87.LBR(Neptune.getCoefficients())
            else -> throw IllegalArgumentException("Planet not found")
        }
        val (actualPos, actualVel) = model.computeSpherical(time)
        //assertEquals( expectedPos.dst, actualPos.dst, 1.2e-4 );  // 1.2e-4 AU = 18 km
        assertEquals(expectedPos.lon, actualPos.lon, 1e-5)  // 1e-5 rad = 2 arcsec
        assertEquals(expectedPos.lat, actualPos.lat, 1e-5)
        // Reference values from VSOP test files are given in [dist] per DAY
        //assertEquals( expectedVel.dst, actualVel.dst * Time.SECONDS_PER_DAY.toDouble(), 1e-6 );
        assertEquals(expectedVel.lon, actualVel.lon * Time.SECONDS_PER_DAY.toDouble(), 1e-6)
        assertEquals(expectedVel.lat, actualVel.lat * Time.SECONDS_PER_DAY.toDouble(), 1e-6)
    }

    companion object {
        @Parameters(name = "{0} -- {1}")
        fun data(): Iterable<Array<Any>> {
            return Vsop87AbstractTest.data(VSOP87File.Version.D)
        }
    }
}
