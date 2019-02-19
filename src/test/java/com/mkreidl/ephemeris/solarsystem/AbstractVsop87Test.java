package com.mkreidl.ephemeris.solarsystem;

import com.mkreidl.ephemeris.TestUtil;
import com.mkreidl.ephemeris.time.Instant;
import com.mkreidl.math.Angle;
import org.junit.runners.Parameterized.Parameters;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class AbstractVsop87Test {

    public enum Version {
        O, A, B, C, D, E
    }

    public enum Planet {
        SUN, MER, VEN, EAR, EMB, MAR, JUP, SAT, URA, NEP
    }

    protected final double julianDate;
    protected final Instant instant;
    protected final Planet planet;

    public AbstractVsop87Test(Planet planet, DataSet dataSet) {
        this.planet = planet;
        this.julianDate = dataSet.julianDate;
        this.instant = dataSet.instant;
    }

    public static class DataSet {
        public Planet planet;
        public Instant instant;
        String VSOP87filename;
        String dateString;
        double julianDate;
        double[] coordinates = new double[6];
    }

    private static final String DATASETS = "/VSOP87/vsop87.chk";
    private static final Map<Planet, Map<Version, Map<String, DataSet>>> fullData = new LinkedHashMap<>();

    static {
        for (Planet p : Planet.values()) {
            final Map<Version, Map<String, DataSet>> planetMap = new LinkedHashMap<>();
            for (Version v : Version.values())
                planetMap.put(v, new LinkedHashMap<>());
            fullData.put(p, planetMap);
        }
        readDataFromFile();
    }

    @Parameters(name = "{0} -- {1}")
    public static Iterable<Object[]> data(Version version) {
        final LinkedList<Object[]> parameters = new LinkedList<>();
        for (Planet planet : fullData.keySet())
            for (String timeStr : fullData.get(planet).get(version).keySet())
                parameters.add(new Object[]{planet, fullData.get(planet).get(version).get(timeStr)});
        return parameters;
    }

    private static void readDataFromFile() {
        String VSOP87filename;
        BufferedReader lineReader;
        String line;
        String[] parts;

        try {
            URL url = AbstractVsop87Test.class.getResource(DATASETS);
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
                final DataSet nextRecord = new DataSet();
                nextRecord.VSOP87filename = VSOP87filename;
                nextRecord.dateString = parts[4] + " " + parts[5];
                nextRecord.julianDate = Double.parseDouble(parts[3].substring(2));

                for (int i = 0; i <= 3; i += 3) {
                    parts = lineReader.readLine().split("\\s+");
                    nextRecord.coordinates[i] = Double.parseDouble(parts[2]);
                    nextRecord.coordinates[i + 1] = Double.parseDouble(parts[5]);
                    nextRecord.coordinates[i + 2] = Double.parseDouble(parts[8]);
                }
                standardizeCoordinates(nextRecord.coordinates, version);
                nextRecord.instant = TestUtil.getAstronomicalTime(nextRecord.dateString);
                if (nextRecord.instant == null) {
                    System.err.println("String '" + nextRecord.dateString + "' does not represent a valid date.");
                    continue;
                }
                fullData.get(planet).get(version).put(nextRecord.dateString, nextRecord);
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
            return AbstractVsop87Test.Version.valueOf(fileName.substring(6, 7));
        } catch (IllegalArgumentException e) {
            return AbstractVsop87Test.Version.O;
        }
    }

    private static Planet getPlanet(String fileName) {
        return AbstractVsop87Test.Planet.valueOf(fileName.split("\\.")[1].toUpperCase());
    }
}
