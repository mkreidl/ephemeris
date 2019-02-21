package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.SECONDS_PER_DAY
import com.mkreidl.ephemeris.solarsystem.vsop87.ModelVsop87
import com.mkreidl.ephemeris.solarsystem.vsop87.c.*
import com.mkreidl.math.Vector3
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class Vsop87CTest(planet: Planet, dataSet: AbstractVsop87Test.DataSet) : AbstractVsop87Test(planet, dataSet) {

    private val expectedPos = Vector3(dataSet.coordinates[0], dataSet.coordinates[1], dataSet.coordinates[2])
    private val expectedVel = Vector3(dataSet.coordinates[3], dataSet.coordinates[4], dataSet.coordinates[5])

    @Test
    fun testModel() {
        val model: ModelVsop87.XYZ = when (planet) {
            Planet.MER -> MercuryVsop87C()
            Planet.VEN -> VenusVsop87C()
            Planet.EAR -> EarthVsop87C()
            Planet.MAR -> MarsVsop87C()
            Planet.JUP -> JupiterVsop87C()
            Planet.SAT -> SaturnVsop87C()
            Planet.URA -> UranusVsop87C()
            Planet.NEP -> NeptuneVsop87C()
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
        fun data(): Iterable<Array<Any>> = AbstractVsop87Test.data(Version.C)
    }
}
