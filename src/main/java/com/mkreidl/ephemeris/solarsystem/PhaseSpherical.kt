package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.math.Matrix3x3
import com.mkreidl.math.Spherical3
import com.mkreidl.math.Vector3

data class PhaseSpherical(val position: Spherical3, val velocity: Spherical3) {

    companion object {
        val ZERO = PhaseSpherical(Spherical3.ZERO, Spherical3.ZERO)
    }

    fun toCartesian() = PhaseCartesian(
            position.toCartesian(), jacobian() * Vector3(velocity.dst, velocity.lon, velocity.lat)
    )

    private fun jacobian(): Matrix3x3 {
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
