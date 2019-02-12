package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Angle
import com.mkreidl.ephemeris.Instant
import com.mkreidl.ephemeris.aberration
import com.mkreidl.ephemeris.getMeanSolarTimeRadians
import com.mkreidl.math.Polynomial
import com.mkreidl.math.Sphe

abstract class Sun(internal val instant: Instant, internal val ecliptic: Ecliptic = Ecliptic(instant)) {

    protected val julianCenturies = instant.julianCenturiesSinceJ2000
    private val julianMillennia = instant.julianMillenniaSinceJ2000

    val meanLongitude by lazy { Angle.reduce(L0(julianMillennia)) }
    val excentricity by lazy { E(julianCenturies) }
    val perihelion by lazy { P(julianCenturies) }
    val equationOfTime by lazy { computeEquationOfTime() }
    val trueSolarTime by lazy { Angle.reduce(instant.getMeanSolarTimeRadians() + equationOfTime) }

    fun computeAberrationCorrectionEcliptical(position: Sphe): Sphe {
        val p = perihelion - position.lon
        val g = geometricLongitude - position.lon
        val l = aberration / Math.cos(position.lat) * (excentricity * Math.cos(p) - Math.cos(g))
        val b = aberration * Math.sin(position.lat) * (excentricity * Math.sin(p) - Math.sin(g))
        return position.copy(lon = position.lon + l, lat = position.lat + b)
    }

    abstract val geometricLongitude: Double
    abstract val apparentLongitude: Double
    abstract val apparentRightAscension: Double

    private fun computeEquationOfTime() =
            Angle.reduce(meanLongitude + aberrationFk5Correction - apparentRightAscension + ecliptic.nutationInRightAscension)

    companion object {
        private const val aberrationFk5Correction = -0.005_718_3 * Math.PI / 180

        private val L0 = Polynomial(
                280.466_456_7,
                360_007.698_277_9,
                0.030_320_28,
                1.0 / 49_931,
                -1.0 / 15_299,
                -1.0 / 198_800
        ) * (Math.PI / 180)

        private val P = Polynomial(
                102.937_35,
                1.719_53,
                0.000_46
        ) * (Math.PI / 180)

        private val E = Polynomial(
                0.016_708_617,
                -0.000_042_037,
                -0.000_000_123_6
        )
    }
}