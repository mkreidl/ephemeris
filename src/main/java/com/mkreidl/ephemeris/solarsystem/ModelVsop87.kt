package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Distance
import com.mkreidl.ephemeris.MILLENNIA_PER_SECOND
import com.mkreidl.ephemeris.time.Instant
import com.mkreidl.math.*

typealias Vsop87Series = Array<Array<Array<DoubleArray>>>

abstract class ModelVsop87(private val coefficients: Vsop87Series) : OrbitalModel {

    override val type = OrbitalModel.Type.HELIOCENTRIC
    override val distanceUnit = Distance.AU

    protected var results = DoubleArray(6)
    private var instantCached: Instant? = null

    protected fun computeSeries(instant: Instant) {
        if (instant != instantCached) {
            val t = instant.julianMillenniaSinceJ2000
            for (dim in 0 until 3) {
                var pos = 0.0
                var vel = 0.0
                for (n in coefficients[dim].size - 1 downTo 1) {
                    val c = cosSeries(dim, n, t)
                    val s = sinSeries(dim, n - 1, t)
                    pos = pos * t + c
                    vel = vel * t + s + n * c
                }
                results[dim] = pos * t + cosSeries(dim, 0, t)
                results[dim + 3] = vel * MILLENNIA_PER_SECOND
            }
            // remember time for which last calculation was performed
            instantCached = instant
        }
    }

    private fun cosSeries(dim: Int, n: Int, time: Double) = coefficients[dim][n]
            .map { it[0] * Math.cos(it[1] + it[2] * time) }
            .sum()

    private fun sinSeries(dim: Int, n: Int, time: Double) = coefficients[dim][n]
            .map { -it[0] * it[2] * Math.sin(it[1] + it[2] * time) }
            .sum()

    open class XYZ(coefficients: Vsop87Series) : ModelVsop87(coefficients) {

        final override var phase = PhaseCartesian.ZERO
            private set

        override fun compute(instant: Instant): PhaseCartesian {
            computeSeries(instant)
            return PhaseCartesian(
                    Vector3(results[0], results[1], results[2]),
                    Vector3(results[3], results[4], results[5])
            )
        }
    }

    open class LBR(coefficients: Vsop87Series) : ModelVsop87(coefficients) {

        final override var phase = PhaseSpherical.ZERO
            private set

        override fun compute(instant: Instant): Phase {
            computeSeries(instant)
            phase = PhaseSpherical(
                    Spherical3(results[2], results[0], results[1]).reduce(),
                    Vector3(results[5], results[3], results[4])
            )
            return phase
        }
    }
}
