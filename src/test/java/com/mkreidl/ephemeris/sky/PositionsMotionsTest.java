package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.Distance;
import com.mkreidl.ephemeris.TestUtil;
import com.mkreidl.ephemeris.TestUtil.EphemerisData;
import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Angle;
import com.mkreidl.ephemeris.geometry.Spherical;
import com.mkreidl.ephemeris.sky.SolarSystem.Body;
import com.mkreidl.ephemeris.sky.coordinates.Ecliptical;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

import static com.mkreidl.ephemeris.sky.SolarSystem.Body.EARTH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Parses txt test data produced by http://ephemeris.com/ephemeris.php
 */
@RunWith( Parameterized.class )
public class PositionsMotionsTest
{
    private static final double TOL_PLANET = 0.58 * Angle.MIN;
    private static final double TOL_MOON = 4 * Angle.MIN;

    private final SolarSystem solarSystem = new SolarSystem();
    private final Time time;
    private final SolarSystem.Body body;
    private final EphemerisData expected;
    private final Ecliptical.Sphe ecliptical = new Ecliptical.Sphe();
    private final Equatorial.Sphe equatorial = new Equatorial.Sphe();

    @Parameters( name = "{0}" )
    public static Iterable<Object[]> data()
    {
        return TestUtil.solarSystemData( Arrays.asList( SolarSystem.Body.values() ) );
    }

    public PositionsMotionsTest( String testname, SolarSystem.Body body, Time time, EphemerisData expected )
    {
        this.body = body;
        this.time = time;
        this.expected = expected;
    }

    @Test
    public void testGeocentricCoordinates()
    {
        solarSystem.compute( time, body );
        solarSystem.compute( time, EARTH );

        final Position actual = solarSystem.getEphemerides( body, new Position() );
        actual.setTimeLocation( time, new Spherical() );
        actual.get( ecliptical, Position.CoordinatesCenter.GEOCENTRIC );
        actual.get( equatorial, Position.CoordinatesCenter.GEOCENTRIC );

        final Angle longitude = ecliptical.getLongitude( new Angle.Sexagesimal( Angle.Unit.DEGREES ) );
        final Angle latitude = ecliptical.getLatitude( new Angle.Sexagesimal( Angle.Unit.DEGREES ) );
        final Angle rightAscension = equatorial.getRightAscension( new Angle.Sexagesimal( Angle.Unit.HOURS ) );
        final Angle declination = equatorial.getDeclination( new Angle.Sexagesimal( Angle.Unit.DEGREES ) );

        System.out.println( "-------------------------------------" );
        System.out.println( time + " -- " + body );
        System.out.println( time.julianDayNumber() );
        System.out.println( "-------------------------------------" );

        System.out.println( "Distance: " + equatorial.distance( Distance.AU ) );
        System.out.println( String.format( "%s  | ecliptical longitude |  %s", expected.longitude, longitude ) );
        System.out.println( String.format( "%s  | ecliptical latitude  |  %s", expected.latitude, latitude ) );
        System.out.println( String.format( "%s  | equatorial longitude |  %s", expected.rightAscension, rightAscension ) );
        System.out.println( String.format( "%s  | equatorial latitude  |  %s", expected.declination, declination ) );
        System.out.println( "expected: " + ( expected.retrograde ? "R" : "-" ) + "  |  actual: " + ( actual.isRetrograde() ? "R" : "-" ) );

        final double tol = body == Body.MOON ? TOL_MOON : TOL_PLANET;

        assertEquals( expected.longitude.getRadians(), longitude.getRadians(), tol );
        assertEquals( expected.latitude.getRadians(), latitude.getRadians(), tol );
        assertEquals( expected.rightAscension.getRadians(), rightAscension.getRadians(), tol );
        assertEquals( expected.declination.getRadians(), declination.getRadians(), tol );
        assertTrue( expected.retrograde == actual.isRetrograde() );
    }
}
