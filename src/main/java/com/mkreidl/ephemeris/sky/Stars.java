package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Matrix3x3;
import com.mkreidl.ephemeris.sky.coordinates.Ecliptical;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;
import com.mkreidl.ephemeris.solarsystem.SolarSystem;
import com.mkreidl.ephemeris.solarsystem.SolarSystemVSOP87C;

import java.util.Arrays;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

final public class Stars
{
    private static final double[] POS_J2000 = new double[StarsCatalog.SIZE * 3];
    private static final double[] VEL_J2000 = new double[StarsCatalog.SIZE * 3];

    static
    {
        final Matrix3x3 equ2ecl = SolarSystemVSOP87C.getEqu2EclMatrix( Time.J2000, new Matrix3x3() );
        final Matrix3x3 jacobian = new Matrix3x3();
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
            jacobian.applyTo( tmp, velEquatorial );
            final Cartesian vel = equ2ecl.applyTo( velEquatorial, new Ecliptical.Cart() );

            posJ2000.set( 1.0, StarsCatalog.getRAscJ2000( i ), StarsCatalog.getDeclJ2000( i ) );
            posJ2000.transform( posEquatorial );
            final Cartesian pos = equ2ecl.applyTo( posEquatorial, new Ecliptical.Cart() );

            int offset = 3 * i;
            POS_J2000[offset] = pos.x;
            VEL_J2000[offset] = vel.x;
            POS_J2000[++offset] = pos.y;
            VEL_J2000[offset] = vel.y;
            POS_J2000[++offset] = pos.z;
            VEL_J2000[offset] = vel.z;
        }
    }

    private Stars()
    {
    }

    public static void computeEclipticalJ2000( Time time, double[] eclipticalPositions )
    {
        computeEclipticalJ2000( time.getTime(), eclipticalPositions, 0, 1 );
    }

    public static void computeEclipticalJ2000( long timeInMillis, double[] eclipticalPositions, int start, int delta )
    {
        final double yearsSince2000 = Time.julianYearsSinceJ2000( timeInMillis );
        for ( int i = start; i < StarsCatalog.SIZE; i += delta )
        {
            final int offset = 3 * i;
            // Compute ecliptical cartesian coordinates for Y2000 frame
            eclipticalPositions[offset] = POS_J2000[offset] + VEL_J2000[offset] * yearsSince2000;
            eclipticalPositions[offset + 1] = POS_J2000[offset + 1] + VEL_J2000[offset + 1] * yearsSince2000;
            eclipticalPositions[offset + 2] = POS_J2000[offset + 2] + VEL_J2000[offset + 2] * yearsSince2000;
        }
    }

    public static void computeEquatorial( int starIndex, Time time, Equatorial.Cart outputPosition )
    {
        final Matrix3x3 transformation = new Matrix3x3();
        final double yearsSince2000 = time.julianYearsSinceJ2000();
        SolarSystem.computeEclJ2000ToEquToDate( time, transformation );
        // Compute ecliptical cartesian coordinates resp. to Y2000
        final int offset = 3 * starIndex;
        outputPosition.x = POS_J2000[offset] + VEL_J2000[offset] * yearsSince2000;
        outputPosition.y = POS_J2000[offset + 1] + VEL_J2000[offset + 1] * yearsSince2000;
        outputPosition.z = POS_J2000[offset + 2] + VEL_J2000[offset + 2] * yearsSince2000;
        transformation.applyTo( outputPosition ).normalize();
    }

    public static void computeConstellationCenter( Constellation constellation, double[] starsCoordinates, double[] center )
    {
        Arrays.fill( center, 0 );
        for ( int star : constellation.getStarSet() )
        {
            int index = star * 3;
            center[0] += starsCoordinates[index];
            center[1] += starsCoordinates[++index];
            center[2] += starsCoordinates[++index];
        }
    }
}
