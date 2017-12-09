package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.dynamics.Moon;
import com.mkreidl.ephemeris.dynamics.OrbitalModel;
import com.mkreidl.ephemeris.dynamics.Sun;
import com.mkreidl.ephemeris.dynamics.VSOP87.C.Earth;
import com.mkreidl.ephemeris.dynamics.VSOP87.C.Jupiter;
import com.mkreidl.ephemeris.dynamics.VSOP87.C.Mars;
import com.mkreidl.ephemeris.dynamics.VSOP87.C.Mercury;
import com.mkreidl.ephemeris.dynamics.VSOP87.C.Neptune;
import com.mkreidl.ephemeris.dynamics.VSOP87.C.Saturn;
import com.mkreidl.ephemeris.dynamics.VSOP87.C.Uranus;
import com.mkreidl.ephemeris.dynamics.VSOP87.C.Venus;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Coordinates;
import com.mkreidl.ephemeris.geometry.Matrix;
import com.mkreidl.ephemeris.sky.coordinates.Ecliptical;

import java.util.EnumMap;
import java.util.LinkedList;


public class SolarSystem
{
    public static final Body[] PLANETS = new Body[]{
            Body.MERCURY, Body.VENUS, Body.EARTH, Body.MARS,
            Body.JUPITER, Body.SATURN, Body.URANUS, Body.NEPTUNE
    };

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

    public static Matrix getEcl2EquMatrix( Time time, Matrix output )
    {
        output.setRotation( getEcliptic( time ), Coordinates.Axis.X );
        return output;
    }

    public static Matrix getEqu2EclMatrix( Time time, Matrix output )
    {
        output.setRotation( -getEcliptic( time ), Coordinates.Axis.X );
        return output;
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

    private final EnumMap<Body, Ecliptical.Cart> positions = new EnumMap<>( Body.class );
    private final EnumMap<Body, Ecliptical.Cart> velocities = new EnumMap<>( Body.class );
    private final EnumMap<Body, OrbitalModel<Cartesian>> models = new EnumMap<>( Body.class );

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

    public Cartesian getHeliocentric( final Body body, final Cartesian output )
    {
        output.set( positions.get( body ) );
        if ( models.get( body ).getType() == OrbitalModel.Type.GEOCENTRIC )
            output.add( positions.get( Body.EARTH ) );
        return output;
    }

    /**
     * Calculate ephemerides of objects in the solar system
     *
     * @param body
     * @param position
     * @return
     */
    public Position getEphemerides( final Body body, final Position position )
    {
        switch ( models.get( body ).getType() )
        {
            case HELIOCENTRIC:
                position.setHeliocentricPosition( positions.get( body ), positions.get( Body.EARTH ) );
                position.setHeliocentricVelocity( velocities.get( body ), velocities.get( Body.EARTH ) );
                break;
            case GEOCENTRIC:
                position.setGeocentricPosition( positions.get( body ), positions.get( Body.EARTH ) );
                position.setGeocentricVelocity( velocities.get( body ), velocities.get( Body.EARTH ) );
                break;
        }
        position.correctAberration();
        return position;
    }

    public void compute( final Time time, final Body body )
    {
        final OrbitalModel<Cartesian> model = models.get( body );
        model.compute( time, positions.get( body ), velocities.get( body ) );
        positions.get( body ).scale( model.getUnit().toMeters() );
        velocities.get( body ).scale( model.getUnit().toMeters() );
    }

    /**
     * Parallel computation of all the positions and velocities of objects in the solar
     * system at the given time
     *
     * @param time
     */
    public void compute( final Time time )
    {
        final LinkedList<Thread> threadList = new LinkedList<>();
        for ( final Body body : Body.values() )
        {
            final Thread calculationThread = new Thread( () -> compute( time, body ) );
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
