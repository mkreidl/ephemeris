package com.mkreidl.ephemeris.sky

import com.mkreidl.ephemeris.stars.BrightStarCatalog
import com.mkreidl.ephemeris.stars.Stars

class StarRiseSetCalculator(private val starIndex: Int, mode: EventType, lookupDirection: LookupDirection)
    : RiseSetCalculator(mode, lookupDirection) {

    constructor(starName: String, mode: EventType, lookupDirection: LookupDirection)
            : this(BrightStarCatalog.findIndexByName(starName), mode, lookupDirection)

    private val stars = Stars(BrightStarCatalog)

    override fun compute(): Boolean {
        topocentric = stars.computeTrueEquatorial(starIndex, time)
        return if (eventHappensToday()) {
            adjustTime()
            true
        } else
            false
    }
}
