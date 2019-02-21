package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Distance
import com.mkreidl.ephemeris.sky.Topos
import com.mkreidl.ephemeris.solarsystem.meeus.*
import com.mkreidl.ephemeris.time.Instant
import com.mkreidl.math.PhaseCartesian
import com.mkreidl.math.PhaseSpherical
import com.mkreidl.math.times

val PhaseCartesian.retrograde get() = position.x * velocity.y - position.y * velocity.x < 0
val PhaseSpherical.retrograde get() = velocity.y < 0

class SolarSystem(private val models: Map<Body, OrbitalModel>) {

    private val eclipticalHeliocentric = mutableMapOf(Body.SUN to PhaseCartesian.ZERO)
    private val eclipticalGeocentric = mutableMapOf(Body.EARTH to PhaseCartesian.ZERO)
    private val geocentricDistances = mutableMapOf(Body.EARTH to 0.0)

    private val toMetersEarth = models.getValue(Body.EARTH).distanceUnit.toMeters()

    private lateinit var ecliptic: Ecliptic

    fun compute(instant: Instant, ecliptic: Ecliptic = Ecliptic(instant)) {
        computeEarth(instant, ecliptic)
        models.keys.forEach { compute(instant, it) }
    }

    fun computeSingle(instant: Instant, ecliptic: Ecliptic = Ecliptic(instant), body: Body) {
        computeEarth(instant, ecliptic)
        compute(instant, body)
    }

    private fun computeEarth(instant: Instant, ecliptic: Ecliptic) {
        this.ecliptic = ecliptic
        eclipticalHeliocentric[Body.EARTH] = models.getValue(Body.EARTH).compute(instant).cartesian * toMetersEarth
        eclipticalGeocentric[Body.SUN] = -correctAberration(eclipticalHeliocentric.getValue(Body.EARTH))
        geocentricDistances[Body.SUN] = eclipticalGeocentric.getValue(Body.SUN).position.norm
    }

    private fun compute(instant: Instant, body: Body) {
        val model = models.getValue(body)
        val earth = eclipticalHeliocentric.getValue(Body.EARTH)
        val toMeters = models.getValue(body).distanceUnit.toMeters()
        if (models.getValue(body).type == OrbitalModel.Type.GEOCENTRIC) {
            val geometricGeocentric = model.compute(instant).cartesian * toMeters
            eclipticalHeliocentric[body] = geometricGeocentric + earth
            eclipticalGeocentric[body] = correctAberration(geometricGeocentric)
        } else {
            val geometricHeliocentric = model.compute(instant).cartesian * toMeters
            eclipticalHeliocentric[body] = geometricHeliocentric
            eclipticalGeocentric[body] = correctAberration(geometricHeliocentric - earth)
        }
        geocentricDistances[body] = eclipticalGeocentric.getValue(body).position.norm
    }

    private fun correctAberration(uncorrected: PhaseCartesian): PhaseCartesian {
        val timeLightTravel = uncorrected.position.norm / Distance.ls.toMeters()
        return uncorrected.copy(position = uncorrected.position - uncorrected.velocity * timeLightTravel)
    }

    fun getGeocentricDistance(body: Body) = geocentricDistances.getValue(body)

    fun getMeanEclipticalHeliocentric(body: Body) = eclipticalHeliocentric.getValue(body)
    fun getMeanEclipticalGeocentric(body: Body) = eclipticalGeocentric.getValue(body)

    fun getTrueEclipticalHeliocentric(body: Body) = ecliptic.trafoMeanEcl2TrueEcl * eclipticalHeliocentric.getValue(body)
    fun getTrueEclipticalGeocentric(body: Body) = ecliptic.trafoMeanEcl2TrueEcl * eclipticalGeocentric.getValue(body)

    fun getMeanEquatorialHeliocentric(body: Body) = ecliptic.trafoEcl2MeanEqu * eclipticalHeliocentric.getValue(body)
    fun getMeanEquatorialGeocentric(body: Body) = ecliptic.trafoEcl2MeanEqu * eclipticalGeocentric.getValue(body)

    fun getTrueEquatorialHeliocentric(body: Body) = ecliptic.trafoMeanEcl2TrueEqu * eclipticalHeliocentric.getValue(body)
    fun getTrueEquatorialGeocentric(body: Body) = ecliptic.trafoMeanEcl2TrueEqu * eclipticalGeocentric.getValue(body)

    fun getMeanEclipticalTopocentric(body: Body, topos: Topos) =
            eclipticalGeocentric.getValue(body).position - topos.meanEcliptical

    fun getTrueEclipticalTopocentric(body: Body, topos: Topos) =
            ecliptic.trafoMeanEcl2TrueEcl * eclipticalGeocentric.getValue(body).position - topos.trueEcliptical

    fun getMeanEquatorialTopocentric(body: Body, topos: Topos) =
            ecliptic.trafoEcl2MeanEqu * eclipticalGeocentric.getValue(body).position - topos.meanEquatorial

    fun getTrueEquatorialTopocentric(body: Body, topos: Topos) =
            ecliptic.trafoMeanEcl2TrueEqu * eclipticalGeocentric.getValue(body).position - topos.trueEquatorial

    /**
     * Compute the fraction of the disk which is lighted by the sun
     */
    fun getIlluminatedFraction(body: Body): Double {
        val helioCart = eclipticalHeliocentric.getValue(body).position
        val geoCart = eclipticalGeocentric.getValue(body).position
        return (1 + (helioCart * geoCart) / (helioCart.norm * geoCart.norm)) * 0.5
    }

    fun getPhase(body: Body) =
            eclipticalHeliocentric.getValue(body).position angle eclipticalGeocentric.getValue(body).position

    /**
     * Compute angle between the rays originating from the earth (observer),
     * pointing to the sun and body, resp.
     *
     * @return Angle in radians
     */
    fun getElongationRadians(body: Body) =
            eclipticalGeocentric.getValue(Body.SUN).position angle eclipticalGeocentric.getValue(body).position

    fun getElongationRadians(body: Body, topos: Topos) =
            getMeanEclipticalTopocentric(Body.SUN, topos) angle getMeanEclipticalTopocentric(body, topos)

    private fun getElongationSign(body: Body) = Math.signum(getElongationRadians(body).radians)

    private fun getElongationSign(body: Body, topos: Topos) = Math.signum(getElongationRadians(body, topos).radians)

    companion object {
        fun createFromMeeus() = SolarSystem(mapOf(
                Body.SUN to SUN,
                Body.MERCURY to MercuryMeeus(),
                Body.VENUS to VenusMeeus(),
                Body.EARTH to EarthMeeus(),
                Body.MARS to MarsMeeus(),
                Body.JUPITER to JupiterMeeus(),
                Body.SATURN to SaturnMeeus(),
                Body.URANUS to UranusMeeus(),
                Body.NEPTUNE to NeptuneMeeus(),
                Body.MOON to ModelMoon(),
                Body.PLUTO to ModelPluto()
        ))

        private object SUN : OrbitalModel {
            override val type = OrbitalModel.Type.HELIOCENTRIC
            override val distanceUnit = Distance.AU
            override val phase = PhaseCartesian.ZERO
            override fun compute(instant: Instant) = phase
        }
    }
}