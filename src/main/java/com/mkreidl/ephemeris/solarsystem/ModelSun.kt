package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Distance
import com.mkreidl.ephemeris.time.Instant
import com.mkreidl.math.PhaseCartesian

object ModelSun : OrbitalModel {
    override val type = OrbitalModel.Type.HELIOCENTRIC
    override val distanceUnit = Distance.AU
    override val phase = PhaseCartesian.ZERO
    override fun compute(instant: Instant) = phase
}