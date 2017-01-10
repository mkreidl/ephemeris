package com.mkreidl.ephemeris.dynamics;

import com.mkreidl.ephemeris.Distance;
import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Coordinates;

public abstract class OrbitalModel<T extends Coordinates>
{
    public enum Type
    {
        HELIOCENTRIC, GEOCENTRIC
    }

    public abstract void calculate( Time time, T position, T velocity );

    public T calculate( Time time, T position )
    {
        calculate( time, position, null );
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
