package com.mkreidl.ephemeris;

import com.mkreidl.ephemeris.geometry.Angle;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Coordinates;
import com.mkreidl.ephemeris.geometry.Spherical;
import com.mkreidl.ephemeris.sky.coordinates.Ecliptical;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;
import com.mkreidl.ephemeris.sky.coordinates.Horizontal;
import com.mkreidl.ephemeris.solarsystem.Body;
import com.mkreidl.ephemeris.solarsystem.SolarSystemVSOP87C;


public class Position
{
    public enum CoordinatesCenter
    {
        HELIOCENTRIC, GEOCENTRIC, TOPOCENTRIC
    }

    private final Ecliptical.Cart helioCart = new Ecliptical.Cart();
    private final Ecliptical.Cart geoCart = new Ecliptical.Cart();
    private final Ecliptical.Cart velocityHeliocentric = new Ecliptical.Cart();
    private final Ecliptical.Cart velocityGeocentric = new Ecliptical.Cart();

    private final Ecliptical.Cart posSun = new Ecliptical.Cart();
    private final Ecliptical.Cart velocitySun = new Ecliptical.Cart();

    private double currentEcliptic;

    private final Cartesian toposEcliptical = new Cartesian();
    private final Cartesian toposEquatorial = new Cartesian();
    private final Spherical toposEquatorialSpherical = new Spherical();
    private final Angle angle = new Angle();

    private final Ecliptical.Sphe tmpEclipticalSpherical = new Ecliptical.Sphe();
    private final Ecliptical.Cart tmpEclipticalCartesian = new Ecliptical.Cart();
    private final Equatorial.Cart tmpEquatorialCartesian = new Equatorial.Cart();
    private final Horizontal.Cart tmpHorizontalCartesian = new Horizontal.Cart();

    public void setHelioGeocentric( Ecliptical.Cart pos, Ecliptical.Cart earth )
    {
        helioCart.set( pos );
        geoCart.set( pos );
        posSun.set( earth ).scale( -1 );
    }

    public void setHeliocentricPosition( Ecliptical.Cart heliocentric, Ecliptical.Cart earth )
    {
        helioCart.set( heliocentric );
        geoCart.set( heliocentric ).sub( earth );
        posSun.set( earth ).scale( -1 );
    }

    public void setGeocentricPosition( Ecliptical.Cart geocentric, Ecliptical.Cart earth )
    {
        geoCart.set( geocentric );
        helioCart.set( geocentric ).add( earth );
        posSun.set( earth ).scale( -1 );
    }

    public void setHeliocentricVelocity( Ecliptical.Cart velBody, Ecliptical.Cart velEarth )
    {
        velocityHeliocentric.set( velBody );
        velocityGeocentric.set( velBody ).sub( velEarth );
        velocitySun.set( velEarth ).scale( -1 );
    }

    public void setGeocentricVelocity( Ecliptical.Cart velBody, Ecliptical.Cart velEarth )
    {
        velocityGeocentric.set( velBody );
        velocityHeliocentric.set( velBody ).add( velEarth );
        velocitySun.set( velEarth ).scale( -1 );
    }

    public void correctAberration()
    {
        final double timeLightTravel = geoCart.distance( Distance.m ) / Distance.ls.toMeters();
        tmpEclipticalCartesian.set( velocityGeocentric ).scale( -timeLightTravel );
        geoCart.add( tmpEclipticalCartesian );
        final double timeLightTravelSun = posSun.distance( Distance.m ) / Distance.ls.toMeters();
        tmpEclipticalCartesian.set( velocitySun ).scale( -timeLightTravelSun );
        posSun.add( tmpEclipticalCartesian );
    }

    public void setTimeLocation( Time time, double lonRad, double latRad )
    {
        toposEquatorialSpherical.set( 1, lonRad, latRad );
        setTimeLocation( time, toposEquatorialSpherical );
    }

    public void setTimeLocation( Time time, Spherical geographicLocation )
    {
        this.currentEcliptic = SolarSystemVSOP87C.getEcliptic( time );
        angle.setRadians( geographicLocation.lon );
        final double localSiderealTime = time.getMeanSiderealTime( angle, angle ).getRadians();
        // current equatorial coordinates of zenith are used to compute current horizontal positions (in Planets)
        toposEquatorialSpherical.set(
                Body.EARTH.RADIUS_MEAN_M, localSiderealTime, geographicLocation.lat );
        toposEquatorialSpherical.transform( toposEquatorial );
        // The following stores the current observer position in geocentric ecliptical coordinates
        toposEcliptical.set( toposEquatorial ).rotate( Coordinates.Axis.X, -currentEcliptic );
    }

    public Cartesian get( Ecliptical.Cart cartesian, CoordinatesCenter coordinatesCenter )
    {
        switch ( coordinatesCenter )
        {
            case GEOCENTRIC:
                return cartesian.set( geoCart );
            case TOPOCENTRIC:
                return cartesian.set( geoCart ).sub( toposEcliptical );
            default:
                return cartesian.set( helioCart );
        }
    }

    public Cartesian get( Equatorial.Cart cartesian, CoordinatesCenter coordinatesCenter )
    {
        switch ( coordinatesCenter )
        {
            case GEOCENTRIC:
                return geoCart.toEquatorial( currentEcliptic, cartesian );
            case TOPOCENTRIC:
                return geoCart.toEquatorial( currentEcliptic, cartesian ).sub( toposEquatorial );
            default:
                return helioCart.toEquatorial( currentEcliptic, cartesian );
        }
    }

