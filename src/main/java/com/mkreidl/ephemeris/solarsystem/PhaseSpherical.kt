package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Time

data class PhaseSpherical(val position: Sphe, val velocity: Sphe) {

    fun toCartesian(): PhaseCartesian {
        val r = position.dst
        val sl = Math.sin(position.lon)
        val sb = Math.sin(position.lat)
        val cl = Math.cos(position.lon)
        val cb = Math.cos(position.lat)

        return PhaseCartesian(
                position.toCartesian(),
                Cart(
                        x = (velocity.dst * cl * cb - r * (velocity.lon * sl * cb + velocity.lat * cl * sb)) / Time.SECONDS_PER_DAY,
                        y = (velocity.dst * sl * cb + r * (velocity.lon * cl * cb - velocity.lat * sl * sb)) / Time.SECONDS_PER_DAY,
                        z = (velocity.dst * sb + r * velocity.lat * cb) / Time.SECONDS_PER_DAY
                )
        )
    }
}
