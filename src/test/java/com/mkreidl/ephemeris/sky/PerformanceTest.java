package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.Time;

import org.junit.Test;

public class PerformanceTest
{
    private final SolarSystem solarSystem = new SolarSystem();
    private int runs = 0;

    @Test( timeout = 1000 )
    public void testPerformance()
    {
        System.out.println( "PERFORMANCE TEST" );
        for ( runs = 0; runs < 600; runs++ )
            solarSystem.compute( Time.J2000 );
        System.out.println( "Solar system calculations in <1s: " + runs );
    }

    public void count()
    {
        while ( true )
        {
            solarSystem.compute( Time.J2000 );
            ++runs;
        }
    }

}
