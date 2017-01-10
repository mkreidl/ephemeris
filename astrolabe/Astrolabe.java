package com.mkreidl.ephemeris.astrolabe;

import android.support.annotation.NonNull;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Angle;
import com.mkreidl.ephemeris.geometry.Spherical;
import com.mkreidl.ephemeris.sky.Ephemerides;
import com.mkreidl.ephemeris.sky.SolarSystem;
import com.mkreidl.ephemeris.sky.StarsCatalogue;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;


public class Astrolabe
{
    protected final Time time = new Time();
    protected final Equatorial.Sphe geographicLocation = new Equatorial.Sphe();
    protected final Camera camera = new Camera( Camera.Direction.SOUTH, Camera.Position.INNER );

    public final Tympanon tympanon = new Tympanon( this );
    public final Rete rete = new Rete( this );
    public final Planets planets = new Planets( this );

    public Spherical getGeographicLocation()
    {
        return geographicLocation;
    }

    public long getTimeInMillis()
    {
        return time.getTime();
    }

    public Time getTime()
    {
        return time;
    }

    public void setTime( @NonNull Time newTime )
    {
        time.setTime( newTime.getTime() );
    }

    public Ephemerides getEphemerides( String object, Ephemerides output )
    {
        try
        {
            output.setTimeLocation( time, geographicLocation );
            return planets.getEphemerides( SolarSystem.Body.valueOf( object ), output );
        }
        catch ( IllegalArgumentException e )
        {
            // In that case, object was not convertible to a planet enum constant
            // => lets try with a star
            int index = StarsCatalogue.findIndexByName( object );
            return planets.solarSystem.getEphemerides( rete.coordinates.get( index ), output );
        }
    }

    public void synchronize( long timeInMillis, boolean full )
    {
        time.setTime( timeInMillis );
        tympanon.synchronize();
        if ( full )
        {
            rete.synchronize();
            planets.synchronize();
        }
    }

    public void localize( @NonNull Spherical newLocation )
    {
        geographicLocation.set( newLocation );
        tympanon.localize();
        rete.localize();
        planets.localize();
    }

    public void view( @NonNull Camera camera )
    {
        this.camera.setPosition( camera.position );
        this.camera.setDirection( camera.direction );
        tympanon.view();
        rete.view();
        planets.view();
    }

    /**
     * Calculate the angle as it appears after stereographic projection between
     * the points South and West on the mathematical horizon
     *
     * @return Angle between south and west in radians
     */
    public double getAngleSouthWest( @NonNull Angle angle )
    {
        final double dx = rete.equator.x - tympanon.horizonMathematical.x;
        final double dy = rete.equator.y - tympanon.horizonMathematical.y;
        final double d = Math.sqrt( dx * dx + dy * dy );
        final double r = tympanon.horizonMathematical.r;
        final double eps = camera.position.value();
        angle.set( Math.acos( eps * ( d * d + r * r - 1 ) / ( 2 * d * r ) ), Angle.Unit.RADIANS );
        return angle.get();
    }
}
