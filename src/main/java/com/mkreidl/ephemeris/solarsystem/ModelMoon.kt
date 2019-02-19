package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Distance
import com.mkreidl.ephemeris.SECONDS_PER_DAY
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
        val d = D()
        val dd = 2 * d
        return Math.toRadians(
                (((-1.274 * sin(moon.meanAnomaly - dd) + 0.658 * sin(dd)
                        - 0.186 * sin(sun.meanAnomaly)
                        - 0.059 * sin(2 * moon.meanAnomaly - dd)
                        - 0.057 * sin(moon.meanAnomaly - dd + sun.meanAnomaly))
                        + 0.053 * sin(moon.meanAnomaly + dd)
                        + 0.046 * sin(dd - sun.meanAnomaly)
                        + 0.041 * sin(moon.meanAnomaly - sun.meanAnomaly))
                        - 0.035 * sin(d)
                        - 0.031 * sin(moon.meanAnomaly + sun.meanAnomaly)
                        - 0.015 * sin(2 * (F() - d))) + 0.011 * sin(moon.meanAnomaly - 4 * d)
        )
    }

    private fun latitudeCorrection(): Double {
        val dd = 2 * D()
        val f = F()
        return Math.toRadians(
                (-0.173 * sin(f - dd)
                        - 0.055 * sin(moon.meanAnomaly - f - dd)
                        - 0.046 * sin(moon.meanAnomaly + f - dd))
                        + 0.033 * sin(f + dd)
                        + 0.017 * sin(2 * moon.meanAnomaly + f)
        )
    }

    private fun distanceCorrection(): Double {
        val dd = 2 * D()
        return Math.toRadians(-0.58 * cos(moon.meanAnomaly - dd) - 0.46 * cos(dd))
    }

    companion object {
        private val velSpherical = Vector3(0.0, 2 * Math.PI / (29 * SECONDS_PER_DAY), 0.0)

        // 0-order terms for Moon's orbital elements
        private val orbElMoon0 = ClassicalOrbitalElements(
                node = Math.toRadians(125.1228),
                inclination = Math.toRadians(5.1454),
                periapsis = Math.toRadians(318.0634),
                axis = 60.2666 * Body.EARTH.RADIUS_EQUATORIAL_M,
                excentricity = 0.054900,
                meanAnomaly = Math.toRadians(115.3654)
        )
        // 1-order terms for Moon's orbital elements
        private val orbElMoon1 = ClassicalOrbitalElements(
                node = Math.toRadians(-0.0529538083),
                inclination = Math.toRadians(0.0),
                periapsis = Math.toRadians(0.1643573223),
                axis = 0.0 * Body.EARTH.RADIUS_EQUATORIAL_M,
                excentricity = 0.0,
                meanAnomaly = Math.toRadians(13.0649929509)
        )

        // 0-order terms for Sun's orbital elements
        private val orbElSun0 = ClassicalOrbitalElements(
                node = Math.toRadians(0.0),
                inclination = Math.toRadians(0.0),
                periapsis = Math.toRadians(282.9404),
                axis = 1.0 * Distance.AU.toMeters(),
                excentricity = 0.016709,
                meanAnomaly = Math.toRadians(356.0470)
        )
        // 1-order terms for Sun's orbital elements
        private val orbElSun1 = ClassicalOrbitalElements(
                node = Math.toRadians(0.0),
                inclination = Math.toRadians(0.0),
                periapsis = Math.toRadians(4.70935E-5),
                axis = 0.0 * Distance.AU.toMeters(),
                excentricity = -1.151E-9,
                meanAnomaly = Math.toRadians(0.9856002585)
        )
    }
}
