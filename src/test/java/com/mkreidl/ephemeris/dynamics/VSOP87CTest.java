package com.mkreidl.ephemeris.dynamics;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.dynamics.VSOP87.Model;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.VSOP87File;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class VSOP87CTest extends VSOP87Test
{
    @Parameters(name = "{0} -- {1}")
    public static Iterable<Object[]> data()
    {
        return data(VSOP87File.Version.C);
    }

    private final Cartesian expectedPos = new Cartesian();
    private final Cartesian actualPos = new Cartesian();
    private final Cartesian expectedVel = new Cartesian();
    private final Cartesian actualVel = new Cartesian();

    public VSOP87CTest( VSOP87File.Planet planet, String timeStr, DataSet dataSet )
    {
        super(planet, timeStr, dataSet);
        expectedPos.set( dataSet.coordinates[0], dataSet.coordinates[1], dataSet.coordinates[2] );
        expectedVel.set( dataSet.coordinates[3], dataSet.coordinates[4], dataSet.coordinates[5] );
    }

    @Test
    public void testModel()
    {
        final Model.XYZ model;
        switch ( planet )
        {
            case MER:
                model = Model.getVersionC( Model.MERCURY );
                break;
            case VEN:
                model = Model.getVersionC( Model.VENUS );
                break;
            case EAR:
                model = Model.getVersionC( Model.EARTH );
                break;
            case MAR:
                model = Model.getVersionC( Model.MARS );
                break;
            case JUP:
                model = Model.getVersionC( Model.JUPITER );
                break;
            case SAT:
                model = Model.getVersionC( Model.SATURN );
                break;
            case URA:
                model = Model.getVersionC( Model.URANUS );
                break;
            case NEP:
                model = Model.getVersionC( Model.NEPTUNE );
                break;
            default:
                model = null;
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
