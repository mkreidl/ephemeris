package com.mkreidl.ephemeris.solarsystem

import java.util.*

enum class Body(
        val MASS: Double,
        val RADIUS_MEAN_M: Double,
        val RADIUS_EQUATORIAL_M: Double
) {
    SUN(0.0, 6.96342e8, 6.96342e8),
    MOON(7.342e22, 1.7371e6, 1.7381e6),
    MERCURY(0.0, 0.0, 2.439764e6),
    VENUS(0.0, 0.0, 6.05159e6),
    EARTH(5.9726e24, 6.371e6, 6.37815e6),
    MARS(0.0, 0.0, 3.397e6),
    JUPITER(0.0, 0.0, 7.149268e7),
    SATURN(0.0, 0.0, 6.026714e7),
    URANUS(0.0, 0.0, 2.5559e7),
    NEPTUNE(0.0, 0.0, 2.4764e7),
    PLUTO(1.303e22, 2.374e6 / 2, 2.374e6 / 2);

    companion object {
        val EXTRA_TERRESTRIAL = Arrays.asList(
                SUN, MOON, MERCURY, VENUS, MARS, JUPITER, SATURN, URANUS, NEPTUNE, PLUTO
        )
    }
}
