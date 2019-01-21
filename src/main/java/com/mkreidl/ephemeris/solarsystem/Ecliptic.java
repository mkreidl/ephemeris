package com.mkreidl.ephemeris.solarsystem;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Coordinates;
import com.mkreidl.ephemeris.geometry.Matrix3x3;

public class Ecliptic {

  private Ecliptic() {
  }

  public static void computeEclJ2000ToEquToDate(Time time, Matrix3x3 transformation) {
    PrecessionMatrix.compute(time, transformation);
    transformation.postRotateX(getObliquity(time));
  }

  /**
   * Calculate the obliquity of the ecliptic.
   * <p>
   * Reference: Astronomical Almanac (1984),
   * https://de.wikipedia.org/wiki/Ekliptik
   *
   * @return Obliquity of the ecliptic in radians at the given date.
   */
  public static double getObliquity( final Time time ) {
    return getObliquity(time.julianCenturiesSince(Time.J2000));
  }
  
  static double getObliquity( double t ) {
    return Math.toRadians(23.4392911111 - t * (1.30041667e-2 + t * (1.638888e-7 - t * 5.036111e-7)));
  }

  static Matrix3x3 getEcl2EquMatrix(Time time, Matrix3x3 output) {
    return output.setRotation(getObliquity(time), Coordinates.Axis.X);
  }

  public static Matrix3x3 getEqu2EclMatrix(Time time, Matrix3x3 output) {
    return output.setRotation(-getObliquity(time), Coordinates.Axis.X);
  }
}
