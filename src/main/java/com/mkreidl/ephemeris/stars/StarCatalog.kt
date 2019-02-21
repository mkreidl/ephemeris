package com.mkreidl.ephemeris.stars

interface StarCatalog {

    val size: Int

    fun getDist(index: Int): Double
    fun getRAscJ2000(index: Int): Double
    fun getDeclJ2000(index: Int): Double
    fun getVDist(index: Int): Double
    fun getVRAscJ2000(index: Int): Double
    fun getVDeclJ2000(index: Int): Double

    companion object {
        val COLOR_TABLE = mapOf(
                'O' to -0x644f01,
                'B' to -0x554001,
                'A' to -0x1b1704,
                'F' to -0x60519,
                'G' to -0x2064d,
                'K' to -0x2790,
                'M' to -0x4377a,
                'L' to -0x44e5b,
                'T' to -0xb5352,
                'V' to -0xf4f44,
                'R' to -0x4377a,
                'N' to -0x4377a,
                'S' to -0x44e5b,
                'W' to -0x1,
                'C' to -0x4377a
        )
    }
}
