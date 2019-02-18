package com.mkreidl.math

operator fun Matrix3x3.times(phase: PhaseCartesian) = PhaseCartesian(this * phase.position, this * phase.velocity)

data class PhaseCartesian(val position: Vector3, val velocity: Vector3) : Phase {

    companion object {
        val ZERO = PhaseCartesian(Vector3.ZERO, Vector3.ZERO)
    }

    override val cartesian get() = this

    override val spherical by lazy { PhaseSpherical(position.spherical, jacobian * velocity) }

    operator fun times(factor: Double) = PhaseCartesian(position * factor, velocity * factor)

    operator fun plus(other: PhaseCartesian) = PhaseCartesian(position + other.position, velocity + other.velocity)

    operator fun minus(other: PhaseCartesian) = PhaseCartesian(position - other.position, velocity - other.velocity)

    operator fun unaryMinus() = PhaseCartesian(-position, -velocity)

    val angularVelocity get() = (position.x * velocity.y - position.y * velocity.x) / (position * position)
    val retrograde get() = position.x * velocity.y - position.y * velocity.x < 0

    fun scaleVelocity(factor: Double) = PhaseCartesian(position, factor * velocity)

    private val jacobian by lazy { computeJacobian() }

    private fun computeJacobian(): Matrix3x3 {
        throw NotImplementedError("The Jacobian matrix for transformation from Cartesian to Spherical coordinates is not yet implemented")
    }
}