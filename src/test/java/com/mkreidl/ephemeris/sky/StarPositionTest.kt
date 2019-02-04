package com.mkreidl.ephemeris.sky

import com.mkreidl.ephemeris.Time
import com.mkreidl.ephemeris.solarsystem.Ecliptic
import com.mkreidl.math.Vector3
import org.junit.Assert
import org.junit.Test

class StarPositionTest {

    val thetaPersei = 588

    val raJ2000 = Math.toRadians((2 + 44.0 / 60 + 11.986 / 3600) * 15)
    val deJ2000 = Math.toRadians(49 + 13.0 / 60 + 42.48 / 3600)

    val tol = Math.toRadians(0.5 / 3_600)  // half of an arc second

    @Test
    fun testThetaPerseiJ2000() {
        val ra = StarsCatalog.getRAscJ2000(thetaPersei)
        val de = StarsCatalog.getDeclJ2000(thetaPersei)
        Assert.assertEquals(raJ2000, ra, tol)
        Assert.assertEquals(deJ2000, de, tol)
    }

    val epoch = 2_462_088.69  // Julian day number of November 13.19, 2028
    val raEpoch = Math.toRadians((2 + 44.0 / 60 + 12.975 / 3600) * 15)
    val deEpoch = Math.toRadians(49 + 13.0 / 60 + 39.90 / 3600)

    @Test
    fun testThetaPersei2028November13() {
        val julianYears = (epoch - Time.J2000.julianDayNumber()) / Time.DAYS_PER_YEAR
        val output = DoubleArray(3 * StarsCatalog.SIZE)

        Stars.computeEclipticalJ2000(julianYears, output, 0, 1)

        val cartesian = Vector3(output[3 * 588], output[3 * 588 + 1], output[3 * 588 + 2])

        val spherical = (Ecliptic(Time.J2000).trafoEcl2MeanEqu * cartesian).toSpherical()

        Assert.assertEquals(raEpoch, spherical.lon, tol)
        Assert.assertEquals(deEpoch, spherical.lat, tol)
    }
}