package com.mkreidl.ephemeris.geometry;

public abstract class OrbitalElements
{
    public abstract void computePosition();

    protected Cartesian eclipticalCartesian;
    protected Spherical eclipticalSpherical;

    public OrbitalElements()
    {
        eclipticalCartesian = new Cartesian();
        eclipticalSpherical = new Spherical();
    }

    public void getPosition( Cartesian output )
    {
        output.x = eclipticalCartesian.x;
        output.y = eclipticalCartesian.y;
        output.z = eclipticalCartesian.z;
    }

    public void getPosition( Spherical output )
    {
        output.dst = eclipticalSpherical.dst;
        output.lat = eclipticalSpherical.lat;
        output.lon = eclipticalSpherical.lon;
    }

    @Override
    public String toString()
    {
        return "OrbitalElements [eclipticalCartesian=" + eclipticalCartesian
                + ", eclipticalSpherical=" + eclipticalSpherical + "]";
    }
}
