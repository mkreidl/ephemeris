package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.SECONDS_PER_DAY
import com.mkreidl.ephemeris.solarsystem.vsop87.ModelVsop87
import com.mkreidl.ephemeris.solarsystem.vsop87.c.*
import com.mkreidl.ephemeris.time.Instant
import com.mkreidl.ephemeris.util.Vsop87TestUtil
import com.mkreidl.math.Vector3
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class Vsop87CTest(private val planet: Vsop87TestUtil.Planet, private val instant: Instant, coordinates: DoubleArray) {

    private val expectedPos = Vector3(coordinates[0], coordinates[1], coordinates[2])
    private val expectedVel = Vector3(coordinates[3], coordinates[4], coordinates[5])

    @Test
    fun testModel() {
        val model: ModelVsop87.XYZ = when (planet) {
            Vsop87TestUtil.Planet.MER -> MercuryVsop87C()
            Vsop87TestUtil.Planet.VEN -> VenusVsop87C()
            Vsop87TestUtil.Planet.EAR -> EarthVsop87C()
            Vsop87TestUtil.Planet.MAR -> MarsVsop87C()
            Vsop87TestUtil.Planet.JUP -> JupiterVsop87C()
            Vsop87TestUtil.Planet.SAT -> SaturnVsop87C()
            Vsop87TestUtil.Planet.URA -> UranusVsop87C()
            Vsop87TestUtil.Planet.NEP -> NeptuneVsop87C()
            else -> throw IllegalArgumentException("Planet not found")
        }
        val (actualPos, actualVel) = model.compute(instant).cartesian
        assertEquals(expectedPos.x, actualPos.x, 1e-10)
        assertEquals(expectedPos.y, actualPos.y, 1e-10)
        assertEquals(expectedPos.z, actualPos.z, 1e-10)
        // Reference values from VSOP test files are given in [dist] per DAY
        assertEquals(expectedVel.x, actualVel.x * SECONDS_PER_DAY, 1e-8)
        assertEquals(expectedVel.y, actualVel.y * SECONDS_PER_DAY, 1e-8)
        assertEquals(expectedVel.z, actualVel.z * SECONDS_PER_DAY, 1e-9)
    }

    companion object {
        @JvmStatic
        @Parameters(name = "{0} -- {1}")
        fun data(): Iterable<Array<Any>> = Vsop87TestUtil.data(Vsop87TestUtil.Version.C)
    }
}
