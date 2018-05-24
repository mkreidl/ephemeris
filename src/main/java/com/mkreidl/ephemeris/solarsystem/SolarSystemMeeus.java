package com.mkreidl.ephemeris.solarsystem;

import com.mkreidl.ephemeris.Position;
import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Coordinates;
import com.mkreidl.ephemeris.geometry.Matrix;
import com.mkreidl.ephemeris.sky.coordinates.Ecliptical;
import com.mkreidl.ephemeris.solarsystem.meeus.Earth;
import com.mkreidl.ephemeris.solarsystem.meeus.Jupiter;
import com.mkreidl.ephemeris.solarsystem.meeus.Mars;
import com.mkreidl.ephemeris.solarsystem.meeus.Mercury;
import com.mkreidl.ephemeris.solarsystem.meeus.Neptune;
import com.mkreidl.ephemeris.solarsystem.meeus.Saturn;
import com.mkreidl.ephemeris.solarsystem.meeus.Uranus;
import com.mkreidl.ephemeris.solarsystem.meeus.Venus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;


public class SolarSystemMeeus
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

    private final EnumMap<Body, Ecliptical.Cart> positions = new EnumMap<>( Body.class );
    private final EnumMap<Body, Ecliptical.Cart> velocities = new EnumMap<>( Body.class );
    private final EnumMap<Body, OrbitalModel> models = new EnumMap<>( Body.class );

    private final LinkedList<Thread> threadList = new LinkedList<>();
    private final Cartesian cartesian = new Cartesian();
    private final EnumMap<Body, Double> distances = new EnumMap<>( Body.class );
    private final List<Body> sortedByDistance = new ArrayList<>( Arrays.asList( Body.values() ) );

    public SolarSystemMeeus()
    {
        sortedByDistance.remove( Body.EARTH );
        for ( Body body : Body.values() )
        {
            positions.put( body, new Ecliptical.Cart() );
            velocities.put( body, new Ecliptical.Cart() );
        }
        models.put( Body.SUN, new ModelSun() );
        models.put( Body.MERCURY, new Mercury() );
        models.put( Body.VENUS, new Venus() );
        models.put( Body.EARTH, new Earth() );
        models.put( Body.MARS, new Mars() );
        models.put( Body.JUPITER, new Jupiter() );
        models.put( Body.SATURN, new Saturn() );
        models.put( Body.URANUS, new Uranus() );
        models.put( Body.NEPTUNE, new Neptune() );
        models.put( Body.MOON, new ModelMoon() );
        models.put( Body.PLUTO, new ModelPluto() );
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
     * Calculate position of objects in the solar system
     *
     * @param body
     * @param position
     * @return
     */
    public Position getEphemerides( final Body body, final Position position )
    {
        synchronized ( positions )
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
        }
        position.correctAberration();
        return position;
    }

    public void compute( final Time time, final Body body )
    {
        final OrbitalModel model = models.get( body );
        synchronized ( positions )
        {
            model.compute( time, positions.get( body ), velocities.get( body ) );
            positions.get( body ).scale( model.getDistanceUnit().toMeters() );
            velocities.get( body ).scale( model.getDistanceUnit().toMeters() );
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
