package com.mkreidl.ephemeris.geometry;

import java.util.Arrays;

import static com.mkreidl.ephemeris.geometry.Coordinates.Axis;

public class Matrix3x3
{
    public double[] values = new double[9];

    public Matrix3x3()
    {
        setIdentity();
    }

    public Matrix3x3 set( double... values )
    {
        System.arraycopy( values, 0, this.values, 0, 9 );
        return this;
    }

    public Matrix3x3 set( Matrix3x3 original )
    {
        return set( original.values );
    }

    public Matrix3x3 setTransposeOf( Matrix3x3 original )
    {
        return set( original ).transpose();
    }

    public void apply( double[] in, float[] out, int index )
    {
        final int index1 = index + 1;
        final int index2 = index + 2;
        final double x = in[index];
        final double y = in[index1];
        final double z = in[index2];
        out[index] = (float)( values[0] * x + values[1] * y + values[2] * z );
        out[index1] = (float)( values[3] * x + values[4] * y + values[5] * z );
        out[index2] = (float)( values[6] * x + values[7] * y + values[8] * z );
    }

    public void applyTo( float[] points )
    {
        for ( int offset = 0; offset < points.length; ++offset )
        {
            final double x = points[offset];
            final double y = points[offset + 1];
            final double z = points[offset + 2];
            points[offset] = (float)( values[0] * x + values[1] * y + values[2] * z );
            points[++offset] = (float)( values[3] * x + values[4] * y + values[5] * z );
            points[++offset] = (float)( values[6] * x + values[7] * y + values[8] * z );
        }
    }

    public Cartesian applyTo( Cartesian point )
    {
        final double x = point.x;
        final double y = point.y;
        final double z = point.z;
        point.x = values[0] * x + values[1] * y + values[2] * z;
        point.y = values[3] * x + values[4] * y + values[5] * z;
        point.z = values[6] * x + values[7] * y + values[8] * z;
        return point;
    }

    public Cartesian applyTo( Cartesian input, Cartesian output )
    {
        output.x = values[0] * input.x + values[1] * input.y + values[2] * input.z;
        output.y = values[3] * input.x + values[4] * input.y + values[5] * input.z;
        output.z = values[6] * input.x + values[7] * input.y + values[8] * input.z;
        return output;
    }

    public Circle applyTo( Circle circle )
    {
        final double x = circle.x;
        final double y = circle.y;
        circle.x = values[0] * x + values[1] * y + values[2];
        circle.y = values[3] * x + values[4] * y + values[5];
        return circle;
    }

    public Circle applyTo( Circle input, Circle output )
    {
        output.set( input );
        return applyTo( output );
    }

    public Matrix3x3 setIdentity()
    {
        Arrays.fill( values, 0 );
        values[0] = values[4] = values[8] = 1.0;
        return this;
    }

