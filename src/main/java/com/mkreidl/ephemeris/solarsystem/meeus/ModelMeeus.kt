package com.mkreidl.ephemeris.solarsystem.meeus

import com.mkreidl.ephemeris.solarsystem.vsop87.ModelVsop87

interface ModelMeeus {
    fun createModel(): ModelVsop87.LBR
}