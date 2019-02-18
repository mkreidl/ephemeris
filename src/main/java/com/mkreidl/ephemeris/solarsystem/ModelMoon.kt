package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Distance
import com.mkreidl.ephemeris.SECONDS_PER_DAY
import com.mkreidl.ephemeris.geometry.Angle.DEG
import com.mkreidl.ephemeris.time.Instant
import com.mkreidl.math.Phase
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
class ModelMoon : OrbitalModel {

    override val type = OrbitalModel.Type.GEOCENTRIC
    override val distanceUnit = Distance.m

    override var phase = PhaseSpherical.ZERO
        private set

    override fun compute(instant: Instant): Phase {
        val t = instant.terrestrialDynamicalTime

        moon = ClassicalOrbitalElements(
                node = orbElMoon0.node + t * orbElMoon1.node,
                inclination = orbElMoon0.inclination + t * orbElMoon1.inclination,
                periapsis = orbElMoon0.periapsis + t * orbElMoon1.periapsis,
                axis = orbElMoon0.axis + t * orbElMoon1.axis,
                excentricity = orbElMoon0.excentricity + t * orbElMoon1.excentricity,
                meanAnomaly = orbElMoon0.meanAnomaly + t * orbElMoon1.meanAnomaly
        )
        sun = ClassicalOrbitalElements(
                node = orbElSun0.node + t * orbElSun1.node,
                inclination = orbElSun0.inclination + t * orbElSun1.inclination,
                periapsis = orbElSun0.periapsis + t * orbElSun1.periapsis,
                axis = orbElSun0.axis + t * orbElSun1.axis,
                excentricity = orbElSun0.excentricity + t * orbElSun1.excentricity,
                meanAnomaly = orbElSun0.meanAnomaly + t * orbElSun1.meanAnomaly
        )

        val posSphericalRaw = moon.computePosition().spherical
        val posSpherical = Spherical3(
                lon = posSphericalRaw.lon + longitudeCorrection(),
                lat = posSphericalRaw.lat + latitudeCorrection(),
                dst = posSphericalRaw.dst + distanceCorrection()
        )
        phase = PhaseSpherical(posSpherical, velSpherical)
        return phase
    }

    lateinit var moon: ClassicalOrbitalElements
        private set
    private lateinit var sun: ClassicalOrbitalElements

    // Mean elongation of Moon
    private fun D() = moon.meanLongitude - sun.meanLongitude

    // Argument of latitude of Moon
    private fun F() = moon.meanLongitude - moon.node

    private fun longitudeCorrection(): Double {
        val D = D()
        val DD = 2 * D
        return DEG * ((((-1.274 * sin(moon.meanAnomaly - DD) + 0.658 * sin(DD)
                - 0.186 * sin(sun.meanAnomaly)
                - 0.059 * sin(2 * moon.meanAnomaly - DD)
                - 0.057 * sin(moon.meanAnomaly - DD + sun.meanAnomaly))
                + 0.053 * sin(moon.meanAnomaly + DD)
                + 0.046 * sin(DD - sun.meanAnomaly)
                + 0.041 * sin(moon.meanAnomaly - sun.meanAnomaly))
                - 0.035 * sin(D)
                - 0.031 * sin(moon.meanAnomaly + sun.meanAnomaly)
                - 0.015 * sin(2 * (F() - D))) + 0.011 * sin(moon.meanAnomaly - 4 * D))
    }

    private fun latitudeCorrection(): Double {
        val DD = 2 * D()
        val F = F()
        return DEG * ((-0.173 * sin(F - DD)
                - 0.055 * sin(moon.meanAnomaly - F - DD)
                - 0.046 * sin(moon.meanAnomaly + F - DD))
                + 0.033 * sin(F + DD)
                + 0.017 * sin(2 * moon.meanAnomaly + F))
    }

    private fun distanceCorrection(): Double {
        val DD = 2 * D()
        return DEG * (-0.58 * cos(moon.meanAnomaly - DD) - 0.46 * cos(DD))
    }

    companion object {
        private val velSpherical = Vector3(0.0, 2 * Math.PI / (29 * SECONDS_PER_DAY), 0.0)

        // 0-order terms for Moon's orbital elements
        private val orbElMoon0 = ClassicalOrbitalElements(
                node = 125.1228 * DEG,
                inclination = 5.1454 * DEG,
                periapsis = 318.0634 * DEG,
                axis = 60.2666 * Body.EARTH.RADIUS_EQUATORIAL_M,
                excentricity = 0.054900,
                meanAnomaly = 115.3654 * DEG
        )
        // 1-order terms for Moon's orbital elements
        private val orbElMoon1 = ClassicalOrbitalElements(
                node = -0.0529538083 * DEG,
                inclination = 0.0 * DEG,
                periapsis = 0.1643573223 * DEG,
                axis = 0.0 * Body.EARTH.RADIUS_EQUATORIAL_M,
                excentricity = 0.0,
                meanAnomaly = 13.0649929509 * DEG
        )

        // 0-order terms for Sun's orbital elements
        private val orbElSun0 = ClassicalOrbitalElements(
                node = 0.0 * DEG,
                inclination = 0.0 * DEG,
                periapsis = 282.9404 * DEG,
                axis = 1.0 * Distance.AU.toMeters(),
                excentricity = 0.016709,
                meanAnomaly = 356.0470 * DEG
        )
        // 1-order terms for Sun's orbital elements
        private val orbElSun1 = ClassicalOrbitalElements(
                node = 0.0 * DEG,
                inclination = 0.0 * DEG,
                periapsis = 4.70935E-5 * DEG,
                axis = 0.0 * Distance.AU.toMeters(),
                excentricity = -1.151E-9,
                meanAnomaly = 0.9856002585 * DEG
        )
    }
}
