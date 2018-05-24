package com.mkreidl.ephemeris.solarsystem;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

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
    public void testModel()
    {
        final AbstractModelVsop87.LBR model;
        switch ( planet )
        {
            case MER:
                model = AbstractModelVsop87.getVersionD( AbstractModelVsop87.MERCURY );
                break;
            case VEN:
                model = AbstractModelVsop87.getVersionD( AbstractModelVsop87.VENUS );
                break;
            case EAR:
                model = AbstractModelVsop87.getVersionD( AbstractModelVsop87.EARTH );
                break;
            case MAR:
                model = AbstractModelVsop87.getVersionD( AbstractModelVsop87.MARS );
                break;
            case JUP:
                model = AbstractModelVsop87.getVersionD( AbstractModelVsop87.JUPITER );
                break;
            case SAT:
                model = AbstractModelVsop87.getVersionD( AbstractModelVsop87.SATURN );
                break;
            case URA:
                model = AbstractModelVsop87.getVersionD( AbstractModelVsop87.URANUS );
                break;
            case NEP:
                model = AbstractModelVsop87.getVersionD( AbstractModelVsop87.NEPTUNE );
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
