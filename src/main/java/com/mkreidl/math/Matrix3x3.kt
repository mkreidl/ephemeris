package com.mkreidl.math

data class Matrix3x3(
        val a11: Double = 0.0, val a12: Double = 0.0, val a13: Double = 0.0,
        val a21: Double = 0.0, val a22: Double = 0.0, val a23: Double = 0.0,
        val a31: Double = 0.0, val a32: Double = 0.0, val a33: Double = 0.0
) : LieGroup<Matrix3x3>, LieAlgebra<Matrix3x3> {

    constructor(coordinates: DoubleArray) : this(
            coordinates[0], coordinates[1], coordinates[2],
            coordinates[3], coordinates[4], coordinates[5],
            coordinates[6], coordinates[7], coordinates[8]
    )

    constructor(coordinates: FloatArray) : this(
            coordinates[0].toDouble(), coordinates[1].toDouble(), coordinates[2].toDouble(),
            coordinates[3].toDouble(), coordinates[4].toDouble(), coordinates[5].toDouble(),
            coordinates[6].toDouble(), coordinates[7].toDouble(), coordinates[8].toDouble()
    )

    operator fun invoke(i: Int, j: Int) = when (i) {
        1 -> when (j) {
            1 -> a11
            2 -> a12
            3 -> a13
            else -> throw IllegalArgumentException()
        }
        2 -> when (j) {
            1 -> a21
            2 -> a22
            3 -> a23
            else -> throw IllegalArgumentException()
        }
        3 -> when (j) {
            1 -> a31
            2 -> a32
            3 -> a33
            else -> throw IllegalArgumentException()
        }
        else -> throw IllegalArgumentException()
    }

    override fun exp(t: Double): Matrix3x3 {
        val phi = Vector3(a23, -a13, a12).norm
        return if (phi > 0) {
            val invPhi = 1 / phi
            val onb = extendOrthogonally(a23 * invPhi, a31 * invPhi, a12 * invPhi)
            rotation(phi * t, Axis.X).conjugateBy(onb)
        } else
            ONE
    }

    override fun log(): Matrix3x3 {
        val (norm2, rx, ry, rz) = computeRotationAxis()
        return if (norm2 > 0) {
            val invNorm = Math.sqrt(1 / norm2)
            val onb = extendOrthogonally(rx * invNorm, ry * invNorm, rz * invNorm)
            val rot = this.conjugateByTranspose(onb)
            val phi = Math.atan2(rot.a32, rot.a22)
            Matrix3x3.infinitesimalRotation(phi * onb.a11, phi * onb.a21, phi * onb.a31)
        } else
            ZERO
    }

    override fun adjointRep() = this

    override fun lift(): Quaternion {
        val cosPhiOver2 = 0.5 * Math.sqrt(1 + trace)  // = cosine(half of rotation angle)
        val real = if (cosPhiOver2.isNaN()) 0.0 else cosPhiOver2 // could happen that trace < -1 due to rounding errors => cosine(half of rotation angle) = NaN
        val (norm2, x, y, z) = computeRotationAxis()
        return if (norm2 > 0) {
            // determine sign of rotation angle by looking at sign of det(axis,e_i,Rot(e_i))
            // for each i=1..3 (sum the three determinants up, as they all have the same sign)
            val sign = y * a13 - x * a23 + z * a21 - y * a31 + x * a32 - z * a12
            val invNorm = (if (sign >= 0) 0.5 else -0.5) * Math.sqrt((3 - trace) / norm2)
            Quaternion(real, x * invNorm, y * invNorm, z * invNorm)
        } else
            Quaternion(real, x, y, z)
    }

    operator fun invoke(vector: Vector3) = times(vector)

    override operator fun times(other: Matrix3x3) = Matrix3x3(
            a11 * other.a11 + a12 * other.a21 + a13 * other.a31,
            a11 * other.a12 + a12 * other.a22 + a13 * other.a32,
            a11 * other.a13 + a12 * other.a23 + a13 * other.a33,

            a21 * other.a11 + a22 * other.a21 + a23 * other.a31,
            a21 * other.a12 + a22 * other.a22 + a23 * other.a32,
            a21 * other.a13 + a22 * other.a23 + a23 * other.a33,

            a31 * other.a11 + a32 * other.a21 + a33 * other.a31,
            a31 * other.a12 + a32 * other.a22 + a33 * other.a32,
            a31 * other.a13 + a32 * other.a23 + a33 * other.a33
    )

    override operator fun times(vector: Vector3) = Vector3(
            a11 * vector.x + a12 * vector.y + a13 * vector.z,
            a21 * vector.x + a22 * vector.y + a23 * vector.z,
            a31 * vector.x + a32 * vector.y + a33 * vector.z
    )

    override operator fun div(other: Matrix3x3) = (other.transpose() leftDiv this.transpose()).transpose()

    override fun toDoubleArray() = doubleArrayOf(a11, a12, a13, a21, a22, a23, a31, a32, a33)

    override operator fun times(factor: Double) = Matrix3x3(
            a11 * factor, a12 * factor, a13 * factor,
            a21 * factor, a22 * factor, a23 * factor,
            a31 * factor, a32 * factor, a33 * factor
    )

    override operator fun div(divisor: Double) = this * (1.0 / divisor)

    override infix fun leftDiv(other: Matrix3x3): Matrix3x3 {
        val gaussAlgorithm = GaussAlgorithm(3)
        val result = other.toDoubleArray()
        gaussAlgorithm.leftDivide(toDoubleArray(), result)
        return Matrix3x3(
                result[0], result[1], result[2],
                result[3], result[4], result[5],
                result[6], result[7], result[8]
        )
    }

    override operator fun unaryMinus() = Matrix3x3(
            -a11, -a12, -a13,
            -a21, -a22, -a23,
            -a31, -a32, -a33
    )

    override operator fun plus(other: Matrix3x3) = Matrix3x3(
            a11 + other.a11, a12 + other.a12, a13 + other.a13,
            a21 + other.a21, a22 + other.a22, a23 + other.a23,
            a31 + other.a31, a32 + other.a32, a33 + other.a33
    )

    override operator fun minus(other: Matrix3x3) = Matrix3x3(
            a11 - other.a11, a12 - other.a12, a13 - other.a13,
            a21 - other.a21, a22 - other.a22, a23 - other.a23,
            a31 - other.a31, a32 - other.a32, a33 - other.a33
    )

    val trace get() = a11 + a22 + a33

    fun diag() = Vector3(a11, a22, a33)

    fun transpose() = copy(a12 = a21, a13 = a31, a23 = a32, a21 = a12, a31 = a13, a32 = a23)

    fun projectToSO3() = log().exp()

    fun conjugateBy(t: Matrix3x3) = t * this * t.transpose()

    fun conjugateByTranspose(t: Matrix3x3) = t.transpose() * this * t

    fun adjugate() = Matrix3x3(
            a22 * a33 - a32 * a23,
            a31 * a23 - a21 * a33,
            a21 * a32 - a31 * a22,
            a32 * a13 - a12 * a33,
            a11 * a33 - a31 * a13,
            a31 * a12 - a11 * a32,
            a12 * a23 - a22 * a13,
            a21 * a13 - a11 * a23,
            a11 * a22 - a21 * a12
    )

    fun symmetric(): Matrix3x3 {
        val s12 = 0.5 * (a12 + a21)
        computeRotationAxis()
        val s13 = 0.5 * (a13 + a31)
        val s23 = 0.5 * (a23 + a32)
        return Matrix3x3(a11, s12, s13, s12, a22, s23, s13, s23, a33)
    }

    fun computeRotationAxis(): DoubleArray {
        // Calculate the adjugate of the matrix r and find the column with the biggest norm
        val axis = (this - Matrix3x3.ONE).adjugate()
        val n1 = axis.a11 * axis.a11 + axis.a21 * axis.a21 + axis.a31 * axis.a31
        val n2 = axis.a12 * axis.a12 + axis.a22 * axis.a22 + axis.a32 * axis.a32
        val n3 = axis.a13 * axis.a13 + axis.a23 * axis.a23 + axis.a33 * axis.a33
        return if (n3 >= n1 && n3 >= n2)
            doubleArrayOf(n3, axis.a13, axis.a23, axis.a33)
        else if (n2 >= n1)
            doubleArrayOf(n2, axis.a12, axis.a22, axis.a32)
        else
            doubleArrayOf(n1, axis.a11, axis.a21, axis.a31)
    }

    companion object {
        val ZERO = Matrix3x3()
        val ONE = ZERO.copy(a11 = 1.0, a22 = 1.0, a33 = 1.0)

        private val invSqrt3 = Math.sqrt(1.0 / 3)

        fun rotation(angle: Double, axis: Axis): Matrix3x3 {
            val cos = Math.cos(angle)
            val sin = Math.sin(angle)
            return when (axis) {
                Axis.X -> ONE.copy(a22 = cos, a23 = -sin, a32 = sin, a33 = cos)
                Axis.Y -> ONE.copy(a11 = cos, a31 = -sin, a13 = sin, a33 = cos)
                Axis.Z -> ONE.copy(a11 = cos, a12 = -sin, a21 = sin, a22 = cos)
            }
        }

        fun infinitesimalRotation(x: Double, y: Double, z: Double) =
                ZERO.copy(a12 = z, a21 = -z, a13 = -y, a31 = y, a23 = x, a32 = -x)

        fun symmetric(a11: Double, a12: Double, a13: Double, a22: Double, a23: Double, a33: Double) =
                Matrix3x3(a11, a12, a13, a12, a22, a23, a13, a23, a33)

        private fun extendOrthogonally(x: Double, y: Double, z: Double): Matrix3x3 {
            // On the first column of this matrix: Find an orthogonal column vector by looking
            // for the two larger (in absolute value) coordinates and form a column
            // vector which is orthogonal to the first column of this matrix. Store it into
            // the second column of this matrix.
            return if (x > invSqrt3 || x < -invSqrt3) {
                val n = Math.sqrt(x * x + y * y)
                val inv = 1 / n
                val a12 = -y * inv
                val a22 = x * inv
                Matrix3x3(x, a12, -z * a22, y, a22, z * a12, z, 0.0, n)
            } else {
                val n = Math.sqrt(y * y + z * z)
                val inv = 1 / n
                val a22 = -z * inv
                val a32 = y * inv
                Matrix3x3(x, 0.0, n, y, a22, -x * a32, z, a32, x * a22)
            }
        }
    }
}
