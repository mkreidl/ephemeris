package com.mkreidl.ephemeris.geometry

import com.mkreidl.ephemeris.sky.coordinates.Equatorial
import com.mkreidl.ephemeris.sky.coordinates.Horizontal
import com.mkreidl.ephemeris.solarsystem.Body
import com.mkreidl.ephemeris.solarsystem.FullSolarSystem
import com.mkreidl.ephemeris.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.lang.Math.PI
import java.lang.Math.abs
import java.util.*

class HorizontalCoordinatesTest {
    private val angle = Angle()

    private val cartesianNorth = Equatorial.Cart()
    private val sphericalNorth = Equatorial.Sphe()
    private val cartesianEast = Equatorial.Cart()
    private val sphericalEast = Equatorial.Sphe()
    private val cartesian = Horizontal.Cart()
    private val spherical = Horizontal.Sphe()

    private val geographicLocation = Spherical(Body.EARTH.RADIUS_MEAN_M, Math.toRadians(11.5820), Math.toRadians(48.1351))

    private val instant = Instant.ofEpochMilli(GregorianCalendar(TimeZone.getTimeZone("UTC"))
            .apply { set(2016, 10, 10, 8, 0, 0)  /* 2016.11.10 08:00:00*/ }
            .timeInMillis)
    private val solarSystem = FullSolarSystem.createFromMeeus()

    private val height = EnumMap<Body, Angle>(Body::class.java)
    private val azimuth = EnumMap<Body, Angle>(Body::class.java)
    private val locationCart = Equatorial.Cart()
    private val locationSphe = Equatorial.Sphe()

    init {
        //height.put( SolarSystem.Body.MOON, new Angle( -46.5, Angle.Unit.DEGREES ) );
        azimuth[Body.MOON] = Angle(15.6, Angle.Unit.DEGREES)

        height[Body.SUN] = Angle(13.5, Angle.Unit.DEGREES)
        azimuth[Body.SUN] = Angle(136.6, Angle.Unit.DEGREES)

        height[Body.MERCURY] = Angle(7.1, Angle.Unit.DEGREES)
        azimuth[Body.MERCURY] = Angle(131.5, Angle.Unit.DEGREES)

        height[Body.VENUS] = Angle(-16.2, Angle.Unit.DEGREES)
        azimuth[Body.VENUS] = Angle(110.3, Angle.Unit.DEGREES)

        height[Body.MARS] = Angle(-37.1, Angle.Unit.DEGREES)
        azimuth[Body.MARS] = Angle(81.4, Angle.Unit.DEGREES)

        height[Body.JUPITER] = Angle(36.9, Angle.Unit.DEGREES)
        azimuth[Body.JUPITER] = Angle(166.3, Angle.Unit.DEGREES)

        height[Body.SATURN] = Angle(-5.0, Angle.Unit.DEGREES)
        azimuth[Body.SATURN] = Angle(116.7, Angle.Unit.DEGREES)

        height[Body.URANUS] = Angle(-31.7, Angle.Unit.DEGREES)
        azimuth[Body.URANUS] = Angle(338.2, Angle.Unit.DEGREES)

        height[Body.NEPTUNE] = Angle(-47.2, Angle.Unit.DEGREES)
        azimuth[Body.NEPTUNE] = Angle(30.3, Angle.Unit.DEGREES)
    }

    @Test
    fun testPlanets() {
//        solarSystem.compute(Instant.ofEpochMilli(epochMilli))
//
//        val horizontal = Horizontal.Sphe()
//
//        for (planet in height.keys) {
//            val azimuthExpected = azimuth[planet].get(Angle.Unit.DEGREES)
//            val heightExpected = height[planet].get(Angle.Unit.DEGREES)
//            val position = solarSystem.getEphemerides(planet, Position())
//
//            position.setTimeLocation(epochMilli, geographicLocation)
//            position.get(horizontal, Position.CoordinatesCenter.GEOCENTRIC)
//
//            println("===================")
//            println("$planet -- height")
//            println(java.lang.Double.toString(heightExpected))
//            println(horizontal.getHeight(angle).get(Angle.Unit.DEGREES))
//            println("$planet -- azimuth")
//            println(java.lang.Double.toString(azimuthExpected))
//            println(horizontal.getAzimuth(angle).get(Angle.Unit.DEGREES))
//            assertEquals(azimuthExpected, horizontal.getAzimuth(angle).get(Angle.Unit.DEGREES), 0.1)
//            assertEquals(heightExpected, horizontal.getHeight(angle).get(Angle.Unit.DEGREES), 0.1)
//        }
    }

    @Before
    fun initializeZenit() {
        locationSphe.set(geographicLocation)
        locationSphe.transform(locationCart)
    }

    @Test
    fun testZenit() {
        println("================================")
        println(locationCart)
        println(locationSphe)
        locationCart.toHorizontal(locationSphe, cartesian)
        locationSphe.toHorizontal(locationSphe, spherical)
        println(cartesian)
        println(spherical)
        assertEquals(cartesian.z, Body.EARTH.RADIUS_MEAN_M, 1e-16)
        assertEquals(spherical.getHeight(angle).get(Angle.Unit.DEGREES), 90.0, 1e-16)
        println("================================")
    }

    @Test
    fun testNorth() {
        val lon = geographicLocation.lon
        val lat = geographicLocation.lat

        sphericalNorth.set(1.0, if (lat < 0) lon else lon + PI, PI / 2 - abs(lat))
        sphericalNorth.transform(cartesianNorth)
        sphericalNorth.transform(cartesianNorth)

        println("================================")
        println(cartesianNorth)
        println(sphericalNorth)
        cartesianNorth.toHorizontal(locationSphe, cartesian)
        sphericalNorth.toHorizontal(locationSphe, spherical)
        println(cartesian)
        println(spherical)
        assertEquals(cartesian.x, -1.0, 1e-15)
        assertEquals(spherical.getAzimuth(angle).get(Angle.Unit.DEGREES), 0.0, 1e-15)
        println("================================")
    }

    @Test
    fun testEast() {
        locationSphe.set(geographicLocation)
        locationSphe.transform(locationCart)

        sphericalEast.set(1.0, geographicLocation.lon + PI / 2, 0.0)
        sphericalEast.transform(cartesianEast)

        println("================================")
        println(cartesianEast)
        println(sphericalEast)
        cartesianEast.toHorizontal(locationSphe, cartesian)
        sphericalEast.toHorizontal(locationSphe, spherical)
        println(cartesian)
        println(spherical)
        assertEquals(cartesian.y, 1.0, 1e-16)
        assertEquals(spherical.getAzimuth(angle).get(Angle.Unit.DEGREES), 90.0, 1e-16)
        println("================================")
    }
}