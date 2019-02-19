package com.mkreidl.ephemeris.sky

import com.mkreidl.ephemeris.Distance
import com.mkreidl.ephemeris.MILLIS_PER_HOUR
import com.mkreidl.ephemeris.MILLIS_PER_SIDEREAL_DAY
import com.mkreidl.ephemeris.solarsystem.Body
import com.mkreidl.ephemeris.time.Instant
import com.mkreidl.math.Spherical3

class PlanetRiseSetCalculator(
        private val body: Body,
        geographicLocation: Spherical3,
        mode: EventType,
        private val lookupDirection: LookupDirection
) : RiseSetCalculator(geographicLocation, mode, lookupDirection) {

    private var timeMillisPrevious: Long = 0

    private var wasVisibleBefore: Boolean? = null
    private var isVisibleNow: Boolean? = null
    private var isCrossing: Boolean = false
    private var precisionMs: Long = 1000

    fun setPrecision(precisionMs: Long) {
        this.precisionMs = precisionMs
    }

    override fun compute(startTimeEpochMilli: Long): Boolean {
        start = Instant.ofEpochMilli(startTimeEpochMilli)
        isVisibleNow = null
        for (n in 0 until MAX_ITERATION) {
            timeMillisPrevious = topos.instant.epochMilli
            computeTopocentricPosition()
            if (!isCrossing || !adjustTime())
                if (mode === RiseSetCalculator.EventType.RISE && hasAppeared() || mode === RiseSetCalculator.EventType.SET && hasVanished()) {
                    topos = topos.copy(instant = Instant.ofEpochMilli((topos.instant.epochMilli + timeMillisPrevious) / 2))
                } else {
                    return compute(searchOrbitCrossingHorizon())
                }
            if (Math.abs(topos.instant.epochMilli - timeMillisPrevious) < precisionMs)
                return true
        }
        return false
    }

    private fun searchOrbitCrossingHorizon(): Long {
        var searchIncrement = 30 * MILLIS_PER_SIDEREAL_DAY * (if (lookupDirection == RiseSetCalculator.LookupDirection.FORWARD) 1 else -1)
        while (!isCrossing) {
            timeMillisPrevious = topos.instant.epochMilli
            current = topos.instant.addMillis(searchIncrement.toLong())
            computeTopocentricPosition()
        }
        while (Math.abs(searchIncrement) > MILLIS_PER_HOUR) {
            if (!isCrossing)
                timeMillisPrevious = current.epochMilli
            searchIncrement /= 2
            current = Instant.ofEpochMilli(timeMillisPrevious + searchIncrement)
            computeTopocentricPosition()
        }
        return current.epochMilli
    }

    private fun computeTopocentricPosition() {
        wasVisibleBefore = isVisibleNow
        solarSystem.computeSingle(current, Body.EARTH)
        solarSystem.compute(time, body)
        solarSystem.getEphemerides(body, position)
        position.setTimeLocation(time, geographicLocation)
        position.get(topocentric, Position.CoordinatesCenter.TOPOCENTRIC)
        val apparentRadius = body.RADIUS_MEAN_M / topocentric.distance(Distance.m)
        virtualHorizonDeg = RiseSetCalculator.OPTICAL_HORIZON_DEG - Math.toDegrees(apparentRadius)
        isVisibleNow = topocentric.lat >= getVirtualHorizonDeg()
        isCrossing = isCrossing()
    }

    private fun hasAppeared() = wasVisibleBefore != null && isVisibleNow!! && (!wasVisibleBefore)!!

    private fun hasVanished() = wasVisibleBefore != null && (!isVisibleNow)!! && wasVisibleBefore!!

    companion object {
        private const val MAX_ITERATION = 5
    }
}