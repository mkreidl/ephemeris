package com.mkreidl.ephemeris.geometry;

import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.sqrt;

import static com.mkreidl.ephemeris.geometry.Angle.standardize;

/**
 * @author martin
 *
 */
public class VSOP87OrbitalElements extends OrbitalElements
{
    private final ClassicalOrbitalElements classical = new ClassicalOrbitalElements();

    public double a, l, h, k, p, q;

    public VSOP87OrbitalElements()
    {
        eclipticalCartesian = classical.eclipticalCartesian;
        eclipticalSpherical = classical.eclipticalSpherical;
    }

    public void toClassicalOrbitalElements( ClassicalOrbitalElements output )
    {
        output.incl = 2.0 * asin( sqrt( p * p + q * q ) );
        output.node = atan2( p, q );
        output.periapsis = atan2( h, k ) - output.node;

        output.axis = a;
        output.exc = sqrt( h * h + k * k );
        output.meanAnom = l - output.node - output.periapsis;

        output.node = standardize( output.node );
        output.incl = standardize( output.incl );
        output.periapsis = standardize( output.periapsis );
        output.meanAnom = standardize( output.meanAnom );
    }

    @Override
    public String toString()
    {
        return "VSOP87OrbitalElements [a="
                + a
                + ", l="
                + l
                + ", h="
                + h
                + ", k="
                + k
                + ", p="
                + p
                + ", q="
                + q
                + "]";
    }

    @Override
    public void calculatePosition()
    {
        toClassicalOrbitalElements( classical );
        classical.calculatePosition();
    }

}
