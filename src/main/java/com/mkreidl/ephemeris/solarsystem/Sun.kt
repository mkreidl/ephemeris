package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Time
import com.mkreidl.ephemeris.geometry.Angle
import com.mkreidl.math.Polynomial

class Sun(val time: Time) {

    private val ecliptic = Ecliptic(time)

    private val julianCenturies = time.julianCenturiesSince(Time.J2000)
    private val julianMillennia = time.julianMillenniaSince(Time.J2000)

    val equationOfTime by lazy { computeEquationOfTime() }

    private val meanLongitude by lazy { L0[julianMillennia] }
    private val meanAnomaly by lazy { M[julianCenturies] }
    private val apparentRightAscension by lazy { computeApparentRightAscension() }
    private val equationOfCenter by lazy { computeEquationOfCenter() }

    private fun computeEquationOfTime() = Angle.standardize(
            meanLongitude - aberrationCorrection - apparentRightAscension + ecliptic.nutationInRightAscension
    )

    private fun computeEquationOfCenter() = Math.toRadians(
            (1.914_600 - (0.004_817 - 0.000_014 * julianCenturies) * julianCenturies) * Math.sin(meanAnomaly)
                    + (0.019_993 - 0.000_101 * julianCenturies) * Math.sin(2 * meanAnomaly)
                    + 0.000_29 * Math.sin(3 * meanAnomaly)
    )

    private fun computeApparentRightAscension(): Double {
        val theta = meanLongitude + equationOfCenter
        val omega = Math.toRadians(125.04 - 1_934.136 * julianCenturies)
        val lambda = theta - Math.toRadians(0.005_69 + 0.004_78 * Math.sin(omega))
        val epsilon = ecliptic.trueObliquity + Math.toRadians(0.002_56 * Math.cos(omega))
        return Math.atan2(Math.cos(epsilon) * Math.sin(lambda), Math.cos(lambda))
    }

    companion object {
        private val aberrationCorrection = Math.toRadians(0.005_718_3)

        private val L0 = Polynomial(280.466_456_7, 360_000.769_827_79, 0.030_320_28, 1.0 / 49_931, -1.0 / 15_299, -1.0 / 198_800) * Math.toRadians(1.0)
        private val M = Polynomial(357.529_1, 35_999.050_3, -0.000_155_9, -0.000_000_48) * Math.toRadians(1.0)
    }
}
