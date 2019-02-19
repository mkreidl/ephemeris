package com.mkreidl.ephemeris.sky

import com.mkreidl.ephemeris.stars.BrightStarCatalog
import com.mkreidl.ephemeris.stars.Stars
import com.mkreidl.ephemeris.time.Instant
import com.mkreidl.math.Spherical3

class StarRiseSetCalculator(
        private val starIndex: Int,
        geographicLocation: Spherical3,
        mode: EventType,
        lookupDirection: LookupDirection
) : RiseSetCalculator(geographicLocation, mode, lookupDirection) {

    constructor(
            starName: String,
            geographicLocation: Spherical3,
            mode: EventType,
            lookupDirection: LookupDirection
    ) : this(BrightStarCatalog.findIndexByName(starName), geographicLocation, mode, lookupDirection)

    private val stars = Stars(BrightStarCatalog.INSTANCE)

    override fun compute(startTimeEpochMilli: Long): Boolean {
        start = Instant.ofEpochMilli(startTimeEpochMilli)
        val meanEquatorial = stars.computeMeanEquatorial(starIndex, topos.instant)
        topocentric = topos.computeTopocentricFromMeanEquatorial(meanEquatorial)
        val isCrossing = isCrossing()
        if (isCrossing)
            adjustTime()
        return isCrossing
    }
}
