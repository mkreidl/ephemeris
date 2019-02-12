package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Angle
import com.mkreidl.ephemeris.Instant
import com.mkreidl.math.Polynomial

class SunLowPrecision(instant: Instant, ecliptic: Ecliptic = Ecliptic(instant)) : Sun(instant, ecliptic) {

    val meanAnomaly by lazy { Angle.reduce(M(julianCenturies)) }
    val equationOfCenter by lazy { Angle.reduce(computeEquationOfCenter()) }
    val trueAnomaly by lazy { Angle.reduce(meanAnomaly + equationOfCenter) }
    val omega = Angle.reduce(Math.toRadians(125.04 - 1_934.136 * julianCenturies))

    override val geometricLongitude by lazy { Angle.reduce(meanLongitude + equationOfCenter) }
    override val apparentLongitude by lazy { geometricLongitude - Math.toRadians(0.005_69 + 0.004_78 * Math.sin(omega)) }
    override val apparentRightAscension by lazy { computeApparentRightAscension() }

    private fun computeApparentRightAscension(): Double {
        val epsilon = ecliptic.trueObliquity + Math.toRadians(0.002_56 * Math.cos(omega))
        return Math.atan2(Math.cos(epsilon) * Math.sin(apparentLongitude), Math.cos(apparentLongitude))
    }

    private fun computeEquationOfCenter() = Math.toRadians(
            (1.914_600 - (0.004_817 - 0.000_014 * julianCenturies) * julianCenturies) * Math.sin(meanAnomaly)
                    + (0.019_993 - 0.000_101 * julianCenturies) * Math.sin(2 * meanAnomaly)
                    + 0.000_29 * Math.sin(3 * meanAnomaly)
    )

    companion object {
        private val M = Polynomial(
                357.529_1,
                35_999.050_3,
                -0.000_155_9,
                -0.000_000_48
        ) * Math.toRadians(1.0)
    }
}
