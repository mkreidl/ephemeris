package com.mkreidl.math

import java.util.*

class GaussAlgorithm(private val dim: Int) {

    private val dim2 = dim * dim
    private val identity = DoubleArray(dim2)
    private val allLines = 0 until dim2 step dim

    init {
        Arrays.fill(identity, 0.0)
        (0 until dim).forEach { identity[it * (dim + 1)] = 1.0 }
    }

    fun invert(matrix: DoubleArray, result: DoubleArray) {
        System.arraycopy(identity, 0, result, 0, dim2)
        return leftDivide(matrix, result)
    }

    fun leftDivide(divisor: DoubleArray, dividend: DoubleArray) {

        for (colIndex in 0 until dim) {
            // Find pivot...
            var pivotLineStart = colIndex * dim
            var pivot = divisor[pivotLineStart + colIndex]
            allLines.forEach {
                val entry = divisor[it + colIndex]
                if (it > colIndex * dim && Math.abs(entry) > Math.abs(pivot)) {
                    pivotLineStart = it
                    pivot = entry
                }
            }

            // divide the pivot line by pivot
            val invPivot = 1 / pivot
            for (col in 0 until dim) {
                divisor[pivotLineStart + col] *= invPivot
                dividend[pivotLineStart + col] *= invPivot
            }

            // subtract multiples of the pivot line from all other lines
            // in order to clear the entries in the pivot column
            allLines.forEach {
                if (it != pivotLineStart) {
                    val multiple = divisor[it + colIndex]
                    for (col in 0 until dim) {
                        divisor[it + col] -= multiple * divisor[pivotLineStart + col]
                        dividend[it + col] -= multiple * dividend[pivotLineStart + col]
                    }
                }
            }

            // move the pivot line such that the pivot ends up on the diagonal
            if (pivotLineStart != colIndex * dim) {
                swap(divisor, pivotLineStart, colIndex * dim, dim)
                swap(dividend, pivotLineStart, colIndex * dim, dim)
            }
        }
    }

    private fun swap(matrix: DoubleArray, indexA: Int, indexB: Int, count: Int) {
        if (count >= 1) {
            val tmp = matrix[indexA]
            matrix[indexA] = matrix[indexB]
            matrix[indexB] = tmp
            swap(matrix, indexA + 1, indexB + 1, count - 1)
        }
    }
}
