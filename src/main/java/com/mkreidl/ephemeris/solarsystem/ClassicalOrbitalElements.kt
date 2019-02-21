package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.solarsystem.vsop87.Vsop87OrbitalElements
import com.mkreidl.math.Angle
import com.mkreidl.math.Coordinates
import com.mkreidl.math.PhaseCartesian
import com.mkreidl.math.Vector3

/**
 * N = longitude of the ascending node
 * i = inclination to the ecliptic (plane of the Earth's orbit)
 * w = argument of perihelion
 * a = semi-major axis
 * e = eccentricity (0=circle, 0-1=ellipse, 1=parabola)
 * M = mean anomaly (0 at perihelion; increases uniformly with time)
 */
data class ClassicalOrbitalElements(
        val node: Double,
        val inclination: Double,
        val periapsis: Double,
        val axis: Double,
        val excentricity: Double,
        val meanAnomaly: Double
) : OrbitalElements {

    override val classical get() = this

    override val vsop87 by lazy {
        Vsop87OrbitalElements(
                a = axis,
                l = meanLongitude,
                h = excentricity * Math.sin(node + periapsis),
                k = excentricity * Math.cos(node + periapsis),
                p = Math.sin(0.5 * inclination) * Math.sin(node),
                q = Math.sin(0.5 * inclination) * Math.cos(node)
        )
    }

    override fun computePhase() = PhaseCartesian(computePosition().cartesian, Vector3.ZERO)

    override fun computePosition(): Coordinates {
        val eccentricAnomaly = computeEccentricAnomaly(1e-13, 10)
        // Cartesian coordinates in the orbital plane,
        // The ascending node defines the x-axis
        val xv = axis * (Math.cos(eccentricAnomaly) - excentricity)
        val yv = axis * (Math.sqrt(1.0 - excentricity * excentricity) * Math.sin(eccentricAnomaly))
        // Cartesian coordinates after rotation to account for periapsis
        val xw = xv * Math.cos(periapsis) - yv * Math.sin(periapsis)
        val yw = xv * Math.sin(periapsis) + yv * Math.cos(periapsis)
        return Vector3(
                x = Math.cos(node) * xw - Math.sin(node) * yw * Math.cos(inclination),
                y = Math.sin(node) * xw + Math.cos(node) * yw * Math.cos(inclination),
                z = yw * Math.sin(inclination)
        )
    }

    val meanLongitude get() = node + periapsis + meanAnomaly

    /**
     * Solve Kepler's equation with Newton method
     *
     * @param eps Required precision
     * @return
     */
    fun computeEccentricAnomaly(eps: Double, maxIter: Int = Int.MAX_VALUE): Double {
        var e0: Double
        var excentricAnomaly = meanAnomaly
        var iter = 0
        // Solve Kepler's equation E - e*Math.sin(E) = M by Newton iteration
        do {
            e0 = excentricAnomaly
            excentricAnomaly = e0 - (e0 - excentricity * Math.sin(e0) - meanAnomaly) / (1.0 - excentricity * Math.cos(e0))
            println(excentricAnomaly)
            ++iter
        } while (iter < maxIter && Math.abs(excentricAnomaly - e0) > eps)
        return excentricAnomaly
    }

    fun reduce() = copy(
            node = Angle.reduce(node),
            inclination = Angle.reduce(inclination),
            periapsis = Angle.reduce(periapsis),
            meanAnomaly = Angle.reduce(meanAnomaly)
    )
}
