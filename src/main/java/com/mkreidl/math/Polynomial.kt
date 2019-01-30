package com.mkreidl.math

class Polynomial(private vararg val coefficients: Double) {

    operator fun times(factor: Double) = Polynomial(*coefficients.map { it * factor }.toDoubleArray())

    operator fun div(divisor: Double) = times(1 / divisor)

    operator fun get(x: Double) = coefficients.reduceRight { coefficient, horner -> coefficient + horner * x }
}