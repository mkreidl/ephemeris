package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.solarsystem.Body;
import com.mkreidl.ephemeris.solarsystem.SolarSystemMeeus;

import org.junit.Assert;
import org.junit.Test;

public class SunRiseSetAtPolesTest
{
    private PlanetRiseSetCalculator riseSetCalculator = new PlanetRiseSetCalculator( new SolarSystemMeeus(), Body.SUN );

    @Test
    public void testNorthPoleForward()
    {
        riseSetCalculator.setGeographicLocation( 0.0, 88.0 );
        riseSetCalculator.lookupDirection = RiseSetCalculator.LookupDirection.FORWARD;
        riseSetCalculator.setEventType( RiseSetCalculator.EventType.RISE );
        Assert.assertTrue( riseSetCalculator.compute( 0 ) );
    }

    @Test
    public void testNorthPoleBackward()
    {
        riseSetCalculator.setGeographicLocation( 0.0, 88.0 );
        riseSetCalculator.lookupDirection = RiseSetCalculator.LookupDirection.BACKWARD;
        riseSetCalculator.setEventType( RiseSetCalculator.EventType.RISE );
        Assert.assertTrue( riseSetCalculator.compute( 0 ) );
    }

    @Test
    public void testSouthPoleForward()
    {
        riseSetCalculator.setGeographicLocation( 0.0, -88.0 );
        riseSetCalculator.lookupDirection = RiseSetCalculator.LookupDirection.FORWARD;
        riseSetCalculator.setEventType( RiseSetCalculator.EventType.RISE );
        Assert.assertTrue( riseSetCalculator.compute( 0 ) );
    }

    @Test
    public void testSouthPoleBackward()
    {
        riseSetCalculator.setGeographicLocation( 0.0, -88.0 );
        riseSetCalculator.lookupDirection = RiseSetCalculator.LookupDirection.BACKWARD;
        riseSetCalculator.setEventType( RiseSetCalculator.EventType.RISE );
        Assert.assertTrue( riseSetCalculator.compute( 0 ) );
    }
}