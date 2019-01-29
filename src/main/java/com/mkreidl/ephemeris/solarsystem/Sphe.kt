package com.mkreidl.ephemeris.solarsystem

data class Sphe(val dst: Double = 0.0, val lon: Double = 0.0, val lat: Double = 0.0) {
    fun toCartesian() = Cart(
            x = dst * Math.cos(lat) * Math.cos(lon),
            y = dst * Math.cos(lat) * Math.sin(lon),
            z = dst * Math.sin(lat)
    )
}