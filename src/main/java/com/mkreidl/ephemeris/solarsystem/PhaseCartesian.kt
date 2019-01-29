package com.mkreidl.ephemeris.solarsystem

data class PhaseCartesian(val position: Cart, val velocity: Cart) {

    fun toSpherical() = PhaseSpherical(position.toSpherical(), Sphe(0.0, 0.0, 0.0))

    operator fun times(factor: Double) = PhaseCartesian(position * factor, velocity * factor)

    operator fun plus(other: PhaseCartesian) = PhaseCartesian(position + other.position, velocity + other.velocity)

    operator fun minus(other: PhaseCartesian) = PhaseCartesian(position - other.position, velocity - other.velocity)
}