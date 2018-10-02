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

    public static String getPlanetName( Body planet )
    {
        return planetNames.get( planet );
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

    public static CelestialObject of( Body body )
    {
        final CelestialObject planet = new CelestialObject();
        planet.body = body;
        planet.scientificName = planetNames.get( body );
        planet.trivialName = planetNames.get( body );
        planet.catalogName = planetNames.get( body );
        return planet;
    }

    private int index = -1;
    private Body body = null;
    private String scientificName = "";
    private String trivialName = "";
    private String catalogName = "";

    public int getIndex()
    {
        return index;
    }

    public boolean isPlanet()
    {
        return body != null;
    }

    public Body asPlanet()
    {
        return body;
    }

    public boolean isStar()
    {
        return index >= 0;
    }

    public boolean isVoid()
    {
        return false;
    }

    public String getName()
    {
        return trivialName.isEmpty() ?
                ( scientificName.isEmpty() ? catalogName : scientificName ) : trivialName;
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

    public String toString()
    {
        return isPlanet() ? body.toString() : catalogName;
    }
}
