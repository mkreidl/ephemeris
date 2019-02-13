package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.ABERRATION
import com.mkreidl.math.Angle
import com.mkreidl.ephemeris.time.Instant
import com.mkreidl.ephemeris.solarsystem.meeus.EarthMeeus
import com.mkreidl.math.Spherical3

class SunHighPrecision(instant: Instant, ecliptic: Ecliptic = Ecliptic(instant)) : Sun(instant, ecliptic) {

    override val geometricLongitude get() = geometricPosition.lon
    override val apparentLongitude get() = apparentPosition.lon
    override val apparentRightAscension get() = apparentPosEquatorial.lon

    val geometricLatitude get() = geometricPosition.lat
    val apparentLatitude get() = apparentPosition.lat
    val apparentDeclination get() = apparentPosEquatorial.lat

    private val earth = EarthMeeus()

    private val geometricPosition by lazy { computeGeometricPosition() }
    private val apparentPosition by lazy { computeApparentPosition() }
    private val apparentPosEquatorial by lazy { computeApparentEquatorial() }

    private fun computeGeometricPosition(): Spherical3 {
        val (pos, _) = earth.computeSpherical(instant)
        return pos.copy(lon = Angle.reduce(pos.lon + Math.PI), lat = -Angle.reduce(pos.lat))
    }

    private fun computeApparentPosition() = geometricPosition.copy(
            lon = Angle.reduce(geometricPosition.lon + ecliptic.nutationInLongitude - ABERRATION)
    )

    private fun computeApparentEquatorial() =
            ecliptic.trafoTrueEcl2TrueEqu(apparentPosition.toCartesian()).toSpherical()
}