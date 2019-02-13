package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.time.Instant;
import com.mkreidl.ephemeris.stars.BrightStarCatalog;
import com.mkreidl.ephemeris.stars.Stars;
import com.mkreidl.math.Spherical3;

public class StarRiseSetCalculator extends RiseSetCalculator {
    private final Stars stars = new Stars(BrightStarCatalog.INSTANCE);
    private final int starIndex;

    public static StarRiseSetCalculator of(int starIndex) {
        return new StarRiseSetCalculator(starIndex);
    }

    public static StarRiseSetCalculator of(String starName) {
        final int starIndex = BrightStarCatalog.findIndexByName(starName);
        if (starIndex >= 0)
            return new StarRiseSetCalculator(starIndex);
        else
            throw new IllegalArgumentException("Unknown star name: " + starName);
    }

    private StarRiseSetCalculator(int starIndex) {
        this.starIndex = starIndex;
    }

    @Override
    public boolean compute(long startTimeMs) {
        super.setStartTime(startTimeMs);
        final Spherical3 position = stars.computeMeanEquatorial(starIndex, Instant.ofEpochMilli(startTimeMs));
        topocentric.set(position.getDst(), position.getLon(), position.getLat());
        final boolean isCrossing = isCrossing();
        if (isCrossing)
            adjustTime();
        return isCrossing;
    }

}
