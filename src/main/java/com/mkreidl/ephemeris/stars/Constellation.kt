package com.mkreidl.ephemeris.stars

import com.mkreidl.math.Vector3

class Constellation internal constructor(val name: String, vararg paths: IntArray) : Iterable<Int> {

    enum class Hemisphere {
        NORTHERN, SOUTHERN, ZODIAC
    }

    val paths = paths.toList()
    val starList = paths
            .flatMap { it.toList() }
            .distinct()
            .sorted()

    val brightestStar = starList[0]
    val hemisphere = when {
        all { stars.posEclJ2000(it).z > 0 } -> Hemisphere.NORTHERN
        all { stars.posEclJ2000(it).z < 0 } -> Hemisphere.SOUTHERN
        else -> Hemisphere.ZODIAC
    }

    override fun iterator() = starList.iterator()

    fun computeCenter(starsCoordinates: DoubleArray): Vector3 {
        val x = map { starsCoordinates[it * 3] }.sum()
        val y = map { starsCoordinates[it * 3 + 1] }.sum()
        val z = map { starsCoordinates[it * 3 + 2] }.sum()
        return Vector3(x, y, z).normalize()
    }

    companion object {
        val stars = Stars(BrightStarCatalog.INSTANCE)
    }
}