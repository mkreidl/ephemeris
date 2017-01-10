package com.mkreidl.ephemeris.sky;

import static org.junit.Assert.*;

import static com.mkreidl.ephemeris.sky.SolarSystem.Body.*;

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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Angle;
import com.mkreidl.ephemeris.geometry.Spherical;
import com.mkreidl.ephemeris.sky.coordinates.Ecliptical;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;

@RunWith(Parameterized.class)
public class GeocentricCoordinatesTest
{
    private static final URL DIR_NASA = GeocentricCoordinatesTest.class.getResource( "/NASA_Ephemeris_Data/" );
    private static final Map<SolarSystem.Body, Double> TOLERANCE = new LinkedHashMap<>();

    private final SolarSystem solarSystem = new SolarSystem();
    private final Time time;
    private final SolarSystem.Body body;
    private final NasaData expected;

    static class NasaData
    {
        Angle longitude = new Angle();
        Angle latitude = new Angle();
        Angle declination = new Angle();
        Angle rightAscension = new Angle();
    }

    static
    {
        TOLERANCE.put( SUN, 15.0 / 3600 );
        TOLERANCE.put( MOON, 120.0 / 3600 );
        TOLERANCE.put( MERCURY, 25.0 / 3600 );
        TOLERANCE.put( VENUS, 15.0 / 3600 );
        TOLERANCE.put( MARS, 25.0 / 3600 );
        TOLERANCE.put( JUPITER, 10.0 / 3600 );
        TOLERANCE.put( SATURN, 15.0 / 3600 );
        TOLERANCE.put( URANUS, 10.0 / 3600 );
        TOLERANCE.put( NEPTUNE, 5.0 / 3600 );
    }

    private static NasaData parseNASAEphemeris( String LonLatRADecl )
    {
        final NasaData position = new NasaData();
        Matcher m;

        Pattern lonPattern = Pattern.compile( "\\s*(\\d{2})\\s(\\w{3})\\s(\\d{2})'(\\d{2})\"" );
        Pattern latPattern = Pattern.compile( "\\s*([+-]?)\\s*(\\d{1,2}).*(\\d{2})'(\\d{2})\"" );
        Pattern rasPattern = Pattern.compile( "\\s*(\\d\\d):(\\d\\d):(\\d\\d)\\s*" );

        m = lonPattern.matcher( LonLatRADecl.substring( 15, 28 ) );
        m.find();
        double longitude = Double.parseDouble( m.group( 1 ) )
                + ( Double.parseDouble( m.group( 3 ) ) + Double.parseDouble( m.group( 4 ) ) / 60.0 ) / 60.0;
        longitude += Zodiac.getLongitude(
                Zodiac.Sign.valueOf( m.group( 2 ).toUpperCase() ), Angle.Unit.DEGREES
        );
        position.longitude.set( longitude, Angle.Unit.DEGREES );

        m = latPattern.matcher( LonLatRADecl.substring( 31, 41 ) );
        m.find();
        final double latitude = Double.parseDouble( m.group( 2 ) )
                + ( Double.parseDouble( m.group( 3 ) ) + Double.parseDouble( m.group( 4 ) ) / 60.0 ) / 60.0;
        if ( m.group( 1 ).equals( "-" ) )
            position.latitude.set( -latitude, Angle.Unit.DEGREES );
        else
            position.latitude.set( latitude, Angle.Unit.DEGREES );

        m = rasPattern.matcher( LonLatRADecl.substring( 44, 52 ) );
        m.find();
        final double rightAscension = Double.parseDouble( m.group( 1 ) )
                + ( Double.parseDouble( m.group( 2 ) ) + Double.parseDouble( m.group( 3 ) ) / 60.0 ) / 60.0;
        position.rightAscension.set( rightAscension, Angle.Unit.HOURS );

        m = latPattern.matcher( LonLatRADecl.substring( 55, 65 ) );
        m.find();
        final double declination = Double.parseDouble( m.group( 2 ) )
                + ( Double.parseDouble( m.group( 3 ) ) + Double.parseDouble( m.group( 4 ) ) / 60.0 ) / 60.0;
        if ( m.group( 1 ).equals( "-" ) )
            position.declination.set( -declination, Angle.Unit.DEGREES );
        else
            position.declination.set( declination, Angle.Unit.DEGREES );

        return position;
    }

