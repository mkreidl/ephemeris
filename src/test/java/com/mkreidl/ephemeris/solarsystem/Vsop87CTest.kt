package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.time.Instant
import com.mkreidl.ephemeris.Time
import com.mkreidl.ephemeris.geometry.Cartesian
import com.mkreidl.ephemeris.geometry.VSOP87File
import com.mkreidl.ephemeris.solarsystem.vsop87c.*
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class Vsop87CTest(
        planet: VSOP87File.Planet,
        timeStr: String,
        dataSet: Vsop87AbstractTest.DataSet
) : Vsop87AbstractTest(planet, timeStr, dataSet) {

    private val expectedPos = Cartesian(dataSet.coordinates[0], dataSet.coordinates[1], dataSet.coordinates[2])
    private val expectedVel = Cartesian(dataSet.coordinates[3], dataSet.coordinates[4], dataSet.coordinates[5])

    @Throws(IllegalArgumentException::class)
    @Test
    fun testModel() {
        val model: ModelVsop87.XYZ = when (planet) {
            VSOP87File.Planet.MER -> MercuryVsop87C()
            VSOP87File.Planet.VEN -> VenusVsop87C()
            VSOP87File.Planet.EAR -> EarthVsop87C()
            VSOP87File.Planet.MAR -> MarsVsop87C()
            VSOP87File.Planet.JUP -> JupiterVsop87C()
            VSOP87File.Planet.SAT -> SaturnVsop87C()
            VSOP87File.Planet.URA -> UranusVsop87C()
            VSOP87File.Planet.NEP -> NeptuneVsop87C()
            else -> throw IllegalArgumentException("Planet not found")
        }
        val (actualPos, actualVel) = model.computeCartesian(Instant.ofEpochMilli(time.time))
        assertEquals(expectedPos.x, actualPos.x, 1e-10)
        assertEquals(expectedPos.y, actualPos.y, 1e-10)
        assertEquals(expectedPos.z, actualPos.z, 1e-10)
        // Reference values from VSOP test files are given in [dist] per DAY
        assertEquals(expectedVel.x, actualVel.x * Time.SECONDS_PER_DAY.toDouble(), 1e-6)
        assertEquals(expectedVel.y, actualVel.y * Time.SECONDS_PER_DAY.toDouble(), 1e-6)
        assertEquals(expectedVel.z, actualVel.z * Time.SECONDS_PER_DAY.toDouble(), 1e-6)
    }

    companion object {
        @JvmStatic
        @Parameters(name = "{0} -- {1}")
        fun data(): Iterable<Array<Any>> = Vsop87AbstractTest.data(VSOP87File.Version.C)
    }
}
