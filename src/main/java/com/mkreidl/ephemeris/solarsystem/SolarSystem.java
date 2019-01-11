package com.mkreidl.ephemeris.solarsystem;

import com.mkreidl.ephemeris.Position;
import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Coordinates;
import com.mkreidl.ephemeris.geometry.Matrix3x3;
import com.mkreidl.ephemeris.sky.coordinates.Ecliptical;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;
import com.mkreidl.ephemeris.sky.coordinates.Horizontal;

import java.util.EnumMap;
import java.util.Map;

public abstract class SolarSystem
{
    protected final Map<Body, Ecliptical.Cart> positions = new EnumMap<>( Body.class );
    protected final Map<Body, Ecliptical.Cart> velocities = new EnumMap<>( Body.class );
    final Map<Body, OrbitalModel> models = new EnumMap<>( Body.class );
    private final Map<Body, Position> planetsEphemerides = new EnumMap<>( Body.class );
    private final Cartesian cartesian = new Cartesian();
    private final EnumMap<Body, Double> geocentricDistances = new EnumMap<>( Body.class );

    SolarSystem()
    {
        for ( Body body : Body.values() )
        {
            planetsEphemerides.put( body, new Position() );
            positions.put( body, new Ecliptical.Cart() );
            velocities.put( body, new Ecliptical.Cart() );
        }
    }

    public static void computeEclJ2000ToEquToDate( Time time, Matrix3x3 transformation )
    {
        PrecessionMatrix.compute( time, transformation );
        transformation.postRotateX( SolarSystemVSOP87C.getEcliptic( time ) );
    }

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

    static Matrix3x3 getEcl2EquMatrix( Time time, Matrix3x3 output )
    {
        output.setRotation( getEcliptic( time ), Coordinates.Axis.X );
        return output;
    }

    public static Matrix3x3 getEqu2EclMatrix( Time time, Matrix3x3 output )
    {
        output.setRotation( -getEcliptic( time ), Coordinates.Axis.X );
        return output;
    }

    public Cartesian getHeliocentric( final Body body, final Cartesian output )
    {
        output.set( positions.get( body ) );
        if ( models.get( body ).getType() == OrbitalModel.Type.GEOCENTRIC )
            output.add( positions.get( Body.EARTH ) );
        return output;
    }

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

    public double getGeocentricDistance( Body body )
    {
        return geocentricDistances.get( body );
    }

    public void compute( final Time time, final Body body )
    {
        final OrbitalModel model = models.get( body );
        model.compute( time, positions.get( body ), velocities.get( body ) );
        positions.get( body ).scale( model.getDistanceUnit().toMeters() );
        velocities.get( body ).scale( model.getDistanceUnit().toMeters() );
    }

    public void compute( final Time time )
    {
        final Cartesian earth = positions.get( Body.EARTH );
        for ( final Body body : Body.values() )
        {
            final OrbitalModel model = models.get( body );
            model.compute( time, positions.get( body ), velocities.get( body ) );
            positions.get( body ).scale( model.getDistanceUnit().toMeters() );
            velocities.get( body ).scale( model.getDistanceUnit().toMeters() );
        }
        for ( final Body body : Body.values() )
        {
            getEphemerides( body, planetsEphemerides.get( body ) );
            cartesian.set( positions.get( body ) ).sub( earth );
            geocentricDistances.put( body, cartesian.length() );
        }
    }

    public void setTimeLocation( final Time time, double longitudeRad, double latitudeRad )
    {
        final double localSiderealTimeRad = time.getMeanSiderealTimeRadians() + longitudeRad;
        final double currentEclipticRad = SolarSystem.getEcliptic( time );
        setTimeLocation( currentEclipticRad, localSiderealTimeRad, latitudeRad );
    }

    public void setTimeLocation( double currentEclipticRadians, double localSiderealTimeRadians, double latitudeRadians )
    {
        for ( final Body body : Body.values() )
            planetsEphemerides.get( body ).setTimeLocation( currentEclipticRadians, localSiderealTimeRadians, latitudeRadians );
    }

    public void getPosition( Body body, Position.CoordinatesCenter center, Ecliptical.Cart out )
    {
        planetsEphemerides.get( body ).get( out, center );
    }

    public void getPosition( Body body, Position.CoordinatesCenter center, Equatorial.Cart out )
    {
        planetsEphemerides.get( body ).get( out, center );
    }

    public void getPosition( Body body, Position.CoordinatesCenter center, Horizontal.Cart out )
    {
        planetsEphemerides.get( body ).get( out, center );
    }
}
