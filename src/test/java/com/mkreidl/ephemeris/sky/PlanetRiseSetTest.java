package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.geometry.Spherical;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertTrue;

public abstract class PlanetRiseSetTest
{
  static final Spherical MUNICH = new Spherical( 1.0, Math.toRadians( 11.58198 ), Math.toRadians( 48.13513 ) );
    static final Spherical SVOLVAER = new Spherical( 1.0, Math.toRadians( 14.561583 ), Math.toRadians( 68.234764 ) );
    static final Spherical SYDNEY = new Spherical( 1.0, Math.toRadians( 151.2 ), Math.toRadians( -33.85 ) );
    static final Spherical VANCOUVER = new Spherical( 1.0, Math.toRadians( -123.12244 ), Math.toRadians( 49.28098 ) );
    static final Spherical FAR_NORTH = new Spherical( 1.0, Math.toRadians( 11.498888 ), Math.toRadians( 77.170555 ) ); // 11d29m56s 77d10m14s

    private static final long PRECISION_MS = 30000;
    private final RiseSetCalculator.EventType eventType;
    private final Spherical geographicLocation;
    private final String eventTime;
    private final String startTime;
    private final RiseSetCalculator.LookupDirection searchDirection;

    private final DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm Z" );
    private final DateFormat dateFormatSec = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss Z" );

    PlanetRiseSetTest(Spherical geographicLocation, String startTime, RiseSetCalculator.LookupDirection searchDirection, RiseSetCalculator.EventType eventType, String eventTime )
    {
        this.geographicLocation = geographicLocation;
        this.startTime = startTime;
        this.eventType = eventType;
        this.eventTime = eventTime;
        this.searchDirection = searchDirection;
    }

    protected abstract RiseSetCalculator getCalculator();

    @Test
    public void test() throws ParseException
    {
        final RiseSetCalculator calculator = getCalculator();
        final long startTimeMs = dateFormat.parse( startTime ).getTime();
        calculator.setGeographicLocation( geographicLocation );
        calculator.setEventType( eventType );
        calculator.setSearchDirection( searchDirection );
        calculator.compute( startTimeMs );
        final long eventTimeCalculated = calculator.getTime();
        final long eventTimeExpected = dateFormat.parse( eventTime ).getTime();
        assertTrue(
                String.format( "Expected: %s; Actual: %s", dateFormatSec.format( new Date( eventTimeExpected ) ),
                        dateFormatSec.format( new Date( eventTimeCalculated ) ) ),
                Math.abs( eventTimeCalculated - eventTimeExpected ) < PRECISION_MS );
    }
}