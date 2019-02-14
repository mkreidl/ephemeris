package com.mkreidl.math

operator fun Matrix3x3.times(phase: PhaseCartesian) = PhaseCartesian(this * phase.position, this * phase.velocity)

data class PhaseCartesian(val position: Vector3, val velocity: Vector3) {

    companion object {
        val ZERO = PhaseCartesian(Vector3.ZERO, Vector3.ZERO)
    }

    fun toSpherical() = PhaseSpherical(position.toSpherical(), jacobian * velocity)

    private val jacobian by lazy { computeJacobian() }

    private fun computeJacobian(): Matrix3x3 {
        throw NotImplementedError("The Jacobian matrix for transformation from Cartesian to Spherical coordinates is not yet implemented")
    }

    operator fun times(factor: Double) = PhaseCartesian(position * factor, velocity * factor)

    operator fun plus(other: PhaseCartesian) = PhaseCartesian(position + other.position, velocity + other.velocity)

    operator fun minus(other: PhaseCartesian) = PhaseCartesian(position - other.position, velocity - other.velocity)

    operator fun unaryMinus() = PhaseCartesian(-position, -velocity)

    fun scaleVelocity(factor: Double) = PhaseCartesian(position, factor * velocity)

    val angularVelocity get() = (position.x * velocity.y - position.y * velocity.x) / (position * position)
    val retrograde get() = position.x * velocity.y - position.y * velocity.x < 0
}