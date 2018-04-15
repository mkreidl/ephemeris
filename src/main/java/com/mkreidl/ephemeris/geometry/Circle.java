package com.mkreidl.ephemeris.geometry;

public class Circle
{
    public double x, y, r;

    public Circle()
    {
    }

    public Circle( double x, double y, double r )
    {
        set( x, y, r );
    }

    public Circle( Circle orig )
    {
        set( orig.x, orig.y, orig.r );
    }

    public void set( double x, double y, double r )
    {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public void set( Circle source )
    {
        this.x = source.x;
        this.y = source.y;
        this.r = source.r;
    }

    public double distFromOrigin()
    {
        return Math.sqrt( x * x + y * y );
    }

    @Override
    public String toString()
    {
        return "Circle [x=" + x + ", y=" + y + ", r=" + r + "]";
    }
}
