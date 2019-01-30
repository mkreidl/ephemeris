package com.mkreidl.ephemeris.solarsystem;

import com.mkreidl.ephemeris.Position;
import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Spherical;
import com.mkreidl.ephemeris.sky.coordinates.Ecliptical;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;
import org.junit.Test;

import static com.mkreidl.ephemeris.solarsystem.Body.EARTH;

public class PlutoTest {

    private final SolarSystem solarSystem = new SolarSystemMeeus();
    private final Time time = new Time();

    private final Ecliptical.Sphe ecliptical = new Ecliptical.Sphe();
    private final Equatorial.Sphe equatorial = new Equatorial.Sphe();

    @Test
    public void test() {
        time.setTime(System.currentTimeMillis());
        solarSystem.compute(time, Body.PLUTO);
        solarSystem.compute(time, EARTH);

        final Position actual = solarSystem.getEphemerides(Body.PLUTO, new Position());
        actual.setTimeLocation(time, new Spherical());
        actual.get(ecliptical, Position.CoordinatesCenter.GEOCENTRIC);
        actual.get(equatorial, Position.CoordinatesCenter.GEOCENTRIC);

        System.out.println(ecliptical);
        System.out.println(equatorial);
    }

}
