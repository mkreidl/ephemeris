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
            final Constellation constellation = Constellations.findByName( name );
            if ( constellation != null )
                return CelestialObject.of( constellation );
            else
                return CelestialObject.of( StarsCatalog.findIndexByName( name ) );
        }
    }

    public static CelestialObject of( int index )
    {
        if ( index > -1 && index < StarsCatalog.SIZE )
        {
            final CelestialObject object = new CelestialObject();
            object.index = index;
            object.scientificName = StarsCatalog.FLAMSTEED_BAYER[index] != null ? StarsCatalog.FLAMSTEED_BAYER[index] : "";
            object.trivialName = StarsCatalog.IAU_NAME[index] != null ? StarsCatalog.IAU_NAME[index] : "";
            object.catalogName = "HR " + Integer.toString( StarsCatalog.BRIGHT_STAR_NUMBER[index] );
            return object;
        }
        else
            return NONE;
    }

    public static CelestialObject of( Body planet )
    {
        final CelestialObject object = new CelestialObject();
        object.planet = planet;
        object.scientificName = planetNames.get( planet );
        object.trivialName = planetNames.get( planet );
        object.catalogName = planetNames.get( planet );
        return object;
    }

    public static CelestialObject of( Constellation constellation )
    {
        final CelestialObject object = new CelestialObject();
        object.constellation = constellation;
        object.scientificName = constellation.getName();
        object.trivialName = constellation.getName();
        object.catalogName = constellation.getName();
        return object;
    }

    private int index = -1;
    private Body planet = null;
    private Constellation constellation = null;
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

    public boolean isObjectWithLocation()
    {
        return isStar() || isPlanet();
    }

    public boolean isConstellation()
    {
        return constellation != null;
    }

    public int asStar()
    {
        return index;
    }

    public Body asPlanet()
    {
        return planet;
    }

    public Constellation asConstellation()
    {
        return constellation;
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
