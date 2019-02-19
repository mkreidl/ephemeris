package com.mkreidl.ephemeris.sky

import com.mkreidl.ephemeris.solarsystem.Body
import com.mkreidl.ephemeris.solarsystem.Ecliptic
import com.mkreidl.ephemeris.time.Instant
import com.mkreidl.ephemeris.time.SiderealTime
import com.mkreidl.math.*

data class Topos internal constructor(val longitude: Double, val latitude: Double, val instant: Instant) {

    private val ecliptic = Ecliptic(instant)
    private val lon = Angle.ofRad(longitude)

    val localMeanSiderealTime by lazy { SiderealTime(instant).getMeanSiderealTime(lon) }
    val localTrueSiderealTime by lazy { SiderealTime(instant).getTrueSiderealTime(lon) }

    val meanEquatorialSphe by lazy { Spherical3(Body.EARTH.RADIUS_MEAN_M, localMeanSiderealTime.radians, latitude) }
    val trueEquatorialSphe by lazy { Spherical3(Body.EARTH.RADIUS_MEAN_M, localTrueSiderealTime.radians, latitude) }

    val meanEquatorial by lazy { meanEquatorialSphe.cartesian }
    val trueEquatorial by lazy { trueEquatorialSphe.cartesian }

    val meanEcliptical by lazy { Matrix3x3.rotation(ecliptic.meanObliquity, Axis.X) * meanEquatorial }
    val trueEcliptical by lazy { Matrix3x3.rotation(ecliptic.trueObliquity, Axis.X) * trueEquatorial }

    val trafoMeanEquatorialToHorizontal by lazy {
        // North azimuth: N=0d, E=90d, S=180d, W=270d
        Matrix3x3.ONE.copy(a11 = -1.0) *
                Matrix3x3.rotation(latitude - Math.PI * 0.5, Axis.Y) *
                Matrix3x3.rotation(-localMeanSiderealTime.radians, Axis.Z)
    }

    val trafoTrueEquatorialToHorizontal by lazy {
        Matrix3x3.ONE.copy(a11 = -1.0) *
                Matrix3x3.rotation(latitude - Math.PI * 0.5, Axis.Y) *
                Matrix3x3.rotation(-localTrueSiderealTime.radians, Axis.Z)
    }

    fun mapMeanEquatorialToHorizontal(coordinates: Vector3) = trafoMeanEquatorialToHorizontal(coordinates)
    fun mapTrueEquatorialToHorizontal(coordinates: Vector3) = trafoTrueEquatorialToHorizontal(coordinates)

    fun mapMeanEquatorialToHorizontal(coordinates: Spherical3) = trafoMeanEquatorialToHorizontal(coordinates.cartesian).spherical
    fun mapTrueEquatorialToHorizontal(coordinates: Spherical3) = trafoTrueEquatorialToHorizontal(coordinates.cartesian).spherical

    fun computeTopocentricFromMeanEquatorial(geocentric: Vector3) = geocentric - meanEquatorial
    fun computeTopocentricFromTrueEquatorial(geocentric: Vector3) = geocentric - trueEquatorial

    fun computeTopocentricFromMeanEquatorial(geocentric: Spherical3) = (geocentric.cartesian - meanEquatorial).spherical
    fun computeTopocentricFromTrueEquatorial(geocentric: Spherical3) = (geocentric.cartesian - trueEquatorial).spherical

    companion object {

        fun of(location: Spherical3, instant: Instant) =
                Topos(location.lon, location.lat, instant)

        fun of(longitudeDeg: Double, latitudeDeg: Double, epochMilli: Long) =
                Topos(Math.toRadians(longitudeDeg), Math.toRadians(latitudeDeg), Instant(epochMilli))
    }
}