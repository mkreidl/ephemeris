package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.Instant;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;
import com.mkreidl.math.Vector3;

public class StarRiseSetCalculator extends RiseSetCalculator {
    private final Equatorial.Cart cartesian = new Equatorial.Cart();
    private final int starIndex;

    public static StarRiseSetCalculator of(int starIndex) {
        return new StarRiseSetCalculator(starIndex);
    }

    public static StarRiseSetCalculator of(String starName) {
        final int starIndex = StarsCatalog.findIndexByName(starName);
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
        final Vector3 position = Stars.INSTANCE.computeEquatorial(starIndex, Instant.ofEpochMilli(startTimeMs));
        cartesian.set(position.getX(), position.getY(), position.getZ());
        cartesian.transform(topocentric);
        final boolean isCrossing = isCrossing();
        if (isCrossing)
            adjustTime();
        return isCrossing;
    }

}
