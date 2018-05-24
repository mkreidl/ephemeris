package com.mkreidl.ephemeris.solarsystem;

import com.mkreidl.ephemeris.Time;

import org.junit.Test;

public class PerformanceVsop87cTest
{
    private final SolarSystem solarSystem = new SolarSystemVSOP87C();
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
