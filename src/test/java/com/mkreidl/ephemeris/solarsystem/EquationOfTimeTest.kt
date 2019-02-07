package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Instant
import org.junit.Assert
import org.junit.Test

class EquationOfTimeTest {
    private val october13_1992 = Instant.ofJulianDayFraction(2_448_908.50)  // October 13, 1992, 0hTD

    @Test
    internal fun testApparentLongitude() {
        Assert.assertEquals(Math.toRadians(-161.619_18), SunLowPrecision(october13_1992).apparentLongitude, 1e-6)
    }

    @Test
    internal fun testEquationOfTimeMeeusChap28() {
        val radians = SunLowPrecision(october13_1992).equationOfTime
        Assert.assertEquals(13 + 42.6 / 60, Math.toDegrees(radians) * 4, 0.002)
    }
}
