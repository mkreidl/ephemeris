package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.SECONDS_PER_DAY
import com.mkreidl.ephemeris.solarsystem.vsop87d.*
import com.mkreidl.math.Spherical3
import com.mkreidl.math.Vector3
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class Vsop87DTest(planet: Planet, dataSet: AbstractVsop87Test.DataSet) : AbstractVsop87Test(planet, dataSet) {

    private val expectedPos = Spherical3(dataSet.coordinates[2], dataSet.coordinates[0], dataSet.coordinates[1])
    private val expectedVel = Vector3(dataSet.coordinates[5], dataSet.coordinates[3], dataSet.coordinates[4])

    @Test
    fun testModel() {
        val model: ModelVsop87.LBR = when (planet) {
            Planet.MER -> MercuryVsop87D()
            Planet.VEN -> VenusVsop87D()
            Planet.EAR -> EarthVsop87D()
            Planet.MAR -> MarsVsop87D()
            Planet.JUP -> JupiterVsop87D()
            Planet.SAT -> SaturnVsop87D()
            Planet.URA -> UranusVsop87D()
            Planet.NEP -> NeptuneVsop87D()
            else -> throw IllegalArgumentException("Planet not found")
        }
        var (actualPos, actualVel) = model.compute(instant).spherical
        actualPos = actualPos.reduce()
        assertEquals(expectedPos.dst, actualPos.dst, 1e-9)
        assertEquals(expectedPos.lon, actualPos.lon, 1e-9)
        assertEquals(expectedPos.lat, actualPos.lat, 1e-9)
        // Reference values from VSOP test files are given in [dist] per DAY
        assertEquals(expectedVel.x, actualVel.x * SECONDS_PER_DAY, 1e-9)
        assertEquals(expectedVel.y, actualVel.y * SECONDS_PER_DAY, 1e-9)
        assertEquals(expectedVel.z, actualVel.z * SECONDS_PER_DAY, 1e-9)
    }

    companion object {
        @JvmStatic
        @Parameters(name = "{0} -- {1}")
        fun data() = AbstractVsop87Test.data(Version.D)
    }
}
