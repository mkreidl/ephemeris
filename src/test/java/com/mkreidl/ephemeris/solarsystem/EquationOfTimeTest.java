package com.mkreidl.ephemeris.solarsystem;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Angle;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class EquationOfTimeTest
{
    @Test
    public void testEquationOfTimeMeeusChap28()
    {
        final Calendar calendar = new GregorianCalendar( TimeZone.getTimeZone( "UTC" ) );
        calendar.set( 1992, 9, 13, 0, 0, 0 );
        final Time time = new Time( calendar.getTimeInMillis() );
        final double radians = Angle.standardize( Sun.getEquationOfTime( time ) );
        Assert.assertEquals( 13 + 42.6 / 60, Math.toDegrees( radians ) * 4, 0.02 );
    }
}
