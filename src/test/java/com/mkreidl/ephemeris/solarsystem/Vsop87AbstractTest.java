package com.mkreidl.ephemeris.solarsystem;

import com.mkreidl.ephemeris.TestUtil;
import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.VSOP87File;
import com.mkreidl.ephemeris.geometry.VSOP87File.Planet;
import org.junit.runners.Parameterized.Parameters;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import static com.mkreidl.ephemeris.geometry.Angle.standardize;

public abstract class Vsop87AbstractTest {
    protected final VSOP87File.Planet planet;
    protected final Time time;
    protected final double julianDate;

    public Vsop87AbstractTest(Planet planet, String timeStr, DataSet dataSet) {
        this.planet = planet;
        this.time = dataSet.time;
        this.julianDate = dataSet.julianDate;
    }

    public static class DataSet {
        public VSOP87File.Planet planet;
        public Time time;
        public VSOP87File model;
        String VSOP87filename;
        String dateString;
        double julianDate;
        double[] coordinates = new double[6];
    }

    private static final String DATASETS = "/VSOP87/vsop87.chk";
    public static final Map<VSOP87File.Planet, Map<VSOP87File.Version, Map<String, DataSet>>> fullData = new LinkedHashMap<>();

    static {
        for (VSOP87File.Planet p : VSOP87File.Planet.values()) {
            final Map<VSOP87File.Version, Map<String, DataSet>> planetMap = new LinkedHashMap<>();
            for (VSOP87File.Version v : VSOP87File.Version.values())
                planetMap.put(v, new LinkedHashMap<>());
            fullData.put(p, planetMap);
        }
        readDataFromFile();
    }

    @Parameters(name = "{0} -- {1}")
    public static Iterable<Object[]> data(VSOP87File.Version version) {
        final LinkedList<Object[]> parameters = new LinkedList<>();
        for (VSOP87File.Planet planet : fullData.keySet())
            for (String timeStr : fullData.get(planet).get(version).keySet())
                parameters.add(new Object[]{planet, timeStr, fullData.get(planet).get(version).get(timeStr)});
        return parameters;
    }

    private static void readDataFromFile() {
        String VSOP87filename;
        BufferedReader lineReader;
        String line;
        String[] parts;
        DataSet nextRecord = new DataSet();
        VSOP87File model = null;

        try {
            URL url = Vsop87AbstractTest.class.getResource(DATASETS);
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
                if (!VSOP87filename.equals(nextRecord.VSOP87filename))
                    model = VSOP87File.getModel(VSOP87File.class.getResource("/VSOP87/" + VSOP87filename));

                nextRecord = new DataSet();
                nextRecord.VSOP87filename = VSOP87filename;
                nextRecord.model = model;
                nextRecord.dateString = parts[4] + " " + parts[5];
                nextRecord.julianDate = Double.parseDouble(parts[3].substring(2));

                for (int i = 0; i <= 3; i += 3) {
                    parts = lineReader.readLine().split("\\s+");
                    nextRecord.coordinates[i] = Double.parseDouble(parts[2]);
                    nextRecord.coordinates[i + 1] = Double.parseDouble(parts[5]);
                    nextRecord.coordinates[i + 2] = Double.parseDouble(parts[8]);
                }
                standardizeCoordinates(nextRecord.coordinates, model.version);
                nextRecord.time = TestUtil.getAstronomicalTime(nextRecord.dateString);
                if (nextRecord.time == null) {
                    System.err.println("String '" + nextRecord.dateString + "' does not represent a valid date.");
                    continue;
                }
                fullData.get(model.planet).get(model.version).put(nextRecord.dateString, nextRecord);
            } catch (IOException e) {
            }
        } while (true);
    }

    private static void standardizeCoordinates(double[] coordinates, VSOP87File.Version version) {
        switch (version) {
            case B:
            case D:
                coordinates[0] = standardize(coordinates[0]);
                coordinates[1] = standardize(coordinates[1]);
                break;
            case O:
                double[] original = coordinates.clone();
                for (int i = 0; i < 6; i++)
                    coordinates[i] = original[i / 2 + 3 * (i % 2)];
                coordinates[1] = standardize(coordinates[1]);
                break;
            default:
                break;
        }
    }

}
