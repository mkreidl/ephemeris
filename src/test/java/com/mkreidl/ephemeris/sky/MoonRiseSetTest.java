package com.mkreidl.ephemeris.sky;

import java.util.Arrays;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import com.mkreidl.ephemeris.geometry.Spherical;
import com.mkreidl.ephemeris.sky.RiseSetCalculator.EventType;
import com.mkreidl.ephemeris.sky.SolarSystem.Body;

@RunWith(Parameterized.class)
public class MoonRiseSetTest extends RiseSetTest {

  public static final Object[][] EVENTS = {
      // https://www.timeanddate.com/sun/germany/munich
      { MUNICH, "2018-05-02 00:00 +0200", -1, RiseSetCalculator.EventType.SET, "2018-05-1 07:05 +0200" },
      { MUNICH, "2018-05-02 00:00 +0200", -1, RiseSetCalculator.EventType.RISE, "2018-05-1 21:56 +0200" },
      { MUNICH, "2018-05-01 00:00 +0200", +1, RiseSetCalculator.EventType.SET, "2018-05-1 07:05 +0200" },
      { MUNICH, "2018-05-01 00:00 +0200", +1, RiseSetCalculator.EventType.RISE, "2018-05-1 21:56 +0200" },

      { MUNICH, "2018-05-04 00:00 +0200", -1, RiseSetCalculator.EventType.RISE, "2018-05-03 23:55 +0200" },
      { MUNICH, "2018-05-04 00:00 +0200", -1, RiseSetCalculator.EventType.SET, "2018-05-03 08:15 +0200" },
      { MUNICH, "2018-05-04 00:00 +0200", +1, RiseSetCalculator.EventType.SET, "2018-05-04 08:57 +0200" },
      { MUNICH, "2018-05-04 00:00 +0200", +1, RiseSetCalculator.EventType.RISE, "2018-05-05 00:46 +0200" },

      { MUNICH, "2018-05-18 00:00 +0200", -1, RiseSetCalculator.EventType.RISE, "2018-05-17 07:21 +0200" },
      { MUNICH, "2018-05-18 00:00 +0200", -1, RiseSetCalculator.EventType.SET, "2018-05-17 23:06 +0200" },
      { MUNICH, "2018-05-18 00:00 +0200", +1, RiseSetCalculator.EventType.RISE, "2018-05-18 08:14 +0200" },
      { MUNICH, "2018-05-18 00:00 +0200", +1, RiseSetCalculator.EventType.SET, "2018-05-19 00:09 +0200" },
  };

  @Parameters(name = "{0} {1} {2} {3} {4}")
  public static Iterable<Object[]> data() {
    return Arrays.asList(EVENTS);
  }

  @Override
  protected RiseSetCalculator getCalculator() {
    return PlanetRiseSetCalculator.of(new SolarSystem(), Body.MOON);
  }

  public MoonRiseSetTest(Spherical geographicLocation, String startTime, long searchDirection, EventType eventType, String eventTime) {
    super(geographicLocation, startTime, searchDirection, eventType, eventTime);
  }
}