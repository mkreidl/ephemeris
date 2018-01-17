package com.mkreidl.ephemeris.dynamics;

import com.mkreidl.ephemeris.*;
import com.mkreidl.ephemeris.geometry.*;

public abstract class OrbitalModel<T extends Coordinates<T>>
{
    public enum Type
    {
        HELIOCENTRIC, GEOCENTRIC
    }

    public abstract void compute( Time time, T position, T velocity );

    public T compute( Time time, T position )
    {
        compute( time, position, null );
        return position;
    }

    public Distance getUnit()
    {
        return Distance.m;
    }

    public Type getType()
    {
        return Type.HELIOCENTRIC;
    }
}
