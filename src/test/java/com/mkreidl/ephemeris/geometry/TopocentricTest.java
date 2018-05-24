package com.mkreidl.ephemeris.geometry;

import com.mkreidl.ephemeris.Distance;
import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.solarsystem.Body;
import com.mkreidl.ephemeris.Position;
import com.mkreidl.ephemeris.solarsystem.SolarSystemVSOP87;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;

import org.junit.Test;

import java.util.EnumMap;

import static org.junit.Assert.assertEquals;


public class TopocentricTest
{
    private final Equatorial.Cart topoCart = new Equatorial.Cart();
    private final Equatorial.Sphe topoSphe = new Equatorial.Sphe();

    private final SolarSystemVSOP87 solarSystem = new SolarSystemVSOP87();
    private final EnumMap<Body, Position> ephemerides = new EnumMap<>( Body.class );
    private final Spherical expected = new Spherical();
    private final Spherical actual = new Spherical();

    private final Spherical geographicLocation = new Spherical(
            Body.EARTH.RADIUS_MEAN_M, 11.5 * Angle.DEG, 48.0 * Angle.DEG
    );
    private final Stereographic camera = new Stereographic( 1.0 );

    private final Circle diskActual = new Circle();
    private final Circle diskExpected = new Circle();

    @Test
    public void test()
    {
        solarSystem.compute( Time.J2000 );

        for ( Body object : Body.values() )
        {
            final Position ephem = new Position();
            ephemerides.put( object, ephem );
            solarSystem.getEphemerides( object, ephem );  // read data into ephem
            ephem.setTimeLocation( Time.J2000, geographicLocation );
        }

        for ( Body object : Body.values() )
        {
            ephemerides.get( object ).get( topoCart, Position.CoordinatesCenter.TOPOCENTRIC );
            topoCart.transform( expected );
            final double radExpected = object.RADIUS_EQUATORIAL_M / topoCart.distance( Distance.m );
            camera.project( expected, radExpected, diskExpected );

            ephemerides.get( object ).get( topoSphe, Position.CoordinatesCenter.TOPOCENTRIC );
            actual.set( topoSphe );
            final double radActual = object.RADIUS_EQUATORIAL_M / topoSphe.distance( Distance.m );
            camera.project( actual, radActual, diskActual );

            assertEquals( radExpected, radActual, 10e-15 );
            assertEquals( expected.dst, actual.dst, 10e-15 );
            assertEquals( expected.lon, actual.lon, 10e-15 );
            assertEquals( expected.lat, actual.lat, 10e-15 );
            assertEquals( diskExpected.x, diskActual.x, 10e-10 );
            assertEquals( diskExpected.y, diskActual.y, 10e-10 );
            assertEquals( diskExpected.r, diskActual.r, 10e-10 );

            System.out.println( "================================" );
            System.out.println( radExpected );
            System.out.println( radActual );
            System.out.println( "================================" );
            System.out.println( expected );
            System.out.println( actual );
            System.out.println( "================================" );
            System.out.println( diskExpected );
            System.out.println( diskActual );
            System.out.println( "================================" );
        }
    }
}
