package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Angle;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Coordinates;
import com.mkreidl.ephemeris.geometry.Spherical;
import com.mkreidl.ephemeris.sky.coordinates.Ecliptical;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;
import com.mkreidl.ephemeris.sky.coordinates.Horizontal;


public class Ephemerides
{
    private final Ecliptical.Cart helioCart = new Ecliptical.Cart();
    private final Ecliptical.Cart geoCart = new Ecliptical.Cart();
    private final Ecliptical.Cart velocityHeliocentric = new Ecliptical.Cart();
    private final Ecliptical.Cart velocityGeocentric = new Ecliptical.Cart();

    private double currentEcliptic;
    private final Cartesian toposEcliptical = new Cartesian();
    private final Cartesian toposEquatorial = new Cartesian();
    private final Spherical toposEquatorialSpherical = new Spherical();
    private final Angle angle = new Angle();
    private final Time tmpTime = new Time();

    private final Ecliptical.Sphe tmpSphericalEcliptical = new Ecliptical.Sphe();
    private final Equatorial.Cart tmpEquatorialCartesian = new Equatorial.Cart();
    private final Horizontal.Cart tmpHorizontalCartesian = new Horizontal.Cart();
    private final Cartesian tmpCartesian = new Cartesian();

    // TODO: Pass in velocities here and calculate if planet is retrograde
    void setHeliocentric( Ecliptical.Cart heliocentric, Ecliptical.Cart earth )
    {
        helioCart.set( heliocentric );
        geoCart.set( heliocentric ).sub( earth );
    }

    void setGeocentric( Ecliptical.Cart geocentric, Ecliptical.Cart earth )
    {
        geoCart.set( geocentric );
        helioCart.set( geocentric ).add( earth );
    }

    void setHeliocentricVelocities( Ecliptical.Cart velBody, Ecliptical.Cart velEarth )
    {
        velocityHeliocentric.set( velBody );
        velocityGeocentric.set( velBody ).sub( velEarth );
    }

    public void setTimeLocation( Time time, Spherical geographicLocation )
    {
        this.currentEcliptic = SolarSystem.getEcliptic( time );
        final double localSiderealTime = time.getMeanSiderealTime( geographicLocation, angle ).get( Angle.Unit.RADIANS );
        // current equatorial coordinates of zenit are used to calculate current horizontal positions (in Planets)
        toposEquatorialSpherical.set( geographicLocation.dst, localSiderealTime, geographicLocation.lat );
        toposEquatorialSpherical.transform( toposEquatorial );
        // The following stores the current observer position in geocentric ecliptical coordinates
        toposEcliptical.set( toposEquatorial ).rotate( Coordinates.Axis.X, -currentEcliptic );
    }

    public void setTimeLocation( long time, Spherical geographicLocation )
    {
        tmpTime.setTime( time );
        setTimeLocation( tmpTime, geographicLocation );
    }

    public Cartesian getHeliocentric( Ecliptical.Cart cartesian )
    {
        return cartesian.set( helioCart );
    }

    public Spherical getHeliocentric( Ecliptical.Sphe spherical )
    {
        return helioCart.transform( spherical );
    }

    public Cartesian getHeliocentric( Equatorial.Cart cartesian )
    {
        return helioCart.toEquatorial( currentEcliptic, cartesian );
    }

    public Spherical getHeliocentric( Equatorial.Sphe spherical )
    {
        helioCart.transform( tmpSphericalEcliptical );
        return tmpSphericalEcliptical.toEquatorial( currentEcliptic, spherical );
    }

    public Cartesian getHeliocentric( Horizontal.Cart cartesian )
    {
        return helioCart.toHorizontal( currentEcliptic, toposEquatorialSpherical, cartesian );
    }

    public Spherical getHeliocentric( Horizontal.Sphe spherical )
    {
        return helioCart.toHorizontal( currentEcliptic, toposEquatorialSpherical, spherical );
    }

