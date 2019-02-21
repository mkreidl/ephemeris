package com.mkreidl.ephemeris.sky

import com.mkreidl.ephemeris.MILLIS_PER_HOUR
import com.mkreidl.ephemeris.MILLIS_PER_SIDEREAL_DAY
import com.mkreidl.ephemeris.solarsystem.Body
import com.mkreidl.ephemeris.solarsystem.SolarSystem
import com.mkreidl.ephemeris.time.Instant

class PlanetRiseSetCalculator(
        private val solarSystem: SolarSystem,
        private val body: Body,
        mode: EventType,
        lookupDirection: LookupDirection
) : RiseSetCalculator(mode, lookupDirection) {

    private var timeMillisPrevious = 0L
    private var wasVisibleBefore: Boolean? = null
    private var isVisibleNow = false
    private var crossing = false
    private var precisionMs = 1000L

    override fun compute(): Boolean {
        for (n in 0 until MAX_ITERATION) {
            timeMillisPrevious = time.epochMilli
            computeTopocentricPosition()
            if (!crossing || !adjustTime())
                if (mode == RiseSetCalculator.EventType.RISE && hasAppeared() || mode == RiseSetCalculator.EventType.SET && hasVanished()) {
                    time = Instant.ofEpochMilli((time.epochMilli + timeMillisPrevious) / 2)
                } else {
                    searchOrbitCrossingHorizon()
                    return compute()
                }
            if (Math.abs(time.epochMilli - timeMillisPrevious) < precisionMs) {
                return true
            }
            wasVisibleBefore = isVisibleNow
        }
        return false
    }

    private fun searchOrbitCrossingHorizon() {
        var searchIncrement = (30 * MILLIS_PER_SIDEREAL_DAY * lookupDirection.sign).toLong()
        while (!crossing) {
            timeMillisPrevious = time.epochMilli
            startTime = time.addMillis(searchIncrement)
            computeTopocentricPosition()
        }
        while (Math.abs(searchIncrement) > MILLIS_PER_HOUR) {
            if (!crossing)
                timeMillisPrevious = startTime.epochMilli
            searchIncrement /= 2
            startTime = Instant.ofEpochMilli(timeMillisPrevious + searchIncrement)
            computeTopocentricPosition()
        }
    }

    private fun computeTopocentricPosition() {
        solarSystem.computeSingle(time, body = body)
        val geocentric = solarSystem.getTrueEquatorialGeocentric(body).position
        topocentric = topos.computeTopocentricFromTrueEquatorial(geocentric).spherical
        virtualHorizon = RiseSetCalculator.OPTICAL_HORIZON - body.RADIUS_MEAN_M / topocentric.dst
        isVisibleNow = topocentric.lat >= virtualHorizon
        crossing = eventHappensToday()
    }

    private fun hasAppeared() = wasVisibleBefore == false && isVisibleNow
    private fun hasVanished() = wasVisibleBefore == true && !isVisibleNow

    companion object {
        private const val MAX_ITERATION = 5
    }
}