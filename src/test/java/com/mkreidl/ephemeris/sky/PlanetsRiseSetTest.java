package com.mkreidl.ephemeris.sky;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Spherical;
import com.mkreidl.ephemeris.sky.Position.CoordinatesCenter;
import com.mkreidl.ephemeris.sky.RiseSetCalculator.EventType;
import com.mkreidl.ephemeris.sky.SolarSystem.Body;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;

@RunWith(Parameterized.class)
public class PlanetsRiseSetTest {

  private static class RiseSetData {

    public final SolarSystem.Body planet;
    public final RiseSetCalculator.EventType event;
    public final Date eventTime;
    public final Date lookupTime;
    public final int searchDirection;

    public RiseSetData(SolarSystem.Body planet, RiseSetCalculator.EventType event, Date eventTime, Date lookupTime, int searchDirection) {
      this.planet = planet;
      this.event = event;
      this.eventTime = eventTime;
      this.lookupTime = lookupTime;
      this.searchDirection = searchDirection;
    }

    @Override
    public String toString() {
      return String.format("[%s, %s, %s, %s, %s]", planet, event, eventTime, lookupTime, searchDirection >= 0 ? "+" : "-");
    }
  }

  private static final SimpleDateFormat DATE_PARSER = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss XXX", Locale.ENGLISH);
  private static final double LON_LAKE_FOREST = -117.6880;
  private static final double LAT_LAKE_FOREST = 33.6470;
  private static final double OPTICAL_HORIZON_DEG = 34.0 / 60;

  @Parameters(name = "{0}")
  public static Iterable<RiseSetData> data() throws ParseException {
    return Arrays.asList(
        new RiseSetData(SolarSystem.Body.MERCURY, EventType.RISE, DATE_PARSER.parse("2018.04.18 05:23:00 -07:00"), DATE_PARSER.parse("2018.04.17 12:00:00 -07:00"), 1),
        new RiseSetData(SolarSystem.Body.MERCURY, EventType.RISE, DATE_PARSER.parse("2018.04.18 05:23:00 -07:00"), DATE_PARSER.parse("2018.04.18 12:00:00 -07:00"), -1));
  }

  private final RiseSetData riseSetData;

  public PlanetsRiseSetTest(RiseSetData riseSetData) throws ParseException {
    this.riseSetData = riseSetData;
  }

  @Test
  public void test() {
    final RiseSetCalculator riseSetCalculator = getRiseSetCalculator(riseSetData.planet);
    riseSetCalculator.setGeographicLocation(LON_LAKE_FOREST, LAT_LAKE_FOREST, -OPTICAL_HORIZON_DEG);
    riseSetCalculator.setEventType(riseSetData.event);
    riseSetCalculator.setSearchDirection(riseSetData.searchDirection);
    final Date eventTime = new Date(riseSetCalculator.compute(riseSetData.lookupTime.getTime(), 15000));
    Assert.assertEquals(
        String.format("Expected: %s Actual: %s", DATE_PARSER.format(riseSetData.eventTime), DATE_PARSER.format(eventTime)),
        riseSetData.eventTime.getTime(), eventTime.getTime(), 30000);
  }

  private RiseSetCalculator getRiseSetCalculator(Body body) {
    return new RiseSetCalculator() {

      private final SolarSystem solarSystem = new SolarSystem();
      private final Position position = new Position();

      @Override
      public void computeTopocentricPosition(Time time, Spherical geographicLocation, Equatorial.Cart cartesian) {
        solarSystem.compute(time, SolarSystem.Body.EARTH);
        solarSystem.compute(time, body);
        solarSystem.getEphemerides(body, position);
        position.setTimeLocation(time, geographicLocation);
        position.get(cartesian, CoordinatesCenter.TOPOCENTRIC);
      }
    };
  }
}
