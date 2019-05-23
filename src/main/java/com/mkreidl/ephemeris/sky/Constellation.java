package com.mkreidl.ephemeris.sky;

import com.mkreidl.astro.stars.HipparcosCatalog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Constellation
{
    public enum Hemisphere
    {
        NORTHERN, SOUTHERN, ZODIAC
    }

    private final String name;
    private final List<int[]> paths = new ArrayList<>();
    private final List<Integer> starList = new ArrayList<>();
    private final Hemisphere hemisphere;

    public String getName()
    {
        return name;
    }

    public Hemisphere getHemisphere()
    {
        return hemisphere;
    }

    public List<int[]> getPaths()
    {
        return paths;
    }

    public int getBrightestStar()
    {
        return starList.get( 0 );
    }

    List<Integer> getStarList()
    {
        return Collections.unmodifiableList( starList );
    }

    Constellation( String name, int[]... paths )
    {
        this.name = name;
        for ( int[] path : paths )
        {
            this.paths.add( path );
            for ( int star : path )
                if ( !starList.contains( star ) )
                    starList.add( star );
        }
        Collections.sort( starList );
        hemisphere = computeHemisphere();
    }

    private Hemisphere computeHemisphere()
    {
        boolean hasStarNorthern = false;
        boolean hasStarSouthern = false;
        for ( int star : starList )
        {
            final HipparcosCatalog stars = HipparcosCatalog.INSTANCE;
            final double z = stars.phaseEclJ2000[3 * star + 2].getPosition().z;
            hasStarNorthern |= z > 0;
            hasStarSouthern |= z < 0;
        }
        if ( hasStarNorthern && hasStarSouthern )
            return Hemisphere.ZODIAC;
        else if ( hasStarNorthern )
            return Hemisphere.NORTHERN;
        else if ( hasStarSouthern )
            return Hemisphere.SOUTHERN;
        else
            return null;
    }
}
