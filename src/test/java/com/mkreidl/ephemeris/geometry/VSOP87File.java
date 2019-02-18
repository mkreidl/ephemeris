package com.mkreidl.ephemeris.geometry;

import com.mkreidl.ephemeris.Distance;
import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.solarsystem.VSOP87OrbitalElements;
import com.mkreidl.math.Spherical3;
import com.mkreidl.math.Vector3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import static com.mkreidl.ephemeris.Time.DAYS_PER_MILLENNIUM;
import static com.mkreidl.ephemeris.Time.J2000;
import static com.mkreidl.ephemeris.geometry.Angle.standardize;

/**
 * Calculator for the VSOP87 theory.
 * <p>
 * Variant of VSOP87-theory:
 * "": Orbital elements ALKHQP as of J2000.0
 * "A": Heliocentric ecliptical Vector3 coordinates XYZ (ecliptic of J2000.0)
 * "B": Heliocentric ecliptical Spherical3 coordinates LBR (ecliptic of J2000.0)
 * "C": Heliocentric ecliptical Vector3 coordinates XYZ (ecliptic of date)
 * "D": Heliocentric ecliptical Vector3 coordinates LBR (ecliptic of date)
 * "E": Barycentric ecliptical Vector3 coordinates XYZ
 */
public class VSOP87File {
    public static VSOP87File getModel(URL url) {
        VSOP87File newModel = new VSOP87File();
        newModel.initialize(url);
        return newModel;
    }

    public static final int DIMENSION = 6;
    public static final Distance DIST_UNIT = Distance.AU;

    public Version version;
    public Planet planet;

    public VSOP87OrbitalElements getOrbitalElements() {
        if (version != Version.O)
            throw new IllegalArgumentException(
                    "No orbital elements computed with VSOP87-version " + version.toString()
            );
        return new VSOP87OrbitalElements(values[0], values[1], values[2], values[3], values[4], values[5]);
    }

    public Spherical3 getPositionSpherical() {
        if (version == Version.B || version == Version.D) {    // LBR coordinates
            return new Spherical3(values[2], values[0], values[1]);
        } else
            throw new IllegalArgumentException("No Spherical3 coordinates computed with VSOP87-version " + version);
    }

    public Vector3 getPositionCartesian() {
        if (version == Version.A || version == Version.C || version == Version.E) {    // XYZ coordinates
            return new Vector3(values[0], values[1], values[2]);
        } else
            throw new IllegalArgumentException("No Vector3 coordinates computed with VSOP87-version " + version);
    }

    public void get(double[] output) {
        output[0] = values[0];
        output[1] = values[1];
        output[2] = values[2];
        output[3] = values[3];
        output[4] = values[4];
        output[5] = values[5];
    }

    public void initialize(URL url) {
        if (initialized)
            return;
        final String fileName = new File(url.getPath()).getName();
        this.planet = Planet.valueOf(fileName.split("\\.")[1].toUpperCase());
        try {
            this.version = Version.valueOf(fileName.substring(6, 7));
        } catch (IllegalArgumentException e) {
            this.version = Version.O;
        }
        try {
            loadCoefficients(url);
        } catch (IOException e) {
            return;
        }
        initialized = true;
    }

    /**
     * Calculate orbital elements resp. coordinates of body at a given time.
     *
     * @param time
     * @return coordinate(s)
     */
    public void compute(Time time) {
        if (!initialized)
            throw new IllegalArgumentException("Not initialized.");

        double t = time.julianDayNumberSince(J2000) / DAYS_PER_MILLENNIUM;
        for (int i = 0; i < DIMENSION; i++)
            values[i] = series[i].evaluate(t);

        if (version == Version.B || version == Version.D || version == Version.O) {
            values[1] = standardize(values[1]);
            if (version != Version.O)
                values[0] = standardize(values[0]);
        }
    }

    ///////////////////////////////////////////////////////////////

    private boolean initialized = false;
    private final double[] values = new double[DIMENSION];
    private final Series[] series = new Series[DIMENSION];

    private static class Triple {
        public double A;
        public double B;
        public double C;

        public Triple(double A, double B, double C) {
            this.A = A;
            this.B = B;
            this.C = C;
        }
    }

    private static class Coefficient extends Vector<Triple> {
        private static final long serialVersionUID = 1L;

        public double evaluate(double time) {
            double value = 0.0;
            for (Triple triple : this)
                value += triple.A * Math.cos(triple.B + triple.C * time);
            return value;
        }
    }

    private static class Series {
        private Coefficient[] coeffExpr;
        private double[] coefficients;

        public Series(int degree) {
            coefficients = new double[degree + 1];
            coeffExpr = new Coefficient[degree + 1];
            for (int i = 0; i < coeffExpr.length; i++)
                coeffExpr[i] = new Coefficient();
        }

        public void insertTriple(int degree, double A, double B, double C) {
            coeffExpr[degree].add(new Triple(A, B, C));
        }

        public double evaluate(double time) {
            for (int i = coeffExpr.length - 1; i >= 0; --i)
                coefficients[i] = coeffExpr[i].evaluate(time);
            return horner(time, coefficients);
        }

        private static double horner(double x, double[] a) {
            double result = 0.0;
            for (int i = a.length - 1; i >= 0; --i)
                result = result * x + a[i];
            return result;
        }
    }

    /**
     * Parse the VSOP87-coefficients from file
     *
     * @throws IOException
     */
    private void loadCoefficients(URL url) throws IOException {
        BufferedReader lineReader = new BufferedReader(new FileReader(url.getPath()));
        String line;
        String[] parts;

        int numCoord;
        int numCoeff;
        double A, B, C;

        for (int i = 0; i < DIMENSION; i++)
            series[i] = new Series(5);

        while ((line = lineReader.readLine()) != null) {
            if (line.startsWith(" VSOP"))
                continue;
            line = line.replaceAll("-", " -");
            line = line.replaceAll("\\s+", " ");
            parts = line.trim().split(" ");

            // Determine variable, e.g. X=1, Y=2, Z=3 for Vector3 coords
            numCoord = Integer.parseInt(parts[0].substring(2, 3)) - 1;
            // Determine the coefficient of the Taylor polynomial
            numCoeff = Integer.parseInt(parts[0].substring(3, 4));

            A = Double.parseDouble(parts[parts.length - 3]);
            B = Double.parseDouble(parts[parts.length - 2]);
            C = Double.parseDouble(parts[parts.length - 1]);

            series[numCoord].insertTriple(numCoeff, A, B, C);
        }
        lineReader.close();
    }

    public enum Version {
        O, A, B, C, D, E
    }

    public enum Planet {
        SUN, MER, VEN, EAR, EMB, MAR, JUP, SAT, URA, NEP
    }
}
