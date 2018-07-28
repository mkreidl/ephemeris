package com.mkreidl.ephemeris.sky;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Constellation implements Iterable<Integer>
{
    private final String name;
    private final List<int[]> paths;

    private final StarIterator iterator = new StarIterator();

    private class StarIterator implements Iterator<Integer>
    {
        int path = 0;
        int vertex = 0;

        void reset()
        {
            path = 0;
            vertex = 0;
        }

        @Override
        public boolean hasNext()
        {
            return vertex < paths.get( path ).length - 1 || path < paths.size() - 1;
        }

        @Override
        public Integer next()
        {
            if ( vertex < paths.get( path ).length - 1 )
                vertex++;
            else
            {
                vertex = 0;
                path++;
            }
            return paths.get( path )[vertex];
        }
    }

    public List<int[]> getPaths()
    {
        return paths;
    }

    public String getName()
    {
        return name;
    }

    Constellation( String name, int[]... paths )
    {
        this.name = name;
        this.paths = Arrays.asList( paths );
    }

    @Override
    public Iterator<Integer> iterator()
    {
        iterator.reset();
        return iterator;
    }
}
