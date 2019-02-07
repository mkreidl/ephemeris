package com.mkreidl.ephemeris

class Instant internal constructor(val epochMilli: Long) {

    val julianDayFraction get() = julianDaysSinceJ2000 + J2000_DAY_NUMBER

    val julianDaysSinceJ2000 get() = julianDaysSince(J2000)
    val julianYearsSinceJ2000 get() = julianYearsSince(J2000)
    val julianCenturiesSinceJ2000 get() = julianCenturiesSince(J2000)
    val julianMillenniaSinceJ2000 get() = julianMillenniaSince(J2000)

    val terrestrialDynamicalTime get() = julianDaysSinceJ2000 + 1.5

    fun julianDaysSince(instant: Instant) = (epochMilli - instant.epochMilli) * DAYS_PER_MILLI
    fun julianYearsSince(instant: Instant) = (epochMilli - instant.epochMilli) * YEARS_PER_MILLI
    fun julianCenturiesSince(instant: Instant) = (epochMilli - instant.epochMilli) * CENTURIES_PER_MILLI
    fun julianMillenniaSince(instant: Instant) = (epochMilli - instant.epochMilli) * MILLENNIA_PER_MILLI

    override fun equals(other: Any?) = other is Instant && other.epochMilli == epochMilli

    companion object {
        val J2000 = Instant.ofEpochMilli(946_728_000_000L)
        const val J2000_DAY_NUMBER = 2_451_545.0

        @JvmStatic
        fun ofEpochMilli(epochMilli: Long) = Instant(epochMilli)

        @JvmStatic
        fun ofJulianDayFraction(julianDayFraction: Double) = Instant(julianDayToEpochMilli(julianDayFraction))

        private fun julianDayToEpochMilli(d: Double) = ((d - J2000_DAY_NUMBER) * MILLIS_PER_DAY).toLong() + J2000.epochMilli

        private const val MILLIS_PER_HOUR = 3_600_000L

        const val MILLIS_PER_DAY = MILLIS_PER_HOUR * 24
        const val MILLIS_PER_YEAR = MILLIS_PER_DAY * 365.25
        const val MILLIS_PER_CENTURY = MILLIS_PER_YEAR * 100
        const val MILLIS_PER_MILLENNIUM = MILLIS_PER_YEAR * 1_000

        const val HOURS_PER_MILLI = 1.0 / MILLIS_PER_HOUR
        const val DAYS_PER_MILLI = 1.0 / MILLIS_PER_DAY
        const val YEARS_PER_MILLI = 1.0 / MILLIS_PER_YEAR
        const val CENTURIES_PER_MILLI = 1.0 / MILLIS_PER_CENTURY
        const val MILLENNIA_PER_MILLI = 1.0 / MILLIS_PER_MILLENNIUM
    }
}