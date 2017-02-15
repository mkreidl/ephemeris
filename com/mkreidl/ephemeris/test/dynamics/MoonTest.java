package com.mkreidl.ephemeris.test.dynamics;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.dynamics.Moon;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.ClassicalOrbitalElements;
import com.mkreidl.ephemeris.geometry.Spherical;
import com.mkreidl.ephemeris.sky.SolarSystem;

import org.junit.Test;

import static com.mkreidl.ephemeris.geometry.Angle.DEG;
import static com.mkreidl.ephemeris.geometry.Angle.MIN;
import static com.mkreidl.ephemeris.geometry.Angle.SEC;
import static com.mkreidl.ephemeris.geometry.Angle.standardize;
import static org.junit.Assert.assertEquals;

public class MoonTest
{
    // References from the Astronomical Almanac
    private final Time time = Time.getAstronomicalTime( "1990.04.19 00:00:00" );
    private final Moon moon = new Moon();
    private final ClassicalOrbitalElements orbitalEl = new ClassicalOrbitalElements();
    private final Cartesian posCartesian = new Cartesian();
    private final Spherical posSpherical = new Spherical();

    private final Spherical refSpherical = new Spherical(
            60.793 * SolarSystem.Body.EARTH.RADIUS_EQUATORIAL, 306.94 * DEG, -0.55 * DEG
    );

    @Test
    public void testCalculate()
    {
        moon.calculate( time, posCartesian );
        moon.getOrbitalElements( orbitalEl );
        moon.getPosition( posSpherical );

        assertEquals( standardize( 312.7381 * DEG ), orbitalEl.node, 1.0 * SEC );
        assertEquals( standardize( 5.1454 * DEG ), orbitalEl.incl, 1.0 * SEC );
        assertEquals( standardize( -264.2546 * DEG ), orbitalEl.periapsis, 1.0 * SEC );
        assertEquals( 60.2666 * SolarSystem.Body.EARTH.RADIUS_EQUATORIAL, orbitalEl.axis, 1e-12 );
        assertEquals( 0.054900, orbitalEl.exc, 1e-12 );
        assertEquals( standardize( -46173.9046 * DEG ), orbitalEl.meanAnom, 1.0 * SEC );

        assertEquals( refSpherical.lat, posSpherical.lat, 2.5 * MIN );
        assertEquals( refSpherical.lon, posSpherical.lon, 1.0 * MIN );
    }

}
