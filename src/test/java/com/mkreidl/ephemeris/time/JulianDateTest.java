package com.mkreidl.ephemeris.time;

import com.mkreidl.ephemeris.solarsystem.Vsop87AbstractTest;
import com.mkreidl.ephemeris.geometry.VSOP87File;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith( Parameterized.class )
public class JulianDateTest extends Vsop87AbstractTest
{
    @Parameterized.Parameters( name = "{0} -- {1}" )
    public static Iterable<Object[]> data()
    {
        return data( VSOP87File.Version.C );
    }

    public JulianDateTest( VSOP87File.Planet planet, String timeStr, DataSet dataSet )
    {
        super( planet, timeStr, dataSet );
    }

    @Test
    public void testJulianDate()
    {
        Assert.assertEquals( julianDate, time.julianDayNumber(), 1e-15 );
    }
}
