package com.mkreidl.ephemeris.dynamics;

import com.mkreidl.ephemeris.Distance;
import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.ClassicalOrbitalElements;
import com.mkreidl.ephemeris.geometry.Spherical;

import static com.mkreidl.ephemeris.geometry.Angle.DEG;
import static com.mkreidl.ephemeris.sky.SolarSystem.Body;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * @author mkreidl
 *         <p>
 *         Orbital elements taken from:
 *         Paul Schlyter, <a>http://www.stjarnhimlen.se/</a>
 *         <p>
 *         Kepler's equation is solved using Newton's method to obtain
 *         Moon's coordinates in the cartesian reference frame.
 */
public class Moon extends OrbitalModel<Cartesian>
{
    @Override
    public void compute( Time time, Cartesian position, Cartesian velocity )
    {
        compute( time );
        position.set( posCartesian );
        if ( velocity != null )
        {
            // TODO: Implement velocity calculation for the Moon
            // This is a dummy which sets the velocity in a qualitative way
            // to ensure that the Moon is not retrograde
            velocity.x = -position.y;
            velocity.y = position.x;
            velocity.z = 0;
            // Adjust the velocity to result in 1 rotation per 29 days (unit of velocity [m/s])
            velocity.scale( 2 * Math.PI / ( 29 * Time.SECONDS_PER_DAY ) );
        }
    }

    @Override
    public Type getType()
    {
        return Type.GEOCENTRIC;
    }

    public ClassicalOrbitalElements getOrbitalElements( ClassicalOrbitalElements output )
    {
        return output.set( orbitalElements );
    }

    public Cartesian getPosition( Cartesian position )
    {
        return position.set( posCartesian );
    }

    public Spherical getPosition( Spherical position )
    {
        return position.set( posSpherical );
    }

    /**
     * Calculate the position of the Moon at a given time.
     *
     * @param time Time object.
     */
    protected void compute( Time time )
    {
        final double t = time.terrestrialDynamicalTime();

        orbitalElements.set( orbElMoonSeries[1] );
        orbitalElements.times( t );
        orbitalElements.add( orbElMoonSeries[0] );

        orbitalElements.computePosition();
        orbitalElements.getPosition( posSpherical );

        orbElSun.set( orbElSunSeries[1] );
        orbElSun.times( t );
        orbElSun.add( orbElSunSeries[0] );

        posSpherical.lon += longitudeCorrection();
        posSpherical.lat += latitudeCorrection();
        posSpherical.dst += distanceCorrection();

        posSpherical.standardize();
        posSpherical.transform( posCartesian );
    }

    /**
     * N = longitude of the ascending node
     * i = inclination to the ecliptic (plane of the Earth's orbit)
     * w = argument of perihelion
     * a = semi-major axis
     * e = eccentricity (0=circle, 0-1=ellipse, 1=parabola)
     * M = mean anomaly (0 at perihelion; increases uniformly with time)
     */

    private final ClassicalOrbitalElements orbitalElements = new ClassicalOrbitalElements();
    private final Spherical posSpherical = new Spherical();
    private final Spherical velSpherical = new Spherical();
    private final Cartesian posCartesian = new Cartesian();
    private final Cartesian velCartesian = new Cartesian();

    private double D()
    {
        // Mean elongation of Moon
        return orbitalElements.meanLongitude() - orbElSun.meanLongitude();
    }

    private double F()
    {
        // Argument of latitude of Moon
        return orbitalElements.meanLongitude() - orbitalElements.node;
    }

