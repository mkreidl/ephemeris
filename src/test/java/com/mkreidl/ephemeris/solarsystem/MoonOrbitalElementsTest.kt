package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.math.Angle
import com.mkreidl.ephemeris.time.Instant
import com.mkreidl.ephemeris.geometry.Spherical
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class MoonOrbitalElementsTest {

    // References from the Astronomical Almanac
    private val instant = Instant.ofEpochMilli(GregorianCalendar(TimeZone.getTimeZone("UTC"))
            .apply { set(1990, 3, 19, 0, 0, 0)  /* 1990, April 19, 00:00:00*/ }
            .timeInMillis)
    private val modelMoon = ModelMoon()
    private val refSpherical = Spherical(
            60.793 * Body.EARTH.RADIUS_EQUATORIAL_M, Math.toRadians(306.94), -Math.toRadians(0.55)
    )

    @Test
    fun testCalculate() {
        val (position, _) = modelMoon.compute(instant).spherical
        val orbitalEl = modelMoon.moon.reduce()

        assertEquals(Angle.ofDeg(312.7381).radians, orbitalEl.node, sec)
        assertEquals(Angle.ofDeg(5.1454).radians, orbitalEl.inclination, sec)
        assertEquals(Angle.ofDeg(-264.2546).radians, orbitalEl.periapsis, sec)
        assertEquals(60.2666 * Body.EARTH.RADIUS_EQUATORIAL_M, orbitalEl.axis, 1e-12)
        assertEquals(0.054900, orbitalEl.excentricity, 1e-12)
        assertEquals(Angle.ofDeg(-46173.9046).radians, orbitalEl.meanAnomaly, sec)

        assertEquals(refSpherical.lat, position.lat, 2.5 * 60 * sec)
        assertEquals(refSpherical.lon, position.lon, 1.0 * 60 * sec)
    }

    companion object {
        val sec = Angle.ofDeg(0, 0, 1.0).radians
    }
}
