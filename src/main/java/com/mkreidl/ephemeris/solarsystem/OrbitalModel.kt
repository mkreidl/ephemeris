package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Distance
import com.mkreidl.ephemeris.Instant

abstract class OrbitalModel {

    enum class Type {
        HELIOCENTRIC, GEOCENTRIC
    }

    open val type = Type.HELIOCENTRIC

    open val distanceUnit = Distance.AU

    abstract fun computeCartesian(instant: Instant): PhaseCartesian

    abstract fun computeSpherical(instant: Instant): PhaseSpherical
}
