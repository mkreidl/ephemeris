package com.mkreidl.math

interface LieGroup<T : LieGroup<T>> {
    fun log(): T
    fun exp(t: Double = 1.0): T
    fun pow(exponent: Double) = log().exp(exponent)
    fun adjointRep(): Matrix3x3
    fun lift(): Quaternion

    operator fun times(other: T): T
    operator fun div(other: T): T
    infix fun leftDiv(other: T): T

    fun toDoubleArray(): DoubleArray

    // adjoint representation
    operator fun times(vector: Vector3): Vector3
}

operator fun <T : VectorSpace<Double, T>> Double.times(vector: T) = vector * this

interface VectorSpace<S : Number, V : VectorSpace<S, V>> {
    operator fun times(factor: S): V
    operator fun plus(other: V): V
    operator fun minus(other: V): V
}

interface LieAlgebra<T : LieAlgebra<T>> : VectorSpace<Double, T> {
    operator fun unaryMinus(): T
    operator fun unaryPlus() = this

    override operator fun plus(other: T): T
    override operator fun minus(other: T): T
    override operator fun times(factor: Double): T
    operator fun div(divisor: Double) = this * (1 / divisor)
}