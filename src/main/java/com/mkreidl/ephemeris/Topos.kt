package com.mkreidl.ephemeris

import com.mkreidl.ephemeris.solarsystem.Body
import com.mkreidl.ephemeris.solarsystem.Ecliptic
import com.mkreidl.ephemeris.time.Instant
import com.mkreidl.ephemeris.time.SiderealTime
import com.mkreidl.math.Angle
import com.mkreidl.math.Axis
import com.mkreidl.math.Matrix3x3
import com.mkreidl.math.Spherical3

data class Topos internal constructor(val longitude: Double, val latitude: Double, val instant: Instant) {

    private val ecliptic = Ecliptic(instant)
    private val lon = Angle.ofRad(longitude)
    private val lat = Angle.ofRad(latitude)

    val localMeanSiderealTime by lazy { SiderealTime(instant).getMeanSiderealTime(lon) }
    val localTrueSiderealTime by lazy { SiderealTime(instant).getTrueSiderealTime(lat) }

    val meanEquatorialSphe by lazy { Spherical3(Body.EARTH.RADIUS_MEAN_M, localMeanSiderealTime.radians, latitude) }
    val trueEquatorialSphe by lazy { Spherical3(Body.EARTH.RADIUS_MEAN_M, localTrueSiderealTime.radians, latitude) }

    val meanEquatorial by lazy { meanEquatorialSphe.toCartesian() }
    val trueEquatorial by lazy { trueEquatorialSphe.toCartesian() }

    val meanEcliptical by lazy { Matrix3x3.rotation(ecliptic.meanObliquity, Axis.X) * meanEquatorial }
    val trueEcliptical by lazy { Matrix3x3.rotation(ecliptic.trueObliquity, Axis.X) * trueEquatorial }

    companion object {
        fun of(longitudeDeg: Double, latitudeDeg: Double, epochMilli: Long) =
                Topos(Math.toRadians(longitudeDeg), Math.toRadians(latitudeDeg), Instant(epochMilli))
    }
}