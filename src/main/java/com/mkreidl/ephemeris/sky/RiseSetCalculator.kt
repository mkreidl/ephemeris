package com.mkreidl.ephemeris.sky

import com.mkreidl.ephemeris.MILLIS_PER_SIDEREAL_DAY
import com.mkreidl.ephemeris.SIDEREAL_MILLIS_PER_RADIAN
import com.mkreidl.ephemeris.time.Instant
import com.mkreidl.math.Angle
import com.mkreidl.math.Circle
import com.mkreidl.math.Spherical3
import com.mkreidl.math.Stereographic

abstract class RiseSetCalculator(protected val mode: EventType, protected val lookupDirection: LookupDirection) {

    enum class EventType(internal val sign: Double) {
        RISE(-1.0),
        TRANSIT(0.0),
        SET(1.0)
    }

    enum class LookupDirection(internal val sign: Int) {
        FORWARD(1),
        BACKWARD(-1)
    }

    abstract fun compute(): Boolean

    protected var topos = Topos(0.0, 0.0, Instant.J2000)
    private var projection = Stereographic.N
    private lateinit var horizon: Circle

    var geographicLocation = Spherical3.ZERO
        set(location) {
            field = location
            topos = topos.copy(longitude = location.lon, latitude = location.lat)
            projection = if (geographicLocation.lat >= 0) Stereographic.N else Stereographic.S
        }

    var time = Instant.J2000
        protected set(time) {
            field = time
            topos = topos.copy(instant = time)
        }

    protected var startTime = Instant.J2000
        set(startTime) {
            field = startTime
            time = startTime
        }

    fun setStartTimeEpochMilli(epochMilli: Long) {
        startTime = Instant.ofEpochMilli(epochMilli)
        time = startTime
    }

    protected var virtualHorizon = OPTICAL_HORIZON
    protected var topocentric = Spherical3.ZERO

    protected fun adjustTime(): Boolean {
        horizon = projection.project(geographicLocation, PI_2 - virtualHorizon)
        val alpha = computeHourAngleOfEvent() + topocentric.lon - topos.localTrueSiderealTime.radians
        val delta = Angle.reduce(alpha) * SIDEREAL_MILLIS_PER_RADIAN
        val epochMilliNew = time.epochMilli + delta
        time = time.addMillis(when {
            lookupDirection == LookupDirection.FORWARD && epochMilliNew < startTime.epochMilli -> delta + MILLIS_PER_SIDEREAL_DAY
            lookupDirection == LookupDirection.BACKWARD && epochMilliNew > startTime.epochMilli -> delta - MILLIS_PER_SIDEREAL_DAY
            else -> delta
        }.toLong())
        return !java.lang.Double.isNaN(alpha) && !java.lang.Double.isInfinite(alpha)
    }

    private fun computeHourAngleOfEvent(): Double {
        val dist = horizon.distFromOrigin
        val rHor = horizon.r
        val rOrb = orbitRadius()
        return when (mode) {
            EventType.RISE -> -Math.acos((rHor * rHor - dist * dist - rOrb * rOrb) / (2.0 * dist * rOrb))
            EventType.SET -> Math.acos((rHor * rHor - dist * dist - rOrb * rOrb) / (2.0 * dist * rOrb))
            EventType.TRANSIT -> 0.0
        }
    }

    private fun orbitRadius(): Double {
        val z = Math.sin(if (geographicLocation.lat > 0) topocentric.lat else -topocentric.lat)
        return Math.sqrt((1 + z) / (1 - z))
    }

    protected fun isCrossing() = mode == EventType.TRANSIT || !completelyAboveHorizon() && !completelyBelowHorizon()

    private fun completelyAboveHorizon() =
            Math.abs(geographicLocation.lat + topocentric.lat) >= PI_2 + virtualHorizon

    private fun completelyBelowHorizon() =
            Math.abs(geographicLocation.lat - topocentric.lat) >= PI_2 - virtualHorizon

    companion object {
        val OPTICAL_HORIZON = -Math.toRadians(34.0 / 60)
        const val PI_2 = Math.PI * 0.5
    }
}
