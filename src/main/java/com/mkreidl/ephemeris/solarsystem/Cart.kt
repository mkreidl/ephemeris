package com.mkreidl.ephemeris.solarsystem

data class Cart(val x: Double = 0.0, val y: Double = 0.0, val z: Double = 0.0) {

    val length get() = Math.sqrt(x * x + y * y + z * z)

    fun toSpherical(): Sphe {
        val dst = this.length
        return Sphe(dst = dst, lat = Math.asin(z / dst), lon = Math.atan2(y, x))
    }

    operator fun plus(other: Cart) = Cart(x + other.x, y + other.y, z + other.z)

    operator fun minus(other: Cart) = Cart(x - other.x, y - other.y, z - other.z)

    operator fun times(factor: Double) = Cart(x * factor, y * factor, z * factor)

    companion object {
        val ZERO = Cart(0.0, 0.0, 0.0)
    }
}