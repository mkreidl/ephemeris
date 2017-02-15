package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.*;
import com.mkreidl.ephemeris.dynamics.*;
import com.mkreidl.ephemeris.dynamics.VSOP87.C.*;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.sky.coordinates.*;

import java.util.*;


public class SolarSystem
{
    private final EnumMap<Body, Ecliptical.Cart> positions = new EnumMap<>( Body.class );
    private final EnumMap<Body, Ecliptical.Cart> velocities = new EnumMap<>( Body.class );
    private final EnumMap<Body, OrbitalModel<Cartesian>> models = new EnumMap<>( Body.class );

    /**
     * Calculate the ecliptic of the ecliptic.
     * <p>
     * Reference: Astronomical Almanac (1984),
     * https://de.wikipedia.org/wiki/Ekliptik
     *
     * @return Obliquity of the ecliptic in radians at the given date.
     */
    public static double getEcliptic( final Time date )
    {
        final double t = date.julianDayNumberSince( Time.J2000 ) / Time.DAYS_PER_CENTURY;
        return ( 23.4392911111 - t * ( 1.30041667e-2 + t * ( 1.638888e-7 - t * ( 5.036111e-7 ) ) ) ) * Math.PI / 180;
    }

    public enum Body
    {
        SUN( 0.0, 0.0, 6.96342e8 ),
        MOON( 7.342e22, 1.7371e6, 1.7381e6 ),
        MERCURY( 0.0, 0.0, 2.439764e6 ),
        VENUS( 0.0, 0.0, 6.05159e6 ),
        EARTH( 5.9726e24, 6.371e6, 6.37815e6 ),
        MARS( 0.0, 0.0, 3.397e6 ),
        JUPITER( 0.0, 0.0, 7.149268e7 ),
        SATURN( 0.0, 0.0, 6.026714e7 ),
        URANUS( 0.0, 0.0, 2.5559e7 ),
        NEPTUNE( 0.0, 0.0, 2.4764e7 ),;

        public final double MASS;
        public final double RADIUS_EQUATORIAL;
        public final double RADIUS_MEAN;

        Body( double mass, double radiusMean, double radiusEquatorial )
        {
            MASS = mass;
            RADIUS_EQUATORIAL = radiusEquatorial;
            RADIUS_MEAN = radiusMean;
        }
    }

    public SolarSystem()
    {
        for ( Body body : Body.values() )
        {
            positions.put( body, new Ecliptical.Cart() );
            velocities.put( body, new Ecliptical.Cart() );
        }
        models.put( Body.SUN, new Sun() );
        models.put( Body.MERCURY, new Mercury() );
        models.put( Body.VENUS, new Venus() );
        models.put( Body.EARTH, new Earth() );
        models.put( Body.MARS, new Mars() );
        models.put( Body.JUPITER, new Jupiter() );
        models.put( Body.SATURN, new Saturn() );
        models.put( Body.URANUS, new Uranus() );
        models.put( Body.NEPTUNE, new Neptune() );
        models.put( Body.MOON, new Moon() );
    }

    /**
     * Calculate ephemerides of objects in the solar system
     *
     * @param body
     * @param output
     * @return
     */
    public Ephemerides getEphemerides( final Body body, final Ephemerides output )
    {
        switch ( models.get( body ).getType() )
        {
            case HELIOCENTRIC:
                output.setHeliocentric( positions.get( body ), positions.get( Body.EARTH ) );
                output.setHeliocentricVelocities( velocities.get( body ), velocities.get( Body.EARTH ) );
                break;
            case GEOCENTRIC:
                output.setGeocentric( positions.get( body ), positions.get( Body.EARTH ) );
                output.setGeocentricVelocities( velocities.get( body ), velocities.get( Body.EARTH ) );
                break;
        }
        return output;
    }

    /**
     * Calculate ephemerides of fixed stars
     * No velocities, no distinction between heliocentric/geocentric/topocentric
     *
     * @param heliocentricPosition
     * @param output
     * @return
     */
    public Ephemerides getEphemerides( final Ecliptical.Cart heliocentricPosition, final Ephemerides output )
    {
        final Ecliptical.Cart earthPos = positions.get( Body.EARTH );
        output.setHeliocentric( heliocentricPosition, earthPos );
        return output;
    }

    public void calculate( final Time time, final Body body )
    {
        final OrbitalModel<Cartesian> model = models.get( body );
        final Ecliptical.Cart pos = positions.get( body );
        final Ecliptical.Cart vel = velocities.get( body );
        model.calculate( time, pos, vel );
        pos.scale( model.getUnit().toMeters() );
        vel.scale( model.getUnit().toMeters() );
    }

    /**
     * Parallel computation of all the positions and velocities of objects in the solar
     * system at the given time
     *
     * @param time
     */
    public void calculate( final Time time )
    {
        final LinkedList<Thread> threadList = new LinkedList<>();
        for ( final Body body : Body.values() )
        {
            final Thread calculationThread = new Thread()
            {
                public void run()
                {
                    calculate( time, body );
                }
            };
            threadList.add( calculationThread );
            calculationThread.start();
        }
        for ( Thread thread : threadList )
            try
            {
                thread.join();
            }
            catch ( InterruptedException e )
            {
            }
    }
}
