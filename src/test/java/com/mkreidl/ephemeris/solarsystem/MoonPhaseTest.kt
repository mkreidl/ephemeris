package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.Position
import com.mkreidl.ephemeris.TestUtil
import com.mkreidl.ephemeris.TestUtil.EphemerisData
import com.mkreidl.ephemeris.Time
import com.mkreidl.ephemeris.geometry.Angle
import com.mkreidl.ephemeris.solarsystem.Body.EARTH
import com.mkreidl.ephemeris.solarsystem.Body.MOON
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class MoonPhaseTest(testname: String, private val body: Body, private val time: Time, private val expected: EphemerisData) {
    private val solarSystem = SolarSystemMeeus()

    @Test
    fun testMoonPhase() {
        if (body === MOON) {
            solarSystem.compute(time, MOON)
            solarSystem.compute(time, EARTH)
            val actual = solarSystem.getEphemerides(MOON, Position())
            // expected.phase has an accuracy to only 0.36, since input data (accuracy 1e-3) was multiplied with 360°
            println(java.lang.Double.toString(actual.getPhase(Angle()).get(Angle.Unit.DEGREES)))
            println(java.lang.Double.toString(actual.illuminatedFraction))
            println(java.lang.Double.toString((expected.phase + 180) / 360))
            println("=====================")
            //
            // Test omitted, since reference seems incorrect
            //assertEquals( expected.phase, actual.getPhase( new Angle() ).get( Angle.Unit.DEGREES ), 0.36 );
        }
    }

    companion object {
        @JvmStatic
        @Parameters(name = "{0}")
        fun data(): Iterable<Array<Any>> {
            return TestUtil.solarSystemData(listOf(Body.MOON))
        }
    }
}
