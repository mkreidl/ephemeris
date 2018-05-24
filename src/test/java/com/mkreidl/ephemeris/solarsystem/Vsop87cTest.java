package com.mkreidl.ephemeris.solarsystem;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.VSOP87File;
import com.mkreidl.ephemeris.solarsystem.vsop87c.Earth;
import com.mkreidl.ephemeris.solarsystem.vsop87c.Jupiter;
import com.mkreidl.ephemeris.solarsystem.vsop87c.Mars;
import com.mkreidl.ephemeris.solarsystem.vsop87c.Mercury;
import com.mkreidl.ephemeris.solarsystem.vsop87c.Neptune;
import com.mkreidl.ephemeris.solarsystem.vsop87c.Saturn;
import com.mkreidl.ephemeris.solarsystem.vsop87c.Uranus;
import com.mkreidl.ephemeris.solarsystem.vsop87c.Venus;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertEquals;

@RunWith( Parameterized.class )
public class Vsop87cTest extends Vsop87AbstractTest
{
    @Parameters( name = "{0} -- {1}" )
    public static Iterable<Object[]> data()
    {
        return data( VSOP87File.Version.C );
    }

    private final Cartesian expectedPos = new Cartesian();
    private final Cartesian actualPos = new Cartesian();
    private final Cartesian expectedVel = new Cartesian();
    private final Cartesian actualVel = new Cartesian();

    public Vsop87cTest( VSOP87File.Planet planet, String timeStr, DataSet dataSet )
    {
        super( planet, timeStr, dataSet );
        expectedPos.set( dataSet.coordinates[0], dataSet.coordinates[1], dataSet.coordinates[2] );
        expectedVel.set( dataSet.coordinates[3], dataSet.coordinates[4], dataSet.coordinates[5] );
    }

    @Test
    public void testModel()
    {
        final AbstractModelVsop87.XYZ model;
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
        actualVel.scale( Time.SECONDS_PER_DAY );  // Reference values from VSOP test files are given in [dist] per DAY
        assertEquals( expectedPos.x, actualPos.x, 1e-10 );
        assertEquals( expectedPos.y, actualPos.y, 1e-10 );
        assertEquals( expectedPos.z, actualPos.z, 1e-10 );
        assertEquals( expectedVel.x, actualVel.x, 1e-8 );
        assertEquals( expectedVel.y, actualVel.y, 1e-8 );
        assertEquals( expectedVel.z, actualVel.z, 1e-8 );
    }
}
