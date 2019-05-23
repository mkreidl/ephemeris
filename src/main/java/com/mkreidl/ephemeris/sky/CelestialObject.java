package com.mkreidl.ephemeris.sky;

import com.mkreidl.astro.stars.Constellation;
import com.mkreidl.astro.stars.HipparcosCatalog;
import com.mkreidl.astro.stars.StarCatalog;
import com.mkreidl.ephemeris.solarsystem.Body;

public class CelestialObject
{
    private static final StarCatalog hipparcos = HipparcosCatalog.INSTANCE;

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

    public static CelestialObject of( String name )
    {
        try
        {
            return CelestialObject.of( Body.valueOf( name ) );
        }
        catch ( IllegalArgumentException unused )
        {
            final Constellation constellation = Constellation.Companion.findByName( name );
            if ( constellation != null )
                return CelestialObject.of( constellation );
            else
                return CelestialObject.of( hipparcos.findIndexByName( name ) );
        }
    }

    public static CelestialObject of( int index )
    {
        if ( index > -1 && index < hipparcos.getSize() )
        {
            final CelestialObject object = new CelestialObject();
            object.index = index;
            object.scientificName = hipparcos.FLAMSTEED_BAYER[index] != null ? hipparcos.FLAMSTEED_BAYER[index] : "";
            object.trivialName = hipparcos.IAU_NAME[index] != null ? hipparcos.IAU_NAME[index] : "";
            object.catalogName = "HR " + hipparcos.BRIGHT_STAR_NUMBER[index];
            return object;
        }
        else
            return NONE;
    }

    public static CelestialObject of( Body planet )
    {
        final CelestialObject object = new CelestialObject();
        object.planet = planet;
        object.scientificName = planet.toString();
        object.trivialName = planet.toString();
        object.catalogName = planet.toString();
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

    public boolean isPoint()
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

    public String getStarName()
    {
        if ( !trivialName.isEmpty() )
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
