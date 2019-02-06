package com.mkreidl.ephemeris.time

import com.mkreidl.ephemeris.TestUtil
import com.mkreidl.ephemeris.Time
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

import java.security.InvalidParameterException
import java.util.Arrays

import org.junit.Assert.assertEquals

@RunWith(Parameterized::class)
class InstantTest(dateString: String, private val julianDayNumber: Double, siderealTime: String) {
    private val siderealTime: Double

    init {
        julianDate = TestUtil.getAstronomicalTime(dateString)
        if (julianDate == null)
            throw InvalidParameterException("String 'dateString' does not represent a valid date.")
        val siderealHHmmss = siderealTime.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val hours = java.lang.Long.parseLong(siderealHHmmss[0])
        val mins = java.lang.Long.parseLong(siderealHHmmss[1])
        val secs = java.lang.Long.parseLong(siderealHHmmss[2])
        this.siderealTime = hours.toDouble() + mins / 60.0 + secs / 3600.0
    }

    @Test
    fun testJulianDayNumber() {
        assertEquals(Time.STD_EPOCH_DAY_NUMBER, Time.J2000.julianDayNumber(), toleranceJulianDayNumber)
        assertEquals(julianDayNumber, julianDate.julianDayNumber(), toleranceJulianDayNumber)
        assertEquals(siderealTime, julianDate.meanSiderealTime, toleranceSiderealTime)
    }

    @Test
    fun testSiderealTime() {
        assertEquals(siderealTime, julianDate.meanSiderealTime, toleranceSiderealTime)
    }

    companion object {
        private val toleranceJulianDayNumber = 1e-7
        private val toleranceSiderealTime = 2.0 / 3600.0
        private var julianDate = Time()

        @Parameters
        fun data(): Iterable<Array<Any>> {
            return Arrays.asList(*arrayOf(arrayOf("1990.01.01 12:00:00", 2447893.0000000, "18:43:31"), arrayOf("1800.01.01 12:00:00", 2378497.000000, "18:43:33"), arrayOf("2100.01.01 12:00:00", 2488070.0000000, "18:44:55"), arrayOf("1990.01.01 18:00:00", 2447893.2500000, "00:44:30"), arrayOf("1990.04.19 00:00:00", 2448000.5000000, "13:47:20"), arrayOf("2000.01.01 12:00:00", Time.STD_EPOCH_DAY_NUMBER, "18:41:49"), arrayOf("2006.01.14 16:30:00", 2453750.1875000, "00:05:59"), arrayOf("2010.03.25 16:30:00", 2455281.1875000, "04:42:06"), arrayOf("2016.01.22 13:03:01", 2457410.0437616, "21:08:19"), arrayOf("2016.05.17 08:53:25", 2457525.8704282, "00:35:22")))
        }
    }
}
