package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Time

abstract class ModelVsop87(private val coefficients: Array<Array<Array<DoubleArray>>>) : OrbitalModel() {

    protected var results = DoubleArray(2 * DIMENSION)
    private var timeCached: Long = 0

    fun compute(time: Time) {
        if (time.time != timeCached) {
            val t = time.julianDayNumberSince(Time.J2000) / Time.DAYS_PER_MILLENNIUM
            for (dim in 0 until DIMENSION) {
                var pos = 0.0
                var vel = 0.0
                for (n in coefficients[dim].size - 1 downTo 1) {
                    val c = cosSeries(dim, n, t)
                    val s = sinSeries(dim, n - 1, t)
                    pos = pos * t + c
                    vel = vel * t + s + n * c
                }
                results[dim] = pos * t + cosSeries(dim, 0, t)
                results[dim + DIMENSION] = vel / (Time.DAYS_PER_MILLENNIUM * Time.SECONDS_PER_DAY)
            }
            // remember time for which last calculation was performed
            timeCached = time.time
        }
    }

    private fun cosSeries(dim: Int, n: Int, time: Double) = coefficients[dim][n].map { it[0] * Math.cos(it[1] + it[2] * time) }.sum()

    private fun sinSeries(dim: Int, n: Int, time: Double) = coefficients[dim][n].map { -it[0] * it[2] * Math.sin(it[1] + it[2] * time) }.sum()

    class XYZ(coefficients: Array<Array<Array<DoubleArray>>>) : ModelVsop87(coefficients) {

        override fun computeSpherical(time: Time) = computeCartesian(time).toSpherical()

        override fun computeCartesian(time: Time): PhaseCartesian {
            compute(time)
            return PhaseCartesian(
                    Cart(results[0], results[1], results[2]),
                    Cart(results[3], results[4], results[5])
            )
        }
    }

    class LBR(coefficients: Array<Array<Array<DoubleArray>>>) : ModelVsop87(coefficients) {

        override fun computeSpherical(time: Time): PhaseSpherical {
            compute(time)
            return PhaseSpherical(
                    Sphe(results[2], results[0], results[1]),
                    Sphe(results[5], results[3], results[4])
            )
        }

        override fun computeCartesian(time: Time) = computeSpherical(time).toCartesian()
    }

    companion object {
        protected const val DIMENSION = 3

        const val MERCURY = "MERCURY"
        const val VENUS = "VENUS"
        const val EARTH = "EARTH"
        const val MARS = "MARS"
        const val JUPITER = "JUPITER"
        const val SATURN = "SATURN"
        const val URANUS = "URANUS"
        const val NEPTUNE = "NEPTUNE"
    }
}
