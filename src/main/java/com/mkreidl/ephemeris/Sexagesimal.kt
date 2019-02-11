package com.mkreidl.ephemeris

import kotlin.math.roundToInt

fun Sexagesimal<Double>.round(): Sexagesimal<Int> {
    val sec = seconds.roundToInt()
    val min = minutes + sec / 60
    val int = integral + min / 60
    return Sexagesimal(sign, int, min % 60, sec % 60)
}

data class Sexagesimal<T : Number>(val sign: Int, val integral: Int, val minutes: Int, val seconds: T) {

    fun toDecimal() = sign * (integral + minutes * min + seconds.toDouble() * sec)

    companion object {
        fun of(decimal: Double): Sexagesimal<Double> {
            val sign = when {
                decimal > 0 -> 1
                decimal < 0 -> -1
                else -> 0
            }
            val seconds = Math.abs(decimal * 3_600).roundToInt()
            return Sexagesimal(sign, seconds / 3_600, (seconds / 60) % 60, (seconds % 60).toDouble())
        }

        private const val min = 1.0 / 60.0
        private const val sec = 1.0 / 3_600.0
    }
}