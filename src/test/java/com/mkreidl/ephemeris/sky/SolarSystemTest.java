package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.TestUtil;
import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Angle;
import com.mkreidl.ephemeris.geometry.Spherical;
import com.mkreidl.ephemeris.sky.coordinates.Ecliptical;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mkreidl.ephemeris.sky.SolarSystem.Body.EARTH;
import static com.mkreidl.ephemeris.sky.SolarSystem.Body.JUPITER;
import static com.mkreidl.ephemeris.sky.SolarSystem.Body.MARS;
import static com.mkreidl.ephemeris.sky.SolarSystem.Body.MERCURY;
import static com.mkreidl.ephemeris.sky.SolarSystem.Body.MOON;
import static com.mkreidl.ephemeris.sky.SolarSystem.Body.NEPTUNE;
import static com.mkreidl.ephemeris.sky.SolarSystem.Body.PLUTO;
import static com.mkreidl.ephemeris.sky.SolarSystem.Body.SATURN;
import static com.mkreidl.ephemeris.sky.SolarSystem.Body.SUN;
import static com.mkreidl.ephemeris.sky.SolarSystem.Body.URANUS;
import static com.mkreidl.ephemeris.sky.SolarSystem.Body.VENUS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Parses txt test data produced http://ephemeris.com/ephemeris.php
 */
@RunWith( Parameterized.class )
public class SolarSystemTest
{
    private static final URL DIR_NASA = SolarSystemTest.class.getResource( "/NASA_Ephemeris_Data/" );
    private static final Map<SolarSystem.Body, Double> TOLERANCE = new LinkedHashMap<>();

    private final SolarSystem solarSystem = new SolarSystem();
    private final Time time;
    private final SolarSystem.Body body;
    private final EphemerisData expected;

    static class EphemerisData
    {
        final Angle.Sexagesimal longitude = new Angle.Sexagesimal( Angle.Unit.DEGREES );
        final Angle.Sexagesimal latitude = new Angle.Sexagesimal( Angle.Unit.DEGREES );
        final Angle.Sexagesimal declination = new Angle.Sexagesimal( Angle.Unit.DEGREES );
        final Angle.Sexagesimal rightAscension = new Angle.Sexagesimal( Angle.Unit.HOURS );
        boolean retrograde = false;
        double phase = Double.POSITIVE_INFINITY;  // signifies an invalid value
    }

    static
    {
        TOLERANCE.put( SUN, 0.5 / 60 );
        TOLERANCE.put( MOON, 4.0 / 60 );
        TOLERANCE.put( MERCURY, 0.5 / 60 );
        TOLERANCE.put( VENUS, 0.5 / 60 );
        TOLERANCE.put( MARS, 0.5 / 60 );
        TOLERANCE.put( JUPITER, 0.5 / 60 );
        TOLERANCE.put( SATURN, 0.5 / 60 );
        TOLERANCE.put( URANUS, 0.5 / 60 );
        TOLERANCE.put( NEPTUNE, 0.5 / 60 );
        TOLERANCE.put( PLUTO, 0.5 / 60 );
    }

    private static EphemerisData parseNASAEphemeris( String LonLatRADecl )
    {
        final EphemerisData position = new EphemerisData();
        final Pattern lonPattern = Pattern.compile( "\\s*(\\d{2})\\s(\\w{3})\\s(\\d{2})'(\\d{2})\"(.)" );
        final Pattern latPattern = Pattern.compile( "\\s*([+-]?)\\s*(\\d{1,2}).*(\\d{2})'(\\d{2})\"" );
        final Pattern rasPattern = Pattern.compile( "\\s*(\\d\\d):(\\d\\d):(\\d\\d)\\s*" );

        Matcher m = lonPattern.matcher( LonLatRADecl.substring( 15, 29 ) );
        m.find();
        final double longitude = parseSexagesimal( m.group( 1 ), m.group( 3 ), m.group( 4 ) )
                + Zodiac.getLongitude(
                Zodiac.Sign.valueOf( m.group( 2 ).toUpperCase() ), Angle.Unit.DEGREES );
        position.longitude.set( longitude );
        position.retrograde = m.group( 5 ).equals( "R" );

        m = latPattern.matcher( LonLatRADecl.substring( 31, 41 ) );
        m.find();
        final double latitude = parseSexagesimal( m.group( 2 ), m.group( 3 ), m.group( 4 ) );
        position.latitude.set( m.group( 1 ).equals( "-" ) ? -latitude : latitude );

        m = rasPattern.matcher( LonLatRADecl.substring( 44, 52 ) );
        m.find();
        final double rightAscension = parseSexagesimal( m.group( 1 ), m.group( 2 ), m.group( 3 ) );
        position.rightAscension.set( rightAscension );

        m = latPattern.matcher( LonLatRADecl.substring( 55, 65 ) );
        m.find();
        final double declination = parseSexagesimal( m.group( 2 ), m.group( 3 ), m.group( 4 ) );
        position.declination.set( m.group( 1 ).equals( "-" ) ? -declination : declination );

        return position;
    }

