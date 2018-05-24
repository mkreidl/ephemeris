package com.mkreidl.ephemeris.geometry;

import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.sqrt;

public class Cartesian extends Coordinates<Cartesian>
{
    public double x = 0.0;
    public double y = 0.0;
    public double z = 0.0;

    public static final Cartesian ORIGIN = new Cartesian( 0, 0, 0 );
    public static final Matrix rotation = new Matrix();

    public Cartesian()
    {
    }

    public Cartesian( Cartesian other )
    {
        set( other );
    }

    public Cartesian( double x, double y, double z )
    {
        set( x, y, z );
    }

    @Override
    public Cartesian set( Cartesian orig )
    {
        this.x = orig.x;
        this.y = orig.y;
        this.z = orig.z;
        return this;
    }

    @Override
    public Cartesian set( double x, double y, double z )
    {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    @Override
    public double length()
    {
        return sqrt( x * x + y * y + z * z );
    }

    @Override
    public Cartesian normalize()
    {
        double n = sqrt( x * x + y * y + z * z );
        x /= n;
        y /= n;
        z /= n;
        return this;
    }

    @Override
    public Cartesian scale( double factor )
    {
        this.x *= factor;
        this.y *= factor;
        this.z *= factor;
        return this;
    }

    @Override
    public Cartesian rotate( Axis axis, double angle )
    {
        synchronized ( rotation )
        {
            rotation.setRotation( angle, axis );
            rotation.apply( this );
        }
        return this;
    }

    public Spherical transform( Spherical output )
    {
        output.dst = sqrt( x * x + y * y + z * z );
        output.lat = asin( z / output.dst );
        output.lon = atan2( y, x );
        return output.standardize();
    }

    public Spherical transformVelocity( Cartesian cartesianPos, Spherical velocity )
    {
        // TODO implement
        return velocity;
    }

    public Cartesian add( Cartesian summand )
    {
        this.x += summand.x;
        this.y += summand.y;
        this.z += summand.z;
        return this;
    }

    public Cartesian sub( Cartesian summand )
    {
        this.x -= summand.x;
        this.y -= summand.y;
        this.z -= summand.z;
        return this;
    }

    public double dot( Cartesian other )
    {
        return x * other.x + y * other.y + z * other.z;
    }

    @Override
    public String toString()
    {
        return "Cartesian [ x=" + x + ", y=" + y + ", z=" + z + " ]";
    }
}
