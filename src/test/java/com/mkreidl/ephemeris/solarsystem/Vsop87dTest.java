package com.mkreidl.ephemeris.solarsystem;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Spherical;
import com.mkreidl.ephemeris.geometry.VSOP87File;
import com.mkreidl.ephemeris.solarsystem.vsop87d.Earth;
import com.mkreidl.ephemeris.solarsystem.vsop87d.Jupiter;
import com.mkreidl.ephemeris.solarsystem.vsop87d.Mars;
import com.mkreidl.ephemeris.solarsystem.vsop87d.Mercury;
import com.mkreidl.ephemeris.solarsystem.vsop87d.Neptune;
import com.mkreidl.ephemeris.solarsystem.vsop87d.Saturn;
import com.mkreidl.ephemeris.solarsystem.vsop87d.Uranus;
import com.mkreidl.ephemeris.solarsystem.vsop87d.Venus;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertEquals;

@RunWith( Parameterized.class )
public class Vsop87dTest extends Vsop87AbstractTest
{
    @Parameters( name = "{0} -- {1}" )
    public static Iterable<Object[]> data()
    {
        return data( VSOP87File.Version.D );
    }

    private final Spherical expectedPos = new Spherical();
    private final Spherical actualPos = new Spherical();
    private final Spherical expectedVel = new Spherical();
    private final Spherical actualVel = new Spherical();

    public Vsop87dTest( VSOP87File.Planet planet, String timeStr, DataSet dataSet )
    {
        super( planet, timeStr, dataSet );
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
                model = new Mercury();
                break;
            case VEN:
                model = new Venus();
                break;
            case EAR:
                model = new Earth();
                break;
            case MAR:
                model = new Mars();
                break;
            case JUP:
                model = new Jupiter();
                break;
            case SAT:
                model = new Saturn();
                break;
            case URA:
                model = new Uranus();
                break;
            case NEP:
                model = new Neptune();
                break;
            default:
                throw new IllegalArgumentException( "Planet not found" );
        }
        model.compute( time, actualPos, actualVel );
        actualVel.dst *= Time.SECONDS_PER_DAY;  // Reference values from VSOP test files are given in [dist] per DAY
        actualVel.lon *= Time.SECONDS_PER_DAY;  // Reference values from VSOP test files are given in [dist] per DAY
        actualVel.lat *= Time.SECONDS_PER_DAY;  // Reference values from VSOP test files are given in [dist] per DAY
        assertEquals( expectedPos.dst, actualPos.dst, 1e-10 );
        assertEquals( expectedPos.lon, actualPos.lon, 1e-09 );
        assertEquals( expectedPos.lat, actualPos.lat, 1e-10 );
        assertEquals( expectedVel.dst, actualVel.dst, 1e-8 );
        assertEquals( expectedVel.lon, actualVel.lon, 1e-8 );
        assertEquals( expectedVel.lat, actualVel.lat, 1e-8 );
    }
}
