package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Time
import com.mkreidl.math.Vector3
import java.util.*

class FullSolarSystem(private val models: EnumMap<Body, OrbitalModel>) {

    private val eclipticalHeliocentric = mutableMapOf(Body.SUN to PhaseCartesian(Vector3.ZERO, Vector3.ZERO))
    private val eclipticalGeocentric = mutableMapOf(Body.EARTH to PhaseCartesian(Vector3.ZERO, Vector3.ZERO))
    private val geocentricDistances = mutableMapOf(Body.EARTH to 0.0)

    fun compute(time: Time) {
        val earth = models[Body.EARTH]!!.computeCartesian(time)
        eclipticalHeliocentric[Body.EARTH] = earth
        Body.EXTRA_TERRESTRIAL.forEach { compute(it, time) }
    }

    private fun compute(body: Body, time: Time) {
        val model = models[body]!!
        val earth = eclipticalHeliocentric[Body.EARTH]!!
        if (model.type == OrbitalModel.Type.GEOCENTRIC) {
            eclipticalGeocentric[body] = model.computeCartesian(time)
            eclipticalHeliocentric[body] = eclipticalGeocentric[body]!! + earth
        } else {
            eclipticalHeliocentric[body] = model.computeCartesian(time)
            eclipticalGeocentric[body] = eclipticalHeliocentric[body]!! - earth
        }
        geocentricDistances[body] = eclipticalGeocentric[body]!!.position.norm
    }

    fun getEclipticalHeliocentric(body: Body) = eclipticalHeliocentric[body]

    fun getEclipticalGeocentric(body: Body) = eclipticalGeocentric[body]

    fun getGeocentricDistance(body: Body) = geocentricDistances[body]
}