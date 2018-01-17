package com.mkreidl.ephemeris.geometry;

import static java.lang.Math.PI;

public class Angle
{
    public enum Unit
    {
        RADIANS, DEGREES, HOURS,
        MIN, SEC, MAS
    }

    public static final double DEG = PI / 180;
    public static final double HRS = PI / 12;
    public static final double MIN = DEG / 60;
    public static final double SEC = MIN / 60;
    public static final double MAS = SEC / 1000;
    public static final double HOURS = 12 / PI;

    protected double radians = 0.0;

    public static double standardize( double angle )
    {
        angle = standardizePositive( angle );
        return angle - ( angle > PI ? 2 * PI : 0 );
    }

    public static double standardizePositive( double angle )
    {
        angle %= 2 * PI;
        return angle >= 0 ? angle : angle + 2 * PI;
    }

    public Angle()
    {
    }

    public Angle( double d, Unit unit )
    {
        set( d, unit );
    }

    public double getRadians()
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

    public Angle setRadians( double d )
    {
        return set( d, Unit.RADIANS );
    }

    public Angle set( double d, Unit unit )
    {
        switch ( unit )
        {
            case HOURS:
                radians = d * HRS;
                return this;
            case DEGREES:
                radians = d * DEG;
                return this;
            case MIN:
                radians = d * MIN;
                return this;
            case SEC:
                radians = d * SEC;
                return this;
            case MAS:
                radians = d * MAS;
                return this;
            default:
                radians = d;
                return this;
        }
    }

    public Angle set( Angle other )
    {
        setRadians( other.getRadians() );
        return this;
    }

    @Override
    public String toString()
    {
        return String.format( java.util.Locale.getDefault(), "%f", radians );
    }

    public static class Sexagesimal extends Angle
    {
        private Angle.Unit unit;
        private int sign, integral, minutes, seconds;

        public Sexagesimal( Angle.Unit unit )
        {
            if ( unit != Unit.DEGREES && unit != Unit.HOURS )
                throw new IllegalArgumentException( "Splitting off minutes and seconds requires DEGREES or HOURS" );
            this.unit = unit;
        }

        public Sexagesimal setRadians( double value )
        {
            super.setRadians( value );
            computeSexagesimal();
            return this;
        }

        public double get()
        {
            return get( unit );
        }

        public void set( double value )
        {
            set( value, unit );
            computeSexagesimal();
        }

        public int getSign()
        {
            return sign;
        }

        public int getIntegral()
        {
            return integral;
        }

        public int getMinutes()
        {
            return minutes;
        }

        public int getSeconds()
        {
            return seconds;
        }

        public Sexagesimal set( double value, Unit unit )
        {
            super.set( value, unit );
            computeSexagesimal();
            return this;
        }

        private void computeSexagesimal()
        {
            int s = (int)Math.round( get( unit ) * 3600 );
            sign = s > 0 ? 1 : ( s < 0 ? -1 : 0 );
            s = Math.abs( s );
            seconds = s % 60;
            s /= 60;
            minutes = s % 60;
            integral = s / 60;
        }

        @Override
        public String toString()
        {
            final java.util.Locale locale = java.util.Locale.getDefault();
            final String formatString;
            switch ( unit )
            {
                case HOURS:
                    formatString = "%02d:%02d:%02d";
                    break;
                case DEGREES:
                    formatString = "%02dd %02d' %02d\"";
                    break;
                default:
                    return String.format( locale, "%f", radians );
            }
            return ( radians >= 0.0 ? "" : "-" ) + String.format( locale, formatString, integral, minutes, seconds );
        }
    }
}
