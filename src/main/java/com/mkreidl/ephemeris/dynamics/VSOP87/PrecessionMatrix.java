package com.mkreidl.ephemeris.dynamics.VSOP87;

import com.mkreidl.ephemeris.*;
import com.mkreidl.ephemeris.geometry.*;

import static com.mkreidl.ephemeris.Time.*;


public class PrecessionMatrix extends Matrix
{
    public PrecessionMatrix compute( final Time time )
    {
        final double t = time.julianDayNumberSince( J2000 ) / DAYS_PER_MILLENNIUM;

        final double s11 = evaluatePolynomial( t, S11 );
        final double c11 = evaluatePolynomial( t, C11 );
        final double s12 = evaluatePolynomial( t, S12 );
        final double c12 = evaluatePolynomial( t, C12 );
        final double s13 = evaluatePolynomial( t, S13 );
        final double c13 = evaluatePolynomial( t, C13 );

        final double xi = 0.243817483530 * t;
        final double cos_xi = Math.cos( xi );
        final double sin_xi = Math.sin( xi );

        values[0] = s11 * sin_xi + c11 * cos_xi;
        values[1] = s12 * sin_xi + c12 * cos_xi;
        values[2] = s13 * sin_xi + c13 * cos_xi;
        values[3] = c11 * sin_xi - s11 * cos_xi;
        values[4] = c12 * sin_xi - s12 * cos_xi;
        values[5] = c13 * sin_xi - s13 * cos_xi;
        values[6] = evaluatePolynomial( t, A31 );
        values[7] = evaluatePolynomial( t, A32 );
        values[8] = evaluatePolynomial( t, A33 );

        return this;
    }

    private static double evaluatePolynomial( double t, double[] polynomial )
    {
        final int deg = polynomial.length;
        double result = polynomial[deg - 1];
        for ( int i = polynomial.length - 2; i >= 0; i-- )
            result = result * t + polynomial[i];
        return result * 1e-12;
    }

    private static final double[] S11 = new double[]{0.0, 0.0, -538867722.0, -270670.0, 1138205.0, 8604.0, -813.0};
    private static final double[] C11 = new double[]{1e12, 0.0, -20728.0, -19147.0, -149390.0, -34.0, 617.0};
    private static final double[] S12 = new double[]{-1e12, 0.0, 2575043.0, -56157.0, 140001.0, 383.0, -613.0};
    private static final double[] C12 = new double[]{0.0, 0, -539329768.0, -479046.0, 1144883.0, 8884.0, -830.0};
    private static final double[] S13 = new double[]{0.0, 2269380040.0, -24745348.0, -2422542.0, 78247.0, -468.0, 134.0};
    private static final double[] C13 = new double[]{0.0, -203607820.0, -94040878.0, 2307025.0, 37729.0, -4862.0, 25.0};
    private static final double[] A31 = new double[]{0.0, 203607820.0, 94040878.0, -1083606.0, -50218.0, 929.0, 11.0};
    private static final double[] A32 = new double[]{0.0, 2269380040.0, -24745348.0, -2532307.0, 27473.0, 643.0, -1.0};
    private static final double[] A33 = new double[]{1e12, 0.0, -2595771.0, 37009.0, 1236.0, -13.0, 0};
}
