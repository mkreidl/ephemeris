package com.mkreidl.ephemeris.dynamics;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.mkreidl.ephemeris.dynamics.VSOP87.Model;
import com.mkreidl.ephemeris.geometry.Spherical;
import com.mkreidl.ephemeris.geometry.VSOP87File;

@RunWith(Parameterized.class)
public class VSOP87DTest extends VSOP87Test
{
    @Parameters(name = "{0} -- {1}")
    public static Iterable<Object[]> data()
    {
        return data(VSOP87File.Version.D);
    }

    private final Spherical expectedPos = new Spherical();
    private final Spherical actualPos = new Spherical();
    private final Spherical expectedVel = new Spherical();
    private final Spherical actualVel = new Spherical();

    public VSOP87DTest( VSOP87File.Planet planet, String timeStr, DataSet dataSet )
    {
        super(planet, timeStr, dataSet);
        expectedPos.set( dataSet.coordinates[2], dataSet.coordinates[0], dataSet.coordinates[1] );
        expectedVel.set( dataSet.coordinates[5], dataSet.coordinates[3], dataSet.coordinates[4] );
    }

    @Test
    public void testJulianDate()
    {
        Assert.assertEquals( julianDate, time.julianDayNumber(), 1e-15 );
    }

    @Test
    public void testModel()
    {
        final Model.LBR model;
        switch ( planet )
        {
            case MER:
                model = Model.getVersionD( Model.MERCURY );
                break;
            case VEN:
                model = Model.getVersionD( Model.VENUS );
                break;
            case EAR:
                model = Model.getVersionD( Model.EARTH );
                break;
            case MAR:
                model = Model.getVersionD( Model.MARS );
                break;
            case JUP:
                model = Model.getVersionD( Model.JUPITER );
                break;
            case SAT:
                model = Model.getVersionD( Model.SATURN );
                break;
            case URA:
                model = Model.getVersionD( Model.URANUS );
                break;
            case NEP:
                model = Model.getVersionD( Model.NEPTUNE );
                break;
            default:
                model = null;
        }
        model.compute( time, actualPos, actualVel );
        actualVel.dst *= 86400;  // Reference values from VSOP test files are given in [dist] per DAY
        actualVel.lon *= 86400;  // Reference values from VSOP test files are given in [dist] per DAY
        actualVel.lat *= 86400;  // Reference values from VSOP test files are given in [dist] per DAY
        assertEquals( expectedPos.dst, actualPos.dst, 1e-10 );
        assertEquals( expectedPos.lon, actualPos.lon, 1e-09 );
        assertEquals( expectedPos.lat, actualPos.lat, 1e-10 );
        assertEquals( expectedVel.dst, actualVel.dst, 1e-8 );
        assertEquals( expectedVel.lon, actualVel.lon, 1e-8 );
        assertEquals( expectedVel.lat, actualVel.lat, 1e-8 );
    }
}
