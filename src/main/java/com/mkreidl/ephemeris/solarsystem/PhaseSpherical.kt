package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Time
import com.mkreidl.math.Matrix3x3
import com.mkreidl.math.Sphe
import com.mkreidl.math.Vector3

data class PhaseSpherical(val position: Sphe, val velocity: Sphe) {

    companion object {
        val ZERO = PhaseSpherical(Sphe.ZERO, Sphe.ZERO)
    }

    fun toCartesian() = PhaseCartesian(
            position.toCartesian(),
            jacobian() * Vector3(velocity.dst, velocity.lon, velocity.lat) * (1.0 / Time.SECONDS_PER_DAY)
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
