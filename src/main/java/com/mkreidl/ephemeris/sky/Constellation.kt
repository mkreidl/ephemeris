package com.mkreidl.ephemeris.sky

class Constellation internal constructor(val name: String, vararg paths: IntArray) {

    val paths = paths.toList()
    val starList = paths.flatMap { it.toList() }.distinct().sorted()
    val brightestStar = starList[0]

    val hemisphere = computeHemisphere()

    enum class Hemisphere {
        NORTHERN, SOUTHERN, ZODIAC
    }

    private fun computeHemisphere(): Hemisphere? {
        val hasStarNorthern = starList.any { Stars.POS_J2000[3 * it + 2] > 0 }
        val hasStarSouthern = starList.any { Stars.POS_J2000[3 * it + 2] < 0 }
        return when {
            hasStarNorthern && hasStarSouthern -> Hemisphere.ZODIAC
            hasStarNorthern -> Hemisphere.NORTHERN
            hasStarSouthern -> Hemisphere.SOUTHERN
            else -> null
        }
    }
}
