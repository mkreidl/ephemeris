package com.mkreidl.ephemeris.solarsystem;

import com.mkreidl.ephemeris.Time;

public class Nutation {

  private static final double TO_RAD = Math.toRadians(1 / 3.6e7);
  private static final int NUM_OF_COEFF_TO_COMPUTE = 13;

  private static final double[] D = { 1072260.703692, 1602961601.2090, -6.3706, 0.006593, -0.00003169 };
  private static final double[] M = { 1287104.793048, 129596581.0481, -0.5532, 0.000136, -0.00001149 };
  private static final double[] MP = { 485868.249036, 1717915923.2178, 31.8792, 0.051635, -0.00024470 };
  private static final double[] F = { 335779.526232, 1739527262.8478, -12.7512, -0.001037, 0.00000417 };
  private static final double[] OMEGA = { 450160.398036, -6962890.5431, 7.4722, 0.007702, -0.00005939 };

  private static final double[][] SUMMANDS = new double[][] {
      { 0, 0, 0, 0, 1, -171996, -174.2, 92025, 8.9 },
      { 0, 0, 2, -2, 2, -13187, -1.6, 5736, -3.1 },
      { 0, 0, 2, 0, 2, -2274, -0.2, 977, -0.5 },
      { 0, 0, 0, 0, 2, 2062, 0.2, -895, 0.5 },
      { 0, 1, 0, 0, 0, 1426, -3.4, 54, -0.1 },
      { 1, 0, 0, 0, 0, 712, 0.1, -7, 0.0 },
      { 0, 1, 2, -2, 2, -517, 1.2, 224, -0.6 },
      { 0, 0, 2, 0, 1, -386, -0.4, 200, 0.0 },
      { 1, 0, 2, 0, 2, -301, 0.0, 129, -0.1 },
      { 0, -1, 2, -2, 2, 217, -0.5, -95, 0.3 },
      { 1, 0, 0, -2, 0, -158, 0.0, -1, 0.0 },
      { 0, 0, 2, -2, 1, 129, 0.1, -70, 0.0 },
      { -1, 0, 2, 0, 2, 123, 0.0, -53, 0.0 },
      { 0, 0, 0, 2, 0, 63, 0.0, -2, 0.0 },
      { 1, 0, 0, 0, 1, 63, 0.1, -33, 0.0 },
      { -1, 0, 2, 2, 2, -59, 0.0, 26, 0.0 },
      { -1, 0, 0, 0, 1, -58, -0.1, 32, 0.0 },
      { 1, 0, 2, 0, 1, -51, 0.0, 27, 0.0 },
      { 2, 0, 0, -2, 0, 48, 0.0, 1, 0.0 },
      { -2, 0, 2, 0, 1, 46, 0.0, -24, 0.0 },
      { 0, 0, 2, 2, 2, -38, 0.0, 16, 0.0 },
      { 2, 0, 2, 0, 2, -31, 0.0, 13, 0.0 },
      { 2, 0, 0, 0, 0, 29, 0.0, -1, 0.0 },
      { 1, 0, 2, -2, 2, 29, 0.0, -12, 0.0 },
      { 0, 0, 2, 0, 0, 26, 0.0, -1, 0.0 },
      { 0, 0, 2, -2, 0, -22, 0.0, 0, 0.0 },
      { -1, 0, 2, 0, 1, 21, 0.0, -10, 0.0 },
      { 0, 2, 0, 0, 0, 17, -0.1, 0, 0.0 },
      { 0, 2, 2, -2, 2, -16, 0.1, 7, 0.0 },
      { -1, 0, 0, 2, 1, 16, 0.0, -8, 0.0 },
      { 0, 1, 0, 0, 1, -15, 0.0, 9, 0.0 },
      { 1, 0, 0, -2, 1, -13, 0.0, 7, 0.0 },
      { 0, -1, 0, 0, 1, -12, 0.0, 6, 0.0 },
      { 2, 0, -2, 0, 0, 11, 0.0, 0, 0.0 },
      { -1, 0, 2, 2, 1, -10, 0.0, 5, 0.0 },
      { 1, 0, 2, 2, 2, -8, 0.0, 3, 0.0 },
      { 1, 1, 0, -2, 0, -7, 0.0, 0, 0.0 },
      { 0, 1, 2, 0, 2, 7, 0.0, -3, 0.0 },
      { 0, -1, 2, 0, 2, -7, 0.0, 3, 0.0 },
      { 0, 0, 2, 2, 1, -7, 0.0, 3, 0.0 },
      { -2, 0, 0, 2, 1, -6, 0.0, 3, 0.0 },
      { 1, 0, 0, 2, 0, 6, 0.0, 0, 0.0 },
      { 2, 0, 2, -2, 2, 6, 0.0, -3, 0.0 },
      { 0, 0, 0, 2, 1, -6, 0.0, 3, 0.0 },
      { 1, 0, 2, -2, 1, 6, 0.0, -3, 0.0 },
      { 0, -1, 2, -2, 1, -5, 0.0, 3, 0.0 },
      { 0, 0, 0, -2, 1, -5, 0.0, 3, 0.0 },
      { 1, -1, 0, 0, 0, 5, 0.0, 0, 0.0 },
      { 2, 0, 2, 0, 1, -5, 0.0, 3, 0.0 },
      { 2, 0, 0, -2, 1, 4, 0.0, -2, 0.0 },
      { 0, 1, 2, -2, 1, 4, 0.0, -2, 0.0 },
      { 1, 0, 0, -1, 0, -4, 0.0, 0, 0.0 },
      { 0, 1, 0, -2, 0, -4, 0.0, 0, 0.0 },
      { 1, 0, -2, 0, 0, 4, 0.0, 0, 0.0 },
      { 0, 0, 0, 1, 0, -4, 0.0, 0, 0.0 },
      { -2, 0, 2, 0, 2, -3, 0.0, 1, 0.0 },
      { 1, -1, 0, -1, 0, -3, 0.0, 0, 0.0 },
      { 1, 1, 0, 0, 0, -3, 0.0, 0, 0.0 },
      { 1, 0, 2, 0, 0, 3, 0.0, 0, 0.0 },
      { 1, -1, 2, 0, 2, -3, 0.0, 1, 0.0 },
      { -1, -1, 2, 2, 2, -3, 0.0, 1, 0.0 },
      { 3, 0, 2, 0, 2, -3, 0.0, 1, 0.0 },
      { 0, -1, 2, 2, 2, -3, 0.0, 1, 0.0 },
      { 0, -2, 2, -2, 1, -2, 0.0, 1, 0.0 },
      { -2, 0, 0, 0, 1, -2, 0.0, 1, 0.0 },
      { 1, 1, 2, 0, 2, 2, 0.0, -1, 0.0 },
      { -1, 0, 2, -2, 1, -2, 0.0, 1, 0.0 },
      { 2, 0, 0, 0, 1, 2, 0.0, -1, 0.0 },
      { 1, 0, 0, 0, 2, -2, 0.0, 1, 0.0 },
      { 3, 0, 0, 0, 0, 2, 0.0, 0, 0.0 },
      { 0, 0, 2, 1, 2, 2, 0.0, -1, 0.0 },
      { -1, 0, 2, 4, 2, -2, 0.0, 1, 0.0 },
      { 2, 0, -2, 0, 1, 1, 0.0, 0, 0.0 },
      { 2, 1, 0, -2, 0, 1, 0.0, 0, 0.0 },
      { 0, 0, -2, 2, 1, 1, 0.0, 0, 0.0 },
      { 0, 1, -2, 2, 0, -1, 0.0, 0, 0.0 },
      { 0, 1, 0, 0, 2, 1, 0.0, 0, 0.0 },
      { -1, 0, 0, 1, 1, 1, 0.0, 0, 0.0 },
      { 0, 1, 2, -2, 0, -1, 0.0, 0, 0.0 },
      { -1, 0, 0, 0, 2, 1, 0.0, -1, 0.0 },
      { 1, 0, 0, -4, 0, -1, 0.0, 0, 0.0 },
      { -2, 0, 2, 2, 2, 1, 0.0, -1, 0.0 },
      { 2, 0, 0, -4, 0, -1, 0.0, 0, 0.0 },
      { 1, 1, 2, -2, 2, 1, 0.0, -1, 0.0 },
      { 1, 0, 2, 2, 1, -1, 0.0, 1, 0.0 },
      { -2, 0, 2, 4, 2, -1, 0.0, 1, 0.0 },
      { -1, 0, 4, 0, 2, 1, 0.0, 0, 0.0 },
      { 1, -1, 0, -2, 0, 1, 0.0, 0, 0.0 },
      { 2, 0, 2, -2, 1, 1, 0.0, -1, 0.0 },
      { 2, 0, 2, 2, 2, -1, 0.0, 0, 0.0 },
      { 1, 0, 0, 2, 1, -1, 0.0, 0, 0.0 },
      { 0, 0, 4, -2, 2, 1, 0.0, 0, 0.0 },
      { 3, 0, 2, -2, 2, 1, 0.0, 0, 0.0 },
      { 1, 0, 2, -2, 0, -1, 0.0, 0, 0.0 },
      { 0, 1, 2, 0, 1, 1, 0.0, 0, 0.0 },
      { -1, -1, 0, 2, 1, 1, 0.0, 0, 0.0 },
      { 0, 0, -2, 0, 1, -1, 0.0, 0, 0.0 },
      { 0, 0, 2, -1, 2, -1, 0.0, 0, 0.0 },
      { 0, 1, 0, 2, 0, -1, 0.0, 0, 0.0 },
      { 1, 0, -2, -2, 0, -1, 0.0, 0, 0.0 },
      { 0, -1, 2, 0, 1, -1, 0.0, 0, 0.0 },
      { 1, 1, 0, -2, 1, -1, 0.0, 0, 0.0 },
      { 1, 0, -2, 2, 0, -1, 0.0, 0, 0.0 },
      { 2, 0, 0, 2, 0, 1, 0.0, 0, 0.0 },
      { 0, 0, 2, 4, 2, -1, 0.0, 0, 0.0 },
      { 0, 1, 0, 1, 0, 1, 0.0, 0, 0.0 }
  };

