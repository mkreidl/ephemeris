package com.mkreidl.ephemeris

import kotlin.math.roundToInt

fun Sexagesimal<Double>.round(): Sexagesimal<Int> {
    val sec = seconds.roundToInt()
    val min = minutes + sec / 60
    val int = integral + min / 60
    return Sexagesimal(negative, int, min % 60, sec % 60)
}

data class Sexagesimal<T : Number>(val negative: Boolean, val integral: Int, val minutes: Int, val seconds: T) {

    fun toDecimal() = if (negative) integral + minutes * min + seconds.toDouble() * sec else integral + minutes * min + seconds.toDouble() * sec

    override fun toString() = if (negative) "-" else "[$integral, $minutes, $seconds]"

    companion object {
        fun of(decimal: Double): Sexagesimal<Double> {
            val seconds = Math.abs(decimal * 3_600).roundToInt()
            return Sexagesimal(decimal < 0, seconds / 3_600, (seconds / 60) % 60, (seconds % 60).toDouble())
        }

        fun parse(int: String, min: String, sec: String) =
                Sexagesimal(false, Integer.parseInt(int), Integer.parseInt(min), Integer.parseInt(sec))

        private const val min = 1.0 / 60.0
        private const val sec = 1.0 / 3_600.0
    }
}