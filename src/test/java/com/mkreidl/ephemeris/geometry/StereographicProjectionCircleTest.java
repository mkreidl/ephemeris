package com.mkreidl.ephemeris.geometry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

import static java.lang.Math.*;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class StereographicProjectionCircleTest {
    private Spherical point;
    private Circle expected, actual;
    private Stereographic stereoN, stereoS;
    private double tolerance;
    private String testname;

    @Parameters(name = "{0}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]
                {
                        {
                                "Equator",
                                new Spherical(1.0, 0.0, PI / 2),    // = north centerZ => we are projecting the ...
                                new Circle(0.0, 0.0, 1.0),        // ... equator
                                1.0e-15
                        },
                        {
                                "Great circle tilted by -pi/3 around y-axis",
                                new Spherical(1.0, 0.0, 2 * PI / 3),
                                new Circle(
                                        0.5 * (sqrt(3) - 1.0 / sqrt(3)),
                                        0.0,
                                        0.5 * (sqrt(3) + 1.0 / sqrt(3))
                                ),
                                1.0e-15
                        },
                        {
                                "Great circle tilted by -pi/4 around y-axis",
                                new Spherical(1.0, 0.0, 3 * PI / 4),
                                new Circle(
                                        0.5 * (1.0 / tan(PI / 8) - tan(PI / 8)),
                                        0.0,
                                        0.5 * (tan(PI / 8) + 1 / tan(PI / 8))
                                ),
                                1.0e-15
                        },
                        // {
                        // PI/2.0, 0.0, Double.POSITIVE_INFINITY,
                        // Double.POSITIVE_INFINITY, 1.0e-15
                        // },
                        // {
                        // -PI/2.0, 0.0, Double.NEGATIVE_INFINITY,
                        // Double.POSITIVE_INFINITY, 1.0e-15
                        // },
                });
    }

    @Before
    public void setUp() {
        stereoN = new Stereographic(1.0); // Projection from North centerZ
        stereoS = new Stereographic(-1.0); // Projection from South centerZ
        actual = new Circle();
    }

    public StereographicProjectionCircleTest(String testname, Spherical pole, Circle projection, double tolerance) {
        this.testname = testname;
        this.expected = projection;
        this.point = pole;
        this.tolerance = tolerance;
    }

    @Test
    public void testCircle() {
        stereoN.project(point, PI / 2, actual);
        assertEquals(expected.x, actual.x, tolerance);
        assertEquals(expected.y, actual.y, tolerance);
        assertEquals(expected.r, actual.r, tolerance);
        stereoS.project(point, PI / 2, actual);
        assertEquals(-expected.x, actual.x, tolerance);
        assertEquals(-expected.y, actual.y, tolerance);
        assertEquals(expected.r, actual.r, tolerance);

        if (testname.equals("Equator")) {
            Spherical eclipticPole = new Spherical(1.0, 0.0, 77.0 / 180 * PI);
            double[] alphas = new double[]{PI / 6, PI / 4, PI / 3, PI / 2, 2 * PI / 3, 3 * PI / 4};
            for (double alpha : alphas) {
                stereoN.project(point, alpha, actual);
                assertEquals(0.0, actual.x, tolerance);
                assertEquals(0.0, actual.y, tolerance);
                assertEquals(stereoN.project1D(PI / 2 - alpha), actual.r, tolerance);
                stereoS.project(point, alpha, actual);
                assertEquals(0.0, actual.x, tolerance);
                assertEquals(0.0, actual.y, tolerance);
                assertEquals(stereoS.project1D(PI / 2 - alpha), actual.r, tolerance);
                System.out.println(Float.toString((float) (alpha * 180 / PI)) + ": " + stereoN.project(eclipticPole, alpha, actual));
            }
        }
    }
}
