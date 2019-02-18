package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Distance
import com.mkreidl.ephemeris.time.Instant
import com.mkreidl.ephemeris.Time
import com.mkreidl.ephemeris.geometry.Angle.DEG
import com.mkreidl.ephemeris.geometry.ClassicalOrbitalElements
import com.mkreidl.math.PhaseCartesian
import com.mkreidl.math.PhaseSpherical
import com.mkreidl.math.Spherical3
import com.mkreidl.math.Vector3
import java.lang.Math.cos
import java.lang.Math.sin

/**
 * @author mkreidl
 *
 *
 * Orbital elements taken from:
 * Paul Schlyter, <a>http://www.stjarnhimlen.se/</a>
 *
 *
 * Kepler's equation is solved using Newton's method to obtain
 * Moon's coordinates in the Cartesian reference frame.
 */
class ModelMoon : OrbitalModel() {

    override val type = OrbitalModel.Type.GEOCENTRIC

    override val distanceUnit = Distance.m

    /**
     * N = longitude of the ascending node
     * i = inclination to the ecliptic (plane of the Earth's orbit)
     * w = argument of perihelion
     * a = semi-major axis
     * e = eccentricity (0=circle, 0-1=ellipse, 1=parabola)
     * M = mean anomaly (0 at perihelion; increases uniformly with time)
     */

    private val orbitalElements = ClassicalOrbitalElements()
    private var posSpherical = Spherical3.ZERO
    private var posCartesian = Vector3.ZERO

    override fun computeSpherical(instant: Instant): PhaseSpherical {
        compute(instant)
        return PhaseSpherical(posSpherical, Vector3.ZERO)
    }

    override fun computeCartesian(instant: Instant): PhaseCartesian {
        compute(instant)
        return PhaseCartesian(
                posCartesian,
                Vector3(-posCartesian.y, posCartesian.x, 0.0) * velocityScale)
    }

    fun computeOrbitalElements(instant: Instant): ClassicalOrbitalElements {
        compute(instant)
        return ClassicalOrbitalElements().set(orbitalElements)
    }

    /**
     * Calculate the position of the Moon at a given time.
     *
     * @param time Time object.
     */
    private fun compute(instant: Instant) {
        val t = instant.terrestrialDynamicalTime

        orbitalElements.set(orbElMoonSeries[1])
        orbitalElements.times(t)
        orbitalElements.add(orbElMoonSeries[0])

        orbitalElements.computePosition()
        posSpherical = orbitalElements.positionSpherical

        orbElSun.set(orbElSunSeries[1])
        orbElSun.times(t)
        orbElSun.add(orbElSunSeries[0])

        posSpherical = Spherical3(
                lon = posSpherical.lon + longitudeCorrection(),
                lat = posSpherical.lat + latitudeCorrection(),
                dst = posSpherical.dst + distanceCorrection()
        )

        posSpherical = posSpherical.reduce()
        posCartesian = posSpherical.cartesian
    }

    private fun D(): Double {
        // Mean elongation of Moon
        return orbitalElements.meanLongitude() - orbElSun.meanLongitude()
    }

    private fun F(): Double {
        // Argument of latitude of Moon
        return orbitalElements.meanLongitude() - orbitalElements.node
    }

    private fun longitudeCorrection(): Double {
        val D = D()
        val DD = 2 * D
        return DEG * ((((-1.274 * sin(orbitalElements.meanAnom - DD) + 0.658 * sin(DD)
                - 0.186 * sin(orbElSun.meanAnom)
                - 0.059 * sin(2 * orbitalElements.meanAnom - DD)
                - 0.057 * sin(orbitalElements.meanAnom - DD + orbElSun.meanAnom))
                + 0.053 * sin(orbitalElements.meanAnom + DD)
                + 0.046 * sin(DD - orbElSun.meanAnom)
                + 0.041 * sin(orbitalElements.meanAnom - orbElSun.meanAnom))
                - 0.035 * sin(D)
                - 0.031 * sin(orbitalElements.meanAnom + orbElSun.meanAnom)
                - 0.015 * sin(2 * (F() - D))) + 0.011 * sin(orbitalElements.meanAnom - 4 * D))
    }

    private fun latitudeCorrection(): Double {
        val DD = 2 * D()
        val F = F()
        return DEG * ((-0.173 * sin(F - DD)
                - 0.055 * sin(orbitalElements.meanAnom - F - DD)
                - 0.046 * sin(orbitalElements.meanAnom + F - DD))
                + 0.033 * sin(F + DD)
                + 0.017 * sin(2 * orbitalElements.meanAnom + F))
    }

    private fun distanceCorrection(): Double {
        val DD = 2 * D()
        return DEG * (-0.58 * cos(orbitalElements.meanAnom - DD) - 0.46 * cos(DD))
    }

    companion object {
        private val velocityScale = 2 * Math.PI / (29 * Time.SECONDS_PER_DAY)

        private val orbElMoonSeries = arrayOf(ClassicalOrbitalElements(), ClassicalOrbitalElements())
        private val orbElSunSeries = arrayOf(ClassicalOrbitalElements(), ClassicalOrbitalElements())
        private val orbElSun = ClassicalOrbitalElements()

        init {
            // 0-order terms for Moon's orbital elements
            orbElMoonSeries[0].node = 125.1228 * DEG
            orbElMoonSeries[0].incl = 5.1454 * DEG
            orbElMoonSeries[0].periapsis = 318.0634 * DEG
            orbElMoonSeries[0].axis = 60.2666 * Body.EARTH.RADIUS_EQUATORIAL_M
            orbElMoonSeries[0].exc = 0.054900
            orbElMoonSeries[0].meanAnom = 115.3654 * DEG

            // 1-order terms for Moon's orbital elements
            orbElMoonSeries[1].node = -0.0529538083 * DEG
            orbElMoonSeries[1].incl = 0.0 * DEG
            orbElMoonSeries[1].periapsis = 0.1643573223 * DEG
            orbElMoonSeries[1].axis = 0.0 * Body.EARTH.RADIUS_EQUATORIAL_M
            orbElMoonSeries[1].exc = 0.0
            orbElMoonSeries[1].meanAnom = 13.0649929509 * DEG

            // 0-order terms for Sun's orbital elements
            orbElSunSeries[0].node = 0.0 * DEG
            orbElSunSeries[0].incl = 0.0 * DEG
            orbElSunSeries[0].periapsis = 282.9404 * DEG
            orbElSunSeries[0].axis = 1.0 * Distance.AU.toMeters()
            orbElSunSeries[0].exc = 0.016709
            orbElSunSeries[0].meanAnom = 356.0470 * DEG

            // 1-order terms for Sun's orbital elements
            orbElSunSeries[1].node = 0.0 * DEG
            orbElSunSeries[1].incl = 0.0 * DEG
            orbElSunSeries[1].periapsis = 4.70935E-5 * DEG
            orbElSunSeries[1].axis = 0.0 * Distance.AU.toMeters()
            orbElSunSeries[1].exc = -1.151E-9
            orbElSunSeries[1].meanAnom = 0.9856002585 * DEG
        }
    }
}
