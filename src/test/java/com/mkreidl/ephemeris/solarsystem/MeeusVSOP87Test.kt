package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.SECONDS_PER_DAY
import com.mkreidl.ephemeris.geometry.VSOP87File
import com.mkreidl.ephemeris.solarsystem.meeus.*
import com.mkreidl.math.Spherical3
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class MeeusVSOP87Test(planet: VSOP87File.Planet, dataSet: Vsop87AbstractTest.DataSet) : Vsop87AbstractTest(planet, dataSet) {

    private val expectedPos = Spherical3(dataSet.coordinates[2], dataSet.coordinates[0], dataSet.coordinates[1])
    private val expectedVel = Spherical3(dataSet.coordinates[5], dataSet.coordinates[3], dataSet.coordinates[4])

    @Throws(IllegalArgumentException::class)
    @Test
    fun testModel() {
        val model: ModelVsop87.LBR = when (planet) {
            VSOP87File.Planet.MER -> MercuryMeeus()
            VSOP87File.Planet.VEN -> VenusMeeus()
            VSOP87File.Planet.EAR -> EarthMeeus()
            VSOP87File.Planet.MAR -> MarsMeeus()
            VSOP87File.Planet.JUP -> JupiterMeeus()
            VSOP87File.Planet.SAT -> SaturnMeeus()
            VSOP87File.Planet.URA -> UranusMeeus()
            VSOP87File.Planet.NEP -> NeptuneMeeus()
            else -> throw IllegalArgumentException("Planet not found")
        }
        var (actualPos, actualVel) = model.compute(instant).spherical
        actualPos = actualPos.reduce()
        assertEquals(expectedPos.dst, actualPos.dst, 1.2e-4)  // 1.2e-4 AU = 18 km
        assertEquals(expectedPos.lon, actualPos.lon, 1e-5)  // 1e-5 rad = 2 arcsec
        assertEquals(expectedPos.lat, actualPos.lat, 1e-5)
        // Reference values from VSOP test files are given in [dist] per DAY
        assertEquals(expectedVel.dst, actualVel.x * SECONDS_PER_DAY, 1e-6)
        assertEquals(expectedVel.lon, actualVel.y * SECONDS_PER_DAY, 1e-6)
        assertEquals(expectedVel.lat, actualVel.z * SECONDS_PER_DAY, 1e-6)
    }

    companion object {
        @JvmStatic
        @Parameters(name = "{0} -- {1}")
        fun data() = Vsop87AbstractTest.data(VSOP87File.Version.D)
    }
}
