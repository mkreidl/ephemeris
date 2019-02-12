package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Instant
import com.mkreidl.ephemeris.Time
import com.mkreidl.ephemeris.geometry.Spherical
import com.mkreidl.ephemeris.geometry.VSOP87File
import com.mkreidl.ephemeris.solarsystem.vsop87d.*
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class Vsop87DTest(
        planet: VSOP87File.Planet,
        timeStr: String,
        dataSet: Vsop87AbstractTest.DataSet
) : Vsop87AbstractTest(planet, timeStr, dataSet) {

    private val expectedPos = Spherical(dataSet.coordinates[2], dataSet.coordinates[0], dataSet.coordinates[1])
    private val expectedVel = Spherical(dataSet.coordinates[5], dataSet.coordinates[3], dataSet.coordinates[4])

    @Throws(IllegalArgumentException::class)
    @Test
    fun testModel() {
        val model: ModelVsop87.LBR = when (planet) {
            VSOP87File.Planet.MER -> MercuryVsop87D()
            VSOP87File.Planet.VEN -> VenusVsop87D()
            VSOP87File.Planet.EAR -> EarthVsop87D()
            VSOP87File.Planet.MAR -> MarsVsop87D()
            VSOP87File.Planet.JUP -> JupiterVsop87D()
            VSOP87File.Planet.SAT -> SaturnVsop87D()
            VSOP87File.Planet.URA -> UranusVsop87D()
            VSOP87File.Planet.NEP -> NeptuneVsop87D()
            else -> throw IllegalArgumentException("Planet not found")
        }
        var (actualPos, actualVel) = model.computeSpherical(Instant.ofEpochMilli(time.time))
        actualPos = actualPos.reduce()
        assertEquals(expectedPos.dst, actualPos.dst, 1e-9)
        assertEquals(expectedPos.lon, actualPos.lon, 1e-9)
        assertEquals(expectedPos.lat, actualPos.lat, 1e-9)
        // Reference values from VSOP test files are given in [dist] per DAY
        assertEquals(expectedVel.dst, actualVel.dst * Time.SECONDS_PER_DAY.toDouble(), 1e-6)
        assertEquals(expectedVel.lon, actualVel.lon * Time.SECONDS_PER_DAY.toDouble(), 1e-6)
        assertEquals(expectedVel.lat, actualVel.lat * Time.SECONDS_PER_DAY.toDouble(), 1e-6)
    }

    companion object {
        @JvmStatic
        @Parameters(name = "{0} -- {1}")
        fun data(): Iterable<Array<Any>> = Vsop87AbstractTest.data(VSOP87File.Version.D)
    }
}
