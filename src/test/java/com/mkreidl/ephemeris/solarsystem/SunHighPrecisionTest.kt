package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Instant
import org.junit.Assert
import org.junit.Test

class SunHighPrecisionTest {

    // Meeus Exampla 24.b

    private val october13_1992 = Instant.ofJulianDayFraction(2_448_908.50)  // October 13, 1992, 0hTD
    private val sun = SunHighPrecision(october13_1992)

    @Test
    fun testGeometricLongitude() {
        Assert.assertEquals(Math.toRadians(199 + 54.0 / 60 + 26.45 / 3_600 - 360), sun.geometricLongitude, 5e-7)
    }

    @Test
    fun testGeometricLatitude() {
        Assert.assertEquals(Math.toRadians(0.62 / 3_600), sun.geometricLatitude, 2e-7)
    }

    @Test
    fun testApparentLongitude() {
        Assert.assertEquals(Math.toRadians(199 + 54.0 / 60 + 21.82 / 3_600 - 360), sun.apparentLongitude, 1e-6)
    }

    @Test
    fun testApparentRightAscension() {
        Assert.assertEquals(Math.toRadians(-11 + 13.0 / 60 + 30.763 / 3_600) * 15, sun.apparentRightAscension, 1e-6)
    }
}
