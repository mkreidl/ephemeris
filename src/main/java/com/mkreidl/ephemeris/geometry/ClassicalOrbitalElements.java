package com.mkreidl.ephemeris.geometry;

import static java.lang.Math.*;

public class ClassicalOrbitalElements extends OrbitalElements
{
    public double node;
    public double incl;
    public double periapsis;
    public double axis;
    public double exc;
    public double meanAnom;

    public ClassicalOrbitalElements set( ClassicalOrbitalElements orig )
    {
        this.node = orig.node;
        this.incl = orig.incl;
        this.periapsis = orig.periapsis;
        this.axis = orig.axis;
        this.exc = orig.exc;
        this.meanAnom = orig.meanAnom;
        return this;
    }

    public ClassicalOrbitalElements set( double[] values )
    {
        this.node = values[0];
        this.incl = values[1];
        this.periapsis = values[2];
        this.axis = values[3];
        this.exc = values[4];
        this.meanAnom = values[5];
        return this;
    }

    public double meanLongitude()
    {
        return node + periapsis + meanAnom;
    }

    /**
     * Solve Kepler's equation with Newton method
     *
     * @param eps Required precision
     * @return
     */
    public double eccentricAnomaly( double eps )
    {
        double e0;
        double excentricAnom = meanAnom;
        // Solve Kepler's equation E - e*sin(E) = M by Newton iteration
        do
        {
            e0 = excentricAnom;
            excentricAnom = e0 - ( e0 - exc * sin( e0 ) - meanAnom ) / ( 1.0 - exc * cos( e0 ) );
        } while ( abs( excentricAnom - e0 ) > eps );
        return excentricAnom;
    }

    public void toVSOP87OrbitalElements( VSOP87OrbitalElements output )
    {
        output.a = axis;
        output.l = meanLongitude();
        output.h = exc * sin( node + periapsis );
        output.k = exc * cos( node + periapsis );
        output.p = sin( 0.5 * incl ) * sin( node );
        output.q = sin( 0.5 * incl ) * cos( node );
    }

    public void computePosition()
    {
        double eccentricAnom = eccentricAnomaly( 1e-15 );

        // Cartesian coordinates in the orbital plane,
        // The ascending node defines the x-axis
        double xv = axis * ( cos( eccentricAnom ) - exc );
        double yv = axis * ( sqrt( 1.0 - exc * exc ) * sin( eccentricAnom ) );

        // Cartesian coordinates after rotation to account for periapsis
        double xw = xv * cos( periapsis ) - yv * sin( periapsis );
        double yw = xv * sin( periapsis ) + yv * cos( periapsis );

        eclipticalCartesian.x = cos( node ) * xw - sin( node ) * yw * cos( incl );
        eclipticalCartesian.y = sin( node ) * xw + cos( node ) * yw * cos( incl );
        eclipticalCartesian.z = yw * sin( incl );

        eclipticalCartesian.transform( eclipticalSpherical );
    }

    public void add( ClassicalOrbitalElements summand )
    {
        this.node += summand.node;
        this.incl += summand.incl;
        this.periapsis += summand.periapsis;
        this.axis += summand.axis;
        this.exc += summand.exc;
        this.meanAnom += summand.meanAnom;
        standardize();
    }

    public void times( double x )
    {
        this.node *= x;
        this.incl *= x;
        this.periapsis *= x;
        this.axis *= x;
        this.exc *= x;
        this.meanAnom *= x;
        standardize();
    }

    private void standardize()
    {
        this.node = Angle.standardize( this.node );
        this.incl = Angle.standardize( this.incl );
        this.periapsis = Angle.standardize( this.periapsis );
        this.meanAnom = Angle.standardize( this.meanAnom );
    }

    @Override
    public String toString()
    {
        return "ClassicalOrbitalElements [node="
                + node
                + ", incl="
                + incl
                + ", periapsis="
                + periapsis
                + ", axis="
                + axis
                + ", exc="
                + exc
                + ", meanAnom="
                + meanAnom
                + "]";
    }

}
