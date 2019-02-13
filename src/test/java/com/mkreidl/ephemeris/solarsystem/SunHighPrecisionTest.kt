package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.math.Angle
import com.mkreidl.ephemeris.time.Instant
import org.junit.Assert
import org.junit.Test

class SunHighPrecisionTest {

    // Meeus Exampla 24.b

    private val october13_1992 = Instant.ofJulianDayFraction(2_448_908.50)  // October 13, 1992, 0hTD
    private val sun = SunHighPrecision(october13_1992)

    @Test
    fun testGeometricLongitude() {
        Assert.assertEquals(Angle.ofDeg(199, 54, 26.45).radians, sun.geometricLongitude, 5e-7)
    }

    @Test
    fun testGeometricLatitude() {
        Assert.assertEquals(Angle.ofDeg(0, 0, 0.62).radians, sun.geometricLatitude, 2e-7)
    }

    @Test
    fun testApparentLongitude() {
        Assert.assertEquals(Angle.ofDeg(199, 54, 21.82).radians, sun.apparentLongitude, 1e-6)
    }

    @Test
    fun testApparentRightAscension() {
        Assert.assertEquals(Angle.ofHrs(13, 13, 30.763).radians, sun.apparentRightAscension, 1e-6)
    }

    // Meeus Example 27.a

    @Test
    fun testEquationOfTime() {
        Assert.assertEquals(Angle.ofDeg(3.427_351).radians, sun.equationOfTime, 1e-6)
    }
}
