package com.mkreidl.ephemeris.sky;

import java.util.HashMap;
import java.util.Map;

public class NauticalAlmanach
{
    private static final int[] HR_NUMBERS = new int[]{
            15, 99, 168, 188, 472, 617, 897, 911, 1017, 1457,
            1713, 1708, 1790, 1791, 1903, 2061, 2326, 2491, 2618, 2943,
            2990, 3307, 3634, 3685, 3748, 3982, 4301, 4534, 4662, 4730,
            4763, 4905, 5056, 5191, 5267, 5288, 5340, 5459, 5530, 5563,
            5793, 6134, 6217, 6378, 6527, 6556, 6705, 6879, 7001, 7121,
            7557, 7790, 7924, 8308, 8425, 8728, 8781};

    private static final Map<Integer, Integer> NA_MAP = new HashMap<>();

    static
    {
        NA_MAP.put( 15, 1 );
        NA_MAP.put( 99, 2 );
        NA_MAP.put( 168, 3 );
        NA_MAP.put( 188, 4 );
        NA_MAP.put( 472, 5 );
        NA_MAP.put( 617, 6 );
        NA_MAP.put( 897, 7 );
        NA_MAP.put( 898, 7 );
        NA_MAP.put( 911, 8 );
        NA_MAP.put( 1017, 9 );
        NA_MAP.put( 1457, 10 );
        NA_MAP.put( 1713, 11 );
        NA_MAP.put( 1708, 12 );
        NA_MAP.put( 1790, 13 );
        NA_MAP.put( 1791, 14 );
        NA_MAP.put( 1903, 15 );
        NA_MAP.put( 2061, 16 );
        NA_MAP.put( 2326, 17 );
        NA_MAP.put( 2491, 18 );
        NA_MAP.put( 2618, 19 );
        NA_MAP.put( 2943, 20 );
        NA_MAP.put( 2990, 21 );
        NA_MAP.put( 3307, 22 );
        NA_MAP.put( 3634, 23 );
        NA_MAP.put( 3685, 24 );
        NA_MAP.put( 3748, 25 );
        NA_MAP.put( 3982, 26 );
        NA_MAP.put( 4301, 27 );
        NA_MAP.put( 4534, 28 );
        NA_MAP.put( 4662, 29 );
        NA_MAP.put( 4730, 30 );
        NA_MAP.put( 4763, 31 );
        NA_MAP.put( 4905, 32 );
        NA_MAP.put( 5056, 33 );
        NA_MAP.put( 5191, 34 );
        NA_MAP.put( 5267, 35 );
        NA_MAP.put( 5288, 36 );
        NA_MAP.put( 5340, 37 );
        NA_MAP.put( 5459, 38 );
        NA_MAP.put( 5530, 39 );
        NA_MAP.put( 5531, 39 );
        NA_MAP.put( 5563, 40 );
        NA_MAP.put( 5793, 41 );
        NA_MAP.put( 6134, 42 );
        NA_MAP.put( 6217, 43 );
        NA_MAP.put( 6378, 44 );
        NA_MAP.put( 6527, 45 );
        NA_MAP.put( 6556, 46 );
        NA_MAP.put( 6705, 47 );
        NA_MAP.put( 6879, 48 );
        NA_MAP.put( 7001, 49 );
        NA_MAP.put( 7121, 50 );
        NA_MAP.put( 7557, 51 );
        NA_MAP.put( 7790, 52 );
        NA_MAP.put( 7924, 53 );
        NA_MAP.put( 8308, 54 );
        NA_MAP.put( 8425, 55 );
        NA_MAP.put( 8728, 56 );
        NA_MAP.put( 8781, 57 );
        NA_MAP.put( 424, 58 );  // Polaris
    }

    public static int getNAIndex( int hrNumber )
    {
        final Integer na = NA_MAP.get( hrNumber );
        return na != null ? na : 0;
    }

    public static int getHRNumber( int naIndex )
    {
        return HR_NUMBERS[naIndex - 1];
    }
}