    private double longitudeCorrection()
    {
        final double D = D();
        final double DD = 2 * D;
        return DEG * ( -1.274 * sin( orbitalElements.meanAnom - DD )
                + 0.658 * sin( DD )
                - 0.186 * sin( orbElSun.meanAnom )
                - 0.059 * sin( 2 * orbitalElements.meanAnom - DD )
                - 0.057 * sin( orbitalElements.meanAnom - DD + orbElSun.meanAnom )
                + 0.053 * sin( orbitalElements.meanAnom + DD )
                + 0.046 * sin( DD - orbElSun.meanAnom )
                + 0.041 * sin( orbitalElements.meanAnom - orbElSun.meanAnom )
                - 0.035 * sin( D )
                - 0.031 * sin( orbitalElements.meanAnom + orbElSun.meanAnom )
                - 0.015 * sin( 2 * ( F() - D ) )
                + 0.011 * sin( orbitalElements.meanAnom - 4 * D ) );
    }

    private double latitudeCorrection()
    {
        final double DD = 2 * D();
        final double F = F();
        return DEG * ( -0.173 * sin( F - DD )
                - 0.055 * sin( orbitalElements.meanAnom - F - DD )
                - 0.046 * sin( orbitalElements.meanAnom + F - DD )
                + 0.033 * sin( F + DD )
                + 0.017 * sin( 2 * orbitalElements.meanAnom + F ) );
    }

    private double distanceCorrection()
    {
        final double DD = 2 * D();
        return DEG * ( -0.58 * cos( orbitalElements.meanAnom - DD )
                - 0.46 * cos( DD ) );
    }

    private static final ClassicalOrbitalElements[] orbElMoonSeries;
    private static final ClassicalOrbitalElements[] orbElSunSeries;
    private static final ClassicalOrbitalElements orbElSun = new ClassicalOrbitalElements();

    static
    {
        orbElMoonSeries = new ClassicalOrbitalElements[2];
        orbElSunSeries = new ClassicalOrbitalElements[2];

        orbElMoonSeries[0] = new ClassicalOrbitalElements();
        orbElMoonSeries[1] = new ClassicalOrbitalElements();
        orbElSunSeries[0] = new ClassicalOrbitalElements();
        orbElSunSeries[1] = new ClassicalOrbitalElements();

        // 0-order terms for Moon's orbital elements
        orbElMoonSeries[0].node = 125.1228 * DEG;
        orbElMoonSeries[0].incl = 5.1454 * DEG;
        orbElMoonSeries[0].periapsis = 318.0634 * DEG;
        orbElMoonSeries[0].axis = 60.2666 * Body.EARTH.RADIUS_EQUATORIAL;
        orbElMoonSeries[0].exc = 0.054900;
        orbElMoonSeries[0].meanAnom = 115.3654 * DEG;

        // 1-order terms for Moon's orbital elements
        orbElMoonSeries[1].node = -0.0529538083 * DEG;
        orbElMoonSeries[1].incl = 0.0 * DEG;
        orbElMoonSeries[1].periapsis = 0.1643573223 * DEG;
        orbElMoonSeries[1].axis = 0.0 * Body.EARTH.RADIUS_EQUATORIAL;
        orbElMoonSeries[1].exc = 0.0;
        orbElMoonSeries[1].meanAnom = 13.0649929509 * DEG;

        // 0-order terms for Sun's orbital elements
        orbElSunSeries[0].node = 0.0 * DEG;
        orbElSunSeries[0].incl = 0.0 * DEG;
        orbElSunSeries[0].periapsis = 282.9404 * DEG;
        orbElSunSeries[0].axis = 1.0 * Distance.AU.toMeters();
        orbElSunSeries[0].exc = 0.016709;
        orbElSunSeries[0].meanAnom = 356.0470 * DEG;

        // 1-order terms for Sun's orbital elements
        orbElSunSeries[1].node = 0.0 * DEG;
        orbElSunSeries[1].incl = 0.0 * DEG;
        orbElSunSeries[1].periapsis = 4.70935E-5 * DEG;
        orbElSunSeries[1].axis = 0.0 * Distance.AU.toMeters();
        orbElSunSeries[1].exc = -1.151E-9;
        orbElSunSeries[1].meanAnom = 0.9856002585 * DEG;
    }
}
