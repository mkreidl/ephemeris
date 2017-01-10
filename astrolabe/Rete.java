package com.mkreidl.ephemeris.astrolabe;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.dynamics.VSOP87.PrecessionMatrix;
import com.mkreidl.ephemeris.geometry.Angle;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Circle;
import com.mkreidl.ephemeris.sky.StarsCatalogue;
import com.mkreidl.ephemeris.sky.Zodiac;
import com.mkreidl.ephemeris.sky.coordinates.Ecliptical;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class Rete extends AbstractPart
{
    public static final int POINT_COUNT_SIGN_BOUNDARY = 31;

    public final Circle equator = new Circle( 0.0, 0.0, 1.0 );
    public final Circle cancer = new Circle( 0.0, 0.0, 0.0 );
    public final Circle capricorn = new Circle( 0.0, 0.0, 0.0 );
    public final Circle ecliptic = new Circle( 0.0, 0.0, 1.0 );

    private final Zodiac zodiac = new Zodiac();
    private final Ecliptical.Cart eclipticalCart = new Ecliptical.Cart();
    private final Equatorial.Cart equatorialCart = new Equatorial.Cart();
    private final Equatorial.Cart vEquatorialCart = new Equatorial.Cart();
    private final Angle angle = new Angle();
    private final PrecessionMatrix precessionMatrix = new PrecessionMatrix();

    public final EnumMap<Zodiac.Sign, Cartesian> signs = new EnumMap<>( Zodiac.Sign.class );
    public final EnumMap<Zodiac.Sign, Cartesian> signsMiddle = new EnumMap<>( Zodiac.Sign.class );
    public final EnumMap<Zodiac.Sign, Cartesian[]> signBoundaries = new EnumMap<>( Zodiac.Sign.class );

    public final Map<Integer, Ecliptical.Cart> coordinates = new LinkedHashMap<>();
    public final Map<Integer, Cartesian> projectedPositions = new LinkedHashMap<>();

    public Rete( Astrolabe astrolabe )
    {
        super( astrolabe );
        for ( Zodiac.Sign sign : Zodiac.Sign.values() )
        {
            signs.put( sign, new Cartesian() );
            signsMiddle.put( sign, new Cartesian() );
            signBoundaries.put( sign, new Cartesian[POINT_COUNT_SIGN_BOUNDARY] );
            for ( int i = 0; i < POINT_COUNT_SIGN_BOUNDARY; i++ )
                signBoundaries.get( sign )[i] = new Cartesian();
        }
        for ( int index : StarsCatalogue.POSITIONS.keySet() )
        {
            coordinates.put( index, new Ecliptical.Cart() );
            projectedPositions.put( index, new Cartesian() );
        }
    }

    @Override
    protected void onSynchronize()
    {
        zodiac.calculate( astrolabe.time );
        precessionMatrix.calculate( astrolabe.time );
    }

    @Override
    protected void onChangeObserverParams()
    {
    }

    @Override
    protected void recalculate()
    {
        cancer.r = astrolabe.camera.projectLatitude( zodiac.obliquity );
        capricorn.r = astrolabe.camera.projectLatitude( -zodiac.obliquity );
        astrolabe.camera.project( zodiac.pole, Math.PI / 2, ecliptic );
        for ( Zodiac.Sign sign : Zodiac.Sign.values() )
        {
            astrolabe.camera.project(
                    zodiac.getEquatorialDirection( sign, equatorialCart ),
                    signs.get( sign )
            );
            astrolabe.camera.project(
                    zodiac.getEquatorialDirectionMiddle( sign, equatorialCart ),
                    signsMiddle.get( sign )
            );
            calculateSignBoundary( sign );
        }
        for ( int index : StarsCatalogue.POSITIONS.keySet() )
        {
            final Ecliptical.Cart eclipticalCart = coordinates.get( index );
            StarsCatalogue.POSITIONS.get( index ).transform( eclipticalCart );
            StarsCatalogue.VELOCITIES.get( index ).transform( vEquatorialCart );
            eclipticalCart.add( vEquatorialCart.scale(
                    Angle.MAS / Time.DAYS_PER_YEAR  // vEquatorial is in mas/a -> transform into rad/d
                            * astrolabe.time.julianDayNumberSince( Time.J2000 )
            ) );
            precessionMatrix.apply( eclipticalCart );
            eclipticalCart.toEquatorial( zodiac.obliquity, equatorialCart ).normalize();
            astrolabe.camera.project( equatorialCart, projectedPositions.get( index ) );
        }
    }

    public Cartesian getPosition( int object )
    {
        return projectedPositions.get( object );
    }

    private void calculateSignBoundary( Zodiac.Sign sign )
    {
        final Ecliptical.Sphe ecliptical = new Ecliptical.Sphe();
        final double lon = Zodiac.getLongitude( sign, Angle.Unit.RADIANS );
        for ( int lat = -POINT_COUNT_SIGN_BOUNDARY / 2; lat <= POINT_COUNT_SIGN_BOUNDARY / 2; lat++ )
        {
            angle.set( lat, Angle.Unit.DEGREES );
            ecliptical.set( 1.0, lon, angle.get( Angle.Unit.RADIANS ) );
            ecliptical.toEquatorial( zodiac.obliquity, equatorialCart );
            astrolabe.camera.project( equatorialCart, signBoundaries.get( sign )[lat + POINT_COUNT_SIGN_BOUNDARY / 2] );
        }
    }
}
