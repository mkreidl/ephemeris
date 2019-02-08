package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Instant
import com.mkreidl.ephemeris.getMeanSolarTimeRadians
import com.mkreidl.math.Polynomial

abstract class Sun(internal val instant: Instant, internal val ecliptic: Ecliptic = Ecliptic(instant)) {

    protected val julianCenturies = instant.julianCenturiesSinceJ2000
    private val julianMillennia = instant.julianMillenniaSinceJ2000

    val meanLongitude by lazy { standardize(L0(julianMillennia)) }
    val equationOfTime by lazy { computeEquationOfTime() }
    val trueSolarTime by lazy { standardize(instant.getMeanSolarTimeRadians() + equationOfTime) }

    abstract val geometricLongitude: Double
    abstract val apparentLongitude: Double
    abstract val apparentRightAscension: Double

    private fun computeEquationOfTime() =
            standardize(meanLongitude + aberrationFk5Correction - apparentRightAscension + ecliptic.nutationInRightAscension)

    companion object {
        private val aberrationFk5Correction = -Math.toRadians(0.005_718_3)

        private val L0 = Polynomial(
                280.466_456_7,
                360_007.698_277_9,
                0.030_320_28,
                1.0 / 49_931,
                -1.0 / 15_299,
                -1.0 / 198_800
        ) * Math.toRadians(1.0)

        internal val M = Polynomial(
                357.529_1,
                35_999.050_3,
                -0.000_155_9,
                -0.000_000_48
        ) * Math.toRadians(1.0)

        internal val E = Polynomial(0.016_708_617, -0.000_042_037, -0.000_000_123_6)

        internal fun standardize(radians: Double): Double {
            val reduced = radians % (2 * Math.PI)
            return when {
                reduced <= -Math.PI -> reduced + 2 * Math.PI
                reduced > Math.PI -> reduced - 2 * Math.PI
                else -> reduced
            }
        }
    }
}