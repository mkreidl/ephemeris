package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Angle
import com.mkreidl.ephemeris.Instant
import org.junit.Assert
import org.junit.Test

class SunLowPrecisionTest {

    // Meeus Exampla 24.a

    private val october13_1992 = Instant.ofJulianDayFraction(2_448_908.50)  // October 13, 1992, 0hTD
    private val sun = SunLowPrecision(october13_1992)

    @Test
    fun testTime() {
        Assert.assertEquals(-0.072_183_436, october13_1992.julianCenturiesSinceJ2000, 1e-9)
        Assert.assertEquals(-0.007_218_343_6, october13_1992.julianMillenniaSinceJ2000, 1e-9)
    }

    @Test
    fun testExcentricity() {
        Assert.assertEquals(0.016_711_651, sun.excentricity, 1e-9)
    }

    @Test
    fun testMeanLongitude() {
        Assert.assertEquals(Angle.ofDeg(201.807_193).radians, sun.meanLongitude, 1e-8)
    }

    @Test
    fun testMeanAnomaly() {
        Assert.assertEquals(Angle.ofDeg(278.993_96).radians, sun.meanAnomaly, 1e-7)
    }

    @Test
    fun testEquationOfCenter() {
        Assert.assertEquals(Angle.ofDeg(-1.897_32).radians, sun.equationOfCenter, 1e-7)
    }

    @Test
    fun testTrueLongitude() {
        Assert.assertEquals(Angle.ofDeg(199.909_87).radians, sun.geometricLongitude, 1e-7)
    }

    @Test
    fun testNode() {
        Assert.assertEquals(Angle.ofDeg(264.65).radians, sun.omega, 1e-4)
    }

    @Test
    fun testApparentLongitude() {
        Assert.assertEquals(Angle.ofDeg(199.908_94).radians, sun.apparentLongitude, 1e-7)
    }

    @Test
    fun testApparentRightAscension() {
        Assert.assertEquals(Angle.ofDeg(-161.619_18).radians, sun.apparentRightAscension, 1e-6)
    }

    @Test
    fun testTrueLongitudeNovember2028() {
        val november13_2028 = Instant.ofJulianDayFraction(2_462_088.69)  // November 13.19, 2028
        Assert.assertEquals(Angle.ofDeg(231.328).radians, SunLowPrecision(november13_2028).geometricLongitude, 1e-5)
    }
}