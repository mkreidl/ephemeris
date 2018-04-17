package com.mkreidl.ephemeris.astrolabe;

import com.mkreidl.ephemeris.geometry.Angle;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Circle;
import com.mkreidl.ephemeris.geometry.Coordinates;
import com.mkreidl.ephemeris.geometry.Matrix;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;
import com.mkreidl.ephemeris.sky.coordinates.Horizontal;

import static com.mkreidl.ephemeris.geometry.Angle.DEG;
import static com.mkreidl.ephemeris.geometry.Angle.MIN;
import static java.lang.Math.PI;


public class Tympanon extends AbstractPart
{
    public static final double SUNS_RADIUS = 16 * MIN;
    public static final double ATMOSPHERIC_REFRACTION = 34 * MIN;
    public static final double SUNS_UPPER_LIMB_WITH_REFRACTION = ATMOSPHERIC_REFRACTION + SUNS_RADIUS;

    public final Circle equator = new Circle( 0.0, 0.0, 1.0 );
    public final Circle horizonMathematical = new Circle();
    public final Circle horizonApparent = new Circle();
    public final Circle horizonSunrise = new Circle();  //** Sunrise/set is when the Sun's center passes this line
    public final Circle horizonCivil = new Circle();
    public final Circle horizonNautical = new Circle();
    public final Circle horizonAstronomical = new Circle();

    public final Cartesian zenith = new Cartesian();
    public final Cartesian nadir = new Cartesian();

    public final Circle[] twilightTransition = new Circle[]{
            horizonApparent, horizonCivil, horizonNautical, horizonAstronomical
    };

    public final Circle twilightCivil = new Circle();
    public final Circle twilightNautical = new Circle();
    public final Circle twilightAstronomical = new Circle();

    private final Horizontal.Sphe horizontal = new Horizontal.Sphe();
    private final Equatorial.Sphe equatorial = new Equatorial.Sphe();

    private final Circle _horizon_mathematical = new Circle();
    private final Circle _horizon_apparent = new Circle();
    private final Circle _horizon_sunrise = new Circle();
    private final Circle _horizon_civil = new Circle();
    private final Circle _horizon_nautical = new Circle();
    private final Circle _horizon_astronomical = new Circle();
    private final Circle _twilight_civil = new Circle();
    private final Circle _twilight_nautical = new Circle();
    private final Circle _twilight_astronomical = new Circle();

    private final Angle angle = new Angle();
    private final Matrix rotation = new Matrix();

    private final Cartesian _zenith = new Cartesian();
    private final Cartesian _nadir = new Cartesian();

    public Tympanon( Astrolabe astrolabe )
    {
        super( astrolabe );
    }

    @Override
    protected void onSynchronize()
    {
        // rotation is only by GREENWICH mean sidereal time, because the difference between this
        // and local sidereal time (=geographic longitude) is taken into account via geographic
        // coordinates.
        final double gmSiderealTime = astrolabe.time.getMeanSiderealTime( angle ).get( Angle.Unit.RADIANS );
        rotation.setRotation( gmSiderealTime, Coordinates.Axis.Z );
    }

    @Override
    protected void onChangeViewParameters()
    {
        onSynchronize();
        astrolabe.project( astrolabe.geographicLocation, PI / 2, _horizon_mathematical );
        astrolabe.project( astrolabe.geographicLocation, PI / 2 + ATMOSPHERIC_REFRACTION, _horizon_apparent );
        astrolabe.project( astrolabe.geographicLocation, PI / 2 + SUNS_UPPER_LIMB_WITH_REFRACTION, _horizon_sunrise );
        astrolabe.project( astrolabe.geographicLocation, PI / 2 + 3 * DEG, _twilight_civil );
        astrolabe.project( astrolabe.geographicLocation, PI / 2 + 6 * DEG, _horizon_civil );
        astrolabe.project( astrolabe.geographicLocation, PI / 2 + 9 * DEG, _twilight_nautical );
        astrolabe.project( astrolabe.geographicLocation, PI / 2 + 12 * DEG, _horizon_nautical );
        astrolabe.project( astrolabe.geographicLocation, PI / 2 + 15 * DEG, _twilight_astronomical );
        astrolabe.project( astrolabe.geographicLocation, PI / 2 + 18 * DEG, _horizon_astronomical );
        // use zenith as tmp variable -- restored from new value of _zenith in onRecomputeProjection()
        astrolabe.project( astrolabe.geographicLocation.transform( zenith ).normalize(), _zenith );
        astrolabe.project( zenith.scale( -1 ), _nadir );
    }

    @Override
    protected void onRecomputeProjection()
    {
        rotation.apply( _horizon_mathematical, horizonMathematical );
        rotation.apply( _horizon_apparent, horizonApparent );
        rotation.apply( _horizon_sunrise, horizonSunrise );
        rotation.apply( _horizon_civil, horizonCivil );
        rotation.apply( _horizon_nautical, horizonNautical );
        rotation.apply( _horizon_astronomical, horizonAstronomical );
        rotation.apply( _twilight_civil, twilightCivil );
        rotation.apply( _twilight_nautical, twilightNautical );
        rotation.apply( _twilight_astronomical, twilightAstronomical );
        rotation.apply( _zenith, zenith );
        rotation.apply( _nadir, nadir );
    }

    public Circle azimuth( Angle longitude, Circle projection )
    {
        horizontal.set( 1.0, longitude.get( Angle.Unit.RADIANS ), 0.0 );
        horizontal.toEquatorial( astrolabe.geographicLocation, equatorial );
        astrolabe.project( equatorial, PI / 2, projection );
        return rotation.apply( projection );
    }

    public Circle height( Angle latitude, Circle projection )
    {
        astrolabe.project( astrolabe.geographicLocation, latitude.get( Angle.Unit.RADIANS ), projection );
        return rotation.apply( projection );
    }
}
