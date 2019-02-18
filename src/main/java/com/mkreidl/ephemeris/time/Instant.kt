package com.mkreidl.ephemeris.time

import com.mkreidl.ephemeris.*

class Instant internal constructor(val epochMilli: Long) : Comparable<Instant> {

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

    override fun compareTo(other: Instant) = epochMilli.compareTo(other.epochMilli)

    fun addMillis(millis: Long) = Instant(epochMilli + millis)

    override fun equals(other: Any?) = other is Instant && other.epochMilli == epochMilli
    override fun hashCode() = epochMilli.hashCode()

    companion object {
        val J2000 = ofEpochMilli(946_728_000_000L)

        @JvmStatic
        fun ofEpochMilli(epochMilli: Long) = Instant(epochMilli)

        @JvmStatic
        fun ofJulianDayFraction(julianDayFraction: Double) = Instant(julianDayToEpochMilli(julianDayFraction))

        private fun julianDayToEpochMilli(d: Double) = ((d - J2000_DAY_NUMBER) * MILLIS_PER_DAY).toLong() + J2000.epochMilli
    }
}