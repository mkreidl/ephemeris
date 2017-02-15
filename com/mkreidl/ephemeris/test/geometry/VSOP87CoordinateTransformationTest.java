package com.mkreidl.ephemeris.test.geometry;

import com.mkreidl.ephemeris.dynamics.VSOP87.PrecessionMatrix;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Matrix;
import com.mkreidl.ephemeris.geometry.Spherical;
import com.mkreidl.ephemeris.geometry.VSOP87OrbitalElements;
import com.mkreidl.ephemeris.test.dynamics.VSOP87Test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public class VSOP87CoordinateTransformationTest extends VSOP87Test
{
    private LinkedHashMap<VSOP87File.Version, LinkedHashMap<String, DataSet>> datasets;

    private void testTransformation( VSOP87File.Version first, VSOP87File.Version second, double d1, double d2, double d3 )
    {
        final LinkedHashMap<String, DataSet> datasetsFirst = datasets.get( first );
        final LinkedHashMap<String, DataSet> datasetsSecond = datasets.get( second );

        final VSOP87OrbitalElements orbitalElements = new VSOP87OrbitalElements();
        final Cartesian origCartesian = new Cartesian();
        final Cartesian actualCartesian = new Cartesian();
        final Spherical origSpherical = new Spherical();
        final Spherical actualSpherical = new Spherical();

        for ( String dateString : datasetsFirst.keySet() )
        {
            final DataSet dataSetFirst = datasetsFirst.get( dateString );
            final DataSet dataSetSecond = datasetsSecond.get( dateString );

            if ( dataSetSecond == null )
            {
                if ( !datasetsSecond.isEmpty() )
                    fail( "Matching date not found in dataset for version " + second );
                continue;
            }
            dataSetFirst.model.calculate( dataSetFirst.time );
            dataSetSecond.model.calculate( dataSetSecond.time );

            if ( first == VSOP87File.Version.O )
            {
                dataSetFirst.model.storeTo( orbitalElements );
                orbitalElements.calculatePosition();
                orbitalElements.getPosition( actualSpherical );
                orbitalElements.getPosition( actualCartesian );
            }
            else if ( first == VSOP87File.Version.B || first == VSOP87File.Version.D )
            {
                dataSetFirst.model.getPosition( origSpherical );
                origSpherical.transform( actualCartesian );
            }
            else if ( second == VSOP87File.Version.B || second == VSOP87File.Version.D )
            {
                dataSetFirst.model.getPosition( origCartesian );
                origCartesian.transform( actualSpherical );
            }
            else
            {
                final Matrix matrix = new PrecessionMatrix().calculate( dataSetFirst.time );
                matrix.apply( dataSetFirst.model.getPosition( actualCartesian ) );
            }

            if ( second == VSOP87File.Version.B || second == VSOP87File.Version.D )
            {
                dataSetSecond.model.getPosition( origSpherical );
                assertEquals( origSpherical.dst, actualSpherical.dst, d1 );
                assertEquals( origSpherical.lat, actualSpherical.lat, d2 );
                assertEquals( origSpherical.lon, actualSpherical.lon, d3 );
            }
            else
            {
                dataSetSecond.model.getPosition( origCartesian );
                assertEquals( origCartesian.x, actualCartesian.x, d1 );
                assertEquals( origCartesian.y, actualCartesian.y, d2 );
                assertEquals( origCartesian.z, actualCartesian.z, d3 );
            }
        }
    }

    @Test
    public void testOA()
    {
        this.testTransformation( VSOP87File.Version.O, VSOP87File.Version.A, 1e-5, 1e-5, 1e-5 );
    }

    @Test
    public void testOB()
    {
        this.testTransformation( VSOP87File.Version.O, VSOP87File.Version.B, 1e-5, 1e-7, 1e-7 );
    }

    @Test
    public void testAB()
    {
        this.testTransformation( VSOP87File.Version.A, VSOP87File.Version.B, 1e-6, 1e-7, 1e-7 );
    }

    @Test
    public void testAC()
    {
        this.testTransformation( VSOP87File.Version.A, VSOP87File.Version.C, 1e-6, 1e-6, 1e-6 );
    }

    @Test
    public void testCD()
    {
        this.testTransformation( VSOP87File.Version.C, VSOP87File.Version.D, 1e-6, 1e-7, 1e-7 );
    }

    @Test
    public void testBA()
    {
        this.testTransformation( VSOP87File.Version.B, VSOP87File.Version.A, 1e-6, 1e-6, 1e-6 );
    }

    @Test
    public void testDC()
    {
        this.testTransformation( VSOP87File.Version.D, VSOP87File.Version.C, 1e-5, 1e-5, 1e-5 );
    }

    public VSOP87CoordinateTransformationTest( VSOP87File.Planet planet,
                                               LinkedHashMap<VSOP87File.Version, LinkedHashMap<String, DataSet>> datasets )
    {
        this.datasets = datasets;
    }

    @Parameters(name = "{0}")
    public static Iterable<Object[]> data()
    {
        LinkedList<Object[]> parameters = new LinkedList<>();
        for ( VSOP87File.Planet planet : VSOP87CoordinateTransformationTest.fullData.keySet() )
            parameters.add( new Object[]
                    {
                            planet, fullData.get( planet )
                    } );
        return parameters;
    }
}
