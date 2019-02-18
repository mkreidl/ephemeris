package com.mkreidl.ephemeris.time

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import java.text.SimpleDateFormat
import java.util.*

@RunWith(Parameterized::class)
class InstantTest(private val dateString: String, private val julianDayFraction: Double) {

    private val dateParser = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.ENGLISH)

    @Test
    fun testJulianDayNumber() {
        dateParser.timeZone = TimeZone.getTimeZone("UTC")
        val actual = Instant.ofEpochMilli(dateParser.parse(dateString).time)
        assertEquals(julianDayFraction, actual.julianDayFraction, 1e-7)
    }

    companion object {
        @JvmStatic
        @Parameters
        fun data() = listOf(
                arrayOf("1990.01.01 12:00:00", 2447893.0000000),
                arrayOf("1800.01.01 12:00:00", 2378497.000000),
                arrayOf("2100.01.01 12:00:00", 2488070.0000000),
                arrayOf("1990.01.01 18:00:00", 2447893.2500000),
                arrayOf("1990.04.19 00:00:00", 2448000.5000000),
                arrayOf("2000.01.01 12:00:00", Instant.J2000.julianDayFraction),
                arrayOf("2006.01.14 16:30:00", 2453750.1875000),
                arrayOf("2010.03.25 16:30:00", 2455281.1875000),
                arrayOf("2016.01.22 13:03:01", 2457410.0437616),
                arrayOf("2016.05.17 08:53:25", 2457525.8704282)
        )
    }
}
