package com.mkreidl.ephemeris.solarsystem

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class OrbitalElementsTest(private val M: Double, private val e: Double, private val tolerance: Double) {

    @Test
    fun test() {
        val orbit = ClassicalOrbitalElements(
                node = 0.0,
                inclination = 0.0,
                periapsis = 0.0,
                axis = 0.0,
                excentricity = e,
                meanAnomaly = M
        )
        val e = orbit.computeEccentricAnomaly(tolerance)
        val newM = e - orbit.excentricity * Math.sin(e)
        assertEquals(orbit.meanAnomaly, newM, tolerance)
    }

    companion object {
        @JvmStatic
        @Parameters
        fun data() = listOf(
                arrayOf(0.2, 0.0, 1.0e-13),
                arrayOf(0.2, 0.1, 1.0e-13),
                arrayOf(0.2, 0.2, 1.0e-13),
                arrayOf(0.2, 0.3, 1.0e-13),
                arrayOf(0.2, 0.4, 1.0e-13),
                arrayOf(0.2, 0.5, 1.0e-13),
                arrayOf(0.2, 0.6, 1.0e-13),
                arrayOf(0.2, 0.7, 1.0e-13),
                arrayOf(0.2, 0.8, 1.0e-13),
                arrayOf(0.4, 0.1, 1.0e-13),
                arrayOf(0.4, 0.2, 1.0e-13),
                arrayOf(0.4, 0.3, 1.0e-13),
                arrayOf(0.4, 0.4, 1.0e-13),
                arrayOf(0.4, 0.5, 1.0e-13),
                arrayOf(0.4, 0.6, 1.0e-13),
                arrayOf(0.4, 0.7, 1.0e-13),
                arrayOf(1.0, 0.1, 1.0e-13),
                arrayOf(1.0, 0.2, 1.0e-13),
                arrayOf(1.0, 0.3, 1.0e-13),
                arrayOf(1.0, 0.4, 1.0e-13),
                arrayOf(1.0, 0.5, 1.0e-13),
                arrayOf(2.0, 0.6, 1.0e-13),
                arrayOf(3.0, 0.7, 1.0e-13)
        )
    }
}
