package com.mkreidl.ephemeris.stars

import com.mkreidl.ephemeris.Instant
import com.mkreidl.ephemeris.solarsystem.*
import com.mkreidl.math.Sphe
import com.mkreidl.math.Vector3

class Stars(catalog: StarCatalog) {

    private val size = catalog.size

    private val posJ2000 = DoubleArray(size * 3)
    private val velJ2000 = DoubleArray(size * 3)

    init {
        for (i in 0 until size) {
            val posJ2000 = Sphe(1.0, catalog.getRAscJ2000(i), catalog.getDeclJ2000(i))
            val velJ2000 = Sphe(0.0, catalog.getVRAscJ2000(i), catalog.getVDeclJ2000(i))
            val phaseJ2000 = PhaseSpherical(posJ2000, velJ2000).toCartesian()
            val ecliptical = Ecliptic.J2000.trafoMeanEqu2Ecl * phaseJ2000

            var offset = 3 * i
            this.posJ2000[offset] = ecliptical.position.x
            this.velJ2000[offset] = ecliptical.velocity.x
            this.posJ2000[++offset] = ecliptical.position.y
            this.velJ2000[offset] = ecliptical.velocity.y
            this.posJ2000[++offset] = ecliptical.position.z
            this.velJ2000[offset] = ecliptical.velocity.z
        }
    }

    fun posEclJ2000(index: Int) = Vector3(posJ2000[3 * index], posJ2000[3 * index + 1], posJ2000[3 * index + 2])
    fun velEclJ2000(index: Int) = Vector3(velJ2000[3 * index], velJ2000[3 * index + 1], velJ2000[3 * index + 2])

    fun computeEclipticalJ2000(instant: Instant, eclipticalPositions: DoubleArray) {
        computeEclipticalJ2000(instant, eclipticalPositions, 0, 1)
    }

    fun computeEclipticalJ2000(instant: Instant, eclipticalPositions: DoubleArray, start: Int, delta: Int) {
        for (i in start until size step delta) {
            val pos = posEclJ2000(i) + velEclJ2000(i) * instant.julianYearsSinceJ2000
            eclipticalPositions[3 * i] = pos.x
            eclipticalPositions[3 * i + 1] = pos.y
            eclipticalPositions[3 * i + 2] = pos.z
        }
    }

    fun computeEclipticalJ2000(starIndex: Int, instant: Instant) =
            posEclJ2000(starIndex) + velEclJ2000(starIndex) * instant.julianYearsSinceJ2000

    fun computeMeanEcliptical(starIndex: Int, instant: Instant, ecliptic: Ecliptic = Ecliptic(instant)) =
            ecliptic.trafoEclJ2000ToEclToDate(computeEclipticalJ2000(starIndex, instant))

    fun computeTrueEcliptical(starIndex: Int, instant: Instant, ecliptic: Ecliptic = Ecliptic(instant)) =
            ecliptic.trafoEclJ2000ToTrueEclToDate(computeEclipticalJ2000(starIndex, instant))

    @JvmOverloads
    fun computeMeanEquatorial(starIndex: Int, instant: Instant, ecliptic: Ecliptic = Ecliptic(instant)): Sphe {
        val eclipticalJ2000 = computeEclipticalJ2000(starIndex, instant)
        return ecliptic.trafoEclJ2000ToMeanEquToDate(eclipticalJ2000).toSpherical()
    }

    fun computeTrueEquatorial(starIndex: Int, instant: Instant, ecliptic: Ecliptic = Ecliptic(instant)): Sphe {
        val position = computeTrueEcliptical(starIndex, instant, ecliptic)
        return ecliptic.trafoTrueEcl2TrueEqu(position).toSpherical()
    }

    fun computeEclipticalApparent(starIndex: Int, instant: Instant, ecliptic: Ecliptic = Ecliptic(instant), sun: Sun = SunLowPrecision(instant)): Sphe {
        val position = computeTrueEcliptical(starIndex, instant, ecliptic)
        return sun.computeAberrationCorrectionEcliptical(position.toSpherical())
    }

    fun computeEquatorialApparent(starIndex: Int, instant: Instant, ecliptic: Ecliptic = Ecliptic(instant), sun: Sun = SunLowPrecision(instant)): Sphe {
        val apparent = computeEclipticalApparent(starIndex, instant, ecliptic, sun).toCartesian()
        return ecliptic.trafoTrueEcl2TrueEqu(apparent).toSpherical()
    }
}
