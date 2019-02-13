package com.mkreidl.ephemeris.stars

interface StarCatalog {

    val size: Int

    fun getDist(index: Int): Double
    fun getRAscJ2000(index: Int): Double
    fun getDeclJ2000(index: Int): Double
    fun getVDist(index: Int): Double
    fun getVRAscJ2000(index: Int): Double
    fun getVDeclJ2000(index: Int): Double
}