    public Cartesian get( Horizontal.Cart cartesian, CoordinatesCenter coordinatesCenter )
    {
        switch ( coordinatesCenter )
        {
            case GEOCENTRIC:
                return geoCart.toHorizontal( currentEcliptic, toposEquatorialSpherical, cartesian );
            case TOPOCENTRIC:
                return ( (Equatorial.Cart)get( tmpEquatorialCartesian, CoordinatesCenter.TOPOCENTRIC ) )
                        .toHorizontal( toposEquatorialSpherical, cartesian );
            default:
                return helioCart.toHorizontal( currentEcliptic, toposEquatorialSpherical, cartesian );
        }
    }

    public Spherical get( Ecliptical.Sphe spherical, CoordinatesCenter coordinatesCenter )
    {
        switch ( coordinatesCenter )
        {
            case GEOCENTRIC:
                return geoCart.transform( spherical );
            case TOPOCENTRIC:
                return get( tmpEclipticalCartesian, CoordinatesCenter.TOPOCENTRIC ).transform( spherical );
            default:
                return helioCart.transform( spherical );
        }
    }

    public Spherical get( Equatorial.Sphe spherical, CoordinatesCenter coordinatesCenter )
    {
        switch ( coordinatesCenter )
        {
            case GEOCENTRIC:
                geoCart.transform( tmpEclipticalSpherical );
                return tmpEclipticalSpherical.toEquatorial( currentEcliptic, spherical );
            case TOPOCENTRIC:
                return get( tmpEquatorialCartesian, CoordinatesCenter.TOPOCENTRIC ).transform( spherical );
            default:
                helioCart.transform( tmpEclipticalSpherical );
                return tmpEclipticalSpherical.toEquatorial( currentEcliptic, spherical );
        }
    }

    public Spherical get( Horizontal.Sphe spherical, CoordinatesCenter coordinatesCenter )
    {
        switch ( coordinatesCenter )
        {
            case GEOCENTRIC:
                geoCart.transform( tmpEclipticalSpherical );
                return tmpEclipticalSpherical.toHorizontal( currentEcliptic, toposEquatorialSpherical, spherical );
            case TOPOCENTRIC:
                return get( tmpHorizontalCartesian, coordinatesCenter ).transform( spherical );
            default:
                return helioCart.toHorizontal( currentEcliptic, toposEquatorialSpherical, spherical );
        }
    }

    /**
     * Compute the phase angle
     */
    public Angle getPhase( Angle phase )
    {
        // This computes the angle beween the three bodies:
        final double cosPhase = helioCart.dot( geoCart ) / ( geoCart.length() * helioCart.length() );
        final double det = helioCart.x * geoCart.y - helioCart.y * geoCart.x;
        final double angle = Math.acos( cosPhase );
        return phase.set( ( angle < 0 && det < 0 || angle > 0 && det > 0 ) ?
                angle : -angle, Angle.Unit.RADIANS );
        // This, in contrast, computes the difference in equatorial longitude, which
        // seems to be the correct thing to do according to reference values from
        // www.ephemeris.com:
        //        final double hlon = helioCart.transform( tmpEclipticalSpherical ).lon;
        //        final double glon = geoCart.transform( tmpEclipticalSpherical ).lon;
        //        return phase.set( glon - hlon, Angle.Unit.RADIANS ).standardize();
    }

    /**
     * Compute the fraction of the disk which is lighted by the sun
     */
    public double getIlluminatedFraction()
    {
        final double gh = helioCart.dot( geoCart );
        final double cosPhase = gh / ( geoCart.length() * helioCart.length() );
        return ( 1 + cosPhase ) / 2;
    }

    /**
     * Compute angle between the rays originating from the earth (observer),
     * pointing to the sun and this body, resp.
     *
     * @return Angle between -180° and +180°
     */
    public Angle getElongation( Angle elongation, CoordinatesCenter coordinatesCenter )
    {
        return elongation.set( getElongationRadians( coordinatesCenter ), Angle.Unit.RADIANS );
    }

    public double getElongationRadians( CoordinatesCenter coordinatesCenter )
    {
        get( tmpEclipticalCartesian, coordinatesCenter );
        final double ce = tmpEclipticalCartesian.dot( posSun ) / ( tmpEclipticalCartesian.length() * posSun.length() );
        return Math.acos( ce ) * getElongationSign();
    }

    private double getElongationSign()
    {
        return Math.signum( posSun.x * tmpEclipticalCartesian.y - posSun.y * tmpEclipticalCartesian.x );
    }

    public double getElongationSign( CoordinatesCenter coordinatesCenter )
    {
        get( tmpEclipticalCartesian, coordinatesCenter );
        return getElongationSign();
    }

    public boolean isVisible()
    {
        final Horizontal.Sphe horizSpherical = new Horizontal.Sphe();
        get( horizSpherical, CoordinatesCenter.TOPOCENTRIC );
        return horizSpherical.getHeight( new Angle() ).get( Angle.Unit.RADIANS ) > 0.0;
    }

    public boolean isRetrograde()
    {
        // compute the z-coordinate of the cross produce r x v (with r and v geocentric)
        return geoCart.x * velocityGeocentric.y - geoCart.y * velocityGeocentric.x < 0;
    }
}
