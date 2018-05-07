package com.mkreidl.ephemeris.sky;

import static org.junit.Assert.assertTrue;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Test;
import com.mkreidl.ephemeris.geometry.Spherical;

public abstract class PlanetRiseSetTest {

  private static final long PRECISION_MS = 15000;

  protected static final Spherical MUNICH = new Spherical(1.0, Math.toRadians(11.576), Math.toRadians(48.137));
  protected static final Spherical SVOLVAER = new Spherical(1.0, Math.toRadians(14.561583), Math.toRadians(68.234764));
  protected static final Spherical SYDNEY = new Spherical(1.0, Math.toRadians(151.2), Math.toRadians(-33.85));
  protected static final Spherical VANCOUVER = new Spherical(1.0, Math.toRadians(-123.12244), Math.toRadians(49.28098));

  protected final RiseSetCalculator.EventType eventType;
  protected final Spherical geographicLocation;
  protected final String eventTime;
  protected final String startTime;
  protected final long searchDirection;

  private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm Z");

  protected abstract RiseSetCalculator getCalculator();

  public PlanetRiseSetTest(Spherical geographicLocation, String startTime, long searchDirection, RiseSetCalculator.EventType eventType, String eventTime) {
    this.geographicLocation = geographicLocation;
    this.startTime = startTime;
    this.eventType = eventType;
    this.eventTime = eventTime;
    this.searchDirection = searchDirection;
  }

  @Test
  public void test() throws ParseException {
    final RiseSetCalculator calculator = getCalculator();
    final long startTimeMs = dateFormat.parse(startTime).getTime();
    calculator.setGeographicLocation(geographicLocation);
    calculator.setEventType(eventType);
    calculator.setSearchDirection(searchDirection);
    final long eventTimeCalculated = calculator.compute(startTimeMs, PRECISION_MS);
    final long eventTimeExpected = dateFormat.parse(eventTime).getTime();
    assertTrue(
        String.format("Expected: %s; Actual: %s", dateFormat.format(new Date(eventTimeExpected)), dateFormat.format(new Date(eventTimeCalculated + 30000))),
        Math.abs(eventTimeCalculated - eventTimeExpected) < 30000);
  }

}