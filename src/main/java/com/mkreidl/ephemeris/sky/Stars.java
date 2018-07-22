package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Coordinates;
import com.mkreidl.ephemeris.geometry.Matrix;
import com.mkreidl.ephemeris.geometry.Stereographic;
import com.mkreidl.ephemeris.sky.coordinates.Ecliptical;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;
import com.mkreidl.ephemeris.solarsystem.PrecessionMatrix;
import com.mkreidl.ephemeris.solarsystem.SolarSystemVSOP87C;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Stars
{
    private static final Ecliptical.Cart[] POS_J2000 = new Ecliptical.Cart[StarsCatalog.SIZE];
    private static final Ecliptical.Cart[] VEL_J2000 = new Ecliptical.Cart[StarsCatalog.SIZE];

    private final PrecessionMatrix precession = new PrecessionMatrix();
    private final Matrix rotation = new Matrix();
    private final Matrix transformation = new Matrix();
    private final Equatorial.Cart[] positions = new Equatorial.Cart[StarsCatalog.SIZE];
    private int numberOfThreads = 8;
    private int numCalcPerThread = StarsCatalog.SIZE / numberOfThreads + 1;
    private Thread[] threads = new Thread[numberOfThreads];

    static
    {
        final Matrix equ2ecl = SolarSystemVSOP87C.getEqu2EclMatrix( Time.J2000, new Matrix() );
        final Matrix jacobian = new Matrix();
        final Cartesian tmp = new Cartesian();
        final Equatorial.Cart velEquatorial = new Equatorial.Cart();
        final Equatorial.Cart posEquatorial = new Equatorial.Cart();
        final Equatorial.Sphe posJ2000 = new Equatorial.Sphe();

        for ( int i = 0; i < StarsCatalog.SIZE; ++i )
        {
            final double ra = StarsCatalog.getRAscJ2000( i );
            final double decl = StarsCatalog.getDeclJ2000( i );
            jacobian.set(
                    -sin( ra ) * cos( decl ), -cos( ra ) * sin( decl ), 0,
                    cos( ra ) * cos( decl ), -sin( ra ) * sin( decl ), 0,
                    0, cos( decl ), 0
            );
            tmp.set( StarsCatalog.getVRAscJ2000( i ), StarsCatalog.getVDeclJ2000( i ), 0 );
            jacobian.apply( tmp, velEquatorial );
            VEL_J2000[i] = (Ecliptical.Cart)equ2ecl.apply( velEquatorial, new Ecliptical.Cart() );

            posJ2000.set( 1.0, StarsCatalog.getRAscJ2000( i ), StarsCatalog.getDeclJ2000( i ) );
            posJ2000.transform( posEquatorial );
            POS_J2000[i] = (Ecliptical.Cart)equ2ecl.apply( posEquatorial, new Ecliptical.Cart() );
        }
    }

    public void setNumberOfThreads( int numberOfThreads )
    {
        this.numberOfThreads = numberOfThreads;
        numCalcPerThread = StarsCatalog.SIZE / numberOfThreads + 1;
        threads = new Thread[numberOfThreads];
    }

    public void compute( int starIndex, Time time, Equatorial.Cart outputPosition )
    {
        final double yearsSince2000 = yearsSince2000( time );
        setupTransformationToDate( time, transformation );
        // Calculate ecliptical cartesian coordinates to date
        outputPosition.x = POS_J2000[starIndex].x + VEL_J2000[starIndex].x * yearsSince2000;
        outputPosition.y = POS_J2000[starIndex].y + VEL_J2000[starIndex].y * yearsSince2000;
        outputPosition.z = POS_J2000[starIndex].z + VEL_J2000[starIndex].z * yearsSince2000;
        transformation.apply( outputPosition ).normalize();
    }

    public synchronized void computeAll( Time time )
    {
        computeAll( time, positions );
    }

    public synchronized void getPosition( int index, Equatorial.Cart position )
    {
        position.set( positions[index] );
    }

    public void compute( Time time, final Equatorial.Cart[] outputPositions, int countStars )
    {
        setupTransformationToDate( time, transformation );
        final double yearsSince2000 = yearsSince2000( time );
        final int max = countStars < 0 ? StarsCatalog.SIZE : Math.min( countStars, StarsCatalog.SIZE );
        for ( int i = 0; i < max; ++i )
        {
            // Calculate ecliptical cartesian coordinates to date
            outputPositions[i].x = POS_J2000[i].x + VEL_J2000[i].x * yearsSince2000;
            outputPositions[i].y = POS_J2000[i].y + VEL_J2000[i].y * yearsSince2000;
            outputPositions[i].z = POS_J2000[i].z + VEL_J2000[i].z * yearsSince2000;
            transformation.apply( outputPositions[i] ).normalize();
        }
    }

    public void project( Stereographic projection, Equatorial.Cart[] equatorial, float[] output )
    {
        final Cartesian tmpCartesian = new Cartesian();
        for ( int i = 0; i < StarsCatalog.SIZE; ++i )
        {
            projection.project( equatorial[i], tmpCartesian );
            output[2 * i] = (float)tmpCartesian.x;
            output[2 * i + 1] = (float)tmpCartesian.y;
        }
    }

    public synchronized void computeAll( Time time, final Equatorial.Cart[] outputPositions )
    {
        final double yearsSince2000 = yearsSince2000( time );
        setupTransformationToDate( time, transformation );
        for ( int i = 0; i < numberOfThreads; ++i )
        {
            threads[i] = new StarCalculationThread(
                    yearsSince2000, transformation, outputPositions,
                    i * numCalcPerThread, ( i + 1 ) * numCalcPerThread
            );
            threads[i].start();
        }
        for ( Thread thread : threads )
            try
            {
                thread.join();
            }
            catch ( InterruptedException unused )
            {
            }
    }

    private void setupTransformationToDate( Time time, Matrix transformation )
    {
        precession.compute( time );
        rotation.setRotation( SolarSystemVSOP87C.getEcliptic( time ), Coordinates.Axis.X );
        transformation.setProduct( rotation, precession );
    }

    private double yearsSince2000( Time time )
    {
        return time.julianDayNumberSince( Time.J2000 ) / Time.DAYS_PER_YEAR;
    }

    public synchronized void projectAll( Stereographic projection, Equatorial.Cart[] equatorial, float[] output )
    {
        for ( int i = 0; i < numberOfThreads; ++i )
        {
            threads[i] = new StarsProjectionThread( projection, equatorial, output,
                    i * numCalcPerThread, ( i + 1 ) * numCalcPerThread );
            threads[i].start();
        }
        for ( Thread thread : threads )
            try
            {
                thread.join();
            }
            catch ( InterruptedException unused )
            {
            }
    }

    private static class StarCalculationThread extends Thread
    {
        private int from, to;
        private final double yearsSince2000;
        private final Matrix matrix = new Matrix();
        private final Cartesian[] outputPositions;

        StarCalculationThread( double time, Matrix postTransform, Cartesian[] output, int fromIncl, int toExcl )
        {
            yearsSince2000 = time;
            matrix.set( postTransform );
            outputPositions = output;
            from = fromIncl;
            to = toExcl;
        }

        @Override
        public void run()
        {
            for ( int i = from; i < to && i < StarsCatalog.SIZE; ++i )
            {
                // Calculate ecliptical cartesian coordinates to date
                outputPositions[i].x = POS_J2000[i].x + VEL_J2000[i].x * yearsSince2000;
                outputPositions[i].y = POS_J2000[i].y + VEL_J2000[i].y * yearsSince2000;
                outputPositions[i].z = POS_J2000[i].z + VEL_J2000[i].z * yearsSince2000;
                matrix.apply( outputPositions[i] ).normalize();
            }
        }
    }

    private static class StarsProjectionThread extends Thread
    {
        private int from, to;
        private final Stereographic stereographic;
        private final Cartesian[] input;
        private final float[] output;
        private final Cartesian tmpCartesian = new Cartesian();

        StarsProjectionThread( Stereographic projection, Cartesian[] equatorial, float[] proj, int fromIncl, int toExcl )
        {
            stereographic = projection;
            from = fromIncl;
            to = toExcl;
            input = equatorial;
            output = proj;
        }

        @Override
        public void run()
        {
            for ( int i = from; i < to && i < StarsCatalog.SIZE; ++i )
            {
                stereographic.project( input[i], tmpCartesian );
                output[2 * i] = (float)tmpCartesian.x;
                output[2 * i + 1] = (float)tmpCartesian.y;
            }
        }
    }
}
