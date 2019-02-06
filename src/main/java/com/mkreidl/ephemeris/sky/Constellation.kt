package com.mkreidl.ephemeris.sky

import java.util.ArrayList
import java.util.Collections

class Constellation internal constructor(val name: String, vararg paths: IntArray) {
    private val paths = ArrayList<IntArray>()
    internal val starList = ArrayList<Int>()

    val hemisphere: Hemisphere?

    val brightestStar: Int
        get() = starList[0]

    enum class Hemisphere {
        NORTHERN, SOUTHERN, ZODIAC
    }

    fun getPaths(): List<IntArray> {
        return paths
    }

    internal fun getStarList(): List<Int> {
        return Collections.unmodifiableList(starList)
    }

    init {
        for (path in paths) {
            this.paths.add(path)
            for (star in path)
                if (!starList.contains(star))
                    starList.add(star)
        }
        Collections.sort(starList)
        hemisphere = computeHemisphere()
    }

    private fun computeHemisphere(): Hemisphere? {
        var hasStarNorthern = false
        var hasStarSouthern = false
        for (star in starList) {
            hasStarNorthern = hasStarNorthern or (Stars.POS_J2000[3 * star + 2] > 0)
            hasStarSouthern = hasStarSouthern or (Stars.POS_J2000[3 * star + 2] < 0)
        }
        return if (hasStarNorthern && hasStarSouthern)
            Hemisphere.ZODIAC
        else if (hasStarNorthern)
            Hemisphere.NORTHERN
        else if (hasStarSouthern)
            Hemisphere.SOUTHERN
        else
            null
    }
}
