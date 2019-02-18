package com.mkreidl.ephemeris.stars

object NauticalAlmanach {
    
    private val HR_NUMBERS = intArrayOf(15, 99, 168, 188, 472, 617, 897, 911, 1017, 1457, 1713, 1708, 1790, 1791,
            1903, 2061, 2326, 2491, 2618, 2943, 2990, 3307, 3634, 3685, 3748, 3982, 4301, 4534, 4662, 4730, 4763,
            4905, 5056, 5191, 5267, 5288, 5340, 5459, 5530, 5563, 5793, 6134, 6217, 6378, 6527, 6556, 6705, 6879,
            7001, 7121, 7557, 7790, 7924, 8308, 8425, 8728, 8781)

    private val NA_MAP = mapOf(
            15 to 1,
            99 to 2,
            168 to 3,
            188 to 4,
            472 to 5,
            617 to 6,
            897 to 7,
            898 to 7,
            911 to 8,
            1017 to 9,
            1457 to 10,
            1713 to 11,
            1708 to 12,
            1790 to 13,
            1791 to 14,
            1903 to 15,
            2061 to 16,
            2326 to 17,
            2491 to 18,
            2618 to 19,
            2943 to 20,
            2990 to 21,
            3307 to 22,
            3634 to 23,
            3685 to 24,
            3748 to 25,
            3982 to 26,
            4301 to 27,
            4534 to 28,
            4662 to 29,
            4730 to 30,
            4763 to 31,
            4905 to 32,
            5056 to 33,
            5191 to 34,
            5267 to 35,
            5288 to 36,
            5340 to 37,
            5459 to 38,
            5530 to 39,
            5531 to 39,
            5563 to 40,
            5793 to 41,
            6134 to 42,
            6217 to 43,
            6378 to 44,
            6527 to 45,
            6556 to 46,
            6705 to 47,
            6879 to 48,
            7001 to 49,
            7121 to 50,
            7557 to 51,
            7790 to 52,
            7924 to 53,
            8308 to 54,
            8425 to 55,
            8728 to 56,
            8781 to 57,
            424 to 58  // Polaris
    )

    fun getNAIndex(hrNumber: Int) = NA_MAP.getOrDefault(hrNumber, 0)

    fun getHRNumber(naIndex: Int) = HR_NUMBERS[naIndex - 1]
}
