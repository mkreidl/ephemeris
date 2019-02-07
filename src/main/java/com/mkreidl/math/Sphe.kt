package com.mkreidl.math

data class Sphe(val dst: Double = 0.0, val lon: Double = 0.0, val lat: Double = 0.0) {

    fun toCartesian() = Vector3(
            x = dst * Math.cos(lat) * Math.cos(lon),
            y = dst * Math.cos(lat) * Math.sin(lon),
            z = dst * Math.sin(lat)
    )

    fun standardized(): Sphe {
        val lat = standardize(lat)
        return if (lat > Math.PI / 2 || lat < -Math.PI / 2) {
            Sphe(dst, standardize(lon + Math.PI), standardize(Math.PI - lat))
        } else {
            Sphe(dst, standardize(lon), lat)
        }
    }

    companion object {

        val ZERO = Sphe(0.0, 0.0, 0.0)

        private fun standardize(radians: Double): Double {
            val reduced = radians % (2 * Math.PI)
            return when {
                reduced <= -Math.PI -> reduced + 2 * Math.PI
                reduced > Math.PI -> reduced - 2 * Math.PI
                else -> reduced
            }
        }
    }
}