package com.mkreidl.ephemeris.util;

import com.mkreidl.ephemeris.time.Instant;
import com.mkreidl.math.Angle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Vsop87TestUtil {

    public static Iterable<Object[]> data(Version version) {
        final LinkedList<Object[]> parameters = new LinkedList<>();
        for (Planet planet : fullData.keySet())
            for (Instant instant : fullData.get(planet).get(version).keySet())
                parameters.add(new Object[]{planet, instant, fullData.get(planet).get(version).get(instant)});
        return parameters;
    }

    public enum Version {
        O, A, B, C, D, E
    }

    public enum Planet {
        SUN, MER, VEN, EAR, EMB, MAR, JUP, SAT, URA, NEP
    }

    private static final String DATASETS = "/VSOP87/vsop87.chk";
    private static final SimpleDateFormat VSOP_DATE_PARSER =
            new SimpleDateFormat("dd/MM/yyyy HH", Locale.ENGLISH);

    private static final Map<Planet, Map<Version, Map<Instant, double[]>>> fullData = new LinkedHashMap<>();

    static {
        VSOP_DATE_PARSER.setTimeZone(TimeZone.getTimeZone("UTC"));
        for (Planet p : Planet.values()) {
            final Map<Version, Map<Instant, double[]>> planetMap = new LinkedHashMap<>();
            for (Version v : Version.values())
                planetMap.put(v, new LinkedHashMap<>());
            fullData.put(p, planetMap);
        }
        readDataFromFile();
    }

    private static void readDataFromFile() {
        String VSOP87filename;
        BufferedReader lineReader;
        String line;
        String[] parts;

        try {
            URL url = Vsop87TestUtil.class.getResource(DATASETS);
            lineReader = new BufferedReader(new FileReader(url.getPath()));
        } catch (IOException e) {
            return;
        }

        do {
            try {
                do
                    line = lineReader.readLine();
                while (line != null && !line.startsWith(" VSOP87"));
                if (line == null) {
                    lineReader.close();
                    return;
                }

                parts = line.split("\\s+");
                if (parts[2].equals("EARTH-MOON"))
                    VSOP87filename = parts[1] + ".emb";
                else
                    VSOP87filename = parts[1] + "." + parts[2].substring(0, 3).toLowerCase();

                // Save time and space by creating only one instance of VSOP87 per ( Planet,
                // Version )-pair
                final Version version = getVersion(VSOP87filename);
                final Planet planet = getPlanet(VSOP87filename);
                final String dateString = parts[4] + " " + parts[5];
                final double[] coordinates = new double[6];

                for (int i = 0; i <= 3; i += 3) {
                    parts = lineReader.readLine().split("\\s+");
                    coordinates[i] = Double.parseDouble(parts[2]);
                    coordinates[i + 1] = Double.parseDouble(parts[5]);
                    coordinates[i + 2] = Double.parseDouble(parts[8]);
                }
                standardizeCoordinates(coordinates, version);
                final Instant instant = getAstronomicalTime(dateString);
                if (instant == null) {
                    System.err.println("String '" + dateString + "' does not represent a valid date.");
                    continue;
                }
                fullData.get(planet).get(version).put(instant, coordinates);
            } catch (IOException e) {
            }
        } while (true);
    }

    private static void standardizeCoordinates(double[] coordinates, Version version) {
        switch (version) {
            case B:
            case D:
                coordinates[0] = Angle.Companion.reduce(coordinates[0]);
                coordinates[1] = Angle.Companion.reduce(coordinates[1]);
                break;
            case O:
                double[] original = coordinates.clone();
                for (int i = 0; i < 6; i++)
                    coordinates[i] = original[i / 2 + 3 * (i % 2)];
                coordinates[1] = Angle.Companion.reduce(coordinates[1]);
                break;
            default:
                break;
        }
    }

    private static Version getVersion(String fileName) {
        try {
            return Version.valueOf(fileName.substring(6, 7));
        } catch (IllegalArgumentException e) {
            return Version.O;
        }
    }

    private static Planet getPlanet(String fileName) {
        return Planet.valueOf(fileName.split("\\.")[1].toUpperCase());
    }

    private static com.mkreidl.ephemeris.time.Instant getAstronomicalTime(String dateString) {
        try {
            return com.mkreidl.ephemeris.time.Instant.ofEpochMilli(VSOP_DATE_PARSER.parse(dateString).getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    private Vsop87TestUtil() {
    }
}
