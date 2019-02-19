package com.mkreidl.ephemeris.sky

import com.mkreidl.ephemeris.Distance
import com.mkreidl.ephemeris.Position
import com.mkreidl.ephemeris.Time
import com.mkreidl.ephemeris.solarsystem.Body
import com.mkreidl.ephemeris.solarsystem.FullSolarSystem
import com.mkreidl.ephemeris.time.Instant

class PlanetRiseSetCalculator private constructor(private val solarSystem: FullSolarSystem, private val body: Body) : RiseSetCalculator() {
    private val position = Position()

    private var timeMillisPrevious: Long = 0

    private var wasVisibleBefore: Boolean? = null
    private var isVisibleNow: Boolean? = null
    private var isCrossing: Boolean = false
    private var precisionMs: Long = 1000

    fun setPrecision(precisionMs: Long) {
        this.precisionMs = precisionMs
    }

    override fun compute(timeMillisStart: Long): Boolean {
        time = timeMillisStart
        isVisibleNow = null
        for (n in 0 until MAX_ITERATION) {
            timeMillisPrevious = current.epochMilli
            computeTopocentricPosition()
            if (!isCrossing || !adjustTime())
                if (mode === RiseSetCalculator.EventType.RISE && hasAppeared() || mode === RiseSetCalculator.EventType.SET && hasVanished())
                    current = Instant.ofEpochMilli((current.epochMilli + timeMillisPrevious) / 2)
                else
                    return compute(searchOrbitCrossingHorizon())
            if (Math.abs(current.epochMilli - timeMillisPrevious) < precisionMs)
                return true
        }
        return false
    }

    private fun searchOrbitCrossingHorizon(): Long {
        var searchIncrement = 30 * Time.MILLIS_PER_SIDEREAL_DAY * (if (getLookupDirection() === RiseSetCalculator.LookupDirection.FORWARD) 1 else -1).toLong()
        while (!isCrossing) {
            timeMillisPrevious = current.epochMilli
            current = current.addMillis(searchIncrement)
            computeTopocentricPosition()
        }
        while (Math.abs(searchIncrement) > Time.MILLIS_PER_HOUR) {
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

    private fun hasAppeared(): Boolean {
        return wasVisibleBefore != null && isVisibleNow!! && (!wasVisibleBefore)!!
    }

    private fun hasVanished(): Boolean {
        return wasVisibleBefore != null && (!isVisibleNow)!! && wasVisibleBefore!!
    }

    companion object {
        private val MAX_ITERATION = 5

        fun of(solarSystem: FullSolarSystem, body: Body): PlanetRiseSetCalculator {
            return PlanetRiseSetCalculator(solarSystem, body)
        }
    }
}