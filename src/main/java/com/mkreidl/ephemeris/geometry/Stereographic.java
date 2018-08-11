package com.mkreidl.ephemeris.geometry;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Stereographic
{
    protected double centerZ;

    public Stereographic( double centerZ )
    {
        this.centerZ = centerZ;
    }

    public double project1D( double lat )
    {
        final double scale = 1.0 - centerZ * sin( lat );
        final double d = cos( lat );
        return ( scale == 0.0 || ( d == 0.0 && scale < 1.0 ) )
                ? Double.POSITIVE_INFINITY
                : d / scale;
    }

    public Cartesian project( Cartesian input, Cartesian output )
    {
        final double scale = centerZ / ( centerZ - input.z );
        output.x = input.x * scale;
        output.y = input.y * scale;
        output.z = 0.0;
        return output;
    }

    public float[] project( Cartesian input, float[] output )
    {
        final double scale = centerZ / ( centerZ - input.z );
        output[0] = (float)( input.x * scale );
        output[1] = (float)( input.y * scale );
        return output;
    }

    public Spherical project( Spherical input, Spherical output )
    {
        final double scale = centerZ / (1.0 - input.dst * sin( input.lat ) );
        if ( scale < 0.0 ) // TODO: This might be generalized
            throw new IllegalArgumentException( "Projection in polar coordinates impossible" );
        output.dst = input.dst * cos( input.lat ) * scale;
        output.lat = 0.0;
        output.lon = input.lon;
        return output;
    }

    public Matrix3D getJacobian( Cartesian locus, Matrix3D j )
    {
        final double stretch = 1.0 - locus.z / centerZ;
        final double d_dz_stretch = stretch * stretch / centerZ;
        j.set(
                stretch, 0, d_dz_stretch * locus.x,
                0, stretch, d_dz_stretch * locus.y,
                0, 0, 0
        );
        return j;
    }

    /**
     * Calculate the stereographic projection of any circle on the unit sphere.
     * A circle on the unit sphere is given by a normalized vector orthogonal
     * to the plane through the circle on the unit sphere, and by the
     * (spherical) radius alpha -- the angle between the center and a coordinates on
     * the circle.
     *
     * @param poleOfCircle (Spherical) midpoint of the circle.
     * @param alpha        (Spherical) radius of the circle
     * @param projection   Return value
     * @return Circle      The image of the spherical circle under stereographic projection
     * Special cases:
     * (1) r == Inf means the circle is actually a line which is NOT passing the origin of
     * the coordinate system. Then x and y denote the point on the line which is closest to
     * the origin.
     * (2) r == NaN means the circle is actually a line through the origin of the coordinate
     * system. Then x and y describe a unit vector orthogonal to that line.
     */
    public Circle project( Spherical poleOfCircle, double alpha, Circle projection )
    {
        final double north = project1D( poleOfCircle.lat + alpha );
        final double south = project1D( poleOfCircle.lat - alpha );
        final double dist;
        projection.r = abs( north - south ) * 0.5;
        if ( Double.isInfinite( projection.r ) )
            if ( 1 + north == 1 || 1 + south == 1 )  // clear very small values: 1 + 1e-17 == 1 but 1e-17 != 0
            {
                dist = 1.0;
                projection.r = Double.NaN;
            }
            else
                dist = centerZ == 1.0 ? south : north;
        else
            dist = ( north + south ) * 0.5;
        projection.x = dist * cos( poleOfCircle.lon );
        projection.y = dist * sin( poleOfCircle.lon );
        return projection;
    }

    /**
     * Calculate the stereographic projection of any circle on the unit sphere.
     *
     * A circle on the unit sphere is given by a normalized vector orthogonal
     * to the plane through the circle on the unit sphere, and by the
     * (spherical) radius alpha -- the angle between the center and a coordinates on
     * the circle.
     * <p/>
     * Implements the formula for the stereographic projection
     * of a circle on the unit sphere. The image on the equatorial
     * plane is again a circle.
     * <p/>
     * In this context, a circle on the unit sphere is given by
     * its midpoint (a coordinates on the sphere) and its radius (the
     * angle between the midpoint and any coordinates on the circle).
     * A circle in the plane is given by its midpoint and its
     * radius.
     * <p/>
     * The formula is derived as follows:
     * <p/>
     * (1) Given a circle ((xc, yc), radius) in the plane, derive
     * the formula for midpoint and radius of its preimage under
     * stereographic projection.
     * <p/>
     * Equation of plane circle: (x-xc)^2 + (y-yc)^2 = radius^2
     * For x and y, substitute
     * x = X/(1-Z), y = Y/(1-Z)
     * where (X,Y,Z) is interpreted as the preimage of (x,y) under
     * stereographic projection.
     * <p/>
     * Combining the so obtained equation with
     * X^2 + Y^2 + Z^2 = 1
     * we obain an equation \emph{linear} in X,Y,Z:
     * <p/>
     * 2xc*X + 2yc*Y - (1-xc^2-y^2+r^2) = (1+xc^2+yc^2-r^2)
     * <p/>
     * One of the unit vectors orthogonal to this plane is the
     * center of the preimage circle on the sphere. From the
     * orthogonal distance R of the plane from the origin, we
     * obtain the radius of the preimage circle.
     * <p/>
     * (Open question: Which of the two unit vectors is the correct
     * one in which case?)
     * <p/>
     * (2) Now go the other direction: Given a circle on the unit
     * sphere, get the \emph{radius} of its image under
     * stereographic projection. Obtain the following formula:
     * <p/>
     * r = sin(alpha) * (cos(alpha) - z) /
     * / ( (1-zcos(alpha))^2 - (xsin(alpha))^2 )
     * <p/>
     * Into the expression for R as derived in (1), substitute
     * the expression for r just computed and thereby obtain
     * an expression for xc^2 + yc^2, depending on (X,Y,Z,R).
     * Since (xc,yc) must be a multiple of (X,Y), we are done.
     */

}
