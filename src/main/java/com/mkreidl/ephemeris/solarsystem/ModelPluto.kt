package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.DAYS_PER_SECOND
import com.mkreidl.ephemeris.time.Instant
import com.mkreidl.math.Sphe

class ModelPluto : OrbitalModel() {

    private var d: Double = 0.toDouble()
    private var s: Double = 0.toDouble()
    private var p: Double = 0.toDouble()
    private var s1: Double = 0.toDouble()
    private var p1: Double = 0.toDouble()

    override fun computeCartesian(instant: Instant) =
            computeSpherical(instant).toCartesian().scaleVelocity(DAYS_PER_SECOND)

    override fun computeSpherical(instant: Instant): PhaseSpherical {
        computeTime(instant)
        return PhaseSpherical(computePosition(), computeVelocity())
    }

    private fun computeTime(instant: Instant) {
        d = instant.terrestrialDynamicalTime
        s1 = Math.toRadians(0.033459652)
        p1 = Math.toRadians(0.003968789)
        s = S0 + s1 * d
        p = P0 + p1 * d
    }

    private fun computePosition() = Sphe(
            lon = Math.toRadians((((((238.9508 + 0.00400703 * d - 19.799 * Math.sin(p) + 19.848 * Math.cos(p)
                    + 0.897 * Math.sin(2 * p)) - 4.956 * Math.cos(2 * p)
                    + 0.610 * Math.sin(3 * p) + 1.211 * Math.cos(3 * p))
                    - 0.341 * Math.sin(4 * p) - 0.190 * Math.cos(4 * p)) + 0.128 * Math.sin(5 * p) - 0.034 * Math.cos(5 * p)
                    - 0.038 * Math.sin(6 * p)) + 0.031 * Math.cos(6 * p)
                    + 0.020 * Math.sin(s - p)) - 0.010 * Math.cos(s - p)),
            lat = Math.toRadians((((((-3.9082
                    - 5.453 * Math.sin(p) - 14.975 * Math.cos(p))
                    + 3.527 * Math.sin(2 * p) + 1.673 * Math.cos(2 * p)) - 1.051 * Math.sin(3 * p) + 0.328 * Math.cos(3 * p)
                    + 0.179 * Math.sin(4 * p)) - 0.292 * Math.cos(4 * p)
                    + 0.019 * Math.sin(5 * p) + 0.100 * Math.cos(5 * p))
                    - 0.031 * Math.sin(6 * p) - 0.026 * Math.cos(6 * p)) + 0.011 * Math.cos(s - p)),
            dst = ((40.72
                    + 6.68 * Math.sin(p) + 6.90 * Math.cos(p))
                    - 1.18 * Math.sin(2 * p) - 0.03 * Math.cos(2 * p)) + 0.15 * Math.sin(3 * p) - 0.14 * Math.cos(3 * p)
    )

    private fun computeVelocity() = Sphe(
            lon = Math.toRadians(0.00400703 + p1 * ((((-19.799 * Math.cos(p) - 19.848 * Math.sin(p)
                    + 0.897 * 2.0 * Math.cos(2 * p) + 4.956 * 2.0 * Math.sin(2 * p)
                    + 0.610 * 3.0 * Math.cos(3 * p)) - 1.211 * 3.0 * Math.sin(3 * p)
                    - 0.341 * 4.0 * Math.cos(4 * p)) + 0.190 * 4.0 * Math.sin(4 * p)
                    + 0.128 * 5.0 * Math.cos(5 * p) + 0.034 * 5.0 * Math.sin(5 * p))
                    - 0.038 * 6.0 * Math.cos(6 * p) - 0.031 * 6.0 * Math.sin(6 * p))
                    + (s1 - p1) * (0.020 * Math.cos(s - p) + 0.010 * Math.sin(s - p))),
            lat = Math.toRadians(p1 * (((((-5.453 * Math.cos(p) + 14.975 * Math.sin(p)
                    + 3.527 * 2.0 * Math.cos(2 * p)) - 1.673 * 2.0 * Math.sin(2 * p)
                    - 1.051 * 3.0 * Math.cos(3 * p) - 0.328 * 3.0 * Math.sin(3 * p))
                    + 0.179 * 4.0 * Math.cos(4 * p) + 0.292 * 4.0 * Math.sin(4 * p)
                    + 0.019 * 5.0 * Math.cos(5 * p)) - 0.100 * 5.0 * Math.sin(5 * p)
                    - 0.031 * 6.0 * Math.cos(6 * p)) + 0.026 * 6.0 * Math.sin(6 * p)) - (s1 - p1) * 0.011 * Math.sin(s - p)),
            dst = p1 * ((+6.68 * Math.cos(p) - 6.90 * Math.sin(p)
                    - 1.18 * 2.0 * Math.cos(2 * p)) + 0.03 * 2.0 * Math.sin(2 * p)
                    + 0.15 * 3.0 * Math.cos(3 * p) + 0.14 * 3.0 * Math.sin(3 * p))
    )

    companion object {
        private val S0 = Math.toRadians(50.03)
        private val P0 = Math.toRadians(238.95)
    }
}
