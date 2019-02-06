package com.mkreidl.ephemeris.time

import com.mkreidl.ephemeris.Instant
import com.mkreidl.ephemeris.TestUtil
import com.mkreidl.ephemeris.getMeanSiderealTime
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import java.util.*

@RunWith(Parameterized::class)
class InstantTest(dateString: String, private val julianDayFraction: Double, siderealTime: String) {

    private val instant = Instant.ofEpochMilli(TestUtil.getAstronomicalTime(dateString).time)
    private val siderealTime = siderealTime
            .split(":")
            .map { it.toDouble() }
            .reduceRight { s: Double, acc: Double -> acc / 60.0 + s }

    @Test
    fun testJulianDayNumber() {
        assertEquals(julianDayFraction, instant.julianDayFraction, toleranceJulianDayNumber)
    }

    @Test
    fun testSiderealTime() {
        assertEquals(siderealTime, instant.getMeanSiderealTime(), toleranceSiderealTime)
    }

    companion object {
        private const val toleranceJulianDayNumber = 1e-7
        private const val toleranceSiderealTime = 2.0 / 3600.0

        @JvmStatic
        @Parameters
        fun data() = Arrays.asList(
                arrayOf("1990.01.01 12:00:00", 2447893.0000000, "18:43:31"),
                arrayOf("1800.01.01 12:00:00", 2378497.000000, "18:43:33"),
                arrayOf("2100.01.01 12:00:00", 2488070.0000000, "18:44:55"),
                arrayOf("1990.01.01 18:00:00", 2447893.2500000, "00:44:30"),
                arrayOf("1990.04.19 00:00:00", 2448000.5000000, "13:47:20"),
                arrayOf("2000.01.01 12:00:00", Instant.J2000.julianDayFraction, "18:41:49"),
                arrayOf("2006.01.14 16:30:00", 2453750.1875000, "00:05:59"),
                arrayOf("2010.03.25 16:30:00", 2455281.1875000, "04:42:06"),
                arrayOf("2016.01.22 13:03:01", 2457410.0437616, "21:08:19"),
                arrayOf("2016.05.17 08:53:25", 2457525.8704282, "00:35:22")
        )
    }
}
