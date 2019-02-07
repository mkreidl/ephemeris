package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Instant
import com.mkreidl.ephemeris.Time
import com.mkreidl.ephemeris.solarsystem.meeus.EarthMeeus

class SunHighPrecision(private val instant: Instant, private val ecliptic: Ecliptic = Ecliptic(instant)) {

    val earth = EarthMeeus()

    fun computeTrueLongitude(): Double {
        val time = Time(instant.epochMilli)
        val (pos, _) = earth.computeSpherical(time)
        return Math.PI + pos.lon
    }
}