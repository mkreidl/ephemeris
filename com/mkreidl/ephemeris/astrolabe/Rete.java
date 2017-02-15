package com.mkreidl.ephemeris.astrolabe;

import com.mkreidl.ephemeris.*;
import com.mkreidl.ephemeris.dynamics.VSOP87.*;
import com.mkreidl.ephemeris.geometry.*;
import com.mkreidl.ephemeris.sky.*;
import com.mkreidl.ephemeris.sky.coordinates.*;

import java.util.*;


public class Rete extends AbstractPart
{
    public static final int POINT_COUNT_SIGN_BOUNDARY = 31;

    public final Circle equator = new Circle( 0.0, 0.0, 1.0 );
    public final Circle cancer = new Circle( 0.0, 0.0, 0.0 );
    public final Circle capricorn = new Circle( 0.0, 0.0, 0.0 );
    public final Circle ecliptic = new Circle( 0.0, 0.0, 1.0 );

    private final Zodiac zodiac = new Zodiac();
    private final Equatorial.Sphe equatorialSphe = new Equatorial.Sphe();
    private final Equatorial.Cart equatorialCart = new Equatorial.Cart();
    private final Angle angle = new Angle();
    private final PrecessionMatrix precessionMatrix = new PrecessionMatrix();

    public final EnumMap<Zodiac.Sign, Cartesian> signs = new EnumMap<>( Zodiac.Sign.class );
    public final EnumMap<Zodiac.Sign, Cartesian> signsMiddle = new EnumMap<>( Zodiac.Sign.class );
    public final EnumMap<Zodiac.Sign, Cartesian[]> signBoundaries = new EnumMap<>( Zodiac.Sign.class );
    public final Ecliptical.Cart[] eclipticalPos = new Ecliptical.Cart[StarsCatalogue.CATALOG_SIZE];
    public final float[] projectedPos = new float[2 * StarsCatalogue.CATALOG_SIZE];

    private final Cartesian tmpCartesian = new Cartesian();
    private boolean calculatePrecession = true;

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
        for ( int i = 0; i < StarsCatalogue.CATALOG_SIZE; ++i )
            eclipticalPos[i] = new Ecliptical.Cart();
        equatorialSphe.set( 1.0, 0.0, 0.0 );
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
        // TODO: MAKE PARALLEL FOR LOOP
        for ( int i = 0; i < StarsCatalogue.CATALOG_SIZE; ++i )
        {
            final double yearsSince2000 = astrolabe.time.julianDayNumberSince( Time.J2000 ) / Time.DAYS_PER_YEAR;
            // Calculate spherical equatorial coordinates to date
            equatorialSphe.lon = StarsCatalogue.POS[i].lon + StarsCatalogue.VEL[i].lon * yearsSince2000;
            equatorialSphe.lat = StarsCatalogue.POS[i].lat + StarsCatalogue.VEL[i].lat * yearsSince2000;
            // Transform into Cartesian equatorial coordinates, store in map coordinates
            equatorialSphe.toEcliptical( zodiac.obliquity, eclipticalPos[i] );
            if ( calculatePrecession )
                // We calculate positions relative to the mean equinox of the date,
                // thus apply the precession matrix to the coordinates of the J2000:
                precessionMatrix.apply( eclipticalPos[i] );
            eclipticalPos[i].toEquatorial( zodiac.obliquity, equatorialCart ).normalize();
            astrolabe.camera.project( equatorialCart, tmpCartesian );
            projectedPos[2 * i] = (float)tmpCartesian.x;
            projectedPos[2 * i + 1] = (float)tmpCartesian.y;

            // keep eclipticalPos[i].dst == 1.0 as a calculation-dummy for those stars
            // which have StarsCatalogue.POS[i].dst == Double.NaN:
            if ( !Double.isNaN( StarsCatalogue.POS[i].dst ) )
                eclipticalPos[i].scale( StarsCatalogue.POS[i].dst * Distance.ly.toMeters() );
        }
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

    // TODOS: constellations names
    // phases/magnitudes
    // google maps
    // translate
    // style

    public Cartesian getConstellationCenter( Constellations.Constellation constellation, Cartesian output, boolean weighted )
    {
        double totalWeight = 0.0;
        output.set( 0, 0, 0 );
        for ( int star : constellation )
        {
            double weight = ( weighted ? StarsCatalogue.MAG[star] : 1 );
            output.x += projectedPos[2 * star] * weight;
            output.y += projectedPos[2 * star + 1] * weight;
            totalWeight += weight;
        }
        output.scale( 1.0 / totalWeight );
        return output;
    }
}
