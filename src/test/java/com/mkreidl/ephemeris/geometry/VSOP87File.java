package com.mkreidl.ephemeris.geometry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

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

    public Version version;
    public Planet planet;

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

    private boolean initialized = false;
    private final Series[] series = new Series[6];

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
    }

    private static class Series {
        private Coefficient[] coeffExpr;

        public Series(int degree) {
            coeffExpr = new Coefficient[degree + 1];
            for (int i = 0; i < coeffExpr.length; i++)
                coeffExpr[i] = new Coefficient();
        }

        public void insertTriple(int degree, double A, double B, double C) {
            coeffExpr[degree].add(new Triple(A, B, C));
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

        for (int i = 0; i < 6; i++)
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
