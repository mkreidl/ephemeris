package com.mkreidl.math

data class Spherical3(val dst: Double = 0.0, val lon: Double = 0.0, val lat: Double = 0.0) {

    fun toCartesian() = Vector3(
            x = dst * Math.cos(lat) * Math.cos(lon),
            y = dst * Math.cos(lat) * Math.sin(lon),
            z = dst * Math.sin(lat)
    )

    fun reduce(): Spherical3 {
        val lat = Angle.reduce(lat)
        return if (lat > Math.PI / 2 || lat < -Math.PI / 2) {
            copy(lon = Angle.reduce(lon + Math.PI), lat = Angle.reduce(Math.PI - lat))
        } else {
            copy(lon = Angle.reduce(lon))
        }
    }

    companion object {
        val ZERO = Spherical3(0.0, 0.0, 0.0)
    }
}