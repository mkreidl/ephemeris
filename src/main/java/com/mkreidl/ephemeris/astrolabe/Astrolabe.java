package com.mkreidl.ephemeris.astrolabe;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Angle;
import com.mkreidl.ephemeris.geometry.Spherical;
import com.mkreidl.ephemeris.geometry.Stereographic;
import com.mkreidl.ephemeris.sky.SolarSystem;
import com.mkreidl.ephemeris.sky.StarsCatalog;
import com.mkreidl.ephemeris.sky.coordinates.Ecliptical;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;

import java.util.EnumMap;


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

    private EnumMap<SolarSystem.Body, String> planetNames = new EnumMap<>( SolarSystem.Body.class );
    private int tympanonTimeout;
    private int reteTimeout;
    private int planetsTimeout;
    private int moonTimeout;

    public Astrolabe()
    {
        super( 1 );
        geographicLocation.set( new Spherical( 1, 0, 0 ) );
        for ( SolarSystem.Body planet : SolarSystem.Body.values() )
            planetNames.put( planet, planet.toString() );
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
        boolean tympanonWasTimedOut = false;
        if ( force || tympanon.isTimedOut( tympanonTimeout ) )
        {
            tympanonWasTimedOut = true;
            tympanon.synchronize();
        }
        if ( force || rete.isTimedOut( reteTimeout ) )
            rete.synchronize();
        if ( force || planets.isTimedOut( planetsTimeout ) )
            planets.synchronize();
        if ( !force && planets.isTimedOut( moonTimeout ) )
            planets.synchronize( SolarSystem.Body.MOON );
        return tympanonWasTimedOut;
    }

    public synchronized Equatorial.Sphe getLocation()
    {
        return geographicLocation;
    }

    public synchronized void setLocation( double newLon, double newLat )
    {
        if ( newLat < 0 ^ geographicLocation.lat < 0 )
            centerZ = -centerZ;
        geographicLocation.lon = newLon;
        geographicLocation.lat = newLat;
        changeObserverLocation();
    }

    public synchronized void setProjectionCenter( double centerZ )
    {
        this.centerZ = centerZ;
        changeObserverLocation();
    }

    public void setPlanetNames( EnumMap<SolarSystem.Body, String> planetNames )
    {
        this.planetNames = planetNames;
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

    public CelestialObject createCelestialObjectFromString( String name )
    {
        try
        {
            return new Planet( SolarSystem.Body.valueOf( name ) );
        }
        catch ( IllegalArgumentException unused )
        {
            return createStar( StarsCatalog.findIndexByName( name ) );
        }
    }

    public CelestialObject createPlanet( SolarSystem.Body body )
    {
        return body != null ? new Planet( body ) : NO_OBJECT;
    }

    public CelestialObject createStar( int index )
    {
        return index > -1 && index < StarsCatalog.CATALOG_SIZE ? new Star( index ) : NO_OBJECT;
    }

    private void changeObserverLocation()
    {
        tympanon.changeObserverLocation();
        rete.changeObserverLocation();
        planets.changeObserverLocation();
    }

    public static final Astrolabe.CelestialObject NO_OBJECT = new Astrolabe.CelestialObject()
    {
        @Override
        public float[] getProjectedXY( float[] xy )
        {
            xy[0] = xy[1] = 0;
            return xy;
        }

        @Override
        public double getDistanceFromPole()
        {
            return 0;
        }

        @Override
        public String toString()
        {
            return "";
        }

        @Override
        public boolean isVoid()
        {
            return true;
        }
    };

    public Angle getMeanSiderealTime( Angle angle )
    {
        angle.setRadians( geographicLocation.lon );
        return time.getMeanSiderealTime( angle, angle );
    }

    public abstract static class CelestialObject
    {
        public boolean isPlanet()
        {
            return this instanceof Planet;
        }

        public boolean isStar()
        {
            return this instanceof Star;
        }

        public boolean isVoid()
        {
            return false;
        }

        public SolarSystem.Body asPlanet()
        {
            return null;
        }

        public int getIndex()
        {
            return -1;
        }

        public String getScientificName()
        {
            return "";
        }

        public String getTrivialName()
        {
            return "";
        }

        public String getCatalogName()
        {
            return "";
        }

        public abstract float[] getProjectedXY( float[] xy );

        public abstract double getDistanceFromPole();

        public String getName()
        {
            return getTrivialName().isEmpty()
                    ? ( getScientificName().isEmpty() ? getCatalogName()
                    : getScientificName() ) : getTrivialName();
        }
    }

    public class Planet extends CelestialObject
    {
        private final SolarSystem.Body body;

        Planet( SolarSystem.Body body )
        {
            this.body = body;
        }

        @Override
        public float[] getProjectedXY( float[] xy )
        {
            xy[0] = (float)planets.getApparentDisk( body ).x;
            xy[1] = (float)planets.getApparentDisk( body ).y;
            return xy;
        }

        @Override
        public double getDistanceFromPole()
        {
            return planets.getPosition( body ).length();
        }

        @Override
        public String toString()
        {
            return body.toString();
        }

        @Override
        public SolarSystem.Body asPlanet()
        {
            return body;
        }

        @Override
        public String getScientificName()
        {
            final String name = planetNames.get( body );
            return name != null ? name : super.getScientificName();
        }

        @Override
        public String getTrivialName()
        {
            return getScientificName();
        }

        @Override
        public String getCatalogName()
        {
            return getScientificName();
        }
    }

    public class Star extends CelestialObject
    {
        private final int index;
        private final Ecliptical.Cart toDatePositionEcliptical = new Ecliptical.Cart();
        private final Ecliptical.Cart earth = new Ecliptical.Cart();

        Star( int index )
        {
            this.index = index;
        }

        @Override
        public float[] getProjectedXY( float[] xy )
        {
            xy[0] = rete.projectedPos[2 * index];
            xy[1] = rete.projectedPos[2 * index + 1];
            return xy;
        }

        @Override
        public double getDistanceFromPole()
        {
            final double x = rete.projectedPos[2 * index];
            final double y = rete.projectedPos[2 * index + 1];
            return Math.sqrt( x * x + y * y );
        }

        @Override
        public String toString()
        {
            return StarsCatalog.getCatalogName( index );
        }

        @Override
        public int getIndex()
        {
            return index;
        }

        @Override
        public String getScientificName()
        {
            final String name = StarsCatalog.SCIENTIFIC_NAME[index];
            return name != null ? name : super.getScientificName();
        }

        @Override
        public String getTrivialName()
        {
            final String name = StarsCatalog.TRADITIONAL_NAME[index];
            return name != null ? name : super.getTrivialName();
        }

        @Override
        public String getCatalogName()
        {
            return StarsCatalog.getCatalogName( index );
        }
    }
}
