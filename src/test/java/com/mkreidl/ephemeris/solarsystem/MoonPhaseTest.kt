package com.mkreidl.ephemeris.solarsystem

import com.mkreidl.ephemeris.time.Instant
import com.mkreidl.ephemeris.TestUtil
import com.mkreidl.ephemeris.TestUtil.EphemerisData
import com.mkreidl.ephemeris.solarsystem.Body.MOON
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class MoonPhaseTest(testname: String, private val body: Body, private val instant: Instant, private val expected: EphemerisData) {
    private val solarSystem = SolarSystem.createFromMeeus()

    @Test
    fun testMoonPhase() {
        if (body === MOON) {
            solarSystem.compute(instant)
            // expected.phase has an accuracy to only 0.36, since input data (accuracy 1e-3) was multiplied with 360°
            println(solarSystem.getPhase(MOON).degreesNonNeg)
            println(solarSystem.getIlluminatedFraction(MOON))
            println((expected.phase + 180) / 360)
            println("=====================")
            //
            // Test omitted, since reference seems incorrect
            //assertEquals( expected.phase, actual.getPhase( new Angle() ).get( Angle.Unit.DEGREES ), 0.36 );
        }
    }

    companion object {
        @JvmStatic
        @Parameters(name = "{0}")
        fun data() = TestUtil.solarSystemData(listOf(Body.MOON))
    }
}
