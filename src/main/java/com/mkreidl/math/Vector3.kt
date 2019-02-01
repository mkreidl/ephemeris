package com.mkreidl.math

operator fun Double.times(vector: Vector3) = vector * this

data class Vector3(val x: Double, val y: Double, val z: Double) {

    companion object {
        val ZERO = Vector3(0.0, 0.0, 0.0)
    }

    constructor(x: Float, y: Float, z: Float) : this(x.toDouble(), y.toDouble(), z.toDouble())

    operator fun plus(other: Vector3) = Vector3(
            x + other.x,
            y + other.y,
            z + other.z)

    operator fun minus(other: Vector3) = Vector3(
            x - other.x,
            y - other.y,
            z - other.z)

    operator fun times(factor: Double) = Vector3(
            x * factor,
            y * factor,
            z * factor)

    operator fun times(other: Vector3) = x * other.x + y * other.y + z * other.z

    infix fun x(other: Vector3) = Vector3(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x)

    infix fun tensor(other: Vector3) = Matrix3x3(
            x * other.x, x * other.y, x * other.z,
            y * other.x, y * other.y, y * other.z,
            z * other.x, z * other.y, z * other.z)

    infix fun angle(other: Vector3) = Math.atan2((this x other).norm, this * other)

    val norm get() = Math.sqrt(this * this)

    fun toFloatArray() = arrayOf(x.toFloat(), y.toFloat(), z.toFloat()).toFloatArray()

    fun toSpherical(): Sphe {
        val norm = this.norm
        return Sphe(dst = norm, lat = Math.asin(z / norm), lon = Math.atan2(y, x))
    }
}