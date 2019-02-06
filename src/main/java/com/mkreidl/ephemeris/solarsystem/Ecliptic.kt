package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Time
import com.mkreidl.math.Axis
import com.mkreidl.math.Matrix3x3
import com.mkreidl.math.Polynomial

class Ecliptic(private val time: Time) {

    private val julianCenturies = time.julianCenturiesSince(Time.J2000)
    private val julianMillennia = julianCenturies * 0.1

    /**
     * Mean obliquity of the ecliptic.
     *
     * Reference: Astronomical Almanac (1984),
     * https://de.wikipedia.org/wiki/Ekliptik
     */
    val meanObliquity by lazy { MEAN_OBLIQUITY[julianCenturies] }
    val nutation by lazy { computeNutation() }

    val trafoEcl2MeanEqu by lazy { Matrix3x3.rotation(meanObliquity, Axis.X) }
    val trafoMeanEqu2Ecl by lazy { Matrix3x3.rotation(-meanObliquity, Axis.X) }

    val trafoMeanEcl2TrueEcl by lazy { Matrix3x3.rotation(nutationInLongitude, Axis.Z) }
    val trafoMeanEcl2TrueEqu by lazy { Matrix3x3.rotation(trueObliquity, Axis.X) * trafoMeanEcl2TrueEcl }

    val trafoEclJ2000ToEclToDate by lazy { computeTransformJ2000ToDate() }
    val trafoEclJ2000ToMeanEquToDate by lazy { trafoEcl2MeanEqu * trafoEclJ2000ToEclToDate }
    val trafoEclJ2000ToTrueEquToDate by lazy { trafoMeanEcl2TrueEqu * trafoEclJ2000ToEclToDate }

    val trueObliquity get() = meanObliquity + nutationInObliquity
    val trueSiderealTimeRadians get() = time.meanSiderealTimeRadians + nutationInRightAscension
    val nutationInLongitude get() = nutation.first
    val nutationInObliquity get() = nutation.second
    val nutationInRightAscension get() = nutationInLongitude * Math.cos(trueObliquity)

    fun computeTransformJ2000ToDate(): Matrix3x3 {
        val s11 = S11[julianMillennia]
        val c11 = C11[julianMillennia]
        val s12 = S12[julianMillennia]
        val c12 = C12[julianMillennia]
        val s13 = S13[julianMillennia]
        val c13 = C13[julianMillennia]

        val xi = 0.243817483530 * julianMillennia
        val cosXi = Math.cos(xi)
        val sinXi = Math.sin(xi)

        return Matrix3x3(
                s11 * sinXi + c11 * cosXi, s12 * sinXi + c12 * cosXi, s13 * sinXi + c13 * cosXi,
                c11 * sinXi - s11 * cosXi, c12 * sinXi - s12 * cosXi, c13 * sinXi - s13 * cosXi,
                A31[julianMillennia], A32[julianMillennia], A33[julianMillennia]
        )
    }

    private fun computeNutation(): Pair<Double, Double> {
        val d = D[julianCenturies]
        val m = M[julianCenturies]
        val mp = MP[julianCenturies]
        val f = F[julianCenturies]
        val omega = OMEGA[julianCenturies]

        var psi = 0.0
        var eps = 0.0
        for (i in 0 until NUM_OF_COEFF_TO_COMPUTE) {
            val summand = SUMMANDS[i]
            val arg = summand[0] * mp + summand[1] * m + summand[2] * f + summand[3] * d + summand[4] * omega
            psi += (summand[5] + summand[6] * julianCenturies) * Math.sin(arg)
            eps += (summand[7] + summand[8] * julianCenturies) * Math.cos(arg)
        }
        return Pair(psi * TO_RAD, eps * TO_RAD)
    }

