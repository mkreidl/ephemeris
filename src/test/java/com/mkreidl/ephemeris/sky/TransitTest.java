package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.solarsystem.SolarSystemMeeus;
import org.junit.Assert;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Test;
import com.mkreidl.ephemeris.sky.RiseSetCalculator.EventType;
import com.mkreidl.ephemeris.sky.RiseSetCalculator.LookupDirection;
import com.mkreidl.ephemeris.solarsystem.Body;

public class TransitTest {

  private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm Z");
  private final DateFormat dateFormatSec = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

  @Test
  public void test() throws ParseException {
    final Date start = dateFormat.parse("2018-05-28 14:42 +0200");
    final Date lastTransit = dateFormatSec.parse("2018-05-28 13:10:57 +0200");
    final Date nextTransit = dateFormatSec.parse("2018-05-29 13:11:04 +0200");

    final PlanetRiseSetCalculator calculator = PlanetRiseSetCalculator.of(new SolarSystemMeeus(), Body.SUN);
    calculator.setEventType(EventType.TRANSIT);
    calculator.setGeographicLocation(PlanetRiseSetTest.MUNICH);

    calculator.setSearchDirection(LookupDirection.BACKWARD);
    Assert.assertTrue(calculator.compute(start.getTime()));
    Assert.assertTrue(
        String.format("Expected: %tT -- Actual: %tT", lastTransit.getTime(), calculator.getTime()),
        Math.abs(lastTransit.getTime() - calculator.getTime()) < 1000);

    calculator.setSearchDirection(LookupDirection.FORWARD);
    Assert.assertTrue(calculator.compute(start.getTime()));
    Assert.assertTrue(
        String.format("Expected: %tT -- Actual: %tT", nextTransit.getTime(), calculator.getTime()),
        Math.abs(nextTransit.getTime() - calculator.getTime()) < 1500);
  }
}
