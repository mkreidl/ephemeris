package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Time
import com.mkreidl.math.Sphe
import com.mkreidl.math.Vector3

class ModelSun : OrbitalModel() {

    override fun computeCartesian(time: Time): PhaseCartesian = PhaseCartesian(Vector3.ZERO, Vector3.ZERO)

    override fun computeSpherical(time: Time): PhaseSpherical = PhaseSpherical(Sphe.ZERO, Sphe.ZERO)
}
