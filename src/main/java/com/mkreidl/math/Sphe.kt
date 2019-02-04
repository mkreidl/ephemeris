package com.mkreidl.math

import com.mkreidl.ephemeris.geometry.Angle

data class Sphe(val dst: Double = 0.0, val lon: Double = 0.0, val lat: Double = 0.0) {

    companion object {
        val ZERO = Sphe(0.0, 0.0, 0.0)
    }

    fun toCartesian() = Vector3(
            x = dst * Math.cos(lat) * Math.cos(lon),
            y = dst * Math.cos(lat) * Math.sin(lon),
            z = dst * Math.sin(lat)
    )

    fun standardized(): Sphe {
        var lat = Angle.standardize(lat)
        var lon = lon
        if (lat > Math.PI / 2 || lat < -Math.PI / 2) {
            lat = Angle.standardize(-lat)
            lon += Math.PI
        }
        lon = Angle.standardize(lon)
        return Sphe(dst, lon, lat)
    }
}