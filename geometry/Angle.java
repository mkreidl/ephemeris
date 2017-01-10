package com.mkreidl.ephemeris.geometry;

import static java.lang.Math.PI;

public class Angle
{
    public enum Unit
    {
        RADIANS, DEGREES, HOURS,
        MIN, SEC, MAS;
    }

    public static final double DEG = PI / 180;
    public static final double HRS = PI / 12;
    public static final double MIN = DEG / 60;
    public static final double SEC = MIN / 60;
    public static final double MAS = SEC / 1000;
    public static final double HOURS = 12 / PI;

    private double radians = 0.0;
    private Unit unit = Unit.RADIANS;

    public static double standardize( double angle )
    {
        angle = standardizePositive( angle );
        if ( angle > PI )
            angle -= 2 * PI;
        return angle;
    }

    public static double standardizePositive( double angle )
    {
        angle %= ( 2 * PI );
        if ( angle < 0 )
            angle += 2 * PI;
        return angle;
    }

    public Angle()
    {
    }

    public Angle( double d, Unit unit )
    {
        set( d, unit );
    }

    public Angle standardize()
    {
        radians = standardize( radians );
        return this;
    }

    public Angle standardizePositive()
    {
        radians = standardizePositive( radians );
        return this;
    }

    public double get()
    {
        return radians;
    }

    public double get( Unit unit )
    {
        switch ( unit )
        {
            case DEGREES:
                return radians / DEG;
            case HOURS:
                return radians / HRS;
            case RADIANS:
                return radians;
            default:
                return radians;
        }
    }

    public int[] get( Unit unit, int[] signHrsMinSec )
    {
        if ( unit != Unit.DEGREES && unit != Unit.HOURS )
            throw new IllegalArgumentException( "Splitting off minutes and seconds requires DEGREES or HOURS" );
        final float v = (float)get( unit );
        signHrsMinSec[0] = (int)Math.signum( v );
        final float a = Math.abs( v );
        signHrsMinSec[1] = (int)a;
        final float m = ( a - signHrsMinSec[1] ) * 60;
        signHrsMinSec[2] = (int)m;
        signHrsMinSec[3] = Math.round( ( m - signHrsMinSec[2] ) * 60 );
        return signHrsMinSec;
    }

    public Angle set( double d, Unit unit )
    {
        this.unit = unit;
        switch ( unit )
        {
            case DEGREES:
                radians = d * DEG;
                break;
            case HOURS:
                radians = d * HRS;
                break;
            case RADIANS:
                radians = d;
                break;
        }
        return this;
    }

    @Override
    public String toString()
    {
        return toStringAs( unit );
    }

    public String toStringAs( Unit unit )
    {
        if ( unit == Unit.RADIANS )
            return String.format( "%f", radians );
        else
        {
            double hd = Math.abs( get( unit ) );
            long h = (long)hd;
            double mm = 60 * ( hd - h );
            long m = (long)mm;
            long s = (long)( 60 * ( mm - m ) );
            if ( unit == Unit.HOURS )
                return ( radians >= 0.0 ? "" : "-" ) + String.format( "%02d:%02d:%02d", h, m, s );
            else
                return ( radians >= 0.0 ? "+" : "-" ) + String.format( "%02dd %02d' %02d\"", h, m, s );
        }
    }
}
