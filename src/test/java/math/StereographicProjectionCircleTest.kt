package math

import com.mkreidl.math.Circle
import com.mkreidl.math.Spherical3
import com.mkreidl.math.Stereographic
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import java.lang.Math.*

@RunWith(Parameterized::class)
class StereographicProjectionCircleTest(
        private val testname: String,
        private val point: Spherical3,
        private val expected: Circle,
        private val tolerance: Double
) {
    @Test
    fun testCircle() {
        var actual: Circle

        actual = Stereographic.N.project(point, PI / 2)
        assertEquals(expected.x, actual.x, tolerance)
        assertEquals(expected.y, actual.y, tolerance)
        assertEquals(expected.r, actual.r, tolerance)
        actual = Stereographic.S.project(point, PI / 2)
        assertEquals(-expected.x, actual.x, tolerance)
        assertEquals(-expected.y, actual.y, tolerance)
        assertEquals(expected.r, actual.r, tolerance)

        if (testname == "Equator") {
            val eclipticPole = Spherical3(1.0, 0.0, 77.0 / 180 * PI)
            val alphas = doubleArrayOf(PI / 6, PI / 4, PI / 3, PI / 2, 2 * PI / 3, 3 * PI / 4)
            for (alpha in alphas) {
                actual = Stereographic.N.project(point, alpha)
                assertEquals(0.0, actual.x, tolerance)
                assertEquals(0.0, actual.y, tolerance)
                assertEquals(Stereographic.N.project1D(PI / 2 - alpha), actual.r, tolerance)
                actual = Stereographic.S.project(point, alpha)
                assertEquals(0.0, actual.x, tolerance)
                assertEquals(0.0, actual.y, tolerance)
                assertEquals(Stereographic.S.project1D(PI / 2 - alpha), actual.r, tolerance)
                println((alpha * 180 / PI).toFloat().toString() + ": " + Stereographic.N.project(eclipticPole, alpha))
            }
        }
    }

    companion object {
        @JvmStatic
        @Parameters(name = "{0}")
        fun data() = listOf(
                arrayOf(
                        "Equator",
                        Spherical3(1.0, 0.0, PI / 2), // = north centerZ => we are projecting the ...
                        Circle(0.0, 0.0, 1.0), // ... equator
                        1.0e-15),
                arrayOf(
                        "Great circle tilted by -pi/3 around y-axis",
                        Spherical3(1.0, 0.0, 2 * PI / 3),
                        Circle(
                                0.5 * (sqrt(3.0) - 1.0 / sqrt(3.0)),
                                0.0,
                                0.5 * (sqrt(3.0) + 1.0 / sqrt(3.0))
                        ), 1.0e-15),
                arrayOf(
                        "Great circle tilted by -pi/4 around y-axis",
                        Spherical3(1.0, 0.0, 3 * PI / 4),
                        Circle(
                                0.5 * (1.0 / tan(PI / 8) - tan(PI / 8)),
                                0.0,
                                0.5 * (tan(PI / 8) + 1 / tan(PI / 8))
                        ), 1.0e-15)
        )
    }
}
