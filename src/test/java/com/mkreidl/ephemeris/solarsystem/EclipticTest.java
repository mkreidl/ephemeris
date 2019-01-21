package com.mkreidl.ephemeris.solarsystem;

import com.mkreidl.ephemeris.Time;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.mkreidl.ephemeris.geometry.Angle.DEG;
import static org.junit.Assert.assertEquals;

@RunWith( Parameterized.class )
public class EclipticTest
{
    private final Time julianDate = new Time();
    private double ecliptic, tolerance;

    public EclipticTest( int year, double ecliptic, double tolerance )
    {
        final GregorianCalendar cal = new GregorianCalendar( year, Calendar.JANUARY, 1, 12, 0, 0 );
        julianDate.setTime( cal.getTime().getTime() );
        this.ecliptic = ecliptic;
        this.tolerance = tolerance;
    }

    @Parameters
    public static Iterable<Object[]> data()
    {
        return Arrays.asList( new Object[][]{
                {-3000, 24.0266666667, 10e-3},
                {0, 23.695, 1e-3},
                {2000, 23.44, 1e-3},
                {2016, 23.43721, 0.1 / 3600,},
                {3000, 23.31, 1e-3},
        } );
    }

    @Test
    public void testEcliptic()
    {
        assertEquals( this.ecliptic * DEG, Ecliptic.getObliquity( julianDate ), this.tolerance );
    }

}
