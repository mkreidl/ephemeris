package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.math.Angle
import com.mkreidl.math.Spherical3

class Zodiac(private val ecliptic: Ecliptic) {

    val pole = Spherical3(1.0, -Math.PI / 2, Math.PI / 2 - ecliptic.meanObliquity)

    fun getEquatorialDirection(sign: Sign) = ecliptic.trafoEcl2MeanEqu(sign.posCart)

    fun getEquatorialDirectionMiddle(sign: Sign) = ecliptic.trafoEcl2MeanEqu(sign.posCartMiddle)

    enum class Sign private constructor(ordinal: Int, val longName: String, val shortName: String) {
        ARI(1, "Aries", "Ari"),
        TAU(2, "Taurus", "Tau"),
        GEM(3, "Gemini", "Gem"),
        CNC(4, "Cancer", "Cnc"),
        LEO(5, "Leo", "Leo"),
        VIR(6, "Virgo", "Vir"),
        LIB(7, "Libra", "Lib"),
        SCO(8, "Scorpio", "Sco"),
        SGR(9, "Sagittarius", "Sgr"),
        CAP(10, "Capricornus", "Cap"),
        AQR(11, "Aquarius", "Aqr"),
        PSC(12, "Pisces", "Psc");

        val angle: Angle = Angle.ofDeg(((ordinal - 1) * 30).toDouble())
        private val middleAngle: Angle = Angle.ofDeg((ordinal - 0.5) * 30)
        private val exitAngle: Angle = Angle.ofDeg((ordinal * 30).toDouble())

        private val position: Spherical3 = Spherical3(1.0, (ordinal - 1) * Math.PI / 6, 0.0)
        private val positionMiddle: Spherical3 = Spherical3(1.0, (ordinal - 0.5) * Math.PI / 6, 0.0)
        private val positionExit: Spherical3 = Spherical3(1.0, ordinal * Math.PI / 6, 0.0)

        internal val posCart = position.cartesian
        internal val posCartMiddle = positionMiddle.cartesian
        internal val posCartExit = positionExit.cartesian

        fun convertToRelativeLongitude(angle: Angle): Angle {
            val lonSign = this.angle.radians
            val lonAbs = angle.radians
            val lonRel = lonAbs - lonSign
            return Angle.ofRad(lonRel)
        }
    }

    companion object {

        fun getSign(lon: Angle) = Sign.values()[lon.degrees.toInt() / 30]

        fun getSignFromRad(lon: Double) = getSign(Angle.ofRad(lon))

        fun getLongitude(sign: Sign) = sign.angle

        fun convertToRelativeLongitude(sign: Sign, angle: Angle) = sign.convertToRelativeLongitude(angle)
    }
}