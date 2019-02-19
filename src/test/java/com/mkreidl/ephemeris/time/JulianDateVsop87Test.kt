package com.mkreidl.ephemeris.time

import com.mkreidl.ephemeris.solarsystem.AbstractVsop87Test
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class JulianDateVsop87Test(planet: Planet, dataSet: DataSet) : AbstractVsop87Test(planet, dataSet) {

    @Test
    fun testJulianDate() {
        Assert.assertEquals(julianDate, instant.julianDayFraction, 1e-15)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0} -- {1}")
        fun data() = AbstractVsop87Test.data(Version.C)
    }
}