    public Matrix3x3 setRotation( double angle, Axis axis )
    {
        setIdentity();
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

    public Matrix3x3 transpose()
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

    public Matrix3x3 setProduct( Matrix3x3 left, Matrix3x3 right )
    {
        final double[] l = left.values;
        final double[] r = right.values;
        values[0] = l[0] * r[0] + l[1] * r[3] + l[2] * r[6];
        values[1] = l[0] * r[1] + l[1] * r[4] + l[2] * r[7];
        values[2] = l[0] * r[2] + l[1] * r[5] + l[2] * r[8];
        values[3] = l[3] * r[0] + l[4] * r[3] + l[5] * r[6];
        values[4] = l[3] * r[1] + l[4] * r[4] + l[5] * r[7];
        values[5] = l[3] * r[2] + l[4] * r[5] + l[5] * r[8];
        values[6] = l[6] * r[0] + l[7] * r[3] + l[8] * r[6];
        values[7] = l[6] * r[1] + l[7] * r[4] + l[8] * r[7];
        values[8] = l[6] * r[2] + l[7] * r[5] + l[8] * r[8];
        return this;
    }

    public Matrix3x3 preConcat( Matrix3x3 multiplyRight )
    {
        final double a11 = values[0];
        final double a12 = values[1];
        final double a13 = values[2];
        final double a21 = values[3];
        final double a22 = values[4];
        final double a23 = values[5];
        final double a31 = values[6];
        final double a32 = values[7];
        final double a33 = values[8];
        final double[] r = multiplyRight.values;
        values[0] = a11 * r[0] + a12 * r[3] + a13 * r[6];
        values[1] = a11 * r[1] + a12 * r[4] + a13 * r[7];
        values[2] = a11 * r[2] + a12 * r[5] + a13 * r[8];
        values[3] = a21 * r[0] + a22 * r[3] + a23 * r[6];
        values[4] = a21 * r[1] + a22 * r[4] + a23 * r[7];
        values[5] = a21 * r[2] + a22 * r[5] + a23 * r[8];
        values[6] = a31 * r[0] + a32 * r[3] + a33 * r[6];
        values[7] = a31 * r[1] + a32 * r[4] + a33 * r[7];
        values[8] = a31 * r[2] + a32 * r[5] + a33 * r[8];
        return this;
    }

    public Matrix3x3 preRotateX( double radians )
    {
        final double cos = Math.cos( radians );
        final double sin = Math.sin( radians );
        final double a12 = values[1];
        final double a13 = values[2];
        final double a22 = values[4];
        final double a23 = values[5];
        final double a32 = values[7];
        final double a33 = values[8];
        values[1] = a12 * cos + a13 * sin;
        values[2] = a12 * -sin + a13 * cos;
        values[4] = a22 * cos + a23 * sin;
        values[5] = a22 * -sin + a23 * cos;
        values[7] = a32 * cos + a33 * sin;
        values[8] = a32 * -sin + a33 * cos;
        return this;
    }

    public Matrix3x3 preRotateZ( double radians )
    {
        final double cos = Math.cos( radians );
        final double sin = Math.sin( radians );
        final double a11 = values[0];
        final double a12 = values[1];
        final double a21 = values[3];
        final double a22 = values[4];
        final double a31 = values[6];
        final double a32 = values[7];
        values[0] = a11 * cos + a12 * sin;
        values[1] = a11 * -sin + a12 * cos;
        values[3] = a21 * cos + a22 * sin;
        values[4] = a21 * -sin + a22 * cos;
        values[6] = a31 * cos + a32 * sin;
        values[7] = a31 * -sin + a32 * cos;
        return this;
    }

    public Matrix3x3 postConcat( Matrix3x3 multiplyLeft )
    {
        final double a11 = values[0];
        final double a12 = values[1];
        final double a13 = values[2];
        final double a21 = values[3];
        final double a22 = values[4];
        final double a23 = values[5];
        final double a31 = values[6];
        final double a32 = values[7];
        final double a33 = values[8];
        final double[] l = multiplyLeft.values;
        values[0] = l[0] * a11 + l[1] * a21 + l[2] * a31;
        values[1] = l[0] * a12 + l[1] * a22 + l[2] * a32;
        values[2] = l[0] * a13 + l[1] * a23 + l[2] * a33;
        values[3] = l[3] * a11 + l[4] * a21 + l[5] * a31;
        values[4] = l[3] * a12 + l[4] * a22 + l[5] * a32;
        values[5] = l[3] * a13 + l[4] * a23 + l[5] * a33;
        values[6] = l[6] * a11 + l[7] * a21 + l[8] * a31;
        values[7] = l[6] * a12 + l[7] * a22 + l[8] * a32;
        values[8] = l[6] * a13 + l[7] * a23 + l[8] * a33;
        return this;
    }

    public Matrix3x3 postRotateX( double radians )
    {
        final double cos = Math.cos( radians );
        final double sin = Math.sin( radians );
        final double a21 = values[3];
        final double a22 = values[4];
        final double a23 = values[5];
        final double a31 = values[6];
        final double a32 = values[7];
        final double a33 = values[8];
        values[3] = cos * a21 + -sin * a31;
        values[4] = cos * a22 + -sin * a32;
        values[5] = cos * a23 + -sin * a33;
        values[6] = sin * a21 + cos * a31;
        values[7] = sin * a22 + cos * a32;
        values[8] = sin * a23 + cos * a33;
        return this;
    }

    public Matrix3x3 postRotateZ( double radians )
    {
        final double cos = Math.cos( radians );
        final double sin = Math.sin( radians );
        final double a11 = values[0];
        final double a12 = values[1];
        final double a13 = values[2];
        final double a21 = values[3];
        final double a22 = values[4];
        final double a23 = values[5];
        values[0] = cos * a11 + -sin * a21;
        values[1] = cos * a12 + -sin * a22;
        values[2] = cos * a13 + -sin * a23;
        values[3] = sin * a11 + cos * a21;
        values[4] = sin * a12 + cos * a22;
        values[5] = sin * a13 + cos * a23;
        return this;
    }

    public Matrix3x3 postScale( double scaleX, double scaleY, double scaleZ )
    {
        values[0] *= scaleX;
        values[1] *= scaleX;
        values[2] *= scaleX;
        values[3] *= scaleY;
        values[4] *= scaleY;
        values[5] *= scaleY;
        values[6] *= scaleZ;
        values[7] *= scaleZ;
        values[8] *= scaleZ;
        return this;
    }

    public Matrix3x3 preScale( double scaleX, double scaleY, double scaleZ )
    {
        values[0] *= scaleX;
        values[3] *= scaleX;
        values[6] *= scaleX;
        values[1] *= scaleY;
        values[4] *= scaleY;
        values[7] *= scaleY;
        values[2] *= scaleZ;
        values[5] *= scaleZ;
        values[8] *= scaleZ;
        return this;
    }

    public Matrix3x3 postTranslate( double shiftX, double shiftY )
    {
        values[0] += values[6] * shiftX;
        values[1] += values[7] * shiftX;
        values[2] += values[8] * shiftX;
        values[3] += values[6] * shiftY;
        values[4] += values[7] * shiftY;
        values[5] += values[8] * shiftY;
        return this;
    }

    public Matrix3x3 preTranslate( double shiftX, double shiftY )
    {
        values[2] += values[0] * shiftX + values[1] * shiftY;
        values[5] += values[3] * shiftX + values[4] * shiftY;
        values[8] += values[6] * shiftX + values[7] * shiftY;
        return this;
    }
}
