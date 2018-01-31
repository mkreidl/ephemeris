package com.mkreidl.ephemeris;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class TestUtil
{
    private static final ZoneId UTC = ZoneId.of( "UTC" );

    private static final Calendar calUT0 = new GregorianCalendar( TimeZone.getTimeZone( "UTC" ) );

    private static final SimpleDateFormat NASA_DATE_PARSER =
            new SimpleDateFormat( "yyyy.MM.dd HH:mm:ss", Locale.ENGLISH );
    private static final SimpleDateFormat VSOP_DATE_PARSER =
            new SimpleDateFormat( "dd/MM/yyyy HH", Locale.ENGLISH );

    private static final DateTimeFormatter NASA_DATE_FORMATTER
            = DateTimeFormatter.ofPattern( "yyyy.MM.dd HH:mm:ss", Locale.ENGLISH );
    private static final DateTimeFormatter VSOP_DATE_FORMATTER
            = DateTimeFormatter.ofPattern( "dd/MM/yyyy HH'h'", Locale.ENGLISH );

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
