package math

import com.mkreidl.math.Stereographic
import com.mkreidl.math.Vector3
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class StereographicProjectionTest(
        private var preimage: Vector3,
        private var expected: Vector3,
        private val projection: Stereographic,
        private var tolerance: Double
) {
    @Test
    fun test() {
        val image = projection.project(preimage)
        assertEquals(expected.x, image.x, tolerance)
        assertEquals(expected.y, image.y, tolerance)
        assertEquals(expected.z, image.z, tolerance)
    }

    companion object {
        @JvmStatic
        @Parameters
        fun data() = listOf(
                arrayOf(Vector3(0.0, 0.0, 0.0), Vector3(0.0, 0.0, 0.0), Stereographic.N, 1.0e-15),
                arrayOf(Vector3(0.0, 0.0, 1.0), Vector3(Double.NaN, Double.NaN, 0.0), Stereographic.N, 1.0e-15),
                arrayOf(Vector3(0.0, 0.0, -1.0), Vector3(0.0, 0.0, 0.0), Stereographic.N, 1.0e-15),
                arrayOf(Vector3(1.0, 0.0, 0.0), Vector3(1.0, 0.0, 0.0), Stereographic.N, 1.0e-15),
                arrayOf(Vector3(-1.0, 0.0, 0.0), Vector3(-1.0, 0.0, 0.0), Stereographic.N, 1.0e-15),
                arrayOf(Vector3(0.0, 1.0, 0.0), Vector3(0.0, 1.0, 0.0), Stereographic.N, 1.0e-15),
                arrayOf(Vector3(0.0, -1.0, 0.0), Vector3(0.0, -1.0, 0.0), Stereographic.N, 1.0e-15),
                arrayOf(Vector3(Math.sqrt(3.0) / 2.0, 0.0, -0.5), Vector3(Math.sqrt(1.0 / 3.0), 0.0, 0.0), Stereographic.N, 1.0e-15),
                arrayOf(Vector3(0.0, Math.sqrt(3.0) / 2.0, 0.5), Vector3(0.0, Math.sqrt(3.0), 0.0), Stereographic.N, 1.0e-15),
                arrayOf(Vector3(0.0, 1.0, 1.0), Vector3(Double.NaN, Double.POSITIVE_INFINITY, 0.0), Stereographic.N, 1.0e-15),
                arrayOf(Vector3(0.0, 0.0, 0.0), Vector3(0.0, 0.0, 0.0), Stereographic.S, 1.0e-15),
                arrayOf(Vector3(0.0, 0.0, 1.0), Vector3(0.0, 0.0, 0.0), Stereographic.S, 1.0e-15),
                arrayOf(Vector3(0.0, 0.0, -1.0), Vector3(Double.NaN, Double.NaN, 0.0), Stereographic.S, 1.0e-15),
                arrayOf(Vector3(1.0, 0.0, 0.0), Vector3(1.0, 0.0, 0.0), Stereographic.S, 1.0e-15),
                arrayOf(Vector3(-1.0, 0.0, 0.0), Vector3(-1.0, 0.0, 0.0), Stereographic.S, 1.0e-15),
                arrayOf(Vector3(0.0, 1.0, 0.0), Vector3(0.0, 1.0, 0.0), Stereographic.S, 1.0e-15),
                arrayOf(Vector3(0.0, -1.0, 0.0), Vector3(0.0, -1.0, 0.0), Stereographic.S, 1.0e-15),
                arrayOf(Vector3(Math.sqrt(3.0) / 2.0, 0.0, -0.5), Vector3(Math.sqrt(3.0), 0.0, 0.0), Stereographic.S, 1.0e-15),
                arrayOf(Vector3(0.0, Math.sqrt(3.0) / 2.0, 0.5), Vector3(0.0, Math.sqrt(1.0 / 3.0), 0.0), Stereographic.S, 1.0e-15),
                arrayOf(Vector3(0.0, 1.0, 1.0), Vector3(0.0, 0.5, 0.0), Stereographic.S, 1.0e-15)
        )
    }
}
