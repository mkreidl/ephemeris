package com.mkreidl.ephemeris.sky

import com.mkreidl.ephemeris.solarsystem.Body
import com.mkreidl.ephemeris.solarsystem.SolarSystem
import com.mkreidl.ephemeris.time.Instant
import com.mkreidl.ephemeris.time.SiderealTime
import com.mkreidl.math.Spherical3
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat

class VenusTestMeeus {

    // Example taken from Meeus, "Rising, Transit and Setting"

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z")

    private val dayBefore = dateFormat.parse("1988-03-19 00:00:00 UTC").time
    private val day = dateFormat.parse("1988-03-20 00:00:00 UTC").time
    private val dayAfter = dateFormat.parse("1988-03-21 00:00:00 UTC").time

    private val expectedRise = dateFormat.parse("1988-03-20 12:25:26 UTC").time
    private val expectedTransit = dateFormat.parse("1988-03-20 19:40:31 UTC").time
    private val expectedSet = dateFormat.parse("1988-03-20 02:54:40 UTC").time

    private val boston = Spherical3(1.0, Math.toRadians(-71.0833), Math.toRadians(+42.3333))
    private val solarSystem = SolarSystem.createFromMeeus()

    @Test
    fun testSiderealTime() {
        val siderealTime = SiderealTime.ofEpochMilli(day)
        Assert.assertEquals(177.74208, siderealTime.getTrueSiderealTime().degrees, 0.000_02)
    }

    @Test
    fun apparentPositionsDayBefore() {
        val instant = Instant.ofEpochMilli(dayBefore)
        solarSystem.computeSingle(instant, body = Body.VENUS)
        val position = solarSystem.getTrueEquatorialGeocentric(Body.VENUS).position.spherical
        Assert.assertEquals(40.68021, Math.toDegrees(position.lon), 0.000_05)
        Assert.assertEquals(18.04761, Math.toDegrees(position.lat), 0.000_06)
    }

    @Test
    fun apparentPositionsDay() {
        val instant = Instant.ofEpochMilli(day)
        solarSystem.computeSingle(instant, body = Body.VENUS)
        val position = solarSystem.getTrueEquatorialGeocentric(Body.VENUS).position.spherical
        Assert.assertEquals(41.73129, Math.toDegrees(position.lon), 0.000_08)
        Assert.assertEquals(18.44092, Math.toDegrees(position.lat), 0.000_07)
    }

    @Test
    fun apparentPositionsDayAfter() {
        val instant = Instant.ofEpochMilli(dayAfter)
        solarSystem.computeSingle(instant, body = Body.VENUS)
        val position = solarSystem.getTrueEquatorialGeocentric(Body.VENUS).position.spherical
        Assert.assertEquals(42.78204, Math.toDegrees(position.lon), 0.000_08)
        Assert.assertEquals(18.82742, Math.toDegrees(position.lat), 0.000_08)
    }

    @Test
    fun testRiseForward() {
        val calculator = PlanetRiseSetCalculator(SolarSystem.createFromMeeus(), Body.VENUS, RiseSetCalculator.EventType.RISE, RiseSetCalculator.LookupDirection.FORWARD)
        calculator.geographicLocation = boston
        calculator.setStartTimeEpochMilli(day)
        calculator.compute()
        PlanetRiseSetTest.assert(calculator, expectedRise, 1_000)
    }

    @Test
    fun testTransitForward() {
        val calculator = PlanetRiseSetCalculator(SolarSystem.createFromMeeus(), Body.VENUS, RiseSetCalculator.EventType.TRANSIT, RiseSetCalculator.LookupDirection.FORWARD)
        calculator.geographicLocation = boston
        calculator.setStartTimeEpochMilli(day)
        calculator.compute()
        PlanetRiseSetTest.assert(calculator, expectedTransit, 1_000)
    }

    @Test
    fun testSetForward() {
        val calculator = PlanetRiseSetCalculator(SolarSystem.createFromMeeus(), Body.VENUS, RiseSetCalculator.EventType.SET, RiseSetCalculator.LookupDirection.FORWARD)
        calculator.geographicLocation = boston
        calculator.setStartTimeEpochMilli(day)
        calculator.compute()
        PlanetRiseSetTest.assert(calculator, expectedSet, 1_500)
    }

    @Test
    fun testRiseBackward() {
        val calculator = PlanetRiseSetCalculator(SolarSystem.createFromMeeus(), Body.VENUS, RiseSetCalculator.EventType.RISE, RiseSetCalculator.LookupDirection.BACKWARD)
        calculator.geographicLocation = boston
        calculator.setStartTimeEpochMilli(dayAfter)
        calculator.compute()
        PlanetRiseSetTest.assert(calculator, expectedRise, 1_000)
    }

    @Test
    fun testTransitBackward() {
        val calculator = PlanetRiseSetCalculator(SolarSystem.createFromMeeus(), Body.VENUS, RiseSetCalculator.EventType.TRANSIT, RiseSetCalculator.LookupDirection.BACKWARD)
        calculator.geographicLocation = boston
        calculator.setStartTimeEpochMilli(dayAfter)
        calculator.compute()
        PlanetRiseSetTest.assert(calculator, expectedTransit, 1_000)
    }

    @Test
    fun testSetBackward() {
        val calculator = PlanetRiseSetCalculator(SolarSystem.createFromMeeus(), Body.VENUS, RiseSetCalculator.EventType.SET, RiseSetCalculator.LookupDirection.BACKWARD)
        calculator.geographicLocation = boston
        calculator.setStartTimeEpochMilli(dayAfter)
        calculator.compute()
        PlanetRiseSetTest.assert(calculator, expectedSet, 1_500)
    }
}