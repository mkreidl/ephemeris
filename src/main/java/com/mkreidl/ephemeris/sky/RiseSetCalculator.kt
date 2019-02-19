package com.mkreidl.ephemeris.sky

import com.mkreidl.ephemeris.MILLIS_PER_SIDEREAL_DAY
import com.mkreidl.ephemeris.SIDEREAL_MILLIS_PER_RADIAN
import com.mkreidl.ephemeris.time.Instant
import com.mkreidl.ephemeris.time.SiderealTime
import com.mkreidl.math.*

abstract class RiseSetCalculator(longitudeDeg: Double, latitudeDeg: Double) {

    protected val lon = Angle.ofDeg(longitudeDeg)
    protected val lat = Angle.ofDeg(latitudeDeg)
    protected val geographicLocation = Spherical3(1.0, lon.radians, lat.radians)

    private val projection = if (lat.radians >= 0) Stereographic.N else Stereographic.S

    init {
        updateHorizon()
    }

    protected lateinit var current: Instant
    protected lateinit var start: Instant

    protected val topocentric = Spherical3.ZERO
    protected var mode = EventType.SET

    private var lookupDirection = LookupDirection.FORWARD
    private var virtualHorizonDeg = OPTICAL_HORIZON_DEG

    private var horizon = Circle.UNIT

    fun setEventType(mode: EventType) {
        this.mode = mode
    }

    fun setSearchDirection(lookupDirection: LookupDirection) {
        this.lookupDirection = lookupDirection
    }

    internal val isCrossing: Boolean
        get() = mode == EventType.TRANSIT || !completelyAboveHorizon() && !completelyBelowHorizon()

    enum class EventType(internal val signum: Double) {
        RISE(-1.0), TRANSIT(0.0), SET(1.0)
    }

    enum class LookupDirection {
        FORWARD, BACKWARD
    }

    abstract fun compute(startTimeMs: Long): Boolean

    private fun computeHourAngle() =
            SiderealTime(current).getTrueSiderealTime(lon) - Angle.ofRad(topocentric.lon)

    private fun completelyAboveHorizon() =
            Math.abs(Math.toDegrees(geographicLocation.lat + topocentric.lat)) >= 90 + virtualHorizonDeg

    private fun completelyBelowHorizon() =
            Math.abs(Math.toDegrees(geographicLocation.lat - topocentric.lat)) >= 90 - virtualHorizonDeg

    private fun updateHorizon() {
        horizon = projection.project(geographicLocation, Math.toRadians(90 - virtualHorizonDeg))
    }

    internal fun adjustTime(): Boolean {
        updateHorizon()
        val alpha = mode.signum * computeHourAngleAtSet() - computeHourAngle()
        current = current.addMillis((alpha.radians * SIDEREAL_MILLIS_PER_RADIAN).toLong())
        if (lookupDirection == LookupDirection.FORWARD && current < start)
            current = current.addMillis(MILLIS_PER_SIDEREAL_DAY.toLong())
        if (lookupDirection == LookupDirection.BACKWARD && current > start)
            current = current.addMillis((-MILLIS_PER_SIDEREAL_DAY).toLong())
        return !java.lang.Double.isNaN(alpha.radians) && !java.lang.Double.isInfinite(alpha.radians)
    }

    private fun computeHourAngleAtSet(): Angle {
        val dist = horizon.distFromOrigin
        val rHor = horizon.r
        val rOrb = orbitRadius()
        return Angle.ofRad(Math.acos((rHor * rHor - dist * dist - rOrb * rOrb) / (2.0 * dist * rOrb)))
    }

    private fun orbitRadius(): Double {
        val z = Math.sin(if (lat.radians > 0) topocentric.lat else -topocentric.lat)
        return Math.sqrt((1 + z) / (1 - z))
    }

    companion object {
        const val OPTICAL_HORIZON_DEG = -34.0 / 60
    }
}
