package com.mkreidl.ephemeris.sky

import com.mkreidl.ephemeris.sky.Topos
import com.mkreidl.ephemeris.time.Instant
import com.mkreidl.ephemeris.time.SiderealTime
import com.mkreidl.math.Angle
import com.mkreidl.math.Spherical3
import org.junit.Assert.assertEquals
import org.junit.Test

class ToposTest {

    private val geographicLocation = Spherical3(1.0, Math.toRadians(11.5820), -Math.toRadians(48.1351))
    private val localSiderealTime = SiderealTime(Instant.J2000).getMeanSiderealTime(Angle.ofRad(geographicLocation.lon)).radians
    private val topos = Topos.of(geographicLocation, Instant.J2000)

    @Test
    fun testZenith() {
        val equatorial = Spherical3(1.0, localSiderealTime, geographicLocation.lat)
        val zenith = topos.mapMeanEquatorialToHorizontal(equatorial.cartesian)
        assertEquals(0.0, zenith.x, 1e-15)
        assertEquals(0.0, zenith.y, 1e-15)
        assertEquals(1.0, zenith.z, 1e-15)
    }

    @Test
    fun testNorth() {
        val sphericalNorth = Spherical3(1.0, localSiderealTime + Math.PI, Math.PI * 0.5 - geographicLocation.lat).reduce()
        val north = topos.mapMeanEquatorialToHorizontal(sphericalNorth)
        assertEquals(0.0, north.lon, 1e-15)
        assertEquals(0.0, north.lat, 1e-15)
    }

    @Test
    fun testSouth() {
        val sphericalSouth = Spherical3(1.0, localSiderealTime, geographicLocation.lat - Math.PI * 0.5).reduce()
        val south = topos.mapMeanEquatorialToHorizontal(sphericalSouth)
        assertEquals(Math.toRadians(180.0), south.lon, 1e-15)
        assertEquals(0.0, south.lat, 1e-15)
    }

    @Test
    fun testEast() {
        val sphericalEast = Spherical3(1.0, localSiderealTime + Math.PI * 0.5, 0.0)
        val east = topos.mapMeanEquatorialToHorizontal(sphericalEast)
        assertEquals(0.0, east.lat, 1e-15)
        assertEquals(Math.toRadians(90.0), east.lon, 1e-15)
    }

    @Test
    fun testWest() {
        val sphericalWest = Spherical3(1.0, localSiderealTime - Math.PI * 0.5, 0.0)
        val west = topos.mapMeanEquatorialToHorizontal(sphericalWest)
        assertEquals(0.0, west.lat, 1e-15)
        assertEquals(Math.toRadians(-90.0), west.lon, 1e-15)
    }
}