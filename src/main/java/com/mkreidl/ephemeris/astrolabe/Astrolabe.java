package com.mkreidl.ephemeris.astrolabe;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Angle;
import com.mkreidl.ephemeris.geometry.Spherical;
import com.mkreidl.ephemeris.geometry.Stereographic;
import com.mkreidl.ephemeris.sky.CelestialObject;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;
import com.mkreidl.ephemeris.solarsystem.Body;

public class Astrolabe extends Stereographic
{
    public enum Hemisphere
    {
        SOUTH, NORTH
    }

    public final Tympanon tympanon = new Tympanon( this );
    public final Rete rete = new Rete( this );
    public final Planets planets = new Planets( this );

    protected final Time time = new Time();
    final Equatorial.Sphe geographicLocation = new Equatorial.Sphe();

    private int tympanonTimeout;
    private int reteTimeout;
    private int planetsTimeout;
    private int moonTimeout;

    public Astrolabe()
    {
        super( 1 );
        geographicLocation.set( new Spherical( 1, 0, 0 ) );
    }

    public Hemisphere getHemisphere()
    {
        return centerZ > 0 ? Hemisphere.SOUTH : Hemisphere.NORTH;
    }

    public Time getTime()
    {
        return time;
    }

    long getTimeInMillis()
    {
        return time.getTime();
    }

    public void setSynchronizationTimeouts( int tympanon, int rete, int planets, int moon )
    {
        tympanonTimeout = tympanon;
        reteTimeout = rete;
        planetsTimeout = planets;
        moonTimeout = moon;
    }

    public synchronized boolean setTime( long timeInMillis, boolean force )
    {
        time.setTime( timeInMillis );
        boolean tympanonWasTimedOut = force || tympanon.isTimedOut( tympanonTimeout );
        if ( tympanonWasTimedOut )
            tympanon.synchronize();
        if ( force || rete.isTimedOut( reteTimeout ) )
            rete.synchronize();
        if ( force || planets.isTimedOut( planetsTimeout ) )
            planets.synchronize();
        if ( !force && planets.isTimedOut( moonTimeout ) )
            planets.synchronize( Body.MOON );
        return tympanonWasTimedOut;
    }

    public synchronized Equatorial.Sphe getLocation()
    {
        return geographicLocation;
    }

    public synchronized void setLocation( double lonRad, double latRad )
    {
        if ( latRad < 0 ^ geographicLocation.lat < 0 )
            centerZ = -centerZ;
        geographicLocation.lon = lonRad;
        geographicLocation.lat = latRad;
        changeObserverLocation();
    }

    public synchronized void setProjectionCenter( double centerZ )
    {
        this.centerZ = centerZ;
        changeObserverLocation();
    }

    /**
     * Calculate the angle as it appears after stereographic projection between
     * the points South and West on the mathematical horizon
     *
     * @return Angle between south and west in radians
     */
    public Angle getAngleSouthWest( Angle angle )
    {
        final double dx = rete.equator.x - tympanon.horizonMathematical.x;
        final double dy = rete.equator.y - tympanon.horizonMathematical.y;
        final double d = Math.sqrt( dx * dx + dy * dy );
        final double r = tympanon.horizonMathematical.r;
        angle.set( Math.acos( ( d * d + r * r - 1 ) / ( 2 * d * r ) ), Angle.Unit.RADIANS );
        return angle;
    }

    private void changeObserverLocation()
    {
        tympanon.changeObserverLocation();
        rete.changeObserverLocation();
        planets.changeObserverLocation();
    }

    public void getMeanSiderealTime( Angle angle )
    {
        angle.setRadians( geographicLocation.lon );
        time.getMeanSiderealTime( angle, angle );
    }

    public double computeDeclinationDeg( CelestialObject object )
    {
        if ( object.isPlanet() )
            return Math.toDegrees( planets.getDeclination( object.asPlanet() ) );
        else if ( object.isStar() )
            return Math.toDegrees( rete.getDeclination( object.asStar() ) );
        else if ( object.isConstellation() )
            return Math.toDegrees( rete.getDeclination( object.asConstellation() ) );
        else
            return 0;
    }

    public float[] getProjectedXY( CelestialObject object, float[] xy )
    {
        if ( object.isPlanet() )
        {
            xy[0] = (float)planets.getApparentDisk( object.asPlanet() ).x;
            xy[1] = (float)planets.getApparentDisk( object.asPlanet() ).y;
        }
        else if ( object.isStar() )
            System.arraycopy( rete.projectedPos, 2 * object.asStar(), xy, 0, 2 );
        else if ( object.isConstellation() )
            rete.getConstellationCenter( object.asConstellation(), xy );
        return xy;
    }

    public double getDistanceFromPole( CelestialObject object )
    {
        final float[] p = getProjectedXY( object, new float[2] );
        return Math.sqrt( p[0] * p[0] + p[1] * p[1] );
    }

}
