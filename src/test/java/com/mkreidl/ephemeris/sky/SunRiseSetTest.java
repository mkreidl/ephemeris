package com.mkreidl.ephemeris.sky;

import java.util.Arrays;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import com.mkreidl.ephemeris.geometry.Spherical;
import com.mkreidl.ephemeris.sky.RiseSetCalculator.EventType;
import com.mkreidl.ephemeris.sky.SolarSystem.Body;

@RunWith(Parameterized.class)
public class SunRiseSetTest extends PlanetRiseSetTest {

  public static final Object[][] EVENTS = {
      // https://www.timeanddate.com/sun/germany/munich
      { MUNICH, "2018-05-01 00:00 +0200", +1, RiseSetCalculator.EventType.RISE, "2018-05-01 05:56 +0200" },
      { MUNICH, "2018-05-01 00:00 +0200", +1, RiseSetCalculator.EventType.SET, "2018-05-01 20:27 +0200" },
      { MUNICH, "2018-05-02 00:00 +0200", -1, RiseSetCalculator.EventType.RISE, "2018-05-01 05:56 +0200" },
      { MUNICH, "2018-05-02 00:00 +0200", -1, RiseSetCalculator.EventType.SET, "2018-05-01 20:27 +0200" },

      { MUNICH, "2018-05-15 00:00 +0200", +1, RiseSetCalculator.EventType.RISE, "2018-05-15 05:35 +0200" },
      { MUNICH, "2018-05-15 00:00 +0200", +1, RiseSetCalculator.EventType.SET, "2018-05-15 20:46 +0200" },
      { MUNICH, "2018-05-16 00:00 +0200", -1, RiseSetCalculator.EventType.RISE, "2018-05-15 05:35 +0200" },
      { MUNICH, "2018-05-16 00:00 +0200", -1, RiseSetCalculator.EventType.SET, "2018-05-15 20:46 +0200" },

      { MUNICH, "2018-05-31 00:00 +0200", +1, RiseSetCalculator.EventType.RISE, "2018-05-31 05:19 +0200" },
      { MUNICH, "2018-05-31 00:00 +0200", +1, RiseSetCalculator.EventType.SET, "2018-05-31 21:05 +0200" },
      { MUNICH, "2018-06-01 00:00 +0200", -1, RiseSetCalculator.EventType.RISE, "2018-05-31 05:19 +0200" },
      { MUNICH, "2018-06-01 00:00 +0200", -1, RiseSetCalculator.EventType.SET, "2018-05-31 21:05 +0200" },

      // https://www.timeanddate.com/sun/norway/svolvaer
      { SVOLVAER, "2017-12-01 00:00 +0100", +1, RiseSetCalculator.EventType.RISE, "2017-12-01 10:48 +0100" },
      { SVOLVAER, "2017-12-01 00:00 +0100", +1, RiseSetCalculator.EventType.SET, "2017-12-01 12:53 +0100" },
      { SVOLVAER, "2017-12-02 00:00 +0100", -1, RiseSetCalculator.EventType.RISE, "2017-12-01 10:48 +0100" },
      { SVOLVAER, "2017-12-02 00:00 +0100", -1, RiseSetCalculator.EventType.SET, "2017-12-01 12:53 +0100" },

      { SVOLVAER, "2017-12-07 00:00 +0100", -1, RiseSetCalculator.EventType.RISE, "2017-12-06 11:34 +0100" },
      { SVOLVAER, "2017-12-07 00:00 +0100", -1, RiseSetCalculator.EventType.SET, "2017-12-06 12:11 +0100" },
      { SVOLVAER, "2017-12-07 00:00 +0100", +1, RiseSetCalculator.EventType.RISE, "2018-01-05 11:59 +0100" },
      { SVOLVAER, "2017-12-07 00:00 +0100", +1, RiseSetCalculator.EventType.SET, "2018-01-05 12:16 +0100" },

      { SVOLVAER, "2018-05-01 00:00 +0200", +1, RiseSetCalculator.EventType.RISE, "2018-05-01 03:57 +0200" },
      { SVOLVAER, "2018-05-01 00:00 +0200", +1, RiseSetCalculator.EventType.SET, "2018-05-01 22:04 +0200" },
      { SVOLVAER, "2018-05-02 00:00 +0200", -1, RiseSetCalculator.EventType.RISE, "2018-05-01 03:57 +0200" },
      { SVOLVAER, "2018-05-02 00:00 +0200", -1, RiseSetCalculator.EventType.SET, "2018-05-01 22:04 +0200" },

      { SVOLVAER, "2018-05-25 01:00 +0200", -1, RiseSetCalculator.EventType.RISE, "2018-05-24 01:33 +0200" },
      { SVOLVAER, "2018-05-25 01:00 +0200", -1, RiseSetCalculator.EventType.SET, "2018-05-25 00:43 +0200" },
      { SVOLVAER, "2018-05-25 01:00 +0200", +1, RiseSetCalculator.EventType.RISE, "2018-05-25 01:14 +0200" },
      { SVOLVAER, "2018-05-25 01:00 +0200", +1, RiseSetCalculator.EventType.SET, "2018-07-19 00:53 +0200" },

      { SVOLVAER, "2018-07-19 01:00 +0200", -1, RiseSetCalculator.EventType.RISE, "2018-05-25 01:14 +0200" },
      { SVOLVAER, "2018-07-19 01:00 +0200", -1, RiseSetCalculator.EventType.SET, "2018-07-19 00:53 +0200" },
      { SVOLVAER, "2018-07-19 01:00 +0200", +1, RiseSetCalculator.EventType.RISE, "2018-07-19 01:23 +0200" },
      { SVOLVAER, "2018-07-19 01:00 +0200", +1, RiseSetCalculator.EventType.SET, "2018-07-20 00:34 +0200" },
      
      // https://www.timeanddate.com/sun/canada/vancouver
      { VANCOUVER, "2018-05-01 00:00 -0700", +1, RiseSetCalculator.EventType.RISE, "2018-05-01 05:51 -0700" },
      { VANCOUVER, "2018-05-01 00:00 -0700", +1, RiseSetCalculator.EventType.SET, "2018-05-01 20:29 -0700" },
      { VANCOUVER, "2018-05-02 00:00 -0700", -1, RiseSetCalculator.EventType.RISE, "2018-05-01 05:51 -0700" },
      { VANCOUVER, "2018-05-02 00:00 -0700", -1, RiseSetCalculator.EventType.SET, "2018-05-01 20:29 -0700" },
      
      // https://www.timeanddate.com/sun/australia/sydney
      { SYDNEY, "2018-05-01 00:00 +1000", +1, RiseSetCalculator.EventType.RISE, "2018-05-01 06:29 +1000" },
      { SYDNEY, "2018-05-01 00:00 +1000", +1, RiseSetCalculator.EventType.SET, "2018-05-01 17:15 +1000" },
      { SYDNEY, "2018-05-02 00:00 +1000", -1, RiseSetCalculator.EventType.RISE, "2018-05-01 06:29 +1000" },
      { SYDNEY, "2018-05-02 00:00 +1000", -1, RiseSetCalculator.EventType.SET, "2018-05-01 17:15 +1000" },
  };

  @Parameters(name = "{0} {1} {2} {3} {4}")
  public static Iterable<Object[]> data() {
    return Arrays.asList(EVENTS);
  }

  @Override
  protected RiseSetCalculator getCalculator() {
    return PlanetRiseSetCalculator.of(new SolarSystem(), Body.SUN);
  }

  public SunRiseSetTest(Spherical geographicLocation, String startTime, long searchDirection, EventType eventType, String eventTime) {
    super(geographicLocation, startTime, searchDirection, eventType, eventTime);
  }
}
