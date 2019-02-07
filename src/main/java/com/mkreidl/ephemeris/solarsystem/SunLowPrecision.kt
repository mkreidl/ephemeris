package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Instant
import com.mkreidl.ephemeris.getMeanSolarTimeRadians
import com.mkreidl.math.Polynomial

class SunLowPrecision(private val instant: Instant, internal val ecliptic: Ecliptic = Ecliptic(instant)) {

    private val julianCenturies = instant.julianCenturiesSinceJ2000
    private val julianMillennia = instant.julianMillenniaSinceJ2000

    val equationOfTime by lazy { computeEquationOfTime() }
    val trueSolarTime by lazy { standardize(instant.getMeanSolarTimeRadians() + equationOfTime) }

    val meanLongitude by lazy { standardize(L0(julianMillennia)) }
    val meanAnomaly by lazy { standardize(M(julianCenturies)) }
    val excentricity by lazy { E(julianCenturies) }

    val equationOfCenter by lazy { standardize(computeEquationOfCenter()) }
    val trueLongitude by lazy { standardize(meanLongitude + equationOfCenter) }
    val trueAnomaly by lazy { standardize(meanAnomaly + equationOfCenter) }

    val omega = standardize(Math.toRadians(125.04 - 1_934.136 * julianCenturies))
    val apparentLongitude by lazy { trueLongitude - Math.toRadians(0.005_69 + 0.004_78 * Math.sin(omega)) }
    val apparentRightAscension by lazy { computeApparentRightAscension() }

    private fun computeEquationOfTime() =
            standardize(meanLongitude - aberrationCorrection - apparentRightAscension + ecliptic.nutationInRightAscension)

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
        private val aberrationCorrection = Math.toRadians(0.005_718_3)

        private val L0 = Polynomial(
                280.466_456_7,
                360_007.698_277_9,
                0.030_320_28,
                1.0 / 49_931,
                -1.0 / 15_299,
                -1.0 / 198_800
        ) * Math.toRadians(1.0)

        private val M = Polynomial(
                357.529_1,
                35_999.050_3,
                -0.000_155_9,
                -0.000_000_48
        ) * Math.toRadians(1.0)

        private val E = Polynomial(0.016_708_617, -0.000_042_037, -0.000_000_123_6)

        private fun standardize(radians: Double): Double {
            val reduced = radians % (2 * Math.PI)
            return when {
                reduced <= -Math.PI -> reduced + 2 * Math.PI
                reduced > Math.PI -> reduced - 2 * Math.PI
                else -> reduced
            }
        }
    }
}
