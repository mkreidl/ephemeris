package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.math.Matrix3x3
import com.mkreidl.math.Sphe
import com.mkreidl.math.Vector3

operator fun Matrix3x3.times(phase: PhaseCartesian) = PhaseCartesian(this * phase.position, this * phase.velocity)

data class PhaseCartesian(val position: Vector3, val velocity: Vector3) {

    fun jacobian(): Matrix3x3 {
        throw NotImplementedError("The Jacobian matrix for transformation from Cartesian to Spherical coordinates is not yet implemented")
    }

    fun toSpherical(): PhaseSpherical {
        val sphe = jacobian() * velocity
        return PhaseSpherical(position.toSpherical(), Sphe(sphe.x, sphe.y, sphe.z))
    }

    operator fun times(factor: Double) = PhaseCartesian(position * factor, velocity * factor)

    operator fun plus(other: PhaseCartesian) = PhaseCartesian(position + other.position, velocity + other.velocity)

    operator fun minus(other: PhaseCartesian) = PhaseCartesian(position - other.position, velocity - other.velocity)

    companion object {
        val ZERO = PhaseCartesian(Vector3.ZERO, Vector3.ZERO)
    }
}