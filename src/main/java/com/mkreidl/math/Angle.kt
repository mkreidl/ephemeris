package com.mkreidl.math

operator fun Double.times(angle: Angle) = angle * this

fun sin(angle: Angle) = Math.sin(angle.radians)
fun cos(angle: Angle) = Math.cos(angle.radians)

class Angle(radians: Double) {

    val radians = reduce(radians)

    val degrees get() = Math.toDegrees(radians)
    val hours get() = radToHrs * radians

    val degreesNonNeg get() = Math.toDegrees(toPositive(radians))
    val hoursNonNeg get() = radToHrs * toPositive(radians)

    override fun equals(other: Any?) = other is Angle && other.radians == radians
    override fun hashCode() = radians.hashCode()

    operator fun unaryPlus() = this
    operator fun unaryMinus() = Angle(-radians)
    operator fun plus(other: Angle) = Angle(radians + other.radians)
    operator fun minus(other: Angle) = Angle(radians - other.radians)
    operator fun times(factor: Double) = Angle(radians * factor)
    operator fun div(divisor: Double) = Angle(radians / divisor)

    companion object {
        val ZERO = Angle(0.0)

        val degree = ofDeg(1, 0, 0.0)
        val minute = ofDeg(0, 1, 0.0)
        val second = ofDeg(0, 0, 1.0)

        fun ofRad(radians: Double) = Angle(radians)
        fun ofDeg(degrees: Double) = Angle(Math.toRadians(degrees))
        fun ofHrs(hours: Double) = Angle(hours * hrsToRad)
        fun ofDeg(degrees: Int, minutes: Int, seconds: Double) = Angle(Math.toRadians(degrees + minutes * min + seconds * sec))
        fun ofHrs(hours: Int, minutes: Int, seconds: Double) = Angle((hours + minutes * min + seconds * sec) * hrsToRad)

        fun ofDms(sexagesimal: Sexagesimal<*>) = ofDeg(sexagesimal.toDecimal())
        fun ofHms(sexagesimal: Sexagesimal<*>) = ofHrs(sexagesimal.toDecimal())

        fun reducePositive(radians: Double) = toPositive(reduce(radians))

        fun reduce(radians: Double): Double {
            val reduced = radians % TwoPi
            return when {
                reduced <= -Math.PI -> reduced + TwoPi
                reduced > Math.PI -> reduced - TwoPi
                else -> reduced
            }
        }

        internal fun toPositive(radians: Double) = when {
            radians < 0 -> radians + TwoPi
            else -> radians
        }

        private const val TwoPi = 2 * Math.PI
        private const val hrsToRad = Math.PI / 12.0
        private const val radToHrs = 1.0 / hrsToRad
        private const val min = 1.0 / 60.0
        private const val sec = 1.0 / 3_600.0
    }
}