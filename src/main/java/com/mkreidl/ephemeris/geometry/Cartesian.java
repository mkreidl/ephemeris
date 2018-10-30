package com.mkreidl.ephemeris.geometry;

public class Cartesian extends Coordinates<Cartesian>
{
    public double x = 0.0;
    public double y = 0.0;
    public double z = 0.0;

    public static final Cartesian ORIGIN = new Cartesian( 0, 0, 0 );
    public static final Matrix3x3 rotation = new Matrix3x3();

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
        return Math.sqrt( x * x + y * y + z * z );
    }

    @Override
    public Cartesian normalize()
    {
        final double s = 1 / Math.sqrt( x * x + y * y + z * z );
        x *= s;
        y *= s;
        z *= s;
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

    public float[] projectiveToAffine( float[] affinePoints, int offset )
    {
        final double zInv = 1 / z;
        affinePoints[offset] = (float)( x * zInv );
        affinePoints[offset + 1] = (float)( y * zInv );
        return affinePoints;
    }

    @Override
    public Cartesian rotate( Axis axis, double angle )
    {
        synchronized ( rotation )
        {
            rotation.setRotation( angle, axis );
            rotation.applyTo( this );
        }
        return this;
    }

    public Spherical transform( Spherical output )
    {
        output.dst = length();
        output.lat = Math.asin( z / output.dst );
        output.lon = Math.atan2( y, x );
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

    public Cartesian crossProd( Cartesian v, Cartesian w )
    {
        return set( v.y * w.z - v.z * w.y, v.z * w.x - v.x * w.z, v.x * w.y - v.y * w.x );
    }

    @Override
    public String toString()
    {
        return "Cartesian [ x=" + x + ", y=" + y + ", z=" + z + " ]";
    }
}
