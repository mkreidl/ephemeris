package com.mkreidl.ephemeris;


public enum Distance
{
    ly( 9.460730472580800e15 ),
    AU( 1.495978707e11 ),
    m( 1.0 ),
    km( 1e3 );

    Distance( double unitInMeters )
    {
        this.unitInMeters = unitInMeters;
    }

    public double toMeters()
    {
        return unitInMeters;
    }

    private final double unitInMeters;
}
