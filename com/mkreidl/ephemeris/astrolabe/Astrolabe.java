package com.mkreidl.ephemeris.astrolabe;

import org.eclipse.jdt.annotation.NonNull;

import com.mkreidl.ephemeris.*;
import com.mkreidl.ephemeris.geometry.*;
import com.mkreidl.ephemeris.sky.*;
import com.mkreidl.ephemeris.sky.coordinates.*;


public class Astrolabe
{
    protected final Time time = new Time();
    protected final Equatorial.Sphe geographicLocation = new Equatorial.Sphe();
    protected final Camera camera = new Camera( Camera.Direction.SOUTH, Camera.Position.INNER );

    public final Tympanon tympanon = new Tympanon( this );
    public final Rete rete = new Rete( this );
    public final Planets planets = new Planets( this );

    public Time getTime()
    {
        return time;
    }

    public long getTimeInMillis()
    {
        return time.getTime();
    }

    public void setTimeInMillis( long timeInMillis )
    {
        time.setTime( timeInMillis );
    }

    public Ephemerides getEphemerides( String object, Ephemerides output )
    {
        output.setTimeLocation( time, geographicLocation );
        try
        {
            planets.solarSystem.getEphemerides( SolarSystem.Body.valueOf( object ), output );
        }
        catch ( IllegalArgumentException e )
        {
            // In that case, object was not convertible to a planet enum constant
            // => let's try with a star...
            final int index = StarsCatalogue.findIndexByName( object );
            planets.solarSystem.getEphemerides( rete.eclipticalPos[index], output );
        }
        return output;
    }

    public void synchronize( long timeInMillis )
    {
        time.setTime( timeInMillis );
        tympanon.synchronize();
        rete.synchronize();
        planets.synchronize();
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
