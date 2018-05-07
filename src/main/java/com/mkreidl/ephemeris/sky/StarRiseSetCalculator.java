package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.sky.Stars;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;

public class StarRiseSetCalculator extends RiseSetCalculator {

  private final Stars stars = new Stars();
  private final Equatorial.Cart position = new Equatorial.Cart();
  private int starIndex;

  public StarRiseSetCalculator(int starIndex) {
    this.starIndex = starIndex;
  }

  @Override
  public void computeTopocentricPosition() {
    stars.compute(starIndex, time, position);
  }

  @Override
  public double shiftHorizonDeg() {
    return -OPTICAL_HORIZON_DEG;
  }

}
