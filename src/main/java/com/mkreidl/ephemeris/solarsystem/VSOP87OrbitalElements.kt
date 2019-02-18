package com.mkreidl.ephemeris.solarsystem

import java.lang.Math.*

data class VSOP87OrbitalElements(
        val a: Double,
        val l: Double,
        val h: Double,
        val k: Double,
        val p: Double,
        val q: Double
) : OrbitalElements {

    override val vsop87 get() = this

    override val classical by lazy {
        val node = atan2(p, q)
        val periapsis = atan2(h, k) - node
        ClassicalOrbitalElements(
                node = node,
                inclination = 2.0 * asin(sqrt(p * p + q * q)),
                periapsis = periapsis,
                axis = a,
                excentricity = sqrt(h * h + k * k),
                meanAnomaly = l - node - periapsis
        )
    }

    override fun computePhase() = classical.computePhase()
    override fun computePosition() = classical.computePosition()
}
