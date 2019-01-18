package com.mkreidl.ephemeris.astrolabe;

import com.mkreidl.ephemeris.geometry.Angle;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Circle;
import com.mkreidl.ephemeris.geometry.Coordinates;
import com.mkreidl.ephemeris.geometry.Matrix3x3;
import com.mkreidl.ephemeris.geometry.Spherical;
import com.mkreidl.ephemeris.sky.Constellation;
import com.mkreidl.ephemeris.sky.Constellations;
import com.mkreidl.ephemeris.sky.Stars;
import com.mkreidl.ephemeris.sky.StarsCatalog;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;
import com.mkreidl.ephemeris.solarsystem.SolarSystem;
import com.mkreidl.ephemeris.solarsystem.Zodiac;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class Rete extends AbstractPart
{
    public static final int POINT_COUNT_SIGN_BOUNDARY = 31;

    public final Circle equator = new Circle( 0.0, 0.0, 1.0 );
    public final Circle cancer = new Circle( 0.0, 0.0, 0.0 );
    public final Circle capricorn = new Circle( 0.0, 0.0, 0.0 );
    public final Circle ecliptic = new Circle( 0.0, 0.0, 1.0 );

    private final Zodiac zodiac = new Zodiac();

    private final Equatorial.Cart equatorialCart = new Equatorial.Cart();
    private final Matrix3x3 matrixEcl2Equ = new Matrix3x3();

    private final Map<Zodiac.Sign, Cartesian> signs = new EnumMap<>( Zodiac.Sign.class );
    private final Map<Zodiac.Sign, Cartesian[]> signBoundariesEcliptical = new EnumMap<>( Zodiac.Sign.class );
    private final double[] starsEclipticalJ2000 = new double[3 * StarsCatalog.SIZE];
    private final double[] starsEquatorialToDate = new double[3 * StarsCatalog.SIZE];

    private final Map<Constellation, Cartesian> constellationCenterMap = new HashMap<>();
    public final Map<Zodiac.Sign, Cartesian> signsCenter = new EnumMap<>( Zodiac.Sign.class );
    public final Map<Zodiac.Sign, Cartesian[]> signBoundariesProjected = new EnumMap<>( Zodiac.Sign.class );
    public final float[] projectedPos = new float[2 * StarsCatalog.SIZE];

    Rete( Astrolabe astrolabe )
    {
        super( astrolabe );
        for ( Zodiac.Sign sign : Zodiac.Sign.values() )
        {
            signs.put( sign, new Cartesian() );
            signsCenter.put( sign, new Cartesian() );
            signBoundariesEcliptical.put( sign, new Cartesian[POINT_COUNT_SIGN_BOUNDARY] );
            signBoundariesProjected.put( sign, new Cartesian[POINT_COUNT_SIGN_BOUNDARY] );
            final Spherical eclipticalSphe = new Spherical().set( 1.0, Zodiac.getLongitude( sign, Angle.Unit.RADIANS ), 0 );
            eclipticalSphe.set( 1.0, Zodiac.getLongitude( sign, Angle.Unit.RADIANS ), 0 );
            for ( int i = 0; i < POINT_COUNT_SIGN_BOUNDARY; i++ )
            {
                eclipticalSphe.lat = Math.PI / 180 * ( i - POINT_COUNT_SIGN_BOUNDARY / 2 );
                signBoundariesEcliptical.get( sign )[i] = eclipticalSphe.transform( new Cartesian() );
                signBoundariesProjected.get( sign )[i] = new Cartesian();
            }
        }
        for ( Constellation constellation : Constellations.ALL )
            constellationCenterMap.put( constellation, new Cartesian() );
    }

    @Override
    protected void onSynchronize()
    {
        zodiac.compute( astrolabe.time );
        final Matrix3x3 transformEclipticalJ2000ToEquatorial = new Matrix3x3();
        SolarSystem.computeEclJ2000ToEquToDate( astrolabe.time, transformEclipticalJ2000ToEquatorial );
        Stars.computeEclipticalJ2000( astrolabe.time, starsEclipticalJ2000 );
        for ( int i = 0; i < StarsCatalog.SIZE; ++i )
            transformEclipticalJ2000ToEquatorial.applyTo( starsEclipticalJ2000, starsEquatorialToDate, 3 * i );
        for ( Constellation constellation : Constellations.ALL )
        {
            final Cartesian constellationCenter = constellationCenterMap.get( constellation );
            Stars.computeConstellationCenter( constellation, starsEquatorialToDate, constellationCenter );
        }
    }

    @Override
    protected void onChangeViewParameters()
    {
    }

    @Override
    protected void onRecomputeProjection()
    {
        cancer.r = astrolabe.project1D( zodiac.obliquity );
        capricorn.r = astrolabe.project1D( -zodiac.obliquity );
        astrolabe.project( zodiac.pole, Math.PI / 2, ecliptic );
        projectZodiac();
        projectStars();
    }

    private void projectZodiac()
    {
        for ( Zodiac.Sign sign : Zodiac.Sign.values() )
        {
            astrolabe.project(
                    zodiac.getEquatorialDirection( sign, equatorialCart ),
                    signs.get( sign )
            );
            astrolabe.project(
                    zodiac.getEquatorialDirectionMiddle( sign, equatorialCart ),
                    signsCenter.get( sign )
            );
            computeSignBoundary( sign );
        }
    }

    private void projectStars()
    {
        final Cartesian tmpCartesian = new Cartesian();
        int srcOffset = -1;
        int targetOffset = -1;
        for ( int i = 0; i < StarsCatalog.SIZE; ++i )
        {
            tmpCartesian.set(
                    starsEquatorialToDate[++srcOffset],
                    starsEquatorialToDate[++srcOffset],
                    starsEquatorialToDate[++srcOffset] );
            astrolabe.project( tmpCartesian, tmpCartesian );
            projectedPos[++targetOffset] = (float)tmpCartesian.x;
            projectedPos[++targetOffset] = (float)tmpCartesian.y;
        }
    }

    public void getConstellationCenter( Constellation constellation, Cartesian output )
    {
        astrolabe.project( constellationCenterMap.get( constellation ), output );
    }

    void getConstellationCenter( Constellation constellation, float[] output )
    {
        final Cartesian cartesian = new Cartesian();
        astrolabe.project( constellationCenterMap.get( constellation ), cartesian );
        output[0] = (float)cartesian.x;
        output[1] = (float)cartesian.y;
    }

    double getDeclination( int starIndex )
    {
        return Math.asin( starsEquatorialToDate[3 * starIndex + 2] );
    }

    double getDeclination( Constellation constellation )
    {
        return Math.asin( constellationCenterMap.get( constellation ).z );
    }

    private void computeSignBoundary( Zodiac.Sign sign )
    {
        matrixEcl2Equ.setRotation( zodiac.obliquity, Coordinates.Axis.X );
        for ( int i = 0; i < POINT_COUNT_SIGN_BOUNDARY; i++ )
        {
            equatorialCart.set( signBoundariesEcliptical.get( sign )[i] );
            matrixEcl2Equ.applyTo( equatorialCart );
            astrolabe.project( equatorialCart, signBoundariesProjected.get( sign )[i] );
        }
    }
}
