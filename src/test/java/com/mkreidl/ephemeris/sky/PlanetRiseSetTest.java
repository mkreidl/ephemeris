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
    static final Spherical MUNICH = new Spherical( 1.0, Math.toRadians( 11.576 ), Math.toRadians( 48.137 ) );
    static final Spherical SVOLVAER = new Spherical( 1.0, Math.toRadians( 14.561583 ), Math.toRadians( 68.234764 ) );
    static final Spherical SYDNEY = new Spherical( 1.0, Math.toRadians( 151.2 ), Math.toRadians( -33.85 ) );
    static final Spherical VANCOUVER = new Spherical( 1.0, Math.toRadians( -123.12244 ), Math.toRadians( 49.28098 ) );
    private static final long PRECISION_MS = 15000;
    private final RiseSetCalculator.EventType eventType;
    private final Spherical geographicLocation;
    private final String eventTime;
    private final String startTime;
    private final RiseSetCalculator.LookupDirection searchDirection;

    private final DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm Z" );

    PlanetRiseSetTest( Spherical geographicLocation, String startTime, RiseSetCalculator.LookupDirection searchDirection, RiseSetCalculator.EventType eventType, String eventTime )
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
        final long eventTimeCalculated = calculator.compute( startTimeMs, PRECISION_MS );
        final long eventTimeExpected = dateFormat.parse( eventTime ).getTime();
        assertTrue(
                String.format( "Expected: %s; Actual: %s", dateFormat.format( new Date( eventTimeExpected ) ), dateFormat.format( new Date( eventTimeCalculated + 30000 ) ) ),
                Math.abs( eventTimeCalculated - eventTimeExpected ) < 30000 );
    }
}