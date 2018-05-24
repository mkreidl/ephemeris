package com.mkreidl.ephemeris.solarsystem;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Spherical;

public abstract class AbstractModelVsop87 extends OrbitalModel
{
    protected final static int DIMENSION = 3;
    protected double[][][][] coeff;
    protected double[] results = new double[2 * DIMENSION];
    private long timeCached = 0;
    private long timeCachedVel = 0;

    public static final String MERCURY = "MERCURY";
    public static final String VENUS = "VENUS";
    public static final String EARTH = "EARTH";
    public static final String MARS = "MARS";
    public static final String JUPITER = "JUPITER";
    public static final String SATURN = "SATURN";
    public static final String URANUS = "URANUS";
    public static final String NEPTUNE = "NEPTUNE";

    protected void compute( Time time, boolean computeVelocity )
    {
        if ( time.getTime() != timeCached || computeVelocity && timeCachedVel != timeCached )
        {
            final double t = time.julianDayNumberSince( Time.J2000 ) / Time.DAYS_PER_MILLENNIUM;
            for ( int dim = 0; dim < DIMENSION; dim++ )
            {
                double pos = 0.0;
                double vel = 0.0;
                for ( int n = coeff[dim].length - 1; n > 0; --n )
                {
                    final double c = cosSeries( dim, n, t );
                    pos = pos * t + c;
                    if ( computeVelocity )
                    {
                        final double s = sinSeries( dim, n - 1, t );
                        vel = vel * t + s + n * c;
                    }
                }
                results[dim] = pos * t + cosSeries( dim, 0, t );
                results[dim + DIMENSION] = vel / ( Time.DAYS_PER_MILLENNIUM * Time.SECONDS_PER_DAY );
            }
            // remember time for which last calculation was performed
            timeCached = time.getTime();
            if ( computeVelocity )
                timeCachedVel = timeCached;
        }
    }

    private double cosSeries( int dim, int n, double time )
    {
        double result = 0.0;
        for ( double[] triple : coeff[dim][n] )
            result += triple[0] * Math.cos( triple[1] + triple[2] * time );
        return result;
    }

    private double sinSeries( int dim, int n, double time )
    {
        double result = 0.0;
        for ( double[] triple : coeff[dim][n] )
            result -= triple[0] * triple[2] * Math.sin( triple[1] + triple[2] * time );
        return result;
    }

    public abstract static class XYZ extends AbstractModelVsop87
    {
        private final Cartesian cartesianPos = new Cartesian();
        private final Cartesian cartesianVel = new Cartesian();

        @Override
        public void compute( Time time, Cartesian position, Cartesian velocity )
        {
            compute( time, velocity != null );
            position.set( results[0], results[1], results[2] );
            if ( velocity != null )
                velocity.set( results[3], results[4], results[5] );
        }

        public void compute( Time time, Spherical position, Spherical velocity )
        {
            compute( time, cartesianPos, velocity != null ? cartesianVel : null );
            cartesianPos.transform( position );
            if ( velocity != null )
                cartesianVel.transformVelocity( cartesianPos, velocity );
        }
    }

    public abstract static class LBR extends AbstractModelVsop87
    {
        private final Spherical sphericalPos = new Spherical();
        private final Spherical sphericalVel = new Spherical();

        /**
         * Calculate position and velocity in spherical coordinates
         *
         * @param time
         * @param position
         * @param velocity Units: [rad/day], [rad], [rad]
         * @return
         */
        @Override
        public void compute( Time time, Spherical position, Spherical velocity )
        {
            compute( time, velocity != null );
            position.set( results[2], results[0], results[1] );
            if ( velocity != null )
                velocity.set( results[5], results[3], results[4] );
        }

        public void compute( Time time, Cartesian position, Cartesian velocity )
        {
            compute( time, sphericalPos, velocity != null ? sphericalVel : null );
            sphericalPos.transform( position );
            if ( velocity != null )
                sphericalVel.transformVelocity( sphericalPos, velocity );
        }
    }
}
