package com.mkreidl.ephemeris.geometry;

import static com.mkreidl.ephemeris.geometry.Coordinates.*;

public class Matrix
{
    protected double[] values = new double[9];

    public Matrix()
    {
        this.setIdentity();
    }

    public Matrix set( double a11, double a12, double a13,
                       double a21, double a22, double a23,
                       double a31, double a32, double a33 )
    {
        values[0] = a11;
        values[1] = a12;
        values[2] = a13;
        values[3] = a21;
        values[4] = a22;
        values[5] = a23;
        values[6] = a31;
        values[7] = a32;
        values[8] = a33;
        return this;
    }

    public Cartesian apply( Cartesian point )
    {
        final double x = point.x;
        final double y = point.y;
        final double z = point.z;
        point.x = values[0] * x + values[1] * y + values[2] * z;
        point.y = values[3] * x + values[4] * y + values[5] * z;
        point.z = values[6] * x + values[7] * y + values[8] * z;
        return point;
    }

    public Cartesian apply( Cartesian input, Cartesian output )
    {
        output.set( input );
        return apply( output );
    }

    public Circle apply( Circle circle )
    {
        final double x = circle.x;
        final double y = circle.y;
        circle.x = values[0] * x + values[1] * y + values[2];
        circle.y = values[3] * x + values[4] * y + values[5];
        return circle;
    }

    public Circle apply( Circle input, Circle output )
    {
        output.set( input );
        return apply( output );
    }

    public Matrix setIdentity()
    {
        values[0] = 1.0;
        values[1] = 0.0;
        values[2] = 0.0;
        values[3] = 0.0;
        values[4] = 1.0;
        values[5] = 0.0;
        values[6] = 0.0;
        values[7] = 0.0;
        values[8] = 1.0;
        return this;
    }

    public Matrix setRotation( double angle, Axis axis )
    {
        this.setIdentity();
        final double cosa = Math.cos( angle );
        final double sina = Math.sin( angle );
        switch ( axis )
        {
            case X:
                values[4] = cosa;
                values[5] = -sina;
                values[7] = sina;
                values[8] = cosa;
                break;
            case Y:
                values[0] = cosa;
                values[2] = -sina;
                values[6] = sina;
                values[8] = cosa;
                break;
            case Z:
                values[0] = cosa;
                values[1] = -sina;
                values[3] = sina;
                values[4] = cosa;
                break;
        }
        return this;
    }

    public Matrix transpose()
    {
        double tmp = values[1];
        values[1] = values[3];
        values[3] = tmp;
        tmp = values[2];
        values[2] = values[6];
        values[6] = tmp;
        tmp = values[5];
        values[5] = values[7];
        values[7] = tmp;
        return this;
    }
}
