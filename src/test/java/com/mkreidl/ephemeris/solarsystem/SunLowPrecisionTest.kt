package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Instant
import org.junit.Assert
import org.junit.Test

class SunLowPrecisionTest {

    private val october13_1992 = Instant.ofJulianDayFraction(2_448_908.50)  // October 13, 1992, 0hTD

    @Test
    fun testTime() {
        Assert.assertEquals(-0.072_183_436, october13_1992.julianCenturiesSinceJ2000, 1e-9)
        Assert.assertEquals(-0.007_218_343_6, october13_1992.julianMillenniaSinceJ2000, 1e-9)
    }

    @Test
    fun testExcentricity() {
        Assert.assertEquals(0.016_711_651, SunLowPrecision(october13_1992).excentricity, 1e-9)
    }

    @Test
    fun testMeanLongitude() {
        Assert.assertEquals(Math.toRadians(201.807_193 - 360.0), SunLowPrecision(october13_1992).meanLongitude, 1e-8)
    }

    @Test
    fun testMeanAnomaly() {
        Assert.assertEquals(Math.toRadians(278.993_96 - 360.0), SunLowPrecision(october13_1992).meanAnomaly, 1e-7)
    }

    @Test
    fun testEquationOfCenter() {
        Assert.assertEquals(Math.toRadians(-1.897_32), SunLowPrecision(october13_1992).equationOfCenter, 1e-7)
    }

    @Test
    fun testTrueLongitude() {
        Assert.assertEquals(Math.toRadians(199.909_87 - 360.0), SunLowPrecision(october13_1992).trueLongitude, 1e-7)
    }

    @Test
    fun testNode() {
        Assert.assertEquals(Math.toRadians(264.65 - 360.0), SunLowPrecision(october13_1992).omega, 1e-4)
    }

    @Test
    fun testApparentLongitude() {
        Assert.assertEquals(Math.toRadians(199.908_94 - 360.0), SunLowPrecision(october13_1992).apparentLongitude, 1e-7)
    }

    @Test
    fun testApparentRightAscension() {
        Assert.assertEquals(Math.toRadians(-161.619_18), SunLowPrecision(october13_1992).apparentRightAscension, 1e-6)
    }

    @Test
    fun testTrueLongitudeNovember2028() {
        val november13_2028 = Instant.ofJulianDayFraction(2_462_088.69)  // November 13.19, 2028
        Assert.assertEquals(Math.toRadians(231.328 - 360.0), SunLowPrecision(november13_2028).trueLongitude, 1e-5)
    }
}