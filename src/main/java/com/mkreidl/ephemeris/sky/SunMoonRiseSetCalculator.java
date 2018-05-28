package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.solarsystem.Body;
import com.mkreidl.ephemeris.solarsystem.SolarSystem;

public class SunMoonRiseSetCalculator extends PlanetRiseSetCalculator {

  public static SunMoonRiseSetCalculator of(SolarSystem solarSystem, Body body) {
    return new SunMoonRiseSetCalculator(solarSystem, body);
  }

  private SunMoonRiseSetCalculator(SolarSystem solarSystem, Body body) {
    super(solarSystem, body);
  }

  @Override
  boolean adjustTime() {
    updateHorizon();
    return super.adjustTime();
  }
}
