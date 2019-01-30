package com.mkreidl.ephemeris.solarsystem;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Angle;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class EclipticTest {

    @Test
    public void testEquationOfTimeMeeusChap28() {
        final Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        calendar.set(1992, 9, 13, 0, 0, 0);
        final Time time = new Time(calendar.getTimeInMillis());
        final double radians = Angle.standardize(new Sun(time).getEquationOfTime());
        Assert.assertEquals(13 + 42.6 / 60, Math.toDegrees(radians) * 4, 0.02);
    }

    @Test
    public void testNutationMeeusChap22() {
        final Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        calendar.set(1987, 3, 10, 0, 0, 0);
        final Time time = new Time(calendar.getTimeInMillis());
        final Ecliptic ecliptic = new Ecliptic(time);
        final double longitude = Angle.standardize(ecliptic.getNutationInLongitude());
        final double obliquity = Angle.standardize(ecliptic.getNutationInObliquity());
        Assert.assertEquals(-3.788, Math.toDegrees(longitude) * 3600, 0.02);
        Assert.assertEquals(9.443, Math.toDegrees(obliquity) * 3600, 0.02);
    }

    @Test
    public void testObliquity() {
        Assert.assertEquals(Math.toRadians(24.0266666667), getObliquity(-3000), 1e-3);
        Assert.assertEquals(Math.toRadians(23.695), getObliquity(0), 1e-3);
        Assert.assertEquals(Math.toRadians(23.44), getObliquity(2000), 1e-3);
        Assert.assertEquals(Math.toRadians(23.43721), getObliquity(2016), 1e-3);
        Assert.assertEquals(Math.toRadians(23.31), getObliquity(3000), 1e-3);
    }

    private double getObliquity(int year) {
        final GregorianCalendar cal = new GregorianCalendar(year, Calendar.JANUARY, 1, 12, 0, 0);
        final Time julianDate = new Time(cal.getTime().getTime());
        return new Ecliptic(julianDate).getObliquity();
    }
}
