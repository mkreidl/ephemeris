package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.TestUtil
import com.mkreidl.ephemeris.geometry.Angle.*
import com.mkreidl.ephemeris.geometry.Spherical
import org.junit.Assert.assertEquals
import org.junit.Test

class MoonOrbitalElementsTest {

    // References from the Astronomical Almanac
    private val time = TestUtil.getAstronomicalTime("1990.04.19 00:00:00")
    private val modelMoon = ModelMoon()
    private val refSpherical = Spherical(
            60.793 * Body.EARTH.RADIUS_EQUATORIAL_M, 306.94 * DEG, -0.55 * DEG
    )

    @Test
    fun testCalculate() {
        val (position) = modelMoon.computeSpherical(time!!)
        val orbitalEl = modelMoon.computeOrbitalElements(time)

        assertEquals(standardize(312.7381 * DEG), orbitalEl.node, 1.0 * SEC)
        assertEquals(standardize(5.1454 * DEG), orbitalEl.incl, 1.0 * SEC)
        assertEquals(standardize(-264.2546 * DEG), orbitalEl.periapsis, 1.0 * SEC)
        assertEquals(60.2666 * Body.EARTH.RADIUS_EQUATORIAL_M, orbitalEl.axis, 1e-12)
        assertEquals(0.054900, orbitalEl.exc, 1e-12)
        assertEquals(standardize(-46173.9046 * DEG), orbitalEl.meanAnom, 1.0 * SEC)

        assertEquals(refSpherical.lat, position.lat, 2.5 * MIN)
        assertEquals(refSpherical.lon, position.lon, 1.0 * MIN)
    }
}
