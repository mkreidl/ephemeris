package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Angle;
import com.mkreidl.ephemeris.geometry.Circle;
import com.mkreidl.ephemeris.geometry.Spherical;
import com.mkreidl.ephemeris.geometry.Stereographic;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;

public abstract class RiseSetCalculator {

  private static final int MAX_ITERATION = 1000;

  public enum EventType {
    RISE, SET
  }

  private final Equatorial.Cart topocentric = new Equatorial.Cart();
  private final Spherical geographicLocation = new Spherical(SolarSystem.Body.EARTH.RADIUS_MEAN_M, 0, 0);
  private final Circle horizon = new Circle();
  private final Time time = new Time();

  private EventType mode = EventType.SET;
  private long direction = (long) Time.MILLIS_PER_SIDEREAL_DAY;
  private double oldCos = Double.NaN;

  public abstract void computeTopocentricPosition(Time time, Spherical geographicLocation, Equatorial.Cart topocentric);

  public void setEventType(EventType mode) {
    this.mode = mode;
  }

  public void setSearchDirection(long direction) {
    this.direction = direction * (long) Time.MILLIS_PER_SIDEREAL_DAY;
  }

  public void setGeographicLocation(double lonDeg, double latDeg, double deltaHorizonDeg) {
    geographicLocation.lon = Math.toRadians(lonDeg);
    geographicLocation.lat = Math.toRadians(latDeg);
    final Stereographic projection = new Stereographic(latDeg >= 0 ? 1 : -1);
    projection.project(geographicLocation, Math.toRadians(90 - deltaHorizonDeg), horizon);
  }

  public long computeSingleStep(long millisSinceEpoch) {
    time.setTime(millisSinceEpoch);
    computeTopocentricPosition(time, geographicLocation, topocentric);
    final double cosHourAngleAtSet = computeCosHourAngleAtSet();
    if (cosHourAngleAtSet > 1 || cosHourAngleAtSet < -1) {
    } else
      time.addMillis(computeDeltaT(Math.acos(cosHourAngleAtSet)));
    return time.getTime();
  }

  private double computeCosHourAngleAtSet() {
    final double zNorm = topocentric.z / topocentric.length();
    final double z = geographicLocation.lat > 0 ? zNorm : -zNorm;
    final double rOrb = Math.sqrt((1 + z) / (1 - z));
    final double dist = horizon.distFromOrigin();
    final double rHor = horizon.r;
    return (rHor * rHor - dist * dist - rOrb * rOrb) / (2 * dist * rOrb);
  }

  private long computeDeltaT(double cosHourAngleAtSet) {
    final double dt;
    switch (mode) {
      case RISE:
        dt = -Math.acos(cosHourAngleAtSet) - computeCurrentHourAngle();
        break;
      case SET:
        dt = Math.acos(cosHourAngleAtSet) - computeCurrentHourAngle();
        break;
      default:
        dt = 0;
    }
    return (long) (dt * Time.MILLIS_PER_SIDEREAL_DAY / (2 * Math.PI));
  }

  private double computeCurrentHourAngle() {
    // Right ascension is the sidereal time at (upper) meridian transit (hourAngle == 0)
    final double siderealTime = time.getHourAngleOfVernalEquinox() + geographicLocation.lon;
    final double rightAscension = Math.atan2(topocentric.y, topocentric.x);
    return Angle.standardize(siderealTime - rightAscension);
  }

  public long compute(long millisSinceEpoch, long precisionMs) {
    time.setTime(millisSinceEpoch);
    long deltaMs = Long.MAX_VALUE;
    for (int n = 0; Math.abs(deltaMs) >= precisionMs && n < MAX_ITERATION; ++n) {
      computeTopocentricPosition(time, geographicLocation, topocentric);
      deltaMs = computeDeltaT();
      time.addMillis(deltaMs);
    }
    return time.getTime();
  }

  private long computeDeltaT() {
    final double cosHourAngleAtSet = computeCosHourAngleAtSet();
    if (cosHourAngleAtSet > 1 || cosHourAngleAtSet < -1)
      return computeIncrement(cosHourAngleAtSet);
    else
      return computeDeltaT(cosHourAngleAtSet);
  }

  private long computeIncrement(double cosHourAngle) {
    final boolean isSet = cosHourAngle > 1 && oldCos < -1 && direction < 0
        || cosHourAngle < -1 && oldCos > 1 && direction > 0;
    final boolean isRise = cosHourAngle > 1 && oldCos < -1 && direction > 0
        || cosHourAngle < -1 && oldCos > 1 && direction < 0;
    if (isRise && mode == EventType.RISE || isSet && mode == EventType.SET)
      direction /= -2;
    oldCos = cosHourAngle;
    return direction;
  }
}
