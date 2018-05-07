package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Angle;
import com.mkreidl.ephemeris.geometry.Circle;
import com.mkreidl.ephemeris.geometry.Spherical;
import com.mkreidl.ephemeris.geometry.Stereographic;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;

public abstract class RiseSetCalculator {

  protected static final double OPTICAL_HORIZON_DEG = 34.0 / 60;

  private static final int MAX_ITERATION = 1000;

  public enum EventType {
    RISE, SET
  }

  protected final Equatorial.Cart topocentric = new Equatorial.Cart();
  protected final Spherical geographicLocation = new Spherical(SolarSystem.Body.EARTH.RADIUS_MEAN_M, 0, 0);
  protected final Time time = new Time();

  private EventType mode = EventType.SET;
  private long direction = (long) Time.MILLIS_PER_SIDEREAL_DAY;
  private double oldCos = Double.NaN;
  private final Circle horizon = new Circle();

  public abstract void computeTopocentricPosition();

  public abstract double shiftHorizonDeg();

  public void setEventType(EventType mode) {
    this.mode = mode;
  }

  public void setSearchDirection(long direction) {
    this.direction = direction * (long) Time.MILLIS_PER_SIDEREAL_DAY;
  }

  public void setGeographicLocation(Spherical geographicLocation) {
    this.geographicLocation.lon = geographicLocation.lon;
    this.geographicLocation.lat = geographicLocation.lat;
  }

  public long computeSingleStep(long millisSinceEpoch) {
    time.setTime(millisSinceEpoch);
    final double cosHourAngleAtSet = computeCosHourAngleAtSet();
    if (cosHourAngleAtSet > 1 || cosHourAngleAtSet < -1) {
      throw new IllegalStateException("At the given time the horizon does not cross the declination coordinate line of the object in question.");
    } else
      time.addMillis(computeIncrementMeetsHorizonAt(cosHourAngleAtSet));
    return time.getTime();
  }

  private static final double RAD_TO_SIDEREAL_MILLIS = Time.MILLIS_PER_SIDEREAL_DAY / (2 * Math.PI);

  public long computeIterative(long millisSinceEpoch, long precisionMs) {
    time.setTime(millisSinceEpoch);
    long deltaMs = computeDeltaT(true);
    time.addMillis(deltaMs);
    for (int n = 0; Math.abs(deltaMs) >= precisionMs && n < MAX_ITERATION; ++n) {
      deltaMs = computeDeltaT(false);
      time.addMillis(deltaMs);
    }
    return time.getTime();
  }

  private long computeDeltaT(boolean fixDirection) {
    double cosHourAngleAtSet = computeCosHourAngleAtSet();
    if (cosHourAngleAtSet > 1 || cosHourAngleAtSet < -1)
      return computeIncrementDoesNotMeetHorizon(cosHourAngleAtSet);
    else {
      long deltaMs = computeIncrementMeetsHorizonAt(cosHourAngleAtSet);
      while (fixDirection && deltaMs * direction < 0)
        deltaMs += direction;
      return deltaMs;
    }
  }

  private long computeIncrementDoesNotMeetHorizon(double cosHourAngle) {
    final boolean isSet = cosHourAngle > 1 && oldCos < -1 && direction < 0
        || cosHourAngle < -1 && oldCos > 1 && direction > 0;
    final boolean isRise = cosHourAngle > 1 && oldCos < -1 && direction > 0
        || cosHourAngle < -1 && oldCos > 1 && direction < 0;
    if (isRise && mode == EventType.RISE || isSet && mode == EventType.SET)
      direction /= -2;
    oldCos = cosHourAngle;
    return direction;
  }

  private double computeCosHourAngleAtSet() {
    computeTopocentricPosition();
    projectHorizon();
    final double zNorm = topocentric.z / topocentric.length();
    final double z = geographicLocation.lat > 0 ? zNorm : -zNorm;
    final double rOrb = Math.sqrt((1 + z) / (1 - z));
    final double dist = horizon.distFromOrigin();
    final double rHor = horizon.r;
    return (rHor * rHor - dist * dist - rOrb * rOrb) / (2 * dist * rOrb);
  }

  private void projectHorizon() {
    final Stereographic projection = new Stereographic(geographicLocation.lat >= 0 ? 1 : -1);
    projection.project(geographicLocation, Math.toRadians(90 - shiftHorizonDeg()), horizon);
  }

  private long computeIncrementMeetsHorizonAt(double cosHourAngleAtSet) {
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
    return (long) (dt * RAD_TO_SIDEREAL_MILLIS);
  }

  private double computeCurrentHourAngle() {
    // Right ascension is the sidereal time at (upper) meridian transit (hourAngle == 0)
    final double siderealTime = time.getHourAngleOfVernalEquinox() + geographicLocation.lon;
    final double rightAscension = Math.atan2(topocentric.y, topocentric.x);
    return Angle.standardize(siderealTime - rightAscension);
  }
}
