package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Distance
import com.mkreidl.ephemeris.sky.Topos
import com.mkreidl.ephemeris.solarsystem.meeus.*
import com.mkreidl.ephemeris.solarsystem.vsop87.c.*
import com.mkreidl.ephemeris.time.Instant
import com.mkreidl.math.Angle
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

    fun getPhaseAngle(body: Body): Angle {
        val helioCart = eclipticalHeliocentric.getValue(body).position
        val geoCart = eclipticalGeocentric.getValue(body).position
        val cos = helioCart * geoCart
        val sin = helioCart.x * geoCart.y - helioCart.y * geoCart.x
        return Angle.ofRad(Math.atan2(sin, cos))
    }

    /**
     * Compute angle between the rays originating from the earth (observer),
     * pointing to the sun and body, resp.
     *
     * @return Angle in radians
     */
    fun getElongation(body: Body): Angle {
        val pos = eclipticalGeocentric.getValue(body).position
        val sun = eclipticalGeocentric.getValue(Body.SUN).position
        return Angle.ofRad(Math.atan2(sun.x * pos.y - sun.y * pos.x, pos * sun))
    }

    fun getElongation(body: Body, topos: Topos): Angle {
        val pos = getMeanEclipticalTopocentric(body, topos)
        val sun = getMeanEclipticalTopocentric(Body.SUN, topos)
        return Angle.ofRad(Math.atan2(sun.x * pos.y - sun.y * pos.x, pos * sun))
    }

    private fun getElongationSign(body: Body) = Math.signum(getElongation(body).radians)

    private fun getElongationSign(body: Body, topos: Topos) = Math.signum(getElongation(body, topos).radians)

    companion object {
        fun createFromMeeus() = SolarSystem(mapOf(
                Body.SUN to ModelSun,
                Body.MERCURY to MercuryMeeus.createModel(),
                Body.VENUS to VenusMeeus.createModel(),
                Body.EARTH to EarthMeeus.createModel(),
                Body.MARS to MarsMeeus.createModel(),
                Body.JUPITER to JupiterMeeus.createModel(),
                Body.SATURN to SaturnMeeus.createModel(),
                Body.URANUS to UranusMeeus.createModel(),
                Body.NEPTUNE to NeptuneMeeus.createModel(),
                Body.MOON to ModelMoon(),
                Body.PLUTO to ModelPluto()
        ))

        fun createFromVsop87C() = SolarSystem(mapOf(
                Body.SUN to ModelSun,
                Body.MERCURY to MercuryVsop87C(),
                Body.VENUS to VenusVsop87C(),
                Body.EARTH to EarthVsop87C(),
                Body.MARS to MarsVsop87C(),
                Body.JUPITER to JupiterVsop87C(),
                Body.SATURN to SaturnVsop87C(),
                Body.URANUS to UranusVsop87C(),
                Body.NEPTUNE to NeptuneVsop87C(),
                Body.MOON to ModelMoon(),
                Body.PLUTO to ModelPluto()
        ))
    }
}