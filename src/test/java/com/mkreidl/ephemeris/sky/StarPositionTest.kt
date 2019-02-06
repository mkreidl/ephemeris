package com.mkreidl.ephemeris.sky

import com.mkreidl.ephemeris.Time
import com.mkreidl.ephemeris.solarsystem.Ecliptic
import com.mkreidl.math.Vector3
import org.junit.Assert
import org.junit.Test

class StarPositionTest {

    val thetaPersei = 588  // index in StarsCatalog

    val raJ2000 = Math.toRadians(2 + 44.0 / 60 + 11.986 / 3_600) * 15
    val deJ2000 = Math.toRadians(49 + 13.0 / 60 + 42.48 / 3_600)

    val vraJ2000 = Math.toRadians(+0.034_25 / 3_600 * 15)
    val vdeJ2000 = Math.toRadians(-0.089_5 / 3_600)

    val epoch = 2_462_088.69  // Julian day number of November 13.19, 2028
    val raEpoch = Math.toRadians(41.054_063)
    val deEpoch = Math.toRadians(49.227_750)

    val tolPos = 2.5e-6  // ~ half of an arc second
    val tolVel = 2.5e-9  // ~ half of a milli arc second / year

    init {
        // Substitute value for proper motion in RA to match the values in Example 20.b
        StarsCatalog.QP[thetaPersei][4] = Math.toRadians(0.034_25 * 15 / 3_600).toFloat()
    }

    @Test
    fun testThetaPerseiPosJ2000() {
        val ra = StarsCatalog.getRAscJ2000(thetaPersei)
        val de = StarsCatalog.getDeclJ2000(thetaPersei)
        Assert.assertEquals(raJ2000, ra, tolPos)
        Assert.assertEquals(deJ2000, de, tolPos)
    }

    @Test
    fun testThetaPerseiVelJ2000() {
        val vra = StarsCatalog.getVRAscJ2000(thetaPersei)
        val vde = StarsCatalog.getVDeclJ2000(thetaPersei)
        Assert.assertEquals(vraJ2000, vra, tolVel)
        Assert.assertEquals(vdeJ2000, vde, tolVel)
    }

    @Test
    fun testThetaPersei2028November13() {
        val julianYears = (epoch - Time.J2000.julianDayNumber()) / Time.DAYS_PER_YEAR
        val output = DoubleArray(3 * StarsCatalog.SIZE)
        Stars.computeEclipticalJ2000(julianYears, output, 0, 1)

        val offset = 3 * thetaPersei
        val cartesian = Vector3(output[offset], output[offset + 1], output[offset + 2])
        val spherical = (Ecliptic(Time.J2000).trafoEcl2MeanEqu * cartesian).toSpherical()

        Assert.assertEquals(raEpoch, spherical.lon, tolPos)
        Assert.assertEquals(deEpoch, spherical.lat, tolPos)
    }
}