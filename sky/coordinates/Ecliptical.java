package com.mkreidl.ephemeris.sky.coordinates;

import com.mkreidl.ephemeris.geometry.*;


public interface Ecliptical
{
    Cartesian toEquatorial( double ecliptic, Cartesian cartesian );

    Spherical toEquatorial( double ecliptic, Spherical spherical );

    Cartesian toHorizontal( double ecliptic, Spherical zenit, Cartesian cartesian );

    Spherical toHorizontal( double ecliptic, Spherical zenit, Spherical spherical );

    class Cart extends Cartesian implements Ecliptical
    {
        private final Equatorial.Cart tmp = new Equatorial.Cart();

        @Override
        public Cartesian toEquatorial( double ecliptic, Cartesian cartesian )
        {
            return cartesian.set( this ).rotate( Axis.X, ecliptic );
        }

        @Override
        public Spherical toEquatorial( double ecliptic, Spherical spherical )
        {
            return toEquatorial( ecliptic, spherical.tmpCartesian ).transform( spherical );
        }

        @Override
        public Cartesian toHorizontal( double ecliptic, Spherical zenit, Cartesian cartesian )
        {
            return ( (Equatorial.Cart)toEquatorial( ecliptic, tmp ) )
                    .toHorizontal( zenit, cartesian );
        }

        @Override
        public Spherical toHorizontal( double ecliptic, Spherical zenit, Spherical spherical )
        {
            return toHorizontal( ecliptic, zenit, spherical.tmpCartesian ).transform( spherical );
        }
    }

    class Sphe extends Spherical implements Ecliptical
    {
        private final Ecliptical.Cart tmp = new Ecliptical.Cart();

        @Override
        public Cartesian toEquatorial( double ecliptic, Cartesian cartesian )
        {
            return ( (Ecliptical.Cart)transform( tmp ) )
                    .toEquatorial( ecliptic, cartesian );
        }

        @Override
        public Spherical toEquatorial( double ecliptic, Spherical spherical )
        {
            return toEquatorial( ecliptic, tmpCartesian ).transform( spherical );
        }

        @Override
        public Cartesian toHorizontal( double ecliptic, Spherical zenit, Cartesian cartesian )
        {
            return ( (Ecliptical.Cart)transform( tmp ) )
                    .toHorizontal( ecliptic, zenit, cartesian );
        }

        @Override
        public Spherical toHorizontal( double ecliptic, Spherical zenit, Spherical spherical )
        {
            return ( (Ecliptical.Cart)transform( tmp ) )
                    .toHorizontal( ecliptic, zenit, spherical );
        }

        public Angle getLongitude( Angle angle )
        {
            return angle.set( lon < 0 ? lon + 2 * Math.PI : lon, Angle.Unit.RADIANS );
        }

        public Angle getLatitude( Angle angle )
        {
            return angle.set( lat, Angle.Unit.RADIANS );
        }
    }
}
