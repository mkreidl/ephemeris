package com.mkreidl.ephemeris.astrolabe;

import com.mkreidl.ephemeris.geometry.Angle;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Circle;
import com.mkreidl.ephemeris.geometry.Coordinates;
import com.mkreidl.ephemeris.geometry.Matrix;
import com.mkreidl.ephemeris.sky.Constellations;
import com.mkreidl.ephemeris.sky.Stars;
import com.mkreidl.ephemeris.sky.StarsCatalog;
import com.mkreidl.ephemeris.sky.Zodiac;
import com.mkreidl.ephemeris.sky.coordinates.Ecliptical;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;

import java.util.EnumMap;


public class Rete extends AbstractPart
{
    public static final int POINT_COUNT_SIGN_BOUNDARY = 31;

    public final Circle equator = new Circle( 0.0, 0.0, 1.0 );
    public final Circle cancer = new Circle( 0.0, 0.0, 0.0 );
    public final Circle capricorn = new Circle( 0.0, 0.0, 0.0 );
    public final Circle ecliptic = new Circle( 0.0, 0.0, 1.0 );

    private final Stars stars = new Stars();
    private final Zodiac zodiac = new Zodiac();

    private final Ecliptical.Sphe eclipticalSphe = new Ecliptical.Sphe();
    private final Equatorial.Cart equatorialCart = new Equatorial.Cart();
    private final Matrix matrixEcl2Equ = new Matrix();

    public final EnumMap<Zodiac.Sign, Cartesian> signs = new EnumMap<>( Zodiac.Sign.class );
    public final EnumMap<Zodiac.Sign, Cartesian> signsMiddle = new EnumMap<>( Zodiac.Sign.class );
    public final EnumMap<Zodiac.Sign, Cartesian[]> signBoundariesEcliptical = new EnumMap<>( Zodiac.Sign.class );
    public final EnumMap<Zodiac.Sign, Cartesian[]> signBoundariesProjected = new EnumMap<>( Zodiac.Sign.class );
    public final Equatorial.Cart[] toDatePositionsEquatorial = new Equatorial.Cart[StarsCatalog.CATALOG_SIZE];
    public final float[] projectedPos = new float[2 * StarsCatalog.CATALOG_SIZE];

    public Rete( Astrolabe astrolabe )
    {
        super( astrolabe );
        for ( Zodiac.Sign sign : Zodiac.Sign.values() )
        {
            signs.put( sign, new Cartesian() );
            signsMiddle.put( sign, new Cartesian() );
            signBoundariesEcliptical.put( sign, new Cartesian[POINT_COUNT_SIGN_BOUNDARY] );
            signBoundariesProjected.put( sign, new Cartesian[POINT_COUNT_SIGN_BOUNDARY] );
            eclipticalSphe.set( 1.0, Zodiac.getLongitude( sign, Angle.Unit.RADIANS ), 0 );
            for ( int i = 0; i < POINT_COUNT_SIGN_BOUNDARY; i++ )
            {
                eclipticalSphe.lat = Math.PI / 180 * ( i - POINT_COUNT_SIGN_BOUNDARY / 2 );
                signBoundariesEcliptical.get( sign )[i] = eclipticalSphe.transform( new Cartesian() );
                signBoundariesProjected.get( sign )[i] = new Cartesian();
            }
        }
        for ( int i = 0; i < StarsCatalog.CATALOG_SIZE; ++i )
            toDatePositionsEquatorial[i] = new Equatorial.Cart();
    }

    @Override
    protected void onSynchronize()
    {
        zodiac.compute( astrolabe.time );
        stars.computeAll( astrolabe.time, toDatePositionsEquatorial );
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
        for ( Zodiac.Sign sign : Zodiac.Sign.values() )
        {
            astrolabe.project(
                    zodiac.getEquatorialDirection( sign, equatorialCart ),
                    signs.get( sign )
            );
            astrolabe.project(
                    zodiac.getEquatorialDirectionMiddle( sign, equatorialCart ),
                    signsMiddle.get( sign )
            );
            computeSignBoundary( sign );
        }
        stars.projectAll( astrolabe, toDatePositionsEquatorial, projectedPos );
    }

    public void setNumberOfThreads( int numberOfThreads )
    {
        stars.setNumberOfThreads( numberOfThreads );
    }

    public Cartesian getConstellationCenter( Constellations.Constellation constellation, Cartesian output, boolean weighted )
    {
        double totalWeight = 0.0;
        output.set( 0, 0, 0 );
        for ( int star : constellation )
        {
            double weight = ( weighted ? StarsCatalog.MAG[star] : 1 );
            output.x += projectedPos[2 * star] * weight;
            output.y += projectedPos[2 * star + 1] * weight;
            totalWeight += weight;
        }
        output.scale( 1.0 / totalWeight );
        return output;
    }

    private void computeSignBoundary( Zodiac.Sign sign )
    {
        matrixEcl2Equ.setRotation( zodiac.obliquity, Coordinates.Axis.X );
        for ( int i = 0; i < POINT_COUNT_SIGN_BOUNDARY; i++ )
        {
            equatorialCart.set( signBoundariesEcliptical.get( sign )[i] );
            matrixEcl2Equ.apply( equatorialCart );
            astrolabe.project( equatorialCart, signBoundariesProjected.get( sign )[i] );
        }
    }
}
