package com.mkreidl.ephemeris.stars

import com.mkreidl.math.Angle
import com.mkreidl.ephemeris.time.Instant
import com.mkreidl.ephemeris.solarsystem.Ecliptic
import com.mkreidl.ephemeris.solarsystem.SunHighPrecision
import com.mkreidl.math.Vector3
import org.junit.Assert
import org.junit.Test

class StarPositionTestBSCvsMeeus {

    private val thetaPersei = 588  // index in BrightStarCatalog

    private val raJ2000 = Math.toRadians(2 + 44.0 / 60 + 11.986 / 3_600) * 15
    private val deJ2000 = Math.toRadians(49 + 13.0 / 60 + 42.48 / 3_600)

    private val vraJ2000 = Math.toRadians(+0.034_25 / 3_600 * 15)
    private val vdeJ2000 = Math.toRadians(-0.089_5 / 3_600)

    private val november13_2028 = Instant.ofJulianDayFraction(2_462_088.69)  // November 13.19, 2028
    private val raEpoch = Math.toRadians(41.054_063)
    private val deEpoch = Math.toRadians(49.227_750)
    private val raEpochToDate = Math.toRadians(41.547_214)
    private val deEpochToDate = Math.toRadians(49.348_483)

    private val tolPos = Angle.ofDeg(0, 0, 5.0).radians
    private val tolVel = Angle.ofDeg(0, 0, 0.2).radians

    private val stars = Stars(BrightStarCatalog.INSTANCE)

    @Test
    fun testThetaPerseiPosJ2000() {
        val ra = BrightStarCatalog.INSTANCE.getRAscJ2000(thetaPersei)
        val de = BrightStarCatalog.INSTANCE.getDeclJ2000(thetaPersei)
        Assert.assertEquals(raJ2000, ra, tolPos)
        Assert.assertEquals(deJ2000, de, tolPos)
    }

    @Test
    fun testThetaPerseiVelJ2000() {
        val vra = BrightStarCatalog.INSTANCE.getVRAscJ2000(thetaPersei)
        val vde = BrightStarCatalog.INSTANCE.getVDeclJ2000(thetaPersei)
        Assert.assertEquals(vraJ2000, vra, tolVel)
        Assert.assertEquals(vdeJ2000, vde, tolVel)
    }

    @Test
    fun testThetaPersei2028November13_J2000_singleStar() {
        val output = stars.computeMeanEquatorial(thetaPersei, november13_2028, Ecliptic.J2000)
        Assert.assertEquals(raEpoch, output.lon, tolPos)
        Assert.assertEquals(deEpoch, output.lat, tolPos)
    }

    @Test
    fun testThetaPersei2028November13_J2000_allStars() {
        val output = DoubleArray(3 * BrightStarCatalog.SIZE)
        stars.computeEclipticalJ2000(november13_2028, output)

        val offset = 3 * thetaPersei
        val cartesian = Vector3(output[offset], output[offset + 1], output[offset + 2])
        val spherical = Ecliptic.J2000.trafoEcl2MeanEqu(cartesian).toSpherical()

        Assert.assertEquals(raEpoch, spherical.lon, tolPos)
        Assert.assertEquals(deEpoch, spherical.lat, tolPos)
    }

    @Test
    fun testThetaPersei2028November13_toDate() {
        val output = stars.computeMeanEquatorial(thetaPersei, november13_2028)
        Assert.assertEquals(raEpochToDate, output.lon, tolPos)
        Assert.assertEquals(deEpochToDate, output.lat, tolPos)
    }

    @Test
    fun testThetaPersei2028November13_apparent_SunData() {
        // Meeus, Example 22.a
        val sun = SunHighPrecision(november13_2028)
        Assert.assertEquals(0.288_670_5, november13_2028.julianCenturiesSinceJ2000, 5e-8)
        Assert.assertEquals(0.016_696_47, sun.excentricity, 5e-9)
        Assert.assertEquals(103.434, Math.toDegrees(sun.perihelion), 5e-4)
    }

    @Test
    fun testThetaPersei2028November13_apparent_nutation() {
        val ecliptic = Ecliptic(november13_2028)
        val ecliptical = stars.computeEclipticalJ2000(thetaPersei, november13_2028)
        val equatorialMean = ecliptic.trafoEclJ2000ToMeanEquToDate(ecliptical).toSpherical()
        val equatorialTrue = ecliptic.trafoEclJ2000ToTrueEquToDate(ecliptical).toSpherical()
        val nutationRa = equatorialTrue.lon - equatorialMean.lon
        Assert.assertEquals(Angle.ofDeg(0, 0, 15.843).radians, nutationRa, tolPos)
    }

    @Test
    fun testThetaPersei2028November13_apparent_full() {
        val ecliptic = Ecliptic(november13_2028)
        val apparentEcliptical = stars.computeEclipticalApparent(thetaPersei, november13_2028, ecliptic)
        val apparentEquatorial = ecliptic.trafoTrueEcl2TrueEqu(apparentEcliptical.toCartesian()).toSpherical()
        Assert.assertEquals(Angle.ofHrs(2, 46, 14.39).radians, apparentEquatorial.lon, tolPos)
        Assert.assertEquals(Angle.ofDeg(49, 21, 7.45).radians, apparentEquatorial.lat, tolPos)
    }

    @Test
    fun testThetaPersei2028November13_apparent_direct() {
        val ecliptic = Ecliptic(november13_2028)
        val apparentEquatorial = stars.computeEquatorialApparent(thetaPersei, november13_2028, ecliptic)
        Assert.assertEquals(Angle.ofHrs(2, 46, 14.39).radians, apparentEquatorial.lon, tolPos)
        Assert.assertEquals(Angle.ofDeg(49, 21, 7.45).radians, apparentEquatorial.lat, tolPos)
    }
}