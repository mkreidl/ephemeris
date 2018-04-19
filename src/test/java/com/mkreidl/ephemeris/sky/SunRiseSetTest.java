package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Spherical;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;
import org.junit.Test;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SunRiseSetTest {

  private static final double SUN_APPARENT_RADIUS_DEG =
      Math.toDegrees(SolarSystem.Body.SUN.RADIUS_MEAN_M / 1.5e11);

  public static final double MUNICH_LON = 11.5;
  public static final double MUNICH_LAT = 68.3;
  private static final double OPTICAL_HORIZON_DEG = 34.0 / 60;

  private final RiseSetCalculator sunCalculator = new RiseSetCalculator() {

    private final Position position = new Position();
    private final SolarSystem solarSystem = new SolarSystem();

    @Override
    public void computeTopocentricPosition(Time time, Spherical geographicLocation, Equatorial.Cart topocentric) {
      solarSystem.compute(time, SolarSystem.Body.EARTH);
      solarSystem.compute(time, SolarSystem.Body.SUN);
      solarSystem.getEphemerides(SolarSystem.Body.SUN, position);
      position.setTimeLocation(time, geographicLocation);
      position.get(topocentric, Position.CoordinatesCenter.TOPOCENTRIC);
    }
  };

  @Test
  public void testSunset() throws ParseException {
    final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    final long startTimeMs = dateFormat.parse("2018-04-15 15:41:00").getTime();
    sunCalculator.setGeographicLocation(MUNICH_LON, MUNICH_LAT, -OPTICAL_HORIZON_DEG - SUN_APPARENT_RADIUS_DEG);
    final long eventTime = sunCalculator.compute(startTimeMs, 15000);
    System.out.println(dateFormat.format(new Date(eventTime)));
  }

  @Test
  public void testSunrise() throws ParseException {
    final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    final long startTimeMs = dateFormat.parse("2018-12-16 5:41:00").getTime();
    sunCalculator.setGeographicLocation(MUNICH_LON, MUNICH_LAT, -OPTICAL_HORIZON_DEG - SUN_APPARENT_RADIUS_DEG);
    sunCalculator.setSearchDirection(-1);
    sunCalculator.setEventType(RiseSetCalculator.EventType.RISE);
    final long eventTime = sunCalculator.compute(startTimeMs, 15000);
    System.out.println(dateFormat.format(new Date(eventTime)));
  }
}
