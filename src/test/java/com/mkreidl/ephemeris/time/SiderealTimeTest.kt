package com.mkreidl.ephemeris.time

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import java.text.SimpleDateFormat
import java.util.*

@RunWith(Parameterized::class)
class SiderealTimeTest(private val dateString: String, siderealTime: String) {

    private val dateParser = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.ENGLISH)

    private val expected = siderealTime
            .split(":")
            .map { it.toDouble() }
            .reduceRight { s: Double, acc: Double -> acc / 60.0 + s }

    @Test
    fun testSiderealTime() {
        dateParser.timeZone = TimeZone.getTimeZone("UTC")
        val actual = SiderealTime.ofEpochMilli(dateParser.parse(dateString).time)
        assertEquals(expected, actual.getMeanSiderealTime().hoursNonNeg, 1.6 / 3_600.0)
    }

    companion object {
        @JvmStatic
        @Parameters
        fun data() = Arrays.asList(
                arrayOf("1990.01.01 12:00:00", "18:43:31"),
                arrayOf("1800.01.01 12:00:00", "18:43:33"),
                arrayOf("2100.01.01 12:00:00", "18:44:55"),
                arrayOf("1990.01.01 18:00:00", "00:44:30"),
                arrayOf("1990.04.19 00:00:00", "13:47:20"),
                arrayOf("2000.01.01 12:00:00", "18:41:49"),
                arrayOf("2006.01.14 16:30:00", "00:05:59"),
                arrayOf("2010.03.25 16:30:00", "04:42:06"),
                arrayOf("2016.01.22 13:03:01", "21:08:19"),
                arrayOf("2016.05.17 08:53:25", "00:35:22")
        )
    }
}
