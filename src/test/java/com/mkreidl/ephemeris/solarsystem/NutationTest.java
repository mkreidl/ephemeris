package com.mkreidl.ephemeris.solarsystem;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Angle;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class NutationTest
{
    @Test
    public void testNutationMeeusChap22()
    {
        final Calendar calendar = new GregorianCalendar( TimeZone.getTimeZone( "UTC" ) );
        calendar.set( 1987, 3, 10, 0, 0, 0 );
        final Time time = new Time( calendar.getTimeInMillis() );
        final Nutation nutation = new Nutation();
        nutation.compute( time );
        final double longitude = Angle.standardize( nutation.getLongitude() );
        final double obliquity = Angle.standardize( nutation.getObliquity() );
        Assert.assertEquals( -3.788, Math.toDegrees( longitude ) * 3600, 0.02 );
        Assert.assertEquals( 9.443, Math.toDegrees( obliquity ) * 3600, 0.02 );
    }
}
