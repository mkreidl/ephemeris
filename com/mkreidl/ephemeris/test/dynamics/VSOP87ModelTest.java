package com.mkreidl.ephemeris.test.dynamics;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.dynamics.VSOP87.Model;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.test.geometry.VSOP87CoordinateTransformationTest;
import com.mkreidl.ephemeris.test.geometry.VSOP87File;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class VSOP87ModelTest extends VSOP87Test
{
    @Test
    public void testJulianDate()
    {
        Assert.assertEquals( julianDate, time.julianDayNumber(), 1e-15 );
    }

    @Test
    public void testModel()
    {
        Model.XYZ model;
        switch ( planet )
        {
            case MER:
                model = Model.getModelC( Model.MERCURY );
                break;
            case VEN:
                model = Model.getModelC( Model.VENUS );
                break;
            case EAR:
                model = Model.getModelC( Model.EARTH );
                break;
            case MAR:
                model = Model.getModelC( Model.MARS );
                break;
            case JUP:
                model = Model.getModelC( Model.JUPITER );
                break;
            case SAT:
                model = Model.getModelC( Model.SATURN );
                break;
            case URA:
                model = Model.getModelC( Model.URANUS );
                break;
            case NEP:
                model = Model.getModelC( Model.NEPTUNE );
                break;
            default:
                model = null;
        }
        model.calculate( time, actualPos, actualVel );
        actualVel.scale( 1.0 / Time.DAYS_PER_MILLENNIUM );
        assertEquals( expectedPos.x, actualPos.x, 1e-10 );
        assertEquals( expectedPos.y, actualPos.y, 1e-10 );
        assertEquals( expectedPos.z, actualPos.z, 1e-10 );
        assertEquals( expectedVel.x, actualVel.x, 1e-8 );
        assertEquals( expectedVel.y, actualVel.y, 1e-8 );
        assertEquals( expectedVel.z, actualVel.z, 1e-8 );
    }

    public VSOP87ModelTest( VSOP87File.Planet planet, String timeStr, DataSet dataSet )
    {
        this.planet = planet;
        this.time = dataSet.time;
        this.julianDate = dataSet.julianDate;
        this.expectedPos.set( dataSet.coordinates[0], dataSet.coordinates[1], dataSet.coordinates[2] );
        this.expectedVel.set( dataSet.coordinates[3], dataSet.coordinates[4], dataSet.coordinates[5] );
    }

    @Parameters(name = "{0} -- {1}")
    public static Iterable<Object[]> data()
    {
        final LinkedList<Object[]> parameters = new LinkedList<>();
        for ( VSOP87File.Planet planet : VSOP87CoordinateTransformationTest.fullData.keySet() )
            for ( String timeStr : fullData.get( planet ).get( VSOP87File.Version.C ).keySet() )
                parameters.add( new Object[]
                        {
                                planet, timeStr, fullData.get( planet ).get( VSOP87File.Version.C ).get( timeStr )
                        } );
        return parameters;
    }

    private VSOP87File.Planet planet;
    private Time time;
    private double julianDate;

    private final Cartesian expectedPos = new Cartesian();
    private final Cartesian actualPos = new Cartesian();
    private final Cartesian expectedVel = new Cartesian();
    private final Cartesian actualVel = new Cartesian();
}
