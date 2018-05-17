package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.sky.coordinates.Equatorial;

public class StarRiseSetCalculator extends RiseSetCalculator
{
    private final Stars stars = new Stars();
    private final Equatorial.Cart cart = new Equatorial.Cart();
    private int starIndex;

    public static StarRiseSetCalculator of( int starIndex )
    {
        return new StarRiseSetCalculator( starIndex );
    }

    public static StarRiseSetCalculator of( String starName )
    {
        final int starIndex = StarsCatalog.findIndexByName( starName );
        if ( starIndex >= 0 )
            return new StarRiseSetCalculator( starIndex );
        else
            throw new IllegalArgumentException( "Unknown star name: " + starName );
    }

    private StarRiseSetCalculator( int starIndex )
    {
        this.starIndex = starIndex;
    }

    @Override
    protected void computeTopocentricPosition()
    {
        stars.compute( starIndex, time, cart );
        cart.transform( topocentric );
    }

    @Override
    protected double shiftHorizonDeg()
    {
        return -OPTICAL_HORIZON_DEG;
    }

    @Override
    public Long compute( long startTimeMs )
    {
        return 0l;// computeSingleStep();
    }

}
