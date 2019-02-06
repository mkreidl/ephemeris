package com.mkreidl.ephemeris.sky

import com.mkreidl.ephemeris.Instant
import com.mkreidl.ephemeris.geometry.Cartesian
import com.mkreidl.ephemeris.geometry.Matrix3x3
import com.mkreidl.ephemeris.sky.coordinates.Ecliptical
import com.mkreidl.ephemeris.sky.coordinates.Equatorial
import com.mkreidl.ephemeris.solarsystem.Ecliptic
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

    fun computeEclipticalJ2000(timeInMillis: Long, eclipticalPositions: DoubleArray, start: Int, delta: Int) {
        computeEclipticalJ2000(Instant.ofEpochMilli(timeInMillis).julianYearsSinceJ2000, eclipticalPositions, start, delta)
    }

    fun computeEclipticalJ2000(yearsSince2000: Double, eclipticalPositions: DoubleArray, start: Int, delta: Int) {
        var i = start
        while (i < StarsCatalog.SIZE) {
            val offset = 3 * i
            // Compute ecliptical cartesian coordinates for J2000 frame
            eclipticalPositions[offset] = POS_J2000[offset] + VEL_J2000[offset] * yearsSince2000
            eclipticalPositions[offset + 1] = POS_J2000[offset + 1] + VEL_J2000[offset + 1] * yearsSince2000
            eclipticalPositions[offset + 2] = POS_J2000[offset + 2] + VEL_J2000[offset + 2] * yearsSince2000
            i += delta
        }
    }

    @JvmOverloads
    fun computeEquatorial(starIndex: Int, instant: Instant, ecliptic: Ecliptic = Ecliptic(instant)): Vector3 {
        val yearsSince2000 = instant.julianYearsSinceJ2000
        // Compute ecliptical cartesian coordinates in J2000 frame
        val offset = 3 * starIndex
        return ecliptic.trafoEclJ2000ToMeanEquToDate(
                Vector3(
                        POS_J2000[offset] + VEL_J2000[offset] * yearsSince2000,
                        POS_J2000[offset + 1] + VEL_J2000[offset + 1] * yearsSince2000,
                        POS_J2000[offset + 2] + VEL_J2000[offset + 2] * yearsSince2000
                )).normalize()
    }

    fun computeConstellationCenter(constellation: Constellation, starsCoordinates: DoubleArray): Vector3 {
        val x = constellation.starList.map { starsCoordinates[it * 3] }.sum()
        val y = constellation.starList.map { starsCoordinates[it * 3 + 1] }.sum()
        val z = constellation.starList.map { starsCoordinates[it * 3 + 2] }.sum()
        return Vector3(x, y, z).normalize()
    }

}
