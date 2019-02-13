package com.mkreidl.ephemeris.time

import com.mkreidl.ephemeris.Angle
import com.mkreidl.ephemeris.HOURS_PER_MILLI
import com.mkreidl.ephemeris.SIDEREAL_HOURS_PER_MILLI
import com.mkreidl.ephemeris.solarsystem.Ecliptic
import com.mkreidl.ephemeris.solarsystem.Sun
import com.mkreidl.ephemeris.solarsystem.SunLowPrecision
import com.mkreidl.math.Polynomial
import java.util.*

class SiderealTime(private val instant: Instant) {

    fun getMeanSolarTime(longitude: Angle = Angle.ZERO) =
            Angle.ofHrs(instant hoursFrom midnightAtGreenwichSameDate()) + longitude

    fun getTrueSolarTime(longitude: Angle = Angle.ZERO, sun: Sun = SunLowPrecision(instant)) =
            Angle.ofRad(getMeanSolarTime(longitude).radians + sun.equationOfTime)

    fun getMeanSiderealTime(longitude: Angle = Angle.ZERO): Angle {
        val midnightUTC = midnightAtGreenwichSameDate()
        val daysBase = siderealDaysAtMidnightUTC(midnightUTC.julianCenturiesSinceJ2000)
        return Angle.ofHrs((daysBase * 24.0 + (instant siderealHoursFrom midnightUTC))) + longitude
    }

    fun getTrueSiderealTime(longitude: Angle = Angle.ZERO, ecliptic: Ecliptic = Ecliptic(instant)) =
            Angle.ofRad(getMeanSiderealTime(longitude).radians + ecliptic.nutationInRightAscension)

    private fun midnightAtGreenwichSameDate() = Instant.ofEpochMilli(
            GregorianCalendar(UTC).apply {
                timeInMillis = instant.epochMilli
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis)

    companion object {
        fun ofEpochMilli(epochMilli: Long) = SiderealTime(Instant(epochMilli))

        private infix fun Instant.hoursFrom(instant: Instant) = (epochMilli - instant.epochMilli) * HOURS_PER_MILLI
        private infix fun Instant.siderealHoursFrom(instant: Instant) = (epochMilli - instant.epochMilli) * SIDEREAL_HOURS_PER_MILLI

        private val UTC = TimeZone.getTimeZone("UTC")
        private val siderealDaysAtMidnightUTC = Polynomial(
                100.46061837,
                36000.770053608,
                0.000387933,
                1.0 / 38710000.0
        ) * (1.0 / 360)
    }
}