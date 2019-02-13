package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.geometry.ClassicalOrbitalElements
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import java.util.*

@RunWith(Parameterized::class)
class OrbitalElementsTest(M: Double, e: Double, private val tolerance: Double) {

    private val orbit = ClassicalOrbitalElements()

    init {
        orbit.meanAnom = M
        orbit.exc = e
    }

    @Test
    fun test() {
        val e = orbit.eccentricAnomaly(tolerance)
        val newM = e - orbit.exc * Math.sin(e)
        assertEquals(orbit.meanAnom, newM, tolerance)
    }

    companion object {
        @JvmStatic
        @Parameters
        fun data() = Arrays.asList(
                arrayOf<Any>(0.2, 0.0, 1.0e-13),
                arrayOf<Any>(0.2, 0.1, 1.0e-13),
                arrayOf<Any>(0.2, 0.2, 1.0e-13),
                arrayOf<Any>(0.2, 0.3, 1.0e-13),
                arrayOf<Any>(0.2, 0.4, 1.0e-13),
                arrayOf<Any>(0.2, 0.5, 1.0e-13),
                arrayOf<Any>(0.2, 0.6, 1.0e-13),
                arrayOf<Any>(0.2, 0.7, 1.0e-13),
                arrayOf<Any>(0.2, 0.8, 1.0e-13),
                arrayOf<Any>(0.4, 0.1, 1.0e-13),
                arrayOf<Any>(0.4, 0.2, 1.0e-13),
                arrayOf<Any>(0.4, 0.3, 1.0e-13),
                arrayOf<Any>(0.4, 0.4, 1.0e-13),
                arrayOf<Any>(0.4, 0.5, 1.0e-13),
                arrayOf<Any>(0.4, 0.6, 1.0e-13),
                arrayOf<Any>(0.4, 0.7, 1.0e-13),
                arrayOf<Any>(1.0, 0.1, 1.0e-13),
                arrayOf<Any>(1.0, 0.2, 1.0e-13),
                arrayOf<Any>(1.0, 0.3, 1.0e-13),
                arrayOf<Any>(1.0, 0.4, 1.0e-13),
                arrayOf<Any>(1.0, 0.5, 1.0e-13),
                arrayOf<Any>(2.0, 0.6, 1.0e-13),
                arrayOf<Any>(3.0, 0.7, 1.0e-13)
        )
    }
}
