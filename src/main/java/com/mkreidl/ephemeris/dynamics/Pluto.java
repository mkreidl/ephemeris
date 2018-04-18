package com.mkreidl.ephemeris.dynamics;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Spherical;

public class Pluto extends OrbitalModel<Cartesian>
{
    private final Spherical posSpherical = new Spherical();

    @Override
    public void compute( Time time, Cartesian position, Cartesian velocity )
    {
        final double d = time.terrestrialDynamicalTime();

        final double s0 = Math.toRadians( 50.03 );
        final double s1 = Math.toRadians( 0.033459652 );
        final double p0 = Math.toRadians( 238.95 );
        final double p1 = Math.toRadians( 0.003968789 );

        final double s = s0 + s1 * d;
        final double p = p0 + p1 * d;

        posSpherical.lon = Math.toRadians( 238.9508 + 0.00400703 * d
                - 19.799 * Math.sin( p ) + 19.848 * Math.cos( p )
                + 0.897 * Math.sin( 2 * p ) - 4.956 * Math.cos( 2 * p )
                + 0.610 * Math.sin( 3 * p ) + 1.211 * Math.cos( 3 * p )
                - 0.341 * Math.sin( 4 * p ) - 0.190 * Math.cos( 4 * p )
                + 0.128 * Math.sin( 5 * p ) - 0.034 * Math.cos( 5 * p )
                - 0.038 * Math.sin( 6 * p ) + 0.031 * Math.cos( 6 * p )
                + 0.020 * Math.sin( s - p ) - 0.010 * Math.cos( s - p ) );

        posSpherical.lat = Math.toRadians( -3.9082
                - 5.453 * Math.sin( p ) - 14.975 * Math.cos( p )
                + 3.527 * Math.sin( 2 * p ) + 1.673 * Math.cos( 2 * p )
                - 1.051 * Math.sin( 3 * p ) + 0.328 * Math.cos( 3 * p )
                + 0.179 * Math.sin( 4 * p ) - 0.292 * Math.cos( 4 * p )
                + 0.019 * Math.sin( 5 * p ) + 0.100 * Math.cos( 5 * p )
                - 0.031 * Math.sin( 6 * p ) - 0.026 * Math.cos( 6 * p )
                + 0.011 * Math.cos( s - p ) );

        posSpherical.dst = 40.72
                + 6.68 * Math.sin( p ) + 6.90 * Math.cos( p )
                - 1.18 * Math.sin( 2 * p ) - 0.03 * Math.cos( 2 * p )
                + 0.15 * Math.sin( 3 * p ) - 0.14 * Math.cos( 3 * p );

        posSpherical.transform( position );

        if ( velocity != null )
        {
            final double lp = Math.toRadians( 0.00400703 + p1 * (
                    -19.799 * Math.cos( p ) - 19.848 * Math.sin( p )
                            + 0.897 * 2 * Math.cos( 2 * p ) + 4.956 * 2 * Math.sin( 2 * p )
                            + 0.610 * 3 * Math.cos( 3 * p ) - 1.211 * 3 * Math.sin( 3 * p )
                            - 0.341 * 4 * Math.cos( 4 * p ) + 0.190 * 4 * Math.sin( 4 * p )
                            + 0.128 * 5 * Math.cos( 5 * p ) + 0.034 * 5 * Math.sin( 5 * p )
                            - 0.038 * 6 * Math.cos( 6 * p ) - 0.031 * 6 * Math.sin( 6 * p ) )
                    + ( s1 - p1 ) * ( 0.020 * Math.cos( s - p ) + 0.010 * Math.sin( s - p ) ) );

            final double bp = Math.toRadians( p1 * (
                    -5.453 * Math.cos( p ) + 14.975 * Math.sin( p )
                            + 3.527 * 2 * Math.cos( 2 * p ) - 1.673 * 2 * Math.sin( 2 * p )
                            - 1.051 * 3 * Math.cos( 3 * p ) - 0.328 * 3 * Math.sin( 3 * p )
                            + 0.179 * 4 * Math.cos( 4 * p ) + 0.292 * 4 * Math.sin( 4 * p )
                            + 0.019 * 5 * Math.cos( 5 * p ) - 0.100 * 5 * Math.sin( 5 * p )
                            - 0.031 * 6 * Math.cos( 6 * p ) + 0.026 * 6 * Math.sin( 6 * p ) )
                    - ( s1 - p1 ) * 0.011 * Math.sin( s - p ) );

            final double rp = p1 * (
                    +6.68 * Math.cos( p ) - 6.90 * Math.sin( p )
                            - 1.18 * 2 * Math.cos( 2 * p ) + 0.03 * 2 * Math.sin( 2 * p )
                            + 0.15 * 3 * Math.cos( 3 * p ) + 0.14 * 3 * Math.sin( 3 * p ) );

            final double r = posSpherical.dst;
            final double sl = Math.sin( posSpherical.lon );
            final double sb = Math.sin( posSpherical.lat );
            final double cl = Math.cos( posSpherical.lon );
            final double cb = Math.cos( posSpherical.lat );

            velocity.x = ( rp * cl * cb - r * ( lp * sl * cb + bp * cl * sb ) ) / Time.SECONDS_PER_DAY;
            velocity.y = ( rp * sl * cb + r * ( lp * cl * cb - bp * sl * sb ) ) / Time.SECONDS_PER_DAY;
            velocity.z = ( rp * sb + r * bp * cb ) / Time.SECONDS_PER_DAY;
        }
    }

}
