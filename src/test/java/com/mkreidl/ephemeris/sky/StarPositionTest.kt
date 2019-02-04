package com.mkreidl.ephemeris.sky

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

    
}