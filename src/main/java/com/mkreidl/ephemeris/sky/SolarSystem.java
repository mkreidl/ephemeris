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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;


public class SolarSystem
{
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
        return Math.toRadians( 23.4392911111 - t * ( 1.30041667e-2 + t * ( 1.638888e-7 - t * 5.036111e-7 ) ) );
    }

    static Matrix getEcl2EquMatrix( Time time, Matrix output )
    {
        output.setRotation( getEcliptic( time ), Coordinates.Axis.X );
        return output;
    }

    static Matrix getEqu2EclMatrix( Time time, Matrix output )
    {
        output.setRotation( -getEcliptic( time ), Coordinates.Axis.X );
        return output;
    }

    public enum Body
    {
        SUN( 0.0, 6.96342e8, 6.96342e8 ),
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
        public final double RADIUS_EQUATORIAL_M;
        public final double RADIUS_MEAN_M;

        Body( double mass, double radiusMean, double radiusEquatorial )
        {
            MASS = mass;
            RADIUS_EQUATORIAL_M = radiusEquatorial;
            RADIUS_MEAN_M = radiusMean;
        }
    }

    private final EnumMap<Body, Ecliptical.Cart> positions = new EnumMap<>( Body.class );
    private final EnumMap<Body, Ecliptical.Cart> velocities = new EnumMap<>( Body.class );
    private final EnumMap<Body, OrbitalModel<Cartesian>> models = new EnumMap<>( Body.class );

    private final LinkedList<Thread> threadList = new LinkedList<>();
    private final Cartesian cartesian = new Cartesian();
    private final EnumMap<Body, Double> distances = new EnumMap<>( Body.class );
    private final List<Body> sortedByDistance = new ArrayList<>( Arrays.asList( Body.values() ) );

    public SolarSystem()
    {
        sortedByDistance.remove( Body.EARTH );

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

    public List<Body> getSortedByDistanceDescending()
    {
        synchronized ( positions )
        {
            return Collections.unmodifiableList( sortedByDistance );
        }
    }

    public Cartesian getHeliocentric( final Body body, final Cartesian output )
    {
        synchronized ( positions )
        {
            output.set( positions.get( body ) );
            if ( models.get( body ).getType() == OrbitalModel.Type.GEOCENTRIC )
                output.add( positions.get( Body.EARTH ) );
        }
        return output;
    }

    /**
     * Calculate ephemerides of objects in the solar system
     *
     * @param body
     * @param ephemerides
     * @return
     */
    public Ephemerides getEphemerides( final Body body, final Ephemerides ephemerides )
    {
        synchronized ( positions )
        {
            switch ( models.get( body ).getType() )
            {
                case HELIOCENTRIC:
                    ephemerides.setHeliocentricPosition( positions.get( body ), positions.get( Body.EARTH ) );
                    ephemerides.setHeliocentricVelocity( velocities.get( body ), velocities.get( Body.EARTH ) );
                    break;
                case GEOCENTRIC:
                    ephemerides.setGeocentricPosition( positions.get( body ), positions.get( Body.EARTH ) );
                    ephemerides.setGeocentricVelocity( velocities.get( body ), velocities.get( Body.EARTH ) );
                    break;
            }
        }
        ephemerides.correctAberration();
        return ephemerides;
    }

    public void compute( final Time time, final Body body )
    {
        final OrbitalModel<Cartesian> model = models.get( body );
        synchronized ( positions )
        {
            model.compute( time, positions.get( body ), velocities.get( body ) );
            positions.get( body ).scale( model.getUnit().toMeters() );
            velocities.get( body ).scale( model.getUnit().toMeters() );
        }
    }

    /**
     * Parallel computation of all the positions and velocities of objects in the solar
     * system at the given time
     *
     * @param time
     */
    public void compute( final Time time )
    {
        threadList.clear();
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
        synchronized ( positions )
        {
            final Cartesian earth = positions.get( Body.EARTH );
            for ( final Body body : Body.values() )
            {
                cartesian.set( positions.get( body ) ).sub( earth );
                distances.put( body, cartesian.length() );
            }
            Collections.sort( sortedByDistance, ( o1, o2 ) -> Double.compare( distances.get( o2 ), distances.get( o1 ) ) );
        }
    }
}
