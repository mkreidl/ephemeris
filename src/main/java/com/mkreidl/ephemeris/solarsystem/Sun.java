package com.mkreidl.ephemeris.solarsystem;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Angle;

public class Sun {

  private Sun() {
  }

  public static double getEquationOfTime(Time time) {
    final double t = time.julianCenturiesSince(Time.J2000);
    return Angle.standardize(
        getMeanLongitude(t)
            - Math.toRadians(0.005_7183)
            - getApparentRightAscension(t)
            + Nutation.getLongitude(t) * Math.cos(Ecliptic.getObliquity(t)));
  }

  public static double getMeanLongitude(Time time) {
    return getMeanLongitude(time.julianCenturiesSince(Time.J2000));
  }

  public static double getMeanLongitude(double time) {
    return Math.toRadians(evaluatePolynomial(time, L0));
  }

  public static double getMeanAnomaly(Time time) {
    return getMeanAnomaly(time.julianCenturiesSince(Time.J2000));
  }

  public static double getMeanAnomaly(double time) {
    return Math.toRadians(evaluatePolynomial(time, M));
  }

  public static double getEquationOfCenter(Time time) {
    return getEquationOfCenter(time.julianCenturiesSince(Time.J2000));
  }

  public static double getEquationOfCenter(double t) {
    final double m = getMeanAnomaly(t);
    return Math.toRadians(
        (1.914_600 - (0.004_817 - 0.000_014 * t) * t) * Math.sin(m)
            + (0.019_993 - 0.000_101 * t) * Math.sin(2 * m)
            + 0.000_29 * Math.sin(3 * m));
  }

  public static double getApparentRightAscension(Time time) {
    return getApparentRightAscension(time.julianCenturiesSince(Time.J2000));
  }

  public static double getApparentRightAscension(double t) {
    final double theta = getMeanLongitude(t) + getEquationOfCenter(t);
    final double omega = Math.toRadians(125.04 - 1_934.136 * t);
    final double lambda = theta - Math.toRadians(0.005_69 + 0.004_78 * Math.sin(omega));
    final double epsilon = Ecliptic.getObliquity(t) + Math.toRadians(0.002_56 * Math.cos(omega));
    return Math.atan2(Math.cos(epsilon) * Math.sin(lambda), Math.cos(lambda));
  }

  private static final double[] L0 = { 280.466_4567, 360_00.76982779, 0.030_320_28, 1.0 / 49_931, -1.0 / 15_299, -1.0 / 1_988_00 };
  private static final double[] M = { 357.529_1, 35_999.050_3, -0.000_155_9, -0.000_000_48 };

  private static double evaluatePolynomial(double t, double[] polynomial) {
    final int deg = polynomial.length;
    double result = polynomial[deg - 1];
    for (int i = polynomial.length - 2; i >= 0; --i)
      result = result * t + polynomial[i];
    return result;
  }
}
