package com.mkreidl.ephemeris.sky;

import static org.junit.Assert.assertTrue;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Test;
import com.mkreidl.ephemeris.geometry.Spherical;
import com.mkreidl.ephemeris.sky.RiseSetCalculator.EventType;

public class StarRiseSetTest {

  private static final Spherical MUNICH = new Spherical(1.0, Math.toRadians(11.576), Math.toRadians(48.137));

  @Test(expected = IllegalStateException.class)
  public void testPolarisRise() {
    final StarRiseSetCalculator polaris = StarRiseSetCalculator.of("Polaris");
    polaris.setGeographicLocation(MUNICH);
    polaris.setEventType(EventType.RISE);
    polaris.compute(0, 15000);
  }

  @Test(expected = IllegalStateException.class)
  public void testPolarisSet() {
    final StarRiseSetCalculator polaris = StarRiseSetCalculator.of("Polaris");
    polaris.setGeographicLocation(MUNICH);
    polaris.setEventType(EventType.SET);
    polaris.compute(0, 15000);
  }

  @Test(expected = IllegalStateException.class)
  public void testPolarisRiseIterative() {
    final StarRiseSetCalculator polaris = StarRiseSetCalculator.of("Polaris");
    polaris.setGeographicLocation(MUNICH);
    polaris.setEventType(EventType.RISE);
    polaris.computeIterative(0, 15000);
  }

  @Test(expected = IllegalStateException.class)
  public void testPolarisSetIterative() {
    final StarRiseSetCalculator polaris = StarRiseSetCalculator.of("Polaris");
    polaris.setGeographicLocation(MUNICH);
    polaris.setEventType(EventType.SET);
    polaris.computeIterative(0, 15000);
  }

  private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm Z");

  @Test
  public void testSiriusSet() throws ParseException {
    final long startTime = dateFormat.parse("2018-05-07 12:00 +0200").getTime();
    final long eventTimeExpected = dateFormat.parse("2018-05-07 21:43 +0200").getTime();
    final StarRiseSetCalculator sirius = StarRiseSetCalculator.of("Sirius");
    sirius.setGeographicLocation(MUNICH);
    sirius.setEventType(EventType.SET);
    sirius.setSearchDirection(+1);
    final long eventTimeCalculated = sirius.compute(startTime, 15000);
    assertTrue(
        String.format("Expected: %s; Actual: %s", dateFormat.format(new Date(eventTimeExpected)), dateFormat.format(new Date(eventTimeCalculated + 30000))),
        Math.abs(eventTimeCalculated - eventTimeExpected) < 30000);
  }

  @Test
  public void testSiriusRise() throws ParseException {
    final long startTime = dateFormat.parse("2018-05-08 00:00 +0200").getTime();
    final long eventTimeExpected = dateFormat.parse("2018-05-08 12:10 +0200").getTime();
    final StarRiseSetCalculator sirius = StarRiseSetCalculator.of("Sirius");
    sirius.setGeographicLocation(MUNICH);
    sirius.setEventType(EventType.RISE);
    sirius.setSearchDirection(+1);
    final long eventTimeCalculated = sirius.compute(startTime, 15000);
    assertTrue(
        String.format("Expected: %s; Actual: %s", dateFormat.format(new Date(eventTimeExpected)), dateFormat.format(new Date(eventTimeCalculated + 30000))),
        Math.abs(eventTimeCalculated - eventTimeExpected) < 30000);
  }

}
