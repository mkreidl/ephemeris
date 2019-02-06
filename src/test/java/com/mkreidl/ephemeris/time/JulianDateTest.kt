package com.mkreidl.ephemeris.time

import com.mkreidl.ephemeris.solarsystem.Vsop87AbstractTest
import com.mkreidl.ephemeris.geometry.VSOP87File

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class JulianDateTest(planet: VSOP87File.Planet, timeStr: String, dataSet: Vsop87AbstractTest.DataSet) : Vsop87AbstractTest(planet, timeStr, dataSet) {

    @Test
    fun testJulianDate() {
        Assert.assertEquals(julianDate, time.julianDayNumber(), 1e-15)
    }

    companion object {
        @Parameterized.Parameters(name = "{0} -- {1}")
        fun data(): Iterable<Array<Any>> {
            return Vsop87AbstractTest.data(VSOP87File.Version.C)
        }
    }
}
