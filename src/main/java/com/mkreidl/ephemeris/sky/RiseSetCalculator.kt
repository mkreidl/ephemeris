package com.mkreidl.ephemeris.sky

import com.mkreidl.ephemeris.MILLIS_PER_SIDEREAL_DAY
import com.mkreidl.ephemeris.SIDEREAL_MILLIS_PER_RADIAN
import com.mkreidl.ephemeris.time.Instant
import com.mkreidl.math.*

abstract class RiseSetCalculator(
        private val geographicLocation: Spherical3,
        private val mode: EventType,
        private val lookupDirection: LookupDirection
) {

    enum class EventType(internal val signum: Double) {
        RISE(-1.0),
        TRANSIT(0.0),
        SET(1.0)
    }

    enum class LookupDirection {
        FORWARD,
        BACKWARD
    }

    val time get() = topos.instant

    private val projection = if (geographicLocation.lat >= 0) Stereographic.N else Stereographic.S

    private var virtualHorizonDeg = OPTICAL_HORIZON_DEG
    protected lateinit var topos: Topos

    protected var start = Instant.J2000
        set(start) {
            field = start
            topos = Topos.of(geographicLocation, start)
        }

    private lateinit var horizon: Circle

    protected var topocentric = Spherical3.ZERO

    internal fun isCrossing() = mode == EventType.TRANSIT || !completelyAboveHorizon() && !completelyBelowHorizon()

    abstract fun compute(startTimeMs: Long): Boolean

    private fun computeHourAngle() = topos.localTrueSiderealTime - Angle.ofRad(topocentric.lon)

    private fun completelyAboveHorizon() =
            Math.abs(Math.toDegrees(geographicLocation.lat + topocentric.lat)) >= 90 + virtualHorizonDeg

    private fun completelyBelowHorizon() =
            Math.abs(Math.toDegrees(geographicLocation.lat - topocentric.lat)) >= 90 - virtualHorizonDeg

    internal fun adjustTime(): Boolean {
        horizon = projection.project(geographicLocation, Math.toRadians(90 - virtualHorizonDeg))
        val alpha = mode.signum * computeHourAngleAtSet() - computeHourAngle()
        var update = topos.instant.addMillis((alpha.radians * SIDEREAL_MILLIS_PER_RADIAN).toLong())
        if (lookupDirection == LookupDirection.FORWARD && update < start)
            update = update.addMillis(MILLIS_PER_SIDEREAL_DAY.toLong())
        if (lookupDirection == LookupDirection.BACKWARD && update > start)
            update = update.addMillis((-MILLIS_PER_SIDEREAL_DAY).toLong())
        topos = topos.copy(instant = update)
        return !java.lang.Double.isNaN(alpha.radians) && !java.lang.Double.isInfinite(alpha.radians)
    }

    private fun computeHourAngleAtSet(): Angle {
        val dist = horizon.distFromOrigin
        val rHor = horizon.r
        val rOrb = orbitRadius()
        return Angle.ofRad(Math.acos((rHor * rHor - dist * dist - rOrb * rOrb) / (2.0 * dist * rOrb)))
    }

    private fun orbitRadius(): Double {
        val z = Math.sin(if (geographicLocation.lat > 0) topocentric.lat else -topocentric.lat)
        return Math.sqrt((1 + z) / (1 - z))
    }

    companion object {
        const val OPTICAL_HORIZON_DEG = -34.0 / 60
    }
}
