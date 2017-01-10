package com.mkreidl.ephemeris.geometry;


import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Stereographic;

@RunWith(Parameterized.class)
public class StereographicProjectionTest
{
    double pole;
    double tolerance;
    Cartesian preimage, image, expected;
    Stereographic stereo;

    @Parameters
    public static Iterable<Object[]> data()
    {
        return Arrays.asList( new Object[][]
        {
                // FROM NORTH POLE
                // z-axis
                {
                        new Cartesian( 0.0, 0.0, 0.0 ), new Cartesian( 0.0, 0.0, 0.0 ), 1.0, 1.0e-15
                },
                {
                        new Cartesian( 0.0, 0.0, 1.0 ), new Cartesian( Double.NaN, Double.NaN, 0.0 ), 1.0, 1.0e-15
                },
                {
                        new Cartesian( 0.0, 0.0, -1.0 ), new Cartesian( 0.0, 0.0, 0.0 ), 1.0, 1.0e-15
                },
                // unit-circle in xy-plane
                {
                        new Cartesian( 1.0, 0.0, 0.0 ), new Cartesian( 1.0, 0.0, 0.0 ), 1.0, 1.0e-15
                },
                {
                        new Cartesian( -1.0, 0.0, 0.0 ), new Cartesian( -1.0, 0.0, 0.0 ), 1.0, 1.0e-15
                },
                {
                        new Cartesian( 0.0, 1.0, 0.0 ), new Cartesian( 0.0, 1.0, 0.0 ), 1.0, 1.0e-15
                },
                {
                        new Cartesian( 0.0, -1.0, 0.0 ), new Cartesian( 0.0, -1.0, 0.0 ), 1.0, 1.0e-15
                },
                // various others
                {
                        new Cartesian( Math.sqrt( 3 ) / 2.0, 0.0, -0.5 ),
                        new Cartesian( Math.sqrt( 1.0 / 3.0 ), 0.0, 0.0 ), 1.0, 1.0e-15
                },
                {
                        new Cartesian( 0.0, Math.sqrt( 3 ) / 2.0, 0.5 ), new Cartesian( 0.0, Math.sqrt( 3.0 ), 0.0 ),
                        1.0, 1.0e-15
                },
                {
                        new Cartesian( 0.0, 1.0, 1.0 ), new Cartesian( Double.NaN, Double.POSITIVE_INFINITY, 0.0 ), 1.0,
                        1.0e-15
                },
                // FROM SOUTH POLE
                // z-axis
                {
                        new Cartesian( 0.0, 0.0, 0.0 ), new Cartesian( 0.0, 0.0, 0.0 ), -1.0, 1.0e-15
                },
                {
                        new Cartesian( 0.0, 0.0, 1.0 ), new Cartesian( 0.0, 0.0, 0.0 ), -1.0, 1.0e-15
                },
                {
                        new Cartesian( 0.0, 0.0, -1.0 ), new Cartesian( Double.NaN, Double.NaN, 0.0 ), -1.0, 1.0e-15
                },
                // unit-circle in xy-plane
                {
                        new Cartesian( 1.0, 0.0, 0.0 ), new Cartesian( 1.0, 0.0, 0.0 ), -1.0, 1.0e-15
                },
                {
                        new Cartesian( -1.0, 0.0, 0.0 ), new Cartesian( -1.0, 0.0, 0.0 ), -1.0, 1.0e-15
                },
                {
                        new Cartesian( 0.0, 1.0, 0.0 ), new Cartesian( 0.0, 1.0, 0.0 ), -1.0, 1.0e-15
                },
                {
                        new Cartesian( 0.0, -1.0, 0.0 ), new Cartesian( 0.0, -1.0, 0.0 ), -1.0, 1.0e-15
                },
                // various others
                {
                        new Cartesian( Math.sqrt( 3 ) / 2.0, 0.0, -0.5 ), new Cartesian( Math.sqrt( 3.0 ), 0.0, 0.0 ),
                        -1.0, 1.0e-15
                },
                {
                        new Cartesian( 0.0, Math.sqrt( 3 ) / 2.0, 0.5 ),
                        new Cartesian( 0.0, Math.sqrt( 1.0 / 3.0 ), 0.0 ), -1.0, 1.0e-15
                },
                {
                        new Cartesian( 0.0, 1.0, 1.0 ), new Cartesian( 0.0, 0.5, 0.0 ), -1.0, 1.0e-15
                },
        } );
    }

    @Before
    public void setUp()
    {
        stereo = new Stereographic( pole ); // Projection from origin
        image = new Cartesian();
    }

    public StereographicProjectionTest( Cartesian preimage, Cartesian expected, double pole, double tolerance )
    {
        this.preimage = preimage;
        this.expected = expected;
        this.pole = pole;
        this.tolerance = tolerance;
    }

    @Test
    public void test()
    {
        stereo.project( preimage, image );
        assertEquals( expected.x, image.x, tolerance );
        assertEquals( expected.y, image.y, tolerance );
        assertEquals( expected.z, image.z, tolerance );
    }

}
