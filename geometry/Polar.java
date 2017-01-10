package com.mkreidl.ephemeris.geometry;

public class Polar
{
    public double r = 0.0;
    public double theta = 0.0;
    public double phi = 0.0;

    public Polar()
    {
    }

    public Polar( double r, double phi, double theta )
    {
        this.r = r;
        this.phi = phi;
        this.theta = theta;
    }

    public void convert2cartesian( Cartesian output )
    {
        output.x = r * Math.cos( phi ) * Math.sin( theta );
        output.y = r * Math.sin( phi ) * Math.sin( theta );
        output.z = r * Math.cos( theta );
    }

    @Override
    public String toString()
    {
        return "Polar [r=" + r + ", theta=" + theta + ", phi=" + phi + "]";
    }

}
