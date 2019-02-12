package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Instant
import org.junit.Assert
import org.junit.Test
import java.util.*

class EclipticTest {

    private val november13_2028 = Instant.ofJulianDayFraction(2_462_088.69)  // November 13.19, 2028
    private val april10_1987 = Instant.ofEpochMilli(GregorianCalendar(TimeZone.getTimeZone("UTC"))
            .apply {
                set(1987, 3, 10,
                        0, 0, 0)
            }.timeInMillis)

    @Test
    fun testNutationMeeusChap22() {
        val ecliptic = Ecliptic(april10_1987)
        val longitude = ecliptic.nutationInLongitude
        val obliquity = ecliptic.nutationInObliquity
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

    @Test
    fun testNovember2028() {
        // Meeus, Example 22.a
        val ecliptic = Ecliptic(november13_2028)
        Assert.assertEquals(Math.toRadians(23.436), ecliptic.meanObliquity, 1e-5)
        Assert.assertEquals(14.861, Math.toDegrees(ecliptic.nutationInLongitude) * 3_600, 0.02)
        Assert.assertEquals(2.705, Math.toDegrees(ecliptic.nutationInObliquity) * 3_600, 0.02)
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