    companion object {
        private val MEAN_OBLIQUITY = Polynomial(23.4392911111, -1.30041667e-2, -1.638888e-7, 5.036111e-7) * Math.toRadians(1.0)

        private val S11 = Polynomial(0.0, 0.0, -538867722.0, -270670.0, 1138205.0, 8604.0, -813.0) * 1e-12
        private val C11 = Polynomial(1e12, 0.0, -20728.0, -19147.0, -149390.0, -34.0, 617.0) * 1e-12
        private val S12 = Polynomial(-1e12, 0.0, 2575043.0, -56157.0, 140001.0, 383.0, -613.0) * 1e-12
        private val C12 = Polynomial(0.0, 0.0, -539329768.0, -479046.0, 1144883.0, 8884.0, -830.0) * 1e-12
        private val S13 = Polynomial(0.0, 2269380040.0, -24745348.0, -2422542.0, 78247.0, -468.0, 134.0) * 1e-12
        private val C13 = Polynomial(0.0, -203607820.0, -94040878.0, 2307025.0, 37729.0, -4862.0, 25.0) * 1e-12
        private val A31 = Polynomial(0.0, 203607820.0, 94040878.0, -1083606.0, -50218.0, 929.0, 11.0) * 1e-12
        private val A32 = Polynomial(0.0, 2269380040.0, -24745348.0, -2532307.0, 27473.0, 643.0, -1.0) * 1e-12
        private val A33 = Polynomial(1e12, 0.0, -2595771.0, 37009.0, 1236.0, -13.0, 0.0) * 1e-12

        private const val NUM_OF_COEFF_TO_COMPUTE = 13

        private val TO_RAD = Math.toRadians(1 / 3.6e7)

        private val D = Polynomial(1072260.703692, 1602961601.2090, -6.3706, 0.006593, -0.00003169) * Math.toRadians(1.0 / 3600.0)
        private val M = Polynomial(1287104.793048, 129596581.0481, -0.5532, 0.000136, -0.00001149) * Math.toRadians(1.0 / 3600.0)
        private val MP = Polynomial(485868.249036, 1717915923.2178, 31.8792, 0.051635, -0.00024470) * Math.toRadians(1.0 / 3600.0)
        private val F = Polynomial(335779.526232, 1739527262.8478, -12.7512, -0.001037, 0.00000417) * Math.toRadians(1.0 / 3600.0)
        private val OMEGA = Polynomial(450160.398036, -6962890.5431, 7.4722, 0.007702, -0.00005939) * Math.toRadians(1.0 / 3600.0)

        private val SUMMANDS = arrayOf(
                doubleArrayOf(0.0, 0.0, 0.0, 0.0, 1.0, -171996.0, -174.2, 92025.0, 8.9),
                doubleArrayOf(0.0, 0.0, 2.0, -2.0, 2.0, -13187.0, -1.6, 5736.0, -3.1),
                doubleArrayOf(0.0, 0.0, 2.0, 0.0, 2.0, -2274.0, -0.2, 977.0, -0.5),
                doubleArrayOf(0.0, 0.0, 0.0, 0.0, 2.0, 2062.0, 0.2, -895.0, 0.5),
                doubleArrayOf(0.0, 1.0, 0.0, 0.0, 0.0, 1426.0, -3.4, 54.0, -0.1),
                doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 712.0, 0.1, -7.0, 0.0),
                doubleArrayOf(0.0, 1.0, 2.0, -2.0, 2.0, -517.0, 1.2, 224.0, -0.6),
                doubleArrayOf(0.0, 0.0, 2.0, 0.0, 1.0, -386.0, -0.4, 200.0, 0.0),
                doubleArrayOf(1.0, 0.0, 2.0, 0.0, 2.0, -301.0, 0.0, 129.0, -0.1),
                doubleArrayOf(0.0, -1.0, 2.0, -2.0, 2.0, 217.0, -0.5, -95.0, 0.3),
                doubleArrayOf(1.0, 0.0, 0.0, -2.0, 0.0, -158.0, 0.0, -1.0, 0.0),
                doubleArrayOf(0.0, 0.0, 2.0, -2.0, 1.0, 129.0, 0.1, -70.0, 0.0),
                doubleArrayOf(-1.0, 0.0, 2.0, 0.0, 2.0, 123.0, 0.0, -53.0, 0.0),
                doubleArrayOf(0.0, 0.0, 0.0, 2.0, 0.0, 63.0, 0.0, -2.0, 0.0),
                doubleArrayOf(1.0, 0.0, 0.0, 0.0, 1.0, 63.0, 0.1, -33.0, 0.0),
                doubleArrayOf(-1.0, 0.0, 2.0, 2.0, 2.0, -59.0, 0.0, 26.0, 0.0),
                doubleArrayOf(-1.0, 0.0, 0.0, 0.0, 1.0, -58.0, -0.1, 32.0, 0.0),
                doubleArrayOf(1.0, 0.0, 2.0, 0.0, 1.0, -51.0, 0.0, 27.0, 0.0),
                doubleArrayOf(2.0, 0.0, 0.0, -2.0, 0.0, 48.0, 0.0, 1.0, 0.0),
                doubleArrayOf(-2.0, 0.0, 2.0, 0.0, 1.0, 46.0, 0.0, -24.0, 0.0),
                doubleArrayOf(0.0, 0.0, 2.0, 2.0, 2.0, -38.0, 0.0, 16.0, 0.0),
                doubleArrayOf(2.0, 0.0, 2.0, 0.0, 2.0, -31.0, 0.0, 13.0, 0.0),
                doubleArrayOf(2.0, 0.0, 0.0, 0.0, 0.0, 29.0, 0.0, -1.0, 0.0),
                doubleArrayOf(1.0, 0.0, 2.0, -2.0, 2.0, 29.0, 0.0, -12.0, 0.0),
                doubleArrayOf(0.0, 0.0, 2.0, 0.0, 0.0, 26.0, 0.0, -1.0, 0.0),
                doubleArrayOf(0.0, 0.0, 2.0, -2.0, 0.0, -22.0, 0.0, 0.0, 0.0),
                doubleArrayOf(-1.0, 0.0, 2.0, 0.0, 1.0, 21.0, 0.0, -10.0, 0.0),
                doubleArrayOf(0.0, 2.0, 0.0, 0.0, 0.0, 17.0, -0.1, 0.0, 0.0),
                doubleArrayOf(0.0, 2.0, 2.0, -2.0, 2.0, -16.0, 0.1, 7.0, 0.0),
                doubleArrayOf(-1.0, 0.0, 0.0, 2.0, 1.0, 16.0, 0.0, -8.0, 0.0),
                doubleArrayOf(0.0, 1.0, 0.0, 0.0, 1.0, -15.0, 0.0, 9.0, 0.0),
                doubleArrayOf(1.0, 0.0, 0.0, -2.0, 1.0, -13.0, 0.0, 7.0, 0.0),
                doubleArrayOf(0.0, -1.0, 0.0, 0.0, 1.0, -12.0, 0.0, 6.0, 0.0),
                doubleArrayOf(2.0, 0.0, -2.0, 0.0, 0.0, 11.0, 0.0, 0.0, 0.0),
                doubleArrayOf(-1.0, 0.0, 2.0, 2.0, 1.0, -10.0, 0.0, 5.0, 0.0),
                doubleArrayOf(1.0, 0.0, 2.0, 2.0, 2.0, -8.0, 0.0, 3.0, 0.0),
                doubleArrayOf(1.0, 1.0, 0.0, -2.0, 0.0, -7.0, 0.0, 0.0, 0.0),
                doubleArrayOf(0.0, 1.0, 2.0, 0.0, 2.0, 7.0, 0.0, -3.0, 0.0),
                doubleArrayOf(0.0, -1.0, 2.0, 0.0, 2.0, -7.0, 0.0, 3.0, 0.0),
                doubleArrayOf(0.0, 0.0, 2.0, 2.0, 1.0, -7.0, 0.0, 3.0, 0.0),
                doubleArrayOf(-2.0, 0.0, 0.0, 2.0, 1.0, -6.0, 0.0, 3.0, 0.0),
                doubleArrayOf(1.0, 0.0, 0.0, 2.0, 0.0, 6.0, 0.0, 0.0, 0.0),
                doubleArrayOf(2.0, 0.0, 2.0, -2.0, 2.0, 6.0, 0.0, -3.0, 0.0),
                doubleArrayOf(0.0, 0.0, 0.0, 2.0, 1.0, -6.0, 0.0, 3.0, 0.0),
                doubleArrayOf(1.0, 0.0, 2.0, -2.0, 1.0, 6.0, 0.0, -3.0, 0.0),
                doubleArrayOf(0.0, -1.0, 2.0, -2.0, 1.0, -5.0, 0.0, 3.0, 0.0),
                doubleArrayOf(0.0, 0.0, 0.0, -2.0, 1.0, -5.0, 0.0, 3.0, 0.0),
                doubleArrayOf(1.0, -1.0, 0.0, 0.0, 0.0, 5.0, 0.0, 0.0, 0.0),
                doubleArrayOf(2.0, 0.0, 2.0, 0.0, 1.0, -5.0, 0.0, 3.0, 0.0),
                doubleArrayOf(2.0, 0.0, 0.0, -2.0, 1.0, 4.0, 0.0, -2.0, 0.0),
                doubleArrayOf(0.0, 1.0, 2.0, -2.0, 1.0, 4.0, 0.0, -2.0, 0.0),
                doubleArrayOf(1.0, 0.0, 0.0, -1.0, 0.0, -4.0, 0.0, 0.0, 0.0),
                doubleArrayOf(0.0, 1.0, 0.0, -2.0, 0.0, -4.0, 0.0, 0.0, 0.0),
                doubleArrayOf(1.0, 0.0, -2.0, 0.0, 0.0, 4.0, 0.0, 0.0, 0.0),
                doubleArrayOf(0.0, 0.0, 0.0, 1.0, 0.0, -4.0, 0.0, 0.0, 0.0),
                doubleArrayOf(-2.0, 0.0, 2.0, 0.0, 2.0, -3.0, 0.0, 1.0, 0.0),
                doubleArrayOf(1.0, -1.0, 0.0, -1.0, 0.0, -3.0, 0.0, 0.0, 0.0),
                doubleArrayOf(1.0, 1.0, 0.0, 0.0, 0.0, -3.0, 0.0, 0.0, 0.0),
                doubleArrayOf(1.0, 0.0, 2.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0),
                doubleArrayOf(1.0, -1.0, 2.0, 0.0, 2.0, -3.0, 0.0, 1.0, 0.0),
                doubleArrayOf(-1.0, -1.0, 2.0, 2.0, 2.0, -3.0, 0.0, 1.0, 0.0),
                doubleArrayOf(3.0, 0.0, 2.0, 0.0, 2.0, -3.0, 0.0, 1.0, 0.0),
                doubleArrayOf(0.0, -1.0, 2.0, 2.0, 2.0, -3.0, 0.0, 1.0, 0.0),
                doubleArrayOf(0.0, -2.0, 2.0, -2.0, 1.0, -2.0, 0.0, 1.0, 0.0),
                doubleArrayOf(-2.0, 0.0, 0.0, 0.0, 1.0, -2.0, 0.0, 1.0, 0.0),
                doubleArrayOf(1.0, 1.0, 2.0, 0.0, 2.0, 2.0, 0.0, -1.0, 0.0),
                doubleArrayOf(-1.0, 0.0, 2.0, -2.0, 1.0, -2.0, 0.0, 1.0, 0.0),
                doubleArrayOf(2.0, 0.0, 0.0, 0.0, 1.0, 2.0, 0.0, -1.0, 0.0),
                doubleArrayOf(1.0, 0.0, 0.0, 0.0, 2.0, -2.0, 0.0, 1.0, 0.0),
                doubleArrayOf(3.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0),
                doubleArrayOf(0.0, 0.0, 2.0, 1.0, 2.0, 2.0, 0.0, -1.0, 0.0),
                doubleArrayOf(-1.0, 0.0, 2.0, 4.0, 2.0, -2.0, 0.0, 1.0, 0.0),
                doubleArrayOf(2.0, 0.0, -2.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(2.0, 1.0, 0.0, -2.0, 0.0, 1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(0.0, 0.0, -2.0, 2.0, 1.0, 1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(0.0, 1.0, -2.0, 2.0, 0.0, -1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(0.0, 1.0, 0.0, 0.0, 2.0, 1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(-1.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(0.0, 1.0, 2.0, -2.0, 0.0, -1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(-1.0, 0.0, 0.0, 0.0, 2.0, 1.0, 0.0, -1.0, 0.0),
                doubleArrayOf(1.0, 0.0, 0.0, -4.0, 0.0, -1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(-2.0, 0.0, 2.0, 2.0, 2.0, 1.0, 0.0, -1.0, 0.0),
                doubleArrayOf(2.0, 0.0, 0.0, -4.0, 0.0, -1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(1.0, 1.0, 2.0, -2.0, 2.0, 1.0, 0.0, -1.0, 0.0),
                doubleArrayOf(1.0, 0.0, 2.0, 2.0, 1.0, -1.0, 0.0, 1.0, 0.0),
                doubleArrayOf(-2.0, 0.0, 2.0, 4.0, 2.0, -1.0, 0.0, 1.0, 0.0),
                doubleArrayOf(-1.0, 0.0, 4.0, 0.0, 2.0, 1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(1.0, -1.0, 0.0, -2.0, 0.0, 1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(2.0, 0.0, 2.0, -2.0, 1.0, 1.0, 0.0, -1.0, 0.0),
                doubleArrayOf(2.0, 0.0, 2.0, 2.0, 2.0, -1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(1.0, 0.0, 0.0, 2.0, 1.0, -1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(0.0, 0.0, 4.0, -2.0, 2.0, 1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(3.0, 0.0, 2.0, -2.0, 2.0, 1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(1.0, 0.0, 2.0, -2.0, 0.0, -1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(0.0, 1.0, 2.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(-1.0, -1.0, 0.0, 2.0, 1.0, 1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(0.0, 0.0, -2.0, 0.0, 1.0, -1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(0.0, 0.0, 2.0, -1.0, 2.0, -1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(0.0, 1.0, 0.0, 2.0, 0.0, -1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(1.0, 0.0, -2.0, -2.0, 0.0, -1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(0.0, -1.0, 2.0, 0.0, 1.0, -1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(1.0, 1.0, 0.0, -2.0, 1.0, -1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(1.0, 0.0, -2.0, 2.0, 0.0, -1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(2.0, 0.0, 0.0, 2.0, 0.0, 1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(0.0, 0.0, 2.0, 4.0, 2.0, -1.0, 0.0, 0.0, 0.0),
                doubleArrayOf(0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0)
        )
    }
}
