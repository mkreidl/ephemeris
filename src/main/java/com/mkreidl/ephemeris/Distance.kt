package com.mkreidl.ephemeris

@Suppress("EnumEntryName")
enum class Distance(private val unitInMeters: Double) {
    ls(2.998e8), // light second
    ly(9.460730472580800e15),
    AU(1.495978707e11),
    m(1.0),
    km(1e3);

    fun toMeters(): Double {
        return unitInMeters
    }
}
