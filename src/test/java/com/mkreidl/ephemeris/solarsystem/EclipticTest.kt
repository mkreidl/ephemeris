package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Instant
import com.mkreidl.ephemeris.geometry.Angle
import org.junit.Assert
import org.junit.Test
import java.util.*

class EclipticTest {

    @Test
    fun testEquationOfTimeMeeusChap28() {
        val calendar = GregorianCalendar(TimeZone.getTimeZone("UTC"))
                .apply {
                    set(1992, 9, 13,
                            0, 0, 0)
                }
        val instant = Instant.ofEpochMilli(calendar.timeInMillis)
        val radians = Angle.standardize(Sun(instant).equationOfTime)
        Assert.assertEquals(13 + 42.6 / 60, Math.toDegrees(radians) * 4, 0.002)
    }

    @Test
    fun testNutationMeeusChap22() {
        val calendar = GregorianCalendar(TimeZone.getTimeZone("UTC"))
                .apply {
                    set(1987, 3, 10, 0, 0, 0)
                }
        val ecliptic = Ecliptic(Instant.ofEpochMilli(calendar.timeInMillis))
        val longitude = Angle.standardize(ecliptic.nutationInLongitude)
        val obliquity = Angle.standardize(ecliptic.nutationInObliquity)
        Assert.assertEquals(-3.788, Math.toDegrees(longitude) * 3600, 0.02)
        Assert.assertEquals(9.443, Math.toDegrees(obliquity) * 3600, 0.02)
    }

    @Test
    fun testObliquity() {
        Assert.assertEquals(Math.toRadians(24.0266666667), getObliquity(-3000), 1e-3)
        Assert.assertEquals(Math.toRadians(23.695), getObliquity(0), 1e-3)
        Assert.assertEquals(Math.toRadians(23.44), getObliquity(2000), 1e-3)
        Assert.assertEquals(Math.toRadians(23.43721), getObliquity(2016), 1e-3)
        Assert.assertEquals(Math.toRadians(23.31), getObliquity(3000), 1e-3)
    }

    private fun getObliquity(year: Int) = Ecliptic(
            GregorianCalendar(TimeZone.getTimeZone("UTC"))
                    .apply {
                        set(
                                year, Calendar.JANUARY, 1,
                                12, 0, 0
                        )
                    }.timeInMillis
    ).meanObliquity
}