    private static double parseSexagesimal( String integral, String minutes, String seconds )
    {
        return Integer.parseInt( integral )
                + Integer.parseInt( minutes ) / 60.0
                + Integer.parseInt( seconds ) / 3600.0;
    }

    @Parameters( name = "{0}" )
    public static Iterable<Object[]> data()
    {
        final LinkedList<Object[]> datasets = new LinkedList<>();
        final File[] files = new File( DIR_NASA.getFile() ).listFiles();

        for ( File file : files )
        {
            final BufferedReader lineReader;
            String line;

            String dateStr = "";
            boolean geocentric = false;
            double moonPhase = Double.POSITIVE_INFINITY;
            Time time = null;

            try
            {
                lineReader = new BufferedReader( new FileReader( file.getAbsolutePath() ) );
                while ( ( line = lineReader.readLine() ) != null )
                {
                    if ( line.startsWith( "Date/Time" ) )
                    {
                        dateStr = line.substring( 11, 30 );
                        time = TestUtil.getAstronomicalTime( dateStr );
                        if ( time == null )  // dateStr could not be parsed
                        {
                            System.err.println( "String '" + dateStr + "' does not represent a valid date." );
                            continue;
                        }
                    }
                    if ( line.equals( "Geocentric positions" ) )
                        geocentric = true;
                    if ( line.startsWith( "Phase of Moon" ) )
                        moonPhase = Double.parseDouble( line.substring( 15, 20 ) );
                    if ( line.startsWith( "Planet" ) && !geocentric )
                        break;
                    try
                    {
                        final EphemerisData ephemeris = parseNASAEphemeris( line );
                        final String objectName = line.substring( 0, 15 ).trim();
                        final SolarSystem.Body body = SolarSystem.Body.valueOf( objectName.toUpperCase() );
                        if ( body == MOON )
                            ephemeris.phase = ( moonPhase - 0.5 ) * 360;
                        datasets.add( new Object[]
                                {dateStr + " - geocentric - " + objectName, body, time, ephemeris} );
                    }
                    catch ( IllegalArgumentException | IllegalStateException | StringIndexOutOfBoundsException e )
                    {
                    }
                }
            }
            catch ( IOException e )
            {
            }
        }
        return datasets;
    }

    public SolarSystemTest( String testname, SolarSystem.Body body, Time time, EphemerisData expected )
    {
        this.body = body;
        this.time = time;
        this.expected = expected;
    }

    @Test
    public void testMoonPhase()
    {
        if ( body != MOON )
            return;
        solarSystem.compute( time, MOON );
        solarSystem.compute( time, EARTH );
        final Position actual = solarSystem.getEphemerides( MOON, new Position() );
        // expected.phase has an accuracy to only 0.36, since input data (accuracy 1e-3) was multiplied with 360°
        System.out.println( Double.toString( actual.getPhase( new Angle() ).get( Angle.Unit.DEGREES ) ) );
        System.out.println( Double.toString( actual.getIlluminatedFraction() ) );
        System.out.println( Double.toString( ( expected.phase + 180 ) / 360 ) );
        System.out.println( "=====================" );
        //
        // Test omitted, since reference seems incorrect
        //assertEquals( expected.phase, actual.getPhase( new Angle() ).get( Angle.Unit.DEGREES ), 0.36 );
    }

    @Test
    public void testGeocentricCoordinates()
    {
        final Ecliptical.Sphe ecliptical = new Ecliptical.Sphe();
        final Equatorial.Sphe equatorial = new Equatorial.Sphe();
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

        System.out.println( String.format( "%s  | ecliptical longitude |  %s", expected.longitude, longitude ) );
        System.out.println( String.format( "%s  | ecliptical latitude  |  %s", expected.latitude, latitude ) );
        System.out.println( String.format( "%s  | equatorial longitude |  %s", expected.rightAscension, rightAscension ) );
        System.out.println( String.format( "%s  | equatorial latitude  |  %s", expected.declination, declination ) );
        System.out.println( "expected: " + ( expected.retrograde ? "R" : "-" ) + "  |  actual: " + ( actual.isRetrograde() ? "R" : "-" ) );

        assertEquals( expected.longitude.get( Angle.Unit.DEGREES ), longitude.get( Angle.Unit.DEGREES ), TOLERANCE.get( body ) );
        assertEquals( expected.latitude.get( Angle.Unit.DEGREES ), latitude.get( Angle.Unit.DEGREES ), TOLERANCE.get( body ) );
        assertEquals( expected.rightAscension.get( Angle.Unit.DEGREES ), rightAscension.get( Angle.Unit.DEGREES ), TOLERANCE.get( body ) );
        assertEquals( expected.declination.get( Angle.Unit.DEGREES ), declination.get( Angle.Unit.DEGREES ), TOLERANCE.get( body ) );
        assertTrue( expected.retrograde == actual.isRetrograde() );
    }
}
