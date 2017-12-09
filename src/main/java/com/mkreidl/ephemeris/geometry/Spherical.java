package com.mkreidl.ephemeris.geometry;

public class Spherical extends Coordinates<Spherical>
{
    public double dst = 0.0;
    public double lat = 0.0;
    public double lon = 0.0;

    public final Cartesian tmpCartesian = new Cartesian();

    public Spherical()
    {
    }

    public Spherical( Spherical orig )
    {
        set( orig );
    }

    public Spherical( double dst, double lon, double lat )
    {
        set( dst, lon, lat );
    }

    @Override
    public Spherical set( Spherical orig )
    {
        this.dst = orig.dst;
        this.lat = orig.lat;
        this.lon = orig.lon;
        return standardize();
    }

    @Override
    public Spherical set( double dst, double lon, double lat )
    {
        this.lon = lon;
        this.lat = lat;
        this.dst = dst;
        return this.standardize();
    }

    @Override
    public double length()
    {
        return dst;
    }

    @Override
    public Spherical normalize()
    {
        this.dst = 1.0;
        return this;
    }

    @Override
    public Spherical scale( double factor )
    {
        this.dst *= factor;
        return this;
    }

    @Override
    public Spherical rotate( Axis axis, double angle )
    {
        transform( tmpCartesian );
        tmpCartesian.rotate( axis, angle );
        return tmpCartesian.transform( this );
    }

    public Cartesian transform( Cartesian output )
    {
        output.x = dst * Math.cos( lat ) * Math.cos( lon );
        output.y = dst * Math.cos( lat ) * Math.sin( lon );
        output.z = dst * Math.sin( lat );
        return output;
    }

    public Spherical standardize()
    {
        lat = Angle.standardize( lat );
        if ( lat > Math.PI / 2 || lat < -Math.PI / 2 )
        {
            lat = Angle.standardize( -lat );
            lon += Math.PI;
        }
        lon = Angle.standardize( lon );
        return this;
    }

    @Override
    public String toString()
    {
        final Angle.Sexagesimal angle = new Angle.Sexagesimal( Angle.Unit.DEGREES );
        return "Spherical [ dst=" + dst + "m, "
                + "lat=" + lat + " (" + angle.setRadians( lat ).toString() + "), "
                + "lon=" + lon + " (" + angle.setRadians( lon ).toString() + ") ]";
    }

}
