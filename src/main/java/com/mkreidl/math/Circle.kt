package com.mkreidl.math

data class Circle(val x: Double, val y: Double, val r: Double) {

    val distFromOrigin get() = Math.sqrt(x * x + y * y)

    companion object {
        val UNIT = Circle(0.0, 0.0, 1.0)
    }
}