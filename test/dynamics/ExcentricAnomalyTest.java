package com.mkreidl.ephemeris.test.dynamics;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.mkreidl.ephemeris.geometry.ClassicalOrbitalElements;

@RunWith(Parameterized.class)
public class ExcentricAnomalyTest
{
    double tolerance, M, e;
    ClassicalOrbitalElements orbit;

    @Parameters
    public static Iterable<Object[]> data()
    {
        return Arrays.asList( new Object[][]
        {
                {
                        0.2, 0.0, 1.0e-13
                },
                {
                        0.2, 0.1, 1.0e-13
                },
                {
                        0.2, 0.2, 1.0e-13
                },
                {
                        0.2, 0.3, 1.0e-13
                },
                {
                        0.2, 0.4, 1.0e-13
                },
                {
                        0.2, 0.5, 1.0e-13
                },
                {
                        0.2, 0.6, 1.0e-13
                },
                {
                        0.2, 0.7, 1.0e-13
                },
                {
                        0.2, 0.8, 1.0e-13
                },
                {
                        0.4, 0.1, 1.0e-13
                },
                {
                        0.4, 0.2, 1.0e-13
                },
                {
                        0.4, 0.3, 1.0e-13
                },
                {
                        0.4, 0.4, 1.0e-13
                },
                {
                        0.4, 0.5, 1.0e-13
                },
                {
                        0.4, 0.6, 1.0e-13
                },
                {
                        0.4, 0.7, 1.0e-13
                },
                {
                        1.0, 0.1, 1.0e-13
                },
                {
                        1.0, 0.2, 1.0e-13
                },
                {
                        1.0, 0.3, 1.0e-13
                },
                {
                        1.0, 0.4, 1.0e-13
                },
                {
                        1.0, 0.5, 1.0e-13
                },
                {
                        2.0, 0.6, 1.0e-13
                },
                {
                        3.0, 0.7, 1.0e-13
                },
        } );
    }

    public ExcentricAnomalyTest( double M, double e, double tolerance )
    {
        this.tolerance = tolerance;
        this.orbit = new ClassicalOrbitalElements();
        this.orbit.meanAnom = M;
        this.orbit.exc = e;
    }

    @Test
    public void test()
    {
        double E = orbit.eccentricAnomaly( tolerance );
        double newM = E - orbit.exc * Math.sin( E );
        assertEquals( orbit.meanAnom, newM, tolerance );
    }

}