  private static double evaluatePolynomial(double t, double[] polynomial) {
    final int deg = polynomial.length;
    double result = polynomial[deg - 1];
    for (int i = polynomial.length - 2; i >= 0; --i)
      result = result * t + polynomial[i];
    return result / 3600;
  }

  public static double getLongitude(Time time) {
    return getLongitude(time.julianCenturiesSince(Time.J2000));
  }

  public static double getLongitude(double t) {
    final double d = evaluatePolynomial(t, D);
    final double m = evaluatePolynomial(t, M);
    final double mp = evaluatePolynomial(t, MP);
    final double f = evaluatePolynomial(t, F);
    final double omega = evaluatePolynomial(t, OMEGA);

    double longitude = 0.0;
    for (int i = 0; i < NUM_OF_COEFF_TO_COMPUTE; ++i) {
      final double[] summand = SUMMANDS[i];
      final double arg = summand[0] * mp + summand[1] * m + summand[2] * f + summand[3] * d + summand[4] * omega;
      longitude += (summand[5] + summand[6] * t) * Math.sin(Math.toRadians(arg));
    }
    return longitude * TO_RAD;
  }

  private double longitude = 0.0;
  private double obliquity = 0.0;

  public void compute(Time time) {
    final double t = time.julianCenturiesSince(Time.J2000);
    final double d = evaluatePolynomial(t, D);
    final double m = evaluatePolynomial(t, M);
    final double mp = evaluatePolynomial(t, MP);
    final double f = evaluatePolynomial(t, F);
    final double omega = evaluatePolynomial(t, OMEGA);

    longitude = 0.0;
    obliquity = 0.0;
    for (int i = 0; i < NUM_OF_COEFF_TO_COMPUTE; ++i) {
      final double[] summand = SUMMANDS[i];
      final double arg = summand[0] * mp + summand[1] * m + summand[2] * f + summand[3] * d + summand[4] * omega;
      longitude += (summand[5] + summand[6] * t) * Math.sin(Math.toRadians(arg));
      obliquity += (summand[7] + summand[8] * t) * Math.cos(Math.toRadians(arg));
    }
    longitude *= TO_RAD;
    obliquity *= TO_RAD;
  }

  public double getLongitude() {
    return longitude;
  }

  public double getObliquity() {
    return obliquity;
  }
}
