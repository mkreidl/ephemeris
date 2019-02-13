package com.mkreidl.ephemeris.sky.coordinates;

import com.mkreidl.ephemeris.geometry.Angle;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Coordinates;
import com.mkreidl.ephemeris.geometry.Spherical;

public interface Horizontal
{
    Cartesian toEquatorial( Equatorial.Sphe zenith, Cartesian equatorial );

    Spherical toEquatorial(Equatorial.Sphe zenith, Spherical equatorial );

    final class Cart extends Cartesian implements Horizontal
    {
        @Override
        public Cartesian toEquatorial( Equatorial.Sphe zenith, Cartesian equatorial )
        {
            equatorial.set( this );
            equatorial.rotate( Coordinates.Axis.Y, zenith.lat - Math.PI / 2 );
            equatorial.rotate( Coordinates.Axis.Z, zenith.lon );
            return equatorial;
        }

        @Override
        public Spherical toEquatorial(Equatorial.Sphe zenith, Spherical spherical )
        {
            return toEquatorial( zenith, spherical.tmpCartesian ).transform( spherical );
        }
    }

    final class Sphe extends Spherical implements Horizontal
    {
        private final Horizontal.Cart tmpHorizontalCart = new Horizontal.Cart();

        @Override
        public Cartesian toEquatorial( Equatorial.Sphe zenith, Cartesian equatorial )
        {
            transform( tmpHorizontalCart );
            return tmpHorizontalCart.toEquatorial( zenith, equatorial );
        }

        @Override
        public Spherical toEquatorial(Equatorial.Sphe zenith, Spherical equatorial )
        {
            return toEquatorial( zenith, equatorial.tmpCartesian ).transform( equatorial );
        }

        /**
         * Calculate azimuth with N=0deg, E=90deg, S=180deg, W=270deg
         *
         * @param angle
         * @return
         */
        public Angle getAzimuth( Angle angle )
        {
            final double azimuth = Math.PI - lon;
            return angle.set( azimuth < 0 ? azimuth + 2 * Math.PI : azimuth, Angle.Unit.RADIANS );
        }

        public Angle getHeight( Angle angle )
        {
            return angle.set( lat, Angle.Unit.RADIANS );
        }
    }
}