    @Parameters(name = "{0}")
    public static Iterable<Object[]> data()
    {
        final LinkedList<Object[]> datasets = new LinkedList<>();
        final File[] files = new File( DIR_NASA.getFile() ).listFiles();

        for ( File file : files )
        {
            final BufferedReader lineReader;
            String line;
            String dateStr = "";
            NasaData position;
            Time time = null;

            boolean geocentric = false;
            try
            {
                lineReader = new BufferedReader( new FileReader( file.getAbsolutePath() ) );
                while ( ( line = lineReader.readLine() ) != null )
                {
                    if ( line.startsWith( "Date/Time" ) )
                    {
                        dateStr = line.substring( 11, 30 );
                        time = Time.getAstronomicalTime( dateStr );
                        if ( time == null )  // dateStr could not be parsed
                        {
                            System.err.println( "String '" + dateStr + "' does not represent a valid date." );
                            continue;
                        }
                    }
                    if ( line.equals( "Geocentric positions" ) )
                        geocentric = true;
                    if ( line.startsWith( "Planet" ) && !geocentric )
                        break;
                    try
                    {
                        position = parseNASAEphemeris( line );
                        final String objectName = line.substring( 0, 15 ).trim();
                        datasets.add( new Object[]
                                {
                                        dateStr + " - geocentric - " + objectName,
                                        SolarSystem.Body.valueOf( objectName.toUpperCase() ),
                                        time,
                                        position
                                } );
                    }
                    catch ( IllegalArgumentException | IllegalStateException  | StringIndexOutOfBoundsException e )
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

    public GeocentricCoordinatesTest( String testname, SolarSystem.Body body, Time time, NasaData expected )
    {
        this.body = body;
        this.time = time;
        this.expected = expected;
    }

    @Test
    public void testGeocentricCoordinates()
    {
        final Ecliptical.Sphe ecliptical = new Ecliptical.Sphe();
        final Equatorial.Sphe equatorial = new Equatorial.Sphe();
        final Ephemerides actual = solarSystem.calculate( time, body, new Ephemerides() );
        actual.setTimeLocation( time, new Spherical() );

        actual.getGeocentric( ecliptical );
        actual.getGeocentric( equatorial );

        final Angle longitude = ecliptical.getLongitude( new Angle() );
        final Angle latitude = ecliptical.getLatitude( new Angle() );
        final Angle rightAscension = equatorial.getRightAscension( new Angle() );
        final Angle declination = equatorial.getDeclination( new Angle() );

        System.out.println( "-------------------------------------" );
        System.out.println( time.toString() + " -- " + body.toString() );
        System.out.println( time.julianDayNumber() );
        System.out.println( "-------------------------------------" );

        System.out.println( expected.longitude.toStringAs( Angle.Unit.DEGREES )
                + "  | ecliptical longitude |  "
                + longitude.toStringAs( Angle.Unit.DEGREES ) );
        System.out.println( expected.latitude.toStringAs( Angle.Unit.DEGREES )
                + "  | ecliptical latitude  |  "
                + latitude.toStringAs( Angle.Unit.DEGREES ) );
        System.out.println( expected.rightAscension.toStringAs( Angle.Unit.HOURS )
                + "  | equatorial longitude | "
                + rightAscension.toStringAs( Angle.Unit.HOURS ) );
        System.out.println( expected.declination.toStringAs( Angle.Unit.DEGREES )
                + "  | equatorial latitude  | "
                + declination.toStringAs( Angle.Unit.DEGREES ) );

        assertEquals( expected.longitude.get(), longitude.get(), TOLERANCE.get( body ) );
        assertEquals( expected.latitude.get(), latitude.get(), TOLERANCE.get( body ) );
        assertEquals( expected.rightAscension.get(), rightAscension.get(), TOLERANCE.get( body ) );
        assertEquals( expected.declination.get(), declination.get(), TOLERANCE.get( body ) );
    }
}
