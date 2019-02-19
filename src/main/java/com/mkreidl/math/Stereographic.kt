package com.mkreidl.math

class Stereographic private constructor(private val centerZ: Double) {

    companion object {
        val N = Stereographic(1.0)
        val S = Stereographic(-1.0)
    }

    fun project1D(lat: Double): Double {
        val scale = 1.0 - centerZ * Math.sin(lat)
        val d = Math.cos(lat)
        return if (scale == 0.0 || d == 0.0 && scale < 1.0)
            Double.POSITIVE_INFINITY
        else
            d / scale
    }

    fun project(input: Vector3): Vector3 {
        val scale = centerZ / (centerZ - input.z)
        return Vector3(input.x * scale, input.y * scale, 0.0)
    }

    fun project(input: Spherical3): Spherical3 {
        val scale = centerZ / (1.0 - input.dst * Math.sin(input.lat))
        if (scale < 0.0)
        // TODO: This might be generalized
            throw IllegalArgumentException("Projection in polar coordinates impossible")
        return input.copy(dst = input.dst * Math.cos(input.lat) * scale, lat = 0.0)
    }

    fun getJacobian(locus: Vector3): Matrix3x3 {
        val stretch = 1.0 - locus.z / centerZ
        val dzStretch = stretch * stretch / centerZ
        return Matrix3x3(
                stretch, 0.0, dzStretch * locus.x,
                0.0, stretch, dzStretch * locus.y,
                0.0, 0.0, 0.0
        )
    }

    /**
     * Calculate the stereographic projection of any circle on the unit sphere.
     * A circle on the unit sphere is given by a normalized vector orthogonal
     * to the plane through the circle on the unit sphere, and by the
     * (spherical) radius alpha -- the angle between the center and a coordinates on
     * the circle.
     *
     * @param poleOfCircle (Spherical3) midpoint of the circle.
     * @param alpha        (Spherical3) radius of the circle
     * @param projection   Return value
     * @return Circle      The image of the spherical circle under stereographic projection
     * Special cases:
     * (1) r == Inf means the circle is actually a line which is NOT passing the origin of
     * the coordinate system. Then x and y denote the point on the line which is closest to
     * the origin.
     * (2) r == NaN means the circle is actually a line through the origin of the coordinate
     * system. Then x and y describe a unit vector orthogonal to that line.
     */
    fun project(poleOfCircle: Spherical3, alpha: Double): Circle {
        val north = project1D(poleOfCircle.lat + alpha)
        val south = project1D(poleOfCircle.lat - alpha)
        val dist: Double
        val r = Math.abs(north - south) * 0.5
        dist = if (java.lang.Double.isInfinite(r))
            if (1 + north == 1.0 || 1 + south == 1.0) {
                // clear very small values: 1 + 1e-17 == 1 but 1e-17 != 0
                return Circle(Math.cos(poleOfCircle.lon), Math.sin(poleOfCircle.lon), Double.NaN)
            } else
                if (centerZ == 1.0) south else north
        else
            (north + south) * 0.5
        return Circle(dist * Math.cos(poleOfCircle.lon), dist * Math.sin(poleOfCircle.lon), r)
    }

    /**
     * Calculate the stereographic projection of any circle on the unit sphere.
     *
     * A circle on the unit sphere is given by a normalized vector orthogonal
     * to the plane through the circle on the unit sphere, and by the
     * (spherical) radius alpha -- the angle between the center and a coordinates on
     * the circle.
     *
     *
     * Implements the formula for the stereographic projection
     * of a circle on the unit sphere. The image on the equatorial
     * plane is again a circle.
     *
     *
     * In this context, a circle on the unit sphere is given by
     * its midpoint (a coordinates on the sphere) and its radius (the
     * angle between the midpoint and any coordinates on the circle).
     * A circle in the plane is given by its midpoint and its
     * radius.
     *
     *
     * The formula is derived as follows:
     *
     *
     * (1) Given a circle ((xc, yc), radius) in the plane, derive
     * the formula for midpoint and radius of its preimage under
     * stereographic projection.
     *
     *
     * Equation of plane circle: (x-xc)^2 + (y-yc)^2 = radius^2
     * For x and y, substitute
     * x = X/(1-Z), y = Y/(1-Z)
     * where (X,Y,Z) is interpreted as the preimage of (x,y) under
     * stereographic projection.
     *
     *
     * Combining the so obtained equation with
     * X^2 + Y^2 + Z^2 = 1
     * we obain an equation \emph{linear} in X,Y,Z:
     *
     *
     * 2xc*X + 2yc*Y - (1-xc^2-y^2+r^2) = (1+xc^2+yc^2-r^2)
     *
     *
     * One of the unit vectors orthogonal to this plane is the
     * center of the preimage circle on the sphere. From the
     * orthogonal distance R of the plane from the origin, we
     * obtain the radius of the preimage circle.
     *
     *
     * (Open question: Which of the two unit vectors is the correct
     * one in which case?)
     *
     *
     * (2) Now go the other direction: Given a circle on the unit
     * sphere, get the \emph{radius} of its image under
     * stereographic projection. Obtain the following formula:
     *
     *
     * r = Math.sin(alpha) * (Math.cos(alpha) - z) /
     * / ( (1-zcos(alpha))^2 - (xsin(alpha))^2 )
     *
     *
     * Into the expression for R as derived in (1), substitute
     * the expression for r just computed and thereby obtain
     * an expression for xc^2 + yc^2, depending on (X,Y,Z,R).
     * Since (xc,yc) must be a multiple of (X,Y), we are done.
     */

}
