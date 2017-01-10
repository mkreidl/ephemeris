package com.mkreidl.ephemeris.sky;

import org.junit.Test;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.sky.SolarSystem;

public class PerformanceTest
{
    private final SolarSystem solarSystem = new SolarSystem();
    private int runs = 0;

    @Test(timeout = 1000)
    public void testPerformance()
    {
        System.out.println( "PERFORMANCE TEST" );
        for ( runs = 0; runs < 600; runs++ )
            solarSystem.calculate( Time.J2000 );
        System.out.println( "Solar system calculations in <1s: " + runs );
    }

    public void count()
    {
        while ( true )
        {
            solarSystem.calculate( Time.J2000 );
            ++runs;
        }
    }

}
