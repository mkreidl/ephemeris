package com.mkreidl.ephemeris.geometry;

import com.mkreidl.ephemeris.Distance;
import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.astrolabe.Camera;
import com.mkreidl.ephemeris.geometry.Angle;
import com.mkreidl.ephemeris.geometry.Circle;
import com.mkreidl.ephemeris.geometry.Spherical;
import com.mkreidl.ephemeris.sky.Ephemerides;
import com.mkreidl.ephemeris.sky.SolarSystem;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;

import org.junit.Test;

import java.util.EnumMap;

import static org.junit.Assert.assertEquals;


public class TopocentricTest
{
    private final Equatorial.Cart topoCart = new Equatorial.Cart();
    private final Equatorial.Sphe topoSphe = new Equatorial.Sphe();

    private final SolarSystem solarSystem = new SolarSystem();
    private final EnumMap<SolarSystem.Body, Ephemerides> ephemerides = new EnumMap<>( SolarSystem.Body.class );
    private final Spherical expected = new Spherical();
    private final Spherical actual = new Spherical();

    private final Spherical geographicLocation = new Spherical(
            SolarSystem.Body.EARTH.RADIUS_MEAN,
            11.5 * Angle.DEG, 48.0 * Angle.DEG
    );
    private final Camera camera = new Camera( Camera.Direction.SOUTH, Camera.Position.INNER );

    private final Circle diskActual = new Circle();
    private final Circle diskExpected = new Circle();

    @Test
    public void test()
    {
        solarSystem.calculate( Time.J2000 );

        for ( SolarSystem.Body object : SolarSystem.Body.values() )
        {
            final Ephemerides ephem = new Ephemerides();
            ephemerides.put( object, ephem );
            solarSystem.getEphemerides( object, ephem );  // read data into ephem
            ephem.setTimeLocation( Time.J2000, geographicLocation );
        }

        for ( SolarSystem.Body object : SolarSystem.Body.values() )
        {
            ephemerides.get( object ).getTopocentric( topoCart );
            topoCart.transform( expected );
            final double radExpected = object.RADIUS_EQUATORIAL / topoCart.distance( Distance.m );
            camera.project( expected, radExpected, diskExpected );

            ephemerides.get( object ).getTopocentric( topoSphe );
            actual.set( topoSphe );
            final double radActual = object.RADIUS_EQUATORIAL / topoSphe.distance( Distance.m );
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
