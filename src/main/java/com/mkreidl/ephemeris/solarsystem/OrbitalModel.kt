package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Distance
import com.mkreidl.ephemeris.time.Instant
import com.mkreidl.math.Phase

interface OrbitalModel {

    enum class Type {
        HELIOCENTRIC, GEOCENTRIC
    }

    val type: Type
    val distanceUnit: Distance
    val phase: Phase

    fun compute(instant: Instant): Phase
}
