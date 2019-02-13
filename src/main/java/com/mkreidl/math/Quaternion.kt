package com.mkreidl.math

operator fun Vector3.times(quaternion: Quaternion) = (quaternion.conjugate() * Quaternion(0.0, x, y, z) * quaternion).imag()

data class Quaternion(val r: Double, val i: Double, val j: Double, val k: Double)
    : LieGroup<Quaternion>, LieAlgebra<Quaternion> {

    override fun exp(t: Double): Quaternion {
        val normImag = Math.sqrt(i * i + j * j + k * k)
        val expReal = Math.exp(t * r)
        val scaleImag = expReal * if (normImag > 0) Math.sin(t * normImag) / normImag else t
        return Quaternion(expReal * Math.cos(t * normImag), scaleImag * i, scaleImag * j, scaleImag * k)
    }

    override fun log(): Quaternion {
        if (isAlmostZero(1e-16))
            throw IllegalArgumentException("Logarithm is undefined: argument is too close to zero")
        val normSqrImagIn = i * i + j * j + k * k
        val normImagIn = Math.sqrt(normSqrImagIn)
        val normImagOut = Math.atan2(normImagIn, r)
        val expRealOut = Math.sqrt(r * r + normSqrImagIn)
        val scale = (if (normImagOut > 0) normImagOut / Math.sin(normImagOut) else 1.0) / expRealOut
        return Quaternion(Math.log(expRealOut), i * scale, j * scale, k * scale)
    }

    override fun adjointRep(): Matrix3x3 {
        val aa = r * r
        val ab = r * i
        val ac = r * j
        val ad = r * k
        val bb = i * i
        val bc = i * j
        val bd = i * k
        val cc = j * j
        val cd = j * k
        val dd = k * k
        // this * I * conjugateBy(this):
        val a11 = aa + bb - cc - dd
        val a21 = 2 * (ad + bc)
        val a31 = 2 * (bd - ac)
        // this * J * conjugateBy(this):
        val a12 = 2 * (bc - ad)
        val a22 = aa - bb + cc - dd
        val a32 = 2 * (ab + cd)
        // this * K * conjugateBy(this):
        val a13 = 2 * (ac + bd)
        val a23 = 2 * (cd - ab)
        val a33 = aa - bb - cc + dd
        return Matrix3x3(a11, a12, a13, a21, a22, a23, a31, a32, a33)
    }

    override fun lift() = this

    override operator fun times(other: Quaternion) = Quaternion(
            r * other.r - i * other.i - j * other.j - k * other.k,
            r * other.i + i * other.r + j * other.k - k * other.j,
            r * other.j + j * other.r + k * other.i - i * other.k,
            r * other.k + k * other.r + i * other.j - j * other.i
    )

    override operator fun times(vector: Vector3) = (this * Quaternion(0.0, vector.x, vector.y, vector.z) * conjugate()).imag()

    /**
     * Caution: only for unit quaternions! (Division is implemented as multiplication by conjugates)
     */
    override operator fun div(other: Quaternion) = this * other.conjugate()

    override infix fun leftDiv(other: Quaternion) = conjugate() * other

    fun conjugate() = Quaternion(r, -i, -j, -k)

    override fun toDoubleArray() = doubleArrayOf(r, i, j, k)

    override operator fun times(factor: Double) = Quaternion(r * factor, i * factor, j * factor, k * factor)

    override operator fun div(divisor: Double) = this * (1 / divisor)

    override operator fun unaryMinus() = Quaternion(-r, -i, -j, -k)

    override operator fun plus(other: Quaternion) = Quaternion(r + other.r, i + other.i, j + other.j, k + other.k)

    override operator fun minus(other: Quaternion) = Quaternion(r - other.r, i - other.i, j - other.j, k - other.k)

    infix fun dot(other: Quaternion) = r * other.r + i * other.i + j * other.j + k * other.k

    fun imag() = Vector3(i, j, k)

    fun norm() = Math.sqrt(this dot this)

    fun normalize() = this / norm()

    fun reflectToHalfSpace(halfSpace: Quaternion) = if (this dot halfSpace < 0) -this else this

    private fun isAlmostZero(tol: Double) = -tol < r && r < tol &&
            -tol < i && i < tol &&
            -tol < j && j < tol &&
            -tol < k && k < tol

    infix fun almostEqualTo(other: Quaternion) = (other - this).isAlmostZero(1e-16)

    fun rightMultiplyMatrix(matrix: Matrix3x3) = Quaternion(r,
            i * matrix.a11 + j * matrix.a21 + k * matrix.a31,
            i * matrix.a12 + j * matrix.a22 + k * matrix.a32,
            i * matrix.a13 + j * matrix.a23 + k * matrix.a33)

    companion object {
        val ZERO = Quaternion(0.0, 0.0, 0.0, 0.0)
        val ONE = ZERO.copy(r = 1.0)
        val I = ZERO.copy(i = 1.0)
        val J = ZERO.copy(j = 1.0)
        val K = ZERO.copy(k = 1.0)

        fun rotation(angle: Double, axis: Axis): Quaternion {
            val half = angle * 0.5
            return when (axis) {
                Axis.X -> Quaternion(Math.cos(half), Math.sin(half), 0.0, 0.0)
                Axis.Y -> Quaternion(Math.cos(half), 0.0, Math.sin(half), 0.0)
                Axis.Z -> Quaternion(Math.cos(half), 0.0, 0.0, Math.sin(half))
            }
        }

        fun rotation(v: Vector3) = Quaternion(0.0, v.x * 0.5, v.y * 0.5, v.z * 0.5).exp()

        fun rotation(start: Vector3, end: Vector3): Quaternion {
            val rot = start x end
            val theta = Math.asin(rot.norm) * 0.5
            val sinTheta = Math.sin(theta)
            val u = rot / rot.norm
            return Quaternion(r = Math.cos(theta), i = u.x * sinTheta, j = u.y * sinTheta, k = u.z * sinTheta)
        }
    }
}
