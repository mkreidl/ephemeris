package com.mkreidl.ephemeris.sky.coordinates;

import com.mkreidl.ephemeris.geometry.Angle;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Coordinates;
import com.mkreidl.ephemeris.geometry.Spherical;


public interface Horizontal
{
    Cartesian toEquatorial( Equatorial.Sphe zenit, Cartesian equatorial );

    Spherical toEquatorial( Equatorial.Sphe zenit, Spherical equatorial );

    class Cart extends Cartesian implements Horizontal
    {
        @Override
        public Cartesian toEquatorial( Equatorial.Sphe zenit, Cartesian equatorial )
        {
            equatorial.set( this );
            equatorial.rotate( Coordinates.Axis.Y, zenit.lat - Math.PI / 2 );
            equatorial.rotate( Coordinates.Axis.Z, zenit.lon );
            return equatorial;
        }

        @Override
        public Spherical toEquatorial( Equatorial.Sphe zenit, Spherical spherical )
        {
            return toEquatorial( zenit, spherical.tmpCartesian ).transform( spherical );
        }
    }

    class Sphe extends Spherical implements Horizontal
    {
        private final Horizontal.Cart tmpHorizontalCart = new Horizontal.Cart();

        @Override
        public Cartesian toEquatorial( Equatorial.Sphe zenit, Cartesian equatorial )
        {
            return ( (Cart)transform( tmpHorizontalCart ) ).toEquatorial( zenit, equatorial );
        }

        @Override
        public Spherical toEquatorial( Equatorial.Sphe zenit, Spherical equatorial )
        {
            return toEquatorial( zenit, equatorial.tmpCartesian ).transform( equatorial );
        }

        /**
         * Calculate azimut with N=0deg, E=90deg, S=180deg, W=270deg
         *
         * @param angle
         * @return
         */
        public Angle getAzimuth( Angle angle )
        {
            final double azimut = Math.PI - lon;
            return angle.set( azimut < 0 ? azimut + 2 * Math.PI : azimut, Angle.Unit.RADIANS );
        }

        public Angle getHeight( Angle angle )
        {
            return angle.set( lat, Angle.Unit.RADIANS );
        }
    }
}