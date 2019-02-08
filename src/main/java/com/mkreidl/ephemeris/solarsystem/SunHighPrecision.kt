package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Instant
import com.mkreidl.ephemeris.Time
import com.mkreidl.ephemeris.solarsystem.meeus.EarthMeeus
import com.mkreidl.math.Sphe

class SunHighPrecision(instant: Instant, ecliptic: Ecliptic = Ecliptic(instant)) : Sun(instant, ecliptic) {

    private val earth = EarthMeeus()

    private val geometricPosition by lazy { computeGeometricPosition() }
    private val apparentPosition by lazy { computeApparentPosition() }
    private val apparentPosEquatorial by lazy { computeApparentEquatorial() }

    override val geometricLongitude get() = geometricPosition.lon
    override val apparentLongitude get() = apparentPosition.lon
    override val apparentRightAscension get() = apparentPosEquatorial.lon

    val geometricLatitude get() = geometricPosition.lat
    val apparentLatitude get() = apparentPosition.lat
    val apparentDeclination get() = apparentPosEquatorial.lat

    private fun computeGeometricPosition(): Sphe {
        val time = Time(instant.epochMilli)
        val (pos, _) = earth.computeSpherical(time)
        return pos.copy(lon = standardize(pos.lon + Math.PI), lat = -standardize(pos.lat))
    }

    private fun computeApparentPosition() = geometricPosition.copy(
            lon = standardize(geometricPosition.lon + ecliptic.nutationInLongitude + aberrationCorrection)
    )

    private fun computeApparentEquatorial() =
            ecliptic.trafoTrueEcl2TrueEqu(apparentPosition.toCartesian()).toSpherical()

    companion object {
        private val aberrationCorrection = -Math.toRadians(20.4898 / 3_600)
    }
}