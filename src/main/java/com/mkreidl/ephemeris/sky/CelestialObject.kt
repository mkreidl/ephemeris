package com.mkreidl.ephemeris.sky

import com.mkreidl.ephemeris.solarsystem.Body
import com.mkreidl.ephemeris.stars.BrightStarCatalog
import com.mkreidl.ephemeris.stars.Constellation
import com.mkreidl.ephemeris.stars.ConstellationsCatalog

data class CelestialObject(
        private val index: Int = -1,
        private val planet: Body? = null,
        private val constellation: Constellation? = null,
        val scientificName: String = "",
        val trivialName: String = "",
        val catalogName: String = ""
) {
    val isVoid get() = index == -1 && planet == null && constellation == null
    val isStar get() = index >= 0
    val isPlanet get() = planet != null
    val isPoint get() = isStar || isPlanet
    val isConstellation get() = constellation != null

    val starName
        get() = when {
            !trivialName.isEmpty() -> trivialName
            !scientificName.isEmpty() -> scientificName
            else -> catalogName
        }

    fun asStar() = index
    fun asPlanet() = planet!!
    fun asConstellation() = constellation!!

    companion object {
        val NONE = CelestialObject()

        fun of(name: String) = try {
            CelestialObject.of(Body.valueOf(name))
        } catch (unused: IllegalArgumentException) {
            val constellation = ConstellationsCatalog.findByName(name)
            if (constellation != null)
                CelestialObject.of(constellation)
            else
                CelestialObject.of(BrightStarCatalog.findIndexByName(name))
        }

        fun of(index: Int) = if (index > -1 && index < BrightStarCatalog.size) {
            CelestialObject(
                    index = index,
                    scientificName = BrightStarCatalog.FLAMSTEED_BAYER[index] ?: "",
                    trivialName = BrightStarCatalog.IAU_NAME[index] ?: "",
                    catalogName = "HR " + BrightStarCatalog.BRIGHT_STAR_NUMBER[index]
            )
        } else
            NONE

        fun of(planet: Body) = CelestialObject(
                planet = planet,
                scientificName = planet.toString(),
                trivialName = planet.toString(),
                catalogName = planet.toString())

        fun of(constellation: Constellation) = CelestialObject(
                constellation = constellation,
                scientificName = constellation.name,
                trivialName = constellation.name,
                catalogName = constellation.name)
    }
}
