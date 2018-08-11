package com.mkreidl.ephemeris.sky;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Constellation
{
    private final String name;
    private final List<List<Integer>> paths = new ArrayList<>();
    private final Set<Integer> starSet = new HashSet<>();

    public String getName()
    {
        return name;
    }

    public List<List<Integer>> getPaths()
    {
        return paths;
    }

    public Set<Integer> getStarSet()
    {
        return starSet;
    }

    Constellation( String name, Integer[]... paths )
    {
        this.name = name;
        for ( Integer[] path : paths )
        {
            final List<Integer> pathAsList = Arrays.asList( path );
            this.paths.add( pathAsList );
            this.starSet.addAll( pathAsList );
        }
    }
}
