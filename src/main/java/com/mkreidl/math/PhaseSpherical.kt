package com.mkreidl.math

data class PhaseSpherical(val position: Spherical3, val velocity: Vector3) : Phase {

    companion object {
        val ZERO = PhaseSpherical(Spherical3.ZERO, Vector3.ZERO)
    }

    override val cartesian by lazy { PhaseCartesian(position.cartesian, jacobian * velocity) }

    override val spherical get() = this

    private val jacobian by lazy { computeJacobian() }

    private fun computeJacobian(): Matrix3x3 {
        val r = position.dst
        val sl = Math.sin(position.lon)
        val sb = Math.sin(position.lat)
        val cl = Math.cos(position.lon)
        val cb = Math.cos(position.lat)
        return Matrix3x3(
                cl * cb, -r * sl * cb, -r * cl * sb,
                sl * cb, r * cl * cb, -r * sl * sb,
                sb, 0.0, r * cb
        )
    }
}
