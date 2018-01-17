package com.mkreidl.ephemeris.geometry;

import com.mkreidl.ephemeris.*;


public abstract class Coordinates<T extends Coordinates<T>>
{
    public enum Axis
    {
        X, Y, Z
    }

    public abstract double length();

    public abstract T normalize();

    public abstract T scale( double factor );

    public abstract T set( T original );

    public abstract T set( double a, double b, double c );

    public abstract T rotate( Axis axis, double angle );

    public double distance( Distance unit )
    {
        return length() / unit.toMeters();
    }
}
