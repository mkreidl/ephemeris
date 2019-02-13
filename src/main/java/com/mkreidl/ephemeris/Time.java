package com.mkreidl.ephemeris;

import com.mkreidl.ephemeris.geometry.Angle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Time {
    public static final Time J2000 = new Time(946_728_000_000L);
    public static final double STD_EPOCH_DAY_NUMBER = 2451545.0;
    public static final double DAYS_PER_YEAR = 365.25;

    public static final double TDT_OFFSET = 1.5; // Terrestrial dynamical Time

    public static final long MILLIS_PER_HOUR = 3_600_000;
    public static final long MILLIS_PER_DAY = MILLIS_PER_HOUR * 24;
    public static final long DAYS_PER_CENTURY = 36_525;
    public static final long DAYS_PER_MILLENNIUM = 365_250;
    public static final long MILLIS_PER_CENTURY = MILLIS_PER_DAY * DAYS_PER_CENTURY;
    public static final long MILLIS_PER_MILLENNIUM = MILLIS_PER_DAY * DAYS_PER_MILLENNIUM;

    public static final double SIDEREAL_PER_SOLAR = 1.00273790935;
    public static final double SOLAR_PER_SIDEREAL = 1.0 / SIDEREAL_PER_SOLAR;
    public static final long MILLIS_PER_SIDEREAL_DAY = (long) (MILLIS_PER_DAY * SOLAR_PER_SIDEREAL);

    private static final double[] GMST_COEFF_DAYS =
            {
                    1.0 / 38710000.0 * 1 / 360, 0.000387933 * 1 / 360,
                    36000.770053608 * 1 / 360, 100.46061837 * 1 / 360
            };

    private static final TimeZone UTC = TimeZone.getTimeZone("UTC");
    public static final long SECONDS_PER_DAY = 24 * 3600;
    private long millisSinceEpoch;

    @Override
    public String toString() {
        final DateFormat dateFormat = new SimpleDateFormat();
        dateFormat.setTimeZone(UTC);
        return dateFormat.format(new Date(millisSinceEpoch));
    }

    public Time() {
        millisSinceEpoch = System.currentTimeMillis();
    }

    public Time(long millisSinceEpoch) {
        this.millisSinceEpoch = millisSinceEpoch;
    }

    public long getTime() {
        return millisSinceEpoch;
    }

    public void setTime(long millisSinceEpoch) {
        this.millisSinceEpoch = millisSinceEpoch;
    }

    public double julianDayNumberSince(Time instant) {
        return (double) (millisSinceEpoch - instant.millisSinceEpoch) / MILLIS_PER_DAY;
    }
    /**
     * Calculate the Mean Sidereal Time for Greenwich at given date
     *
     * @return Greenwich Mean Sidereal Time in hours [h]
     */
    public double getHourAngleOfVernalEquinox() {
        return getSiderealTimeDayFraction() * 2 * Math.PI;
    }

    /**
     * Calculate the Mean Sidereal Time for Greenwich at given date
     *
     * @return Greenwich Mean Sidereal Time in hours [h]
     */
    public double getMeanSiderealTime() {
        return getSiderealTimeDayFraction() * 24;
    }

    /**
     * Calculate the Mean Sidereal Time for Greenwich at given date
     *
     * @return Greenwich Mean Sidereal Time in radians
     */
    public double getMeanSiderealTimeRadians() {
        return getSiderealTimeDayFraction() * 2 * Math.PI;
    }

    private double getSiderealTimeDayFraction() {
        final double midnightUT = midnightAtGreenwichSameDate();
        final double julianCenturyUT0 = (midnightUT - J2000.millisSinceEpoch) / MILLIS_PER_CENTURY;

        double daysBase = 0.0;
        for (double c : GMST_COEFF_DAYS)
            daysBase = daysBase * julianCenturyUT0 + c;

        final double dayFraction = (millisSinceEpoch - midnightUT) / MILLIS_PER_SIDEREAL_DAY;
        double siderealTime = ((daysBase + dayFraction) % 1);
        if (siderealTime < 0.0)
            siderealTime += 1.0;
        return siderealTime;
    }

    public Angle getMeanSiderealTime(Angle longitude, Angle siderealTime) {
        double hours = getMeanSiderealTime() + longitude.get(Angle.Unit.HOURS);
        return siderealTime.set(standardize24(hours), Angle.Unit.HOURS);
    }

    private static double standardize24(double hours) {
        return (hours %= 24) < 0 ? hours + 24 : hours;
    }

    private double midnightAtGreenwichSameDate() {
        final Calendar calendar = new GregorianCalendar(UTC);
        calendar.setTimeInMillis(millisSinceEpoch);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public void addMillis(long millis) {
        millisSinceEpoch += millis;
    }
}
