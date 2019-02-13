package com.mkreidl.math

data class Vector3(val x: Double, val y: Double, val z: Double) : VectorSpace<Double, Vector3> {

    constructor(x: Float, y: Float, z: Float) : this(x.toDouble(), y.toDouble(), z.toDouble())

    constructor(v: FloatArray) : this(v[0].toDouble(), v[1].toDouble(), v[2].toDouble())

    fun normalize() = this / norm

    val norm get() = Math.sqrt(this * this)

    operator fun unaryPlus() = this

    operator fun unaryMinus() = Vector3(-x, -y, -z)

    override operator fun plus(other: Vector3) = Vector3(
            x + other.x,
            y + other.y,
            z + other.z
    )

    override operator fun minus(other: Vector3) = Vector3(
            x - other.x,
            y - other.y,
            z - other.z
    )

    override operator fun times(factor: Double) = Vector3(
            x * factor,
            y * factor,
            z * factor
    )

    operator fun times(other: Vector3) = x * other.x + y * other.y + z * other.z

    operator fun div(divisor: Double) = this * (1.0 / divisor)

    infix fun x(other: Vector3) = Vector3(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
    )

    infix fun tensor(other: Vector3) = Matrix3x3(
            x * other.x, x * other.y, x * other.z,
            y * other.x, y * other.y, y * other.z,
            z * other.x, z * other.y, z * other.z
    )

    infix fun angle(other: Vector3) = Angle(Math.atan2((this x other).norm, this * other))

    fun toSpherical(): Spherical3 {
        val norm = this.norm
        return Spherical3(dst = norm, lat = Math.asin(z / norm), lon = Math.atan2(y, x))
    }

    fun toFloatArray() = floatArrayOf(x.toFloat(), y.toFloat(), z.toFloat())

    companion object {
        val ZERO = Vector3(0.0, 0.0, 0.0)
    }
}