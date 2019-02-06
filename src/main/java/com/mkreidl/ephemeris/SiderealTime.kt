package com.mkreidl.ephemeris

import com.mkreidl.ephemeris.geometry.Angle
import com.mkreidl.ephemeris.solarsystem.Ecliptic
import com.mkreidl.ephemeris.solarsystem.Sun
import com.mkreidl.math.Polynomial
import java.util.*

/**
 * Calculate the Mean Sidereal Time for Greenwich at given date
 *
 * @return Greenwich Mean Sidereal Time in hours [h]
 */
fun Instant.getMeanSiderealTime() = getSiderealTimeDayFraction() * 24

/**
 * Calculate the Mean Sidereal Time for Greenwich at given date
 *
 * @return Greenwich Mean Sidereal Time in radians
 */
fun Instant.getMeanSiderealTimeRadians() = getSiderealTimeDayFraction() * 2.0 * Math.PI

fun Ecliptic.getTrueSiderealTimeRadians() = instant.getMeanSiderealTimeRadians() + nutationInRightAscension

private fun Instant.getSiderealTimeDayFraction(): Double {
    val midnightUTC = midnightAtGreenwichSameDate()
    val daysBase = siderealDaysAtMidnightUTC(midnightUTC.julianCenturiesSinceJ2000)
    val siderealTime = (daysBase + (this siderealDaysFrom midnightUTC)) % 1
    return if (siderealTime < 0.0) siderealTime + 1.0 else siderealTime
}

private fun Instant.midnightAtGreenwichSameDate() = Instant.ofEpochMilli(
        GregorianCalendar(UTC).apply {
            timeInMillis = epochMilli
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis)

fun Instant.getMeanSiderealTime(siderealTime: Angle) = siderealTime.set(getMeanSiderealTime(), Angle.Unit.HOURS)

fun Instant.getMeanSiderealTime(longitude: Angle, siderealTime: Angle): Angle {
    val hours = getMeanSiderealTime() + longitude.get(Angle.Unit.HOURS)
    return siderealTime.set(standardize24(hours), Angle.Unit.HOURS)
}

fun Instant.getMeanSolarTime(longitude: Angle, solarTime: Angle): Angle {
    val hours = (this hoursFrom midnightAtGreenwichSameDate()) + longitude.get(Angle.Unit.HOURS)
    return solarTime.set(standardize24(hours), Angle.Unit.HOURS)
}

fun Instant.getMeanSolarTimeHours(): Double {
    val hours = this hoursFrom midnightAtGreenwichSameDate()
    return if (hours < 0) hours + 24.0 else hours
}

fun Instant.getMeanSolarTimeRadians(): Double {
    val rad = (this hoursFrom midnightAtGreenwichSameDate()) * Math.PI / 12
    return if (rad < 0) rad + 2 * Math.PI else rad
}

fun Sun.getTrueSolarTimeRadians() = ecliptic.instant.getMeanSolarTimeRadians() + equationOfTime

private infix fun Instant.hoursFrom(instant: Instant) = (epochMilli - instant.epochMilli) * Instant.HOURS_PER_MILLI
private infix fun Instant.siderealDaysFrom(instant: Instant) = (epochMilli - instant.epochMilli) * Instant.DAYS_PER_MILLI * SIDEREAL_PER_SOLAR

private fun standardize24(hours: Double): Double {
    val hoursReduced = hours % 24
    return if (hoursReduced < 0) hoursReduced + 24 else hoursReduced
}

private val UTC = TimeZone.getTimeZone("UTC")
private const val SIDEREAL_PER_SOLAR = 1.00273790935
private const val SOLAR_PER_SIDEREAL = 1.0 / SIDEREAL_PER_SOLAR
private const val MILLIS_PER_SIDEREAL_DAY = Instant.MILLIS_PER_DAY * SOLAR_PER_SIDEREAL
private val siderealDaysAtMidnightUTC = Polynomial(
        100.46061837,
        36000.770053608,
        0.000387933,
        1.0 / 38710000.0
) * (1.0 / 360)