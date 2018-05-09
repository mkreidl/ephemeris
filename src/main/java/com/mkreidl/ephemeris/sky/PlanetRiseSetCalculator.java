package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.Distance;

public class PlanetRiseSetCalculator extends RiseSetCalculator {

  private final SolarSystem solarSystem;
  private final SolarSystem.Body body;
  private final Position position = new Position();

  public static RiseSetCalculator of(SolarSystem solarSystem, SolarSystem.Body body) {
    return new PlanetRiseSetCalculator(solarSystem, body);
  }

  private PlanetRiseSetCalculator(SolarSystem solarSystem, SolarSystem.Body body) {
    this.solarSystem = solarSystem;
    this.body = body;
  }

  @Override
  protected void computeTopocentricPosition() {
    solarSystem.compute(time, SolarSystem.Body.EARTH);
    solarSystem.compute(time, body);
    solarSystem.getEphemerides(body, position);
    position.setTimeLocation(time, geographicLocation);
    position.get(topocentric, Position.CoordinatesCenter.TOPOCENTRIC);
  }

  @Override
  protected double shiftHorizonDeg() {
    switch (body) {
      case SUN:
      case MOON:
        final double apparentRadius = body.RADIUS_MEAN_M / topocentric.distance(Distance.m);
        return -OPTICAL_HORIZON_DEG - Math.toDegrees(apparentRadius);
      default:
        return -OPTICAL_HORIZON_DEG;
    }
  }

  @Override
  public long compute(long startTimeMs, long precisionMs) {
    return computeIterative(startTimeMs, precisionMs);
  }
}