package com.mkreidl.ephemeris.sky.coordinates;

import com.mkreidl.ephemeris.geometry.Angle;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Coordinates;
import com.mkreidl.ephemeris.geometry.Spherical;

public interface Equatorial
{
    Cartesian toEcliptical( double ecliptic, Cartesian eclipticalCart );

    Spherical toEcliptical(double ecliptic, Spherical eclipticalCart );

    Cartesian toHorizontal(Spherical zenith, Cartesian horizontal );

    Spherical toHorizontal(Spherical zenith, Spherical horizontal );

    final class Cart extends Cartesian implements Equatorial
    {

        @Override
        public Cartesian toEcliptical( double ecliptic, Cartesian eclipticalCart )
        {
            return eclipticalCart.set( this ).rotate( Axis.X, -ecliptic );
        }

        @Override
        public Spherical toEcliptical(double ecliptic, Spherical eclipticalSphe )
        {
            return toEcliptical( ecliptic, eclipticalSphe.tmpCartesian ).transform( eclipticalSphe );
        }

        @Override
        public Cartesian toHorizontal(Spherical zenith, Cartesian horizontal )
        {
            return horizontal.set( this )
                    .rotate( Coordinates.Axis.Z, -zenith.lon )
                    .rotate( Coordinates.Axis.Y, Math.PI / 2 - zenith.lat );
        }

        @Override
        public Spherical toHorizontal(Spherical zenith, Spherical spherical )
        {
            return toHorizontal( zenith, spherical.tmpCartesian ).transform( spherical );
        }
    }

    final class Sphe extends Spherical implements Equatorial
    {
        private final Equatorial.Cart tmp = new Equatorial.Cart();

        @Override
        public Cartesian toEcliptical( double ecliptic, Cartesian eclipticalCart )
        {
            return transform( eclipticalCart ).rotate( Axis.X, -ecliptic );
        }

        @Override
        public Spherical toEcliptical(double ecliptic, Spherical eclipticalSphe )
        {
            return toEcliptical( ecliptic, tmpCartesian ).transform( eclipticalSphe );
        }

        @Override
        public Cartesian toHorizontal(Spherical zenith, Cartesian horizontal )
        {
            transform( tmp );
            return tmp.toHorizontal( zenith, horizontal );
        }

        @Override
        public Spherical toHorizontal(Spherical zenith, Spherical spherical )
        {
            return toHorizontal( zenith, tmpCartesian ).transform( spherical );
        }

        public Angle getRightAscension( Angle angle )
        {
            return angle.set( lon < 0 ? lon + 2 * Math.PI : lon, Angle.Unit.RADIANS );
        }

        public Angle getDeclination( Angle angle )
        {
            return angle.set( lat, Angle.Unit.RADIANS );
        }
    }
}
