package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.time.Instant
import com.mkreidl.ephemeris.util.GeocentricEphemTestUtil
import com.mkreidl.ephemeris.util.GeocentricEphemTestUtil.EphemerisData
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class ElongationTest(body: Body, private val instant: Instant, private val expected: EphemerisData) {
    private val solarSystem = SolarSystem.createFromMeeus()

    @Test
    fun testMoonElongation() {
        solarSystem.compute(instant)
        val phase = solarSystem.getElongation(Body.MOON)
        // expected.phase has an accuracy to only 0.36, since input data (accuracy 1e-3) was multiplied with 360°
        Assert.assertEquals(expected.phase, phase.degreesNonNeg, 0.3)
    }

    companion object {
        @JvmStatic
        @Parameters(name = "{0} -- {1}")
        fun data() = GeocentricEphemTestUtil.data(listOf(Body.MOON))
    }
}
