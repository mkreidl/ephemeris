package com.mkreidl.ephemeris.sky;

import com.mkreidl.ephemeris.solarsystem.Body;

import java.util.EnumMap;
import java.util.Map;

public class CelestialObject
{
    public static final CelestialObject NONE = new CelestialObject()
    {
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

    private static final Map<Body, String> planetNames = new EnumMap<>( Body.class );

    static
    {
        for ( Body planet : Body.values() )
            planetNames.put( planet, planet.toString() );
    }

    public static void setPlanetName( Body planet, String name )
    {
        planetNames.put( planet, name );
    }

    public static CelestialObject of( String name )
    {
        try
        {
            return CelestialObject.of( Body.valueOf( name ) );
        }
        catch ( IllegalArgumentException unused )
        {
            return CelestialObject.of( StarsCatalog.findIndexByName( name ) );
        }
    }

    public static CelestialObject of( int index )
    {
        if ( index > -1 && index < StarsCatalog.SIZE )
        {
            final CelestialObject star = new CelestialObject();
            star.index = index;
            star.scientificName = StarsCatalog.FLAMSTEED_BAYER[index] != null ? StarsCatalog.FLAMSTEED_BAYER[index] : "";
            star.trivialName = StarsCatalog.IAU_NAME[index] != null ? StarsCatalog.IAU_NAME[index] : "";
            star.catalogName = "HR " + Integer.toString( StarsCatalog.BRIGHT_STAR_NUMBER[index] );
            return star;
        }
        else
            return NONE;
    }

    public static CelestialObject of( Body planet )
    {
        final CelestialObject planetObject = new CelestialObject();
        planetObject.planet = planet;
        planetObject.scientificName = planetNames.get( planet );
        planetObject.trivialName = planetNames.get( planet );
        planetObject.catalogName = planetNames.get( planet );
        return planetObject;
    }

    private int index = -1;
    private Body planet = null;
    private String scientificName = "";
    private String trivialName = "";
    private String catalogName = "";

    public boolean isVoid()
    {
        return false;
    }

    public boolean isStar()
    {
        return index >= 0;
    }

    public boolean isPlanet()
    {
        return planet != null;
    }

    public Body asPlanet()
    {
        return planet;
    }

    public int getIndex()
    {
        return index;
    }

    public String getName()
    {
        if ( planet != null )
            return planetNames.get( planet );
        else if ( !trivialName.isEmpty() )
            return trivialName;
        else if ( !scientificName.isEmpty() )
            return scientificName;
        else
            return catalogName;
    }

    public String getScientificName()
    {
        return scientificName;
    }

    public String getTrivialName()
    {
        return trivialName;
    }

    public String getCatalogName()
    {
        return catalogName;
    }

    @Override
    public String toString()
    {
        return isPlanet() ? planet.toString() : catalogName;
    }
}
