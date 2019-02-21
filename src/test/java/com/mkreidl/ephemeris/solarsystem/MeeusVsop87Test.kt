package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.SECONDS_PER_DAY
import com.mkreidl.ephemeris.solarsystem.meeus.*
import com.mkreidl.ephemeris.solarsystem.vsop87.ModelVsop87
import com.mkreidl.ephemeris.time.Instant
import com.mkreidl.ephemeris.util.Vsop87TestUtil
import com.mkreidl.math.Spherical3
import com.mkreidl.math.Vector3
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class MeeusVsop87Test(private val planet: Vsop87TestUtil.Planet, private val instant: Instant, coordinates: DoubleArray) {

    private val expectedPos = Spherical3(coordinates[2], coordinates[0], coordinates[1])
    private val expectedVel = Vector3(coordinates[5], coordinates[3], coordinates[4])

    @Throws(IllegalArgumentException::class)
    @Test
    fun testModel() {
        val model: ModelVsop87.LBR = when (planet) {
            Vsop87TestUtil.Planet.MER -> MercuryMeeus.createModel()
            Vsop87TestUtil.Planet.VEN -> VenusMeeus.createModel()
            Vsop87TestUtil.Planet.EAR -> EarthMeeus.createModel()
            Vsop87TestUtil.Planet.MAR -> MarsMeeus.createModel()
            Vsop87TestUtil.Planet.JUP -> JupiterMeeus.createModel()
            Vsop87TestUtil.Planet.SAT -> SaturnMeeus.createModel()
            Vsop87TestUtil.Planet.URA -> UranusMeeus.createModel()
            Vsop87TestUtil.Planet.NEP -> NeptuneMeeus.createModel()
            else -> throw IllegalArgumentException("Planet not found")
        }
        var (actualPos, actualVel) = model.compute(instant).spherical
        actualPos = actualPos.reduce()
        assertEquals(expectedPos.dst, actualPos.dst, 1.2e-4)  // 1.2e-4 AU = 18 km
        assertEquals(expectedPos.lon, actualPos.lon, 1e-5)  // 1e-5 rad = 2 arcsec
        assertEquals(expectedPos.lat, actualPos.lat, 1e-5)
        // Reference values from VSOP test files are given in [dist] per DAY
        assertEquals(expectedVel.x, actualVel.x * SECONDS_PER_DAY, 1e-6)
        assertEquals(expectedVel.y, actualVel.y * SECONDS_PER_DAY, 1e-6)
        assertEquals(expectedVel.z, actualVel.z * SECONDS_PER_DAY, 1e-6)
    }

    companion object {
        @JvmStatic
        @Parameters(name = "{0} -- {1}")
        fun data() = Vsop87TestUtil.data(Vsop87TestUtil.Version.D)
    }
}
