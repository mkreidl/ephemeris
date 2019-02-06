package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Distance
import com.mkreidl.ephemeris.Time
import com.mkreidl.math.Vector3

class FullSolarSystem(private val models: Map<Body, OrbitalModel>) {

    private val eclipticalHeliocentric = mutableMapOf(Body.SUN to PhaseCartesian(Vector3.ZERO, Vector3.ZERO))
    private val eclipticalGeocentric = mutableMapOf(Body.EARTH to PhaseCartesian(Vector3.ZERO, Vector3.ZERO))
    private val geocentricDistances = mutableMapOf(Body.EARTH to 0.0)

    private val toMetersEarth = models.getValue(Body.EARTH).distanceUnit.toMeters()

    lateinit var ecliptic: Ecliptic private set

    fun compute(time: Time) {
        ecliptic = Ecliptic(time.time)
        eclipticalHeliocentric[Body.EARTH] = models.getValue(Body.EARTH).computeCartesian(time) * toMetersEarth
        Body.EXTRA_TERRESTRIAL.forEach { compute(it, time) }
    }

    private fun compute(body: Body, time: Time) {
        val model = models.getValue(body)
        val earth = eclipticalHeliocentric.getValue(Body.EARTH)
        val toMeters = models.getValue(body).distanceUnit.toMeters()
        if (models.getValue(body).type == OrbitalModel.Type.GEOCENTRIC) {
            val geometricGeocentric = model.computeCartesian(time) * toMeters
            eclipticalHeliocentric[body] = geometricGeocentric + earth
            eclipticalGeocentric[body] = correctAberration(geometricGeocentric)
        } else {
            val geometricHeliocentric = model.computeCartesian(time) * toMeters
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

    fun isRetrograde(body: Body): Boolean {
        val phase = eclipticalGeocentric.getValue(body)
        return phase.position.x * phase.velocity.y - phase.position.y * phase.velocity.x < 0
    }

    fun getAngularVelocity(body: Body): Double {
        val phase = eclipticalGeocentric.getValue(body)
        return (phase.position.x * phase.velocity.y - phase.position.y * phase.velocity.x) / (phase.position * phase.position)
    }

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

    private fun getElongationSign(body: Body) = Math.signum(getElongationRadians(body))

    private fun getElongationSign(body: Body, topos: Topos) = Math.signum(getElongationRadians(body, topos))
}