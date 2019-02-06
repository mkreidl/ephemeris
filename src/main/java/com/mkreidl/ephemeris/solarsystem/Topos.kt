package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Instant
import com.mkreidl.ephemeris.geometry.Angle
import com.mkreidl.ephemeris.getMeanSiderealTimeRadians
import com.mkreidl.ephemeris.getTrueSiderealTimeRadians
import com.mkreidl.math.Axis
import com.mkreidl.math.Matrix3x3
import com.mkreidl.math.Sphe

data class Topos(val longitude: Double, val latitude: Double, val instant: Instant) {

    private val ecliptic = Ecliptic(instant)

    val localMeanSiderealTimeRad = Angle.standardizePositive(instant.getMeanSiderealTimeRadians() + longitude)
    val localTrueSiderealTimeRad = Angle.standardizePositive(ecliptic.getTrueSiderealTimeRadians() + longitude)

    val equatorialMeanSphe = Sphe(Body.EARTH.RADIUS_MEAN_M, localMeanSiderealTimeRad, latitude)
    val equatorialTrueSphe = Sphe(Body.EARTH.RADIUS_MEAN_M, localTrueSiderealTimeRad, latitude)

    val meanEquatorial = equatorialMeanSphe.toCartesian()
    val trueEquatorial = equatorialTrueSphe.toCartesian()

    val meanEcliptical = Matrix3x3.rotation(ecliptic.meanObliquity, Axis.X) * meanEquatorial
    val trueEcliptical = Matrix3x3.rotation(ecliptic.trueObliquity, Axis.X) * trueEquatorial
}