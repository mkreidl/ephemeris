package com.mkreidl.ephemeris.solarsystem;

import com.mkreidl.ephemeris.Distance;
import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Spherical;

public abstract class OrbitalModel
{
    public enum Type
    {
        HELIOCENTRIC, GEOCENTRIC
    }

    public Type getType()
    {
        return Type.HELIOCENTRIC;
    }

    public Distance getDistanceUnit()
    {
        return Distance.AU;
    }

    public abstract void compute( Time time, Cartesian position, Cartesian velocity );

    public abstract void compute( Time time, Spherical position, Spherical velocity );
}