    public Cartesian getGeocentric( Ecliptical.Cart cartesian )
    {
        return cartesian.set( geoCart );
    }

    public Spherical getGeocentric( Ecliptical.Sphe spherical )
    {
        return geoCart.transform( spherical );
    }

    public Cartesian getGeocentric( Equatorial.Cart cartesian )
    {
        return geoCart.toEquatorial( currentEcliptic, cartesian );
    }

    public Spherical getGeocentric( Equatorial.Sphe spherical )
    {
        geoCart.transform( tmpSphericalEcliptical );
        return tmpSphericalEcliptical.toEquatorial( currentEcliptic, spherical );
    }

    public Cartesian getGeocentric( Horizontal.Cart cartesian )
    {
        return geoCart.toHorizontal( currentEcliptic, toposEquatorialSpherical, cartesian );
    }

    public Spherical getGeocentric( Horizontal.Sphe spherical )
    {
        geoCart.transform( tmpSphericalEcliptical );
        return tmpSphericalEcliptical.toHorizontal( currentEcliptic, toposEquatorialSpherical, spherical );
    }

    public Cartesian getTopocentric( Ecliptical.Cart cartesian )
    {
        return cartesian.set( geoCart ).sub( toposEcliptical );
    }

    public Spherical getTopocentric( Ecliptical.Sphe spherical )
    {
        return tmpCartesian.set( geoCart ).sub( toposEcliptical ).transform( spherical );
    }

    public Cartesian getTopocentric( Equatorial.Cart cartesian )
    {
        return geoCart.toEquatorial( currentEcliptic, cartesian ).sub( toposEquatorial );
    }

    public Spherical getTopocentric( Equatorial.Sphe spherical )
    {
        return getTopocentric( tmpEquatorialCartesian ).transform( spherical );
    }

    public Cartesian getTopocentric( Horizontal.Cart cartesian )
    {
        return ( (Equatorial.Cart)getTopocentric( tmpEquatorialCartesian ) )
                .toHorizontal( toposEquatorialSpherical, cartesian );
    }

    public Spherical getTopocentric( Horizontal.Sphe spherical )
    {
        return getTopocentric( tmpHorizontalCartesian ).transform( spherical );
    }

    /**
     * Compute angle between the rays originating from this body,
     * pointing to the sun and the earth, resp.
     */
    public Angle getPhase( Angle phase )
    {
        final double gh = helioCart.dot( geoCart );
        final double cosPhase = gh / ( geoCart.length() * helioCart.length() );
        return phase.set( Math.acos( cosPhase ), Angle.Unit.RADIANS );
    }

    /**
     * Compute the fraction of the disk which is lighted by the sun
     */
    public double getLightenedArea()
    {
        final double gh = helioCart.dot( geoCart );
        final double cosPhase = gh / ( geoCart.length() * helioCart.length() );
        return 0.5 * ( 1 + cosPhase );
    }

    /**
     * Compute angle between the rays originating from the earth (observer),
     * pointing to the sun and this body, resp.
     */
    public double getElongation()
    {
        double gg = geoCart.length() * geoCart.length();
        double hh = helioCart.length() * helioCart.length();
        double gh = helioCart.dot( geoCart );
        double cosElong = ( gg - gh ) / ( geoCart.length() * Math.sqrt( gg - 2 * gh + hh ) );
        return Math.acos( cosElong );
    }

    public boolean isVisible( double ecliptic, Cartesian zenit )
    {
        final Horizontal.Sphe horizSpherical = new Horizontal.Sphe();
        final Angle height = new Angle();
        getTopocentric( horizSpherical );
        return horizSpherical.getHeight( height ).get( Angle.Unit.RADIANS ) > 0.0;
    }

    public boolean isRetrograde()
    {
        // calculate the z-coordinate of the cross produce r x v (with r and v geocentric)
        return geoCart.x * velocityGeocentric.y - geoCart.y - velocityGeocentric.x < 0;
    }
}
