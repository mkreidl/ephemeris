package com.mkreidl.ephemeris.sky;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Constellation
{
    private final String name;
    private final List<int[]> paths = new ArrayList<>();
    private final Integer[] starSet;

    public String getName()
    {
        return name;
    }

    public List<int[]> getPaths()
    {
        return paths;
    }

    public Integer[] getStarSet()
    {
        return starSet;
    }

    Constellation( String name, int[]... paths )
    {
        this.name = name;
        final Set<Integer> starSet = new HashSet<>();
        for ( int[] path : paths )
        {
            this.paths.add( path );
            for ( int i : path )
                starSet.add( i );
        }
        this.starSet = starSet.toArray( new Integer[0] );
    }
}
