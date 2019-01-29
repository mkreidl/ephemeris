package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Time

class ModelSun : OrbitalModel() {

    override fun computeCartesian(time: Time): PhaseCartesian = PhaseCartesian(Cart(0.0, 0.0, 0.0), Cart(0.0, 0.0, 0.0))

    override fun computeSpherical(time: Time): PhaseSpherical = PhaseSpherical(Sphe(0.0, 0.0, 0.0), Sphe(0.0, 0.0, 0.0))
}
