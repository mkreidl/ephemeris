package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Instant

class SunLowPrecision(instant: Instant, ecliptic: Ecliptic = Ecliptic(instant)) : Sun(instant, ecliptic) {

    val meanAnomaly by lazy { standardize(M(julianCenturies)) }
    val excentricity by lazy { E(julianCenturies) }
    val equationOfCenter by lazy { standardize(computeEquationOfCenter()) }
    val trueAnomaly by lazy { standardize(meanAnomaly + equationOfCenter) }
    val omega = standardize(Math.toRadians(125.04 - 1_934.136 * julianCenturies))

    override val geometricLongitude by lazy { standardize(meanLongitude + equationOfCenter) }
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
}
