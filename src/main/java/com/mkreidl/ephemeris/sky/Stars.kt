package com.mkreidl.ephemeris.sky

import com.mkreidl.ephemeris.Instant
import com.mkreidl.ephemeris.geometry.Cartesian
import com.mkreidl.ephemeris.geometry.Matrix3x3
import com.mkreidl.ephemeris.sky.coordinates.Ecliptical
import com.mkreidl.ephemeris.sky.coordinates.Equatorial
import com.mkreidl.ephemeris.solarsystem.Ecliptic
import com.mkreidl.ephemeris.solarsystem.Sun
import com.mkreidl.ephemeris.solarsystem.SunLowPrecision
import com.mkreidl.math.Sphe
import com.mkreidl.math.Vector3
import java.lang.Math.cos
import java.lang.Math.sin

object Stars {
    internal val POS_J2000 = DoubleArray(StarsCatalog.SIZE * 3)
    internal val VEL_J2000 = DoubleArray(StarsCatalog.SIZE * 3)

    init {
        val equ2ecl = Matrix3x3(Ecliptic.J2000.trafoMeanEqu2Ecl)
        val jacobian = Matrix3x3()
        val tmp = Cartesian()
        val velEquatorial = Equatorial.Cart()
        val posEquatorial = Equatorial.Cart()
        val posJ2000 = Equatorial.Sphe()

        for (i in 0 until StarsCatalog.SIZE) {
            val ra = StarsCatalog.getRAscJ2000(i)
            val decl = StarsCatalog.getDeclJ2000(i)
            val sinRa = sin(ra)
            val cosRa = cos(ra)
            val sinDecl = sin(decl)
            val cosDecl = cos(decl)
            jacobian.set(
                    -sinRa * cosDecl, -cosRa * sinDecl, 0.0,
                    cosRa * cosDecl, -sinRa * sinDecl, 0.0,
                    0.0, cosDecl, 0.0
            )
            tmp.set(StarsCatalog.getVRAscJ2000(i), StarsCatalog.getVDeclJ2000(i), 0.0)
            jacobian.applyTo(tmp, velEquatorial)
            val vel = equ2ecl.applyTo(velEquatorial, Ecliptical.Cart())

            posJ2000.set(1.0, StarsCatalog.getRAscJ2000(i), StarsCatalog.getDeclJ2000(i))
            posJ2000.transform(posEquatorial)
            val pos = equ2ecl.applyTo(posEquatorial, Ecliptical.Cart())

            var offset = 3 * i
            POS_J2000[offset] = pos.x
            VEL_J2000[offset] = vel.x
            POS_J2000[++offset] = pos.y
            VEL_J2000[offset] = vel.y
            POS_J2000[++offset] = pos.z
            VEL_J2000[offset] = vel.z
        }
    }

    fun computeEclipticalJ2000(instant: Instant, eclipticalPositions: DoubleArray) {
        computeEclipticalJ2000(instant.julianYearsSinceJ2000, eclipticalPositions, 0, 1)
    }

    fun computeEclipticalJ2000(yearsSince2000: Double, eclipticalPositions: DoubleArray, start: Int, delta: Int) {
        for (i in start until StarsCatalog.SIZE step delta) {
            val offset = 3 * i
            eclipticalPositions[offset] = POS_J2000[offset] + VEL_J2000[offset] * yearsSince2000
            eclipticalPositions[offset + 1] = POS_J2000[offset + 1] + VEL_J2000[offset + 1] * yearsSince2000
            eclipticalPositions[offset + 2] = POS_J2000[offset + 2] + VEL_J2000[offset + 2] * yearsSince2000
        }
    }

    fun computeEclipticalJ2000(starIndex: Int, instant: Instant): Vector3 {
        val yearsSince2000 = instant.julianYearsSinceJ2000
        val offset = 3 * starIndex
        return Vector3(
                POS_J2000[offset] + VEL_J2000[offset] * yearsSince2000,
                POS_J2000[offset + 1] + VEL_J2000[offset + 1] * yearsSince2000,
                POS_J2000[offset + 2] + VEL_J2000[offset + 2] * yearsSince2000
        )
    }

    fun computeMeanEcliptical(starIndex: Int, instant: Instant, ecliptic: Ecliptic = Ecliptic(instant)) =
            ecliptic.trafoEclJ2000ToEclToDate(Stars.computeEclipticalJ2000(starIndex, instant))

    fun computeTrueEcliptical(starIndex: Int, instant: Instant, ecliptic: Ecliptic = Ecliptic(instant)) =
            ecliptic.trafoEclJ2000ToTrueEclToDate(Stars.computeEclipticalJ2000(starIndex, instant))

    @JvmOverloads
    fun computeMeanEquatorial(starIndex: Int, instant: Instant, ecliptic: Ecliptic = Ecliptic(instant)): Sphe {
        val eclipticalJ2000 = Stars.computeEclipticalJ2000(starIndex, instant)
        return ecliptic.trafoEclJ2000ToMeanEquToDate(eclipticalJ2000).toSpherical()
    }

    fun computeTrueEquatorial(starIndex: Int, instant: Instant, ecliptic: Ecliptic = Ecliptic(instant)): Sphe {
        val position = computeTrueEcliptical(starIndex, instant, ecliptic)
        return ecliptic.trafoTrueEcl2TrueEqu(position).toSpherical()
    }

    fun computeEclipticalApparent(starIndex: Int, instant: Instant, ecliptic: Ecliptic = Ecliptic(instant), sun: Sun = SunLowPrecision(instant)): Sphe {
        val position = Stars.computeTrueEcliptical(starIndex, instant, ecliptic)
        return sun.computeAberrationCorrectionEcliptical(position.toSpherical())
    }

    fun computeEquatorialApparent(starIndex: Int, instant: Instant, ecliptic: Ecliptic = Ecliptic(instant), sun: Sun = SunLowPrecision(instant)): Sphe {
        val apparent = computeEclipticalApparent(starIndex, instant, ecliptic, sun).toCartesian()
        return ecliptic.trafoTrueEcl2TrueEqu(apparent).toSpherical()
    }
}
