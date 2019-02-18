package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.math.Coordinates
import com.mkreidl.math.Phase

internal interface OrbitalElements {

    val classical: ClassicalOrbitalElements
    val vsop87: VSOP87OrbitalElements

    fun computePhase(): Phase
    fun computePosition(): Coordinates
}
