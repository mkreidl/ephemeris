package com.mkreidl.ephemeris.dynamics.VSOP87;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.dynamics.OrbitalModel;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Coordinates;
import com.mkreidl.ephemeris.geometry.Spherical;

public abstract class Model<T extends Coordinates<T>> extends OrbitalModel<T>
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

    public static XYZ getVersionC( String planet )
    {
        switch ( planet )
        {
            case MERCURY:
                return new com.mkreidl.ephemeris.dynamics.VSOP87.C.Mercury();
            case VENUS:
                return new com.mkreidl.ephemeris.dynamics.VSOP87.C.Venus();
            case EARTH:
                return new com.mkreidl.ephemeris.dynamics.VSOP87.C.Earth();
            case MARS:
                return new com.mkreidl.ephemeris.dynamics.VSOP87.C.Mars();
            case JUPITER:
                return new com.mkreidl.ephemeris.dynamics.VSOP87.C.Jupiter();
            case SATURN:
                return new com.mkreidl.ephemeris.dynamics.VSOP87.C.Saturn();
            case URANUS:
                return new com.mkreidl.ephemeris.dynamics.VSOP87.C.Uranus();
            case NEPTUNE:
                return new com.mkreidl.ephemeris.dynamics.VSOP87.C.Neptune();
            default:
                throw new RuntimeException( "Model not implemented: " + planet );
        }
    }

    public static LBR getVersionD( String planet )
    {
        switch ( planet )
        {
            case MERCURY:
                return new com.mkreidl.ephemeris.dynamics.VSOP87.D.Mercury();
            case VENUS:
                return new com.mkreidl.ephemeris.dynamics.VSOP87.D.Venus();
            case EARTH:
                return new com.mkreidl.ephemeris.dynamics.VSOP87.D.Earth();
            case MARS:
                return new com.mkreidl.ephemeris.dynamics.VSOP87.D.Mars();
            case JUPITER:
                return new com.mkreidl.ephemeris.dynamics.VSOP87.D.Jupiter();
            case SATURN:
                return new com.mkreidl.ephemeris.dynamics.VSOP87.D.Saturn();
            case URANUS:
                return new com.mkreidl.ephemeris.dynamics.VSOP87.D.Uranus();
            case NEPTUNE:
                return new com.mkreidl.ephemeris.dynamics.VSOP87.D.Neptune();
            default:
                throw new RuntimeException( "Model not implemented: " + planet );
        }
    }

    public static LBR getVersionDsimplified( String planet )
    {
        switch ( planet )
        {
            case MERCURY:
                return new com.mkreidl.ephemeris.dynamics.VSOP87.DMeeus.Mercury();
            case VENUS:
                return new com.mkreidl.ephemeris.dynamics.VSOP87.DMeeus.Venus();
            case EARTH:
                return new com.mkreidl.ephemeris.dynamics.VSOP87.DMeeus.Earth();
            case MARS:
                return new com.mkreidl.ephemeris.dynamics.VSOP87.DMeeus.Mars();
            case JUPITER:
                return new com.mkreidl.ephemeris.dynamics.VSOP87.DMeeus.Jupiter();
            case SATURN:
                return new com.mkreidl.ephemeris.dynamics.VSOP87.DMeeus.Saturn();
            case URANUS:
                return new com.mkreidl.ephemeris.dynamics.VSOP87.DMeeus.Uranus();
            case NEPTUNE:
                return new com.mkreidl.ephemeris.dynamics.VSOP87.DMeeus.Neptune();
            default:
                throw new RuntimeException( "Model not implemented: " + planet );
        }
    }

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

    public abstract static class XYZ extends Model<Cartesian>
    {
        @Override
        public void compute( Time time, Cartesian position, Cartesian velocity )
        {
            compute( time, velocity != null );
            position.set( results[0], results[1], results[2] );
            if ( velocity != null )
                velocity.set( results[3], results[4], results[5] );
        }
    }

    public abstract static class LBR extends Model<Spherical>
    {
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
    }
}
