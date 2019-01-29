package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Distance
import com.mkreidl.ephemeris.Time

abstract class OrbitalModel {

    enum class Type {
        HELIOCENTRIC, GEOCENTRIC
    }

    open val type = Type.HELIOCENTRIC

    open val distanceUnit = Distance.AU

    abstract fun computeCartesian(time: Time): PhaseCartesian

    abstract fun computeSpherical(time: Time): PhaseSpherical
}
