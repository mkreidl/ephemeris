package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Time
import com.mkreidl.ephemeris.geometry.Matrix3x3
import java.util.*

class FullSolarSystem(private val models: EnumMap<Body, OrbitalModel>) {

    val eclipticalHeliocentric = mutableMapOf(Body.SUN to PhaseCartesian(Cart.ZERO, Cart.ZERO))
    val eclipticalGeocentric = mutableMapOf(Body.EARTH to PhaseCartesian(Cart.ZERO, Cart.ZERO))
    val geocentricDistances = mutableMapOf(Body.EARTH to 0.0)

    private var precession = Matrix3x3()
    private var nutation = Nutation()

    fun compute(time: Time) {
        val earth = models[Body.EARTH]!!.computeCartesian(time)
        eclipticalHeliocentric[Body.EARTH] = earth
        Body.EXTRA_TERRESTRIAL.forEach {compute(it, time)}
        precession = PrecessionMatrix.compute(time, Matrix3x3())
        nutation.compute(time)
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
        geocentricDistances[body] = eclipticalGeocentric[body]!!.position.length
    }


}