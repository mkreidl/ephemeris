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
    private final int[] split = new int[4];

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

    @Override
    public String toString()
    {
        return toStringAs( unit );
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
            default:
                return radians;
        }
    }

    public int[] get( Unit unit, int[] signHrsMinSec )
    {
        if ( unit != Unit.DEGREES && unit != Unit.HOURS )
            throw new IllegalArgumentException( "Splitting off minutes and seconds requires DEGREES or HOURS" );
        signHrsMinSec[0] = radians > 0 ? 1 : ( radians < 0 ? -1 : 0 );
        int s = (int)Math.round( Math.abs( get( unit ) * 3600 ) );
        signHrsMinSec[3] = s % 60;
        s /= 60;
        signHrsMinSec[2] = s % 60;
        signHrsMinSec[1] = s / 60;
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

    public synchronized String toStringAs( Unit unit )
    {
        switch ( unit )
        {
            case HOURS:
                get( unit, split );
                return ( radians >= 0.0 ? "" : "-" ) + String.format( "%02d:%02d:%02d", split[1], split[2], split[3] );
            case DEGREES:
                get( unit, split );
                return ( radians >= 0.0 ? "+" : "-" ) + String.format( "%02dd %02d' %02d\"", split[1], split[2], split[3] );
            default:
                return String.format( "%f", radians );
        }
    }
}
