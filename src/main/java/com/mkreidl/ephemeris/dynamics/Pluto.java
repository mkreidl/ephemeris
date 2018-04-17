package com.mkreidl.ephemeris.dynamics;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Spherical;

public class Pluto extends OrbitalModel<Cartesian> {

  private final Cartesian posCartesian = new Cartesian();
  private final Spherical posSpherical = new Spherical();

  @Override
  public void compute(Time time, Cartesian position, Cartesian velocity) {
    final double d = time.terrestrialDynamicalTime();
    final double s = 50.03 + 0.033459652 * d;
    final double p = 238.95 + 0.003968789 * d;

    posSpherical.lon = Math.toRadians(238.9508 + 0.00400703 * d
        - 19.799 * Math.sin(p) + 19.848 * Math.cos(p)
        + 0.897 * Math.sin(2 * p) - 4.956 * Math.cos(2 * p)
        + 0.610 * Math.sin(3 * p) + 1.211 * Math.cos(3 * p)
        - 0.341 * Math.sin(4 * p) - 0.190 * Math.cos(4 * p)
        + 0.128 * Math.sin(5 * p) - 0.034 * Math.cos(5 * p)
        - 0.038 * Math.sin(6 * p) + 0.031 * Math.cos(6 * p)
        + 0.020 * Math.sin(s - p) - 0.010 * Math.cos(s - p));

    posSpherical.lat = Math.toRadians(-3.9082
        - 5.453 * Math.sin(p) - 14.975 * Math.cos(p)
        + 3.527 * Math.sin(2 * p) + 1.673 * Math.cos(2 * p)
        - 1.051 * Math.sin(3 * p) + 0.328 * Math.cos(3 * p)
        + 0.179 * Math.sin(4 * p) - 0.292 * Math.cos(4 * p)
        + 0.019 * Math.sin(5 * p) + 0.100 * Math.cos(5 * p)
        - 0.031 * Math.sin(6 * p) - 0.026 * Math.cos(6 * p)
        + 0.011 * Math.cos(s - p));

    posSpherical.dst = 40.72
        + 6.68 * Math.sin(p) + 6.90 * Math.cos(p)
        - 1.18 * Math.sin(2 * p) - 0.03 * Math.cos(2 * p)
        + 0.15 * Math.sin(3 * p) - 0.14 * Math.cos(3 * p);

    posSpherical.transform(posCartesian);
  }

}
