package com.mkreidl.ephemeris;

import static com.mkreidl.ephemeris.sky.SolarSystem.Body.MOON;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.runners.Parameterized.Parameters;
import com.mkreidl.ephemeris.geometry.Angle;
import com.mkreidl.ephemeris.sky.SolarSystem;
import com.mkreidl.ephemeris.sky.PositionsMotionsTest;
import com.mkreidl.ephemeris.sky.Zodiac;

public class TestUtil
{
    private static final ZoneId UTC = ZoneId.of( "UTC" );

    public static final SimpleDateFormat NASA_DATE_PARSER =
            new SimpleDateFormat( "yyyy.MM.dd HH:mm:ss", Locale.ENGLISH );
    public static final SimpleDateFormat VSOP_DATE_PARSER =
            new SimpleDateFormat( "dd/MM/yyyy HH", Locale.ENGLISH );

    private static final DateTimeFormatter NASA_DATE_FORMATTER
            = DateTimeFormatter.ofPattern( "yyyy.MM.dd HH:mm:ss", Locale.ENGLISH );
    private static final DateTimeFormatter VSOP_DATE_FORMATTER
            = DateTimeFormatter.ofPattern( "dd/MM/yyyy HH'h'", Locale.ENGLISH );
    private static final URL DIR_NASA = PositionsMotionsTest.class.getResource( "/NASA_Ephemeris_Data/" );

    public static class EphemerisData
    {
      public final Angle.Sexagesimal longitude = new Angle.Sexagesimal( Angle.Unit.DEGREES );
      public final Angle.Sexagesimal latitude = new Angle.Sexagesimal( Angle.Unit.DEGREES );
      public final Angle.Sexagesimal declination = new Angle.Sexagesimal( Angle.Unit.DEGREES );
      public final Angle.Sexagesimal rightAscension = new Angle.Sexagesimal( Angle.Unit.HOURS );
      public boolean retrograde = false;
      public double phase = Double.POSITIVE_INFINITY;  // signifies an invalid value
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
    public static Iterable<Object[]> solarSystemData(Collection<SolarSystem.Body> bodies)
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
                        if ( bodies.contains(body) )
                            datasets.add( new Object[]{dateStr + " - geocentric - " + objectName, body, time, ephemeris} );
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

    public static Time getAstronomicalTimeProlepticGregorian( String dateString )
    {
        try
        {
            final LocalDateTime dateTime = LocalDateTime.parse( dateString, NASA_DATE_FORMATTER );
            final ZonedDateTime zonedDateTime = ZonedDateTime.of( dateTime, UTC );
            return new Time( Instant.from( zonedDateTime ).toEpochMilli() );
        }
        catch ( DateTimeParseException e )
        {
        }
        try
        {
            final LocalDateTime dateTime = LocalDateTime.parse( dateString, VSOP_DATE_FORMATTER );
            final ZonedDateTime zonedDateTime = ZonedDateTime.of( dateTime, UTC );
            return new Time( Instant.from( zonedDateTime ).toEpochMilli() );
        }
        catch ( DateTimeParseException e )
        {
        }
        return null;
    }

    public static Time getAstronomicalTime( String dateString )
    {
        try
        {
            return new Time( NASA_DATE_PARSER.parse( dateString ).getTime() );
        }
        catch ( ParseException e )
        {
        }
        try
        {
            return new Time( VSOP_DATE_PARSER.parse( dateString ).getTime() );
        }
        catch ( ParseException e )
        {
        }
        return null;
    }

    static
    {
        NASA_DATE_PARSER.setTimeZone( TimeZone.getTimeZone( "UTC" ) );
        VSOP_DATE_PARSER.setTimeZone( TimeZone.getTimeZone( "UTC" ) );
    }

    private TestUtil() {}
}
