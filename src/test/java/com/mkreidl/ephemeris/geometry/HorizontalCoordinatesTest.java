package com.mkreidl.ephemeris.geometry;

import com.mkreidl.ephemeris.TestUtil;
import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;
import com.mkreidl.ephemeris.sky.coordinates.Horizontal;
import com.mkreidl.ephemeris.solarsystem.Body;
import com.mkreidl.ephemeris.Position;
import com.mkreidl.ephemeris.solarsystem.SolarSystemVSOP87C;

import org.junit.Before;
import org.junit.Test;

import java.util.EnumMap;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static org.junit.Assert.assertEquals;

public class HorizontalCoordinatesTest
{
    private final Angle angle = new Angle();

    private final Equatorial.Cart cartesianNorth = new Equatorial.Cart();
    private final Equatorial.Sphe sphericalNorth = new Equatorial.Sphe();
    private final Equatorial.Cart cartesianEast = new Equatorial.Cart();
    private final Equatorial.Sphe sphericalEast = new Equatorial.Sphe();
    private final Horizontal.Cart cartesian = new Horizontal.Cart();
    private final Horizontal.Sphe spherical = new Horizontal.Sphe();

    private final Spherical geographicLocation = new Spherical(
            Body.EARTH.RADIUS_MEAN_M,
            11.5820 * Angle.DEG, 48.1351 * Angle.DEG
    );

    private final Time time = TestUtil.getAstronomicalTime( "2016.11.10 08:00:00" );
    private final SolarSystemVSOP87C solarSystem = new SolarSystemVSOP87C();

    private final EnumMap<Body, Angle> height = new EnumMap<>( Body.class );
    private final EnumMap<Body, Angle> azimuth = new EnumMap<>( Body.class );
    private final Equatorial.Cart locationCart = new Equatorial.Cart();
    private final Equatorial.Sphe locationSphe = new Equatorial.Sphe();

    {
        //height.put( SolarSystem.Body.MOON, new Angle( -46.5, Angle.Unit.DEGREES ) );
        azimuth.put( Body.MOON, new Angle( 15.6, Angle.Unit.DEGREES ) );

        height.put( Body.SUN, new Angle( 13.5, Angle.Unit.DEGREES ) );
        azimuth.put( Body.SUN, new Angle( 136.6, Angle.Unit.DEGREES ) );

        height.put( Body.MERCURY, new Angle( 7.1, Angle.Unit.DEGREES ) );
        azimuth.put( Body.MERCURY, new Angle( 131.5, Angle.Unit.DEGREES ) );

        height.put( Body.VENUS, new Angle( -16.2, Angle.Unit.DEGREES ) );
        azimuth.put( Body.VENUS, new Angle( 110.3, Angle.Unit.DEGREES ) );

        height.put( Body.MARS, new Angle( -37.1, Angle.Unit.DEGREES ) );
        azimuth.put( Body.MARS, new Angle( 81.4, Angle.Unit.DEGREES ) );

        height.put( Body.JUPITER, new Angle( 36.9, Angle.Unit.DEGREES ) );
        azimuth.put( Body.JUPITER, new Angle( 166.3, Angle.Unit.DEGREES ) );

        height.put( Body.SATURN, new Angle( -5.0, Angle.Unit.DEGREES ) );
        azimuth.put( Body.SATURN, new Angle( 116.7, Angle.Unit.DEGREES ) );

        height.put( Body.URANUS, new Angle( -31.7, Angle.Unit.DEGREES ) );
        azimuth.put( Body.URANUS, new Angle( 338.2, Angle.Unit.DEGREES ) );

        height.put( Body.NEPTUNE, new Angle( -47.2, Angle.Unit.DEGREES ) );
        azimuth.put( Body.NEPTUNE, new Angle( 30.3, Angle.Unit.DEGREES ) );
    }

    @Test
    public void testPlanets()
    {
        solarSystem.compute( time );

        final Horizontal.Sphe horizontal = new Horizontal.Sphe();

        for ( Body planet : height.keySet() )
        {
            final double azimuthExpected = azimuth.get( planet ).get( Angle.Unit.DEGREES );
            final double heightExpected = height.get( planet ).get( Angle.Unit.DEGREES );
            final Position position = solarSystem.getEphemerides( planet, new Position() );

            position.setTimeLocation( time, geographicLocation );
            position.get( horizontal, Position.CoordinatesCenter.GEOCENTRIC );

            System.out.println( "===================" );
            System.out.println( planet.toString() + " -- height" );
            System.out.println( heightExpected );
            System.out.println( horizontal.getHeight( angle ).get( Angle.Unit.DEGREES ) );
            System.out.println( planet.toString() + " -- azimuth" );
            System.out.println( azimuthExpected );
            System.out.println( horizontal.getAzimuth( angle ).get( Angle.Unit.DEGREES ) );
            assertEquals( azimuthExpected, horizontal.getAzimuth( angle ).get( Angle.Unit.DEGREES ), 0.1 );
            assertEquals( heightExpected, horizontal.getHeight( angle ).get( Angle.Unit.DEGREES ), 0.1 );
        }
    }

    @Before
    public void initializeZenit()
    {
        locationSphe.set( geographicLocation );
        locationSphe.transform( locationCart );
    }

    @Test
    public void testZenit()
    {
        System.out.println( "================================" );
        System.out.println( locationCart );
        System.out.println( locationSphe );
        locationCart.toHorizontal( locationSphe, cartesian );
        locationSphe.toHorizontal( locationSphe, spherical );
        System.out.println( cartesian );
        System.out.println( spherical );
        assertEquals( cartesian.z, Body.EARTH.RADIUS_MEAN_M, 1e-16 );
        assertEquals( spherical.getHeight( angle ).get( Angle.Unit.DEGREES ), 90, 1e-16 );
        System.out.println( "================================" );
    }

    @Test
    public void testNorth()
    {
        final double lon = geographicLocation.lon;
        final double lat = geographicLocation.lat;

        sphericalNorth.set( 1.0, lat < 0 ? lon : lon + PI, PI / 2 - abs( lat ) );
        sphericalNorth.transform( cartesianNorth );
        sphericalNorth.transform( cartesianNorth );

        System.out.println( "================================" );
        System.out.println( cartesianNorth );
        System.out.println( sphericalNorth );
        cartesianNorth.toHorizontal( locationSphe, cartesian );
        sphericalNorth.toHorizontal( locationSphe, spherical );
        System.out.println( cartesian );
        System.out.println( spherical );
        assertEquals( cartesian.x, -1.0, 1e-15 );
        assertEquals( spherical.getAzimuth( angle ).get( Angle.Unit.DEGREES ), 0, 1e-15 );
        System.out.println( "================================" );
    }

    @Test
    public void testEast()
    {
        locationSphe.set( geographicLocation );
        locationSphe.transform( locationCart );

        sphericalEast.set( 1.0, geographicLocation.lon + PI / 2, 0.0 );
        sphericalEast.transform( cartesianEast );

        System.out.println( "================================" );
        System.out.println( cartesianEast );
        System.out.println( sphericalEast );
        cartesianEast.toHorizontal( locationSphe, cartesian );
        sphericalEast.toHorizontal( locationSphe, spherical );
        System.out.println( cartesian );
        System.out.println( spherical );
        assertEquals( cartesian.y, 1.0, 1e-16 );
        assertEquals( spherical.getAzimuth( angle ).get( Angle.Unit.DEGREES ), 90, 1e-16 );
        System.out.println( "================================" );
    }
}