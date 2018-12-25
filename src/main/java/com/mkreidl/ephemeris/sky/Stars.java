package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Matrix3x3;
import com.mkreidl.ephemeris.geometry.Stereographic;
import com.mkreidl.ephemeris.sky.coordinates.Ecliptical;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;
import com.mkreidl.ephemeris.solarsystem.SolarSystem;
import com.mkreidl.ephemeris.solarsystem.SolarSystemVSOP87C;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

final public class Stars
{
    private static final Ecliptical.Cart[] POS_J2000 = new Ecliptical.Cart[StarsCatalog.SIZE];
    private static final Ecliptical.Cart[] VEL_J2000 = new Ecliptical.Cart[StarsCatalog.SIZE];

    private final double[] POS_J2000_FLAT = new double[StarsCatalog.SIZE * 3];
    private final double[] VEL_J2000_FLAT = new double[StarsCatalog.SIZE * 3];

    private final Matrix3x3 transformation = new Matrix3x3();

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
            VEL_J2000[i] = (Ecliptical.Cart)equ2ecl.applyTo( velEquatorial, new Ecliptical.Cart() );

            posJ2000.set( 1.0, StarsCatalog.getRAscJ2000( i ), StarsCatalog.getDeclJ2000( i ) );
            posJ2000.transform( posEquatorial );
            POS_J2000[i] = (Ecliptical.Cart)equ2ecl.applyTo( posEquatorial, new Ecliptical.Cart() );
        }
    }

    public Stars()
    {
        for ( int i = 0; i < StarsCatalog.SIZE; ++i )
        {
            final int offset = 3 * i;
            POS_J2000_FLAT[offset] = POS_J2000[i].x;
            POS_J2000_FLAT[offset + 1] = POS_J2000[i].y;
            POS_J2000_FLAT[offset + 2] = POS_J2000[i].z;
            VEL_J2000_FLAT[offset] = VEL_J2000[i].x;
            VEL_J2000_FLAT[offset + 1] = VEL_J2000[i].y;
            VEL_J2000_FLAT[offset + 2] = VEL_J2000[i].z;
        }
    }

    public void compute( double yearsSince2000, double[] positions3d )
    {
        for ( int i = 0; i < StarsCatalog.SIZE; ++i )
        {
            final int offset = 3 * i;
            // Compute ecliptical cartesian coordinates for Y2000 frame
            positions3d[offset] = POS_J2000_FLAT[offset] + VEL_J2000_FLAT[offset] * yearsSince2000;
            positions3d[offset + 1] = POS_J2000_FLAT[offset + 1] + VEL_J2000_FLAT[offset + 1] * yearsSince2000;
            positions3d[offset + 2] = POS_J2000_FLAT[offset + 2] + VEL_J2000_FLAT[offset + 2] * yearsSince2000;
        }
    }

    public static void compute( Time time, double[] eclipticalPositions )
    {
        final double yearsSince2000 = time.julianYearsSinceJ2000();
        for ( int i = 0; i < StarsCatalog.SIZE; ++i )
        {
            final int offset = 3 * i;
            // Compute ecliptical cartesian coordinates for Y2000 frame
            eclipticalPositions[offset] = POS_J2000[i].x + VEL_J2000[i].x * yearsSince2000;
            eclipticalPositions[offset + 1] = POS_J2000[i].y + VEL_J2000[i].y * yearsSince2000;
            eclipticalPositions[offset + 2] = POS_J2000[i].z + VEL_J2000[i].z * yearsSince2000;
        }
    }

    public static void computeEclipticalJ2000( double yearsSince2000, double[] eclipticalPositions, int start, int delta )
    {
        for ( int i = start; i < StarsCatalog.SIZE; i += delta )
        {
            // Compute ecliptical cartesian coordinates for Y2000 frame
            final Cartesian pos = POS_J2000[i];
            final Cartesian vel = VEL_J2000[i];
            int k = i * 3;
            eclipticalPositions[k] = pos.x + vel.x * yearsSince2000;
            eclipticalPositions[++k] = pos.y + vel.y * yearsSince2000;
            eclipticalPositions[++k] = pos.z + vel.z * yearsSince2000;
        }
    }

    public void compute( int starIndex, Time time, Equatorial.Cart outputPosition )
    {
        final double yearsSince2000 = time.julianYearsSinceJ2000();
        SolarSystem.computeTransfEclJ200ToEquToDate( time, transformation );
        // Compute ecliptical cartesian coordinates resp. to Y2000
        outputPosition.x = POS_J2000[starIndex].x + VEL_J2000[starIndex].x * yearsSince2000;
        outputPosition.y = POS_J2000[starIndex].y + VEL_J2000[starIndex].y * yearsSince2000;
        outputPosition.z = POS_J2000[starIndex].z + VEL_J2000[starIndex].z * yearsSince2000;
        transformation.applyTo( outputPosition ).normalize();
    }

    public void project( Stereographic projection, float[] equatorial, float[] output )
    {
        final Cartesian tmpCartesian = new Cartesian();
        for ( int i = 0; i < StarsCatalog.SIZE; ++i )
        {
            int offset = 3 * i;
            tmpCartesian.set( equatorial[offset], equatorial[offset + 1], equatorial[offset + 2] );
            projection.project( tmpCartesian, tmpCartesian );
            output[2 * i] = (float)tmpCartesian.x;
            output[2 * i + 1] = (float)tmpCartesian.y;
        }
    }
}
