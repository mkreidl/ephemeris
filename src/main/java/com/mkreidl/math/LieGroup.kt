package com.mkreidl.math

interface LieGroup<T : LieGroup<T>> {
    fun log(): T
    fun exp(t: Double = 1.0): T
    fun pow(exponent: Double) = log().exp(exponent)
    fun adjointRep(): Matrix3x3
    fun toUniversalCover(): Quaternion

    operator fun times(other: T): T
    operator fun div(divisor: T): T
    infix fun leftdiv(dividend: T): T

    fun toDoubleArray(): DoubleArray

    // adjoint representation
    operator fun times(vector: Vector3): Vector3
}

interface LieAlgebra<T : LieAlgebra<T>> {
    operator fun unaryMinus(): T
    operator fun unaryPlus() = this

    operator fun plus(other: T): T
    operator fun minus(subtrahend: T): T
    operator fun times(scale: Double): T
    operator fun div(shrink: Double) = this * (1 / shrink)
}

internal fun Matrix3x3.computeRotationAxis(): DoubleArray {
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