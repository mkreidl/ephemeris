package com.mkreidl.ephemeris.util;

import com.mkreidl.ephemeris.solarsystem.Body;
import com.mkreidl.ephemeris.solarsystem.Zodiac;
import com.mkreidl.math.Angle;
import com.mkreidl.math.Sexagesimal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mkreidl.ephemeris.solarsystem.Body.MOON;

public class GeocentricEphemTestUtil {

    private static final URL DIR_NASA = GeocentricEphemTestUtil.class.getResource("/NASA_Ephemeris_Data/");

    public static class EphemerisData {
        public Angle longitude;
        public Angle latitude;
        public Angle declination;
        public Angle rightAscension;
        public boolean retrograde = false;
        public double phase = Double.POSITIVE_INFINITY;  // signifies an invalid value
    }

    private static EphemerisData parseNASAEphemeris(String LonLatRADecl) {
        final EphemerisData position = new EphemerisData();
        final Pattern lonPattern = Pattern.compile("\\s*(\\d{2})\\s(\\w{3})\\s(\\d{2})'(\\d{2})\"(.)");
        final Pattern latPattern = Pattern.compile("\\s*([+-]?)\\s*(\\d{1,2}).*(\\d{2})'(\\d{2})\"");
        final Pattern rasPattern = Pattern.compile("\\s*(\\d\\d):(\\d\\d):(\\d\\d)\\s*");

        Matcher m = lonPattern.matcher(LonLatRADecl.substring(15, 29));
        m.find();
        final double longitude = Sexagesimal.Companion.parse(m.group(1), m.group(3), m.group(4)).toDecimal()
                + Zodiac.Companion.getLongitude(Zodiac.Sign.valueOf(m.group(2).toUpperCase())).getDegrees();
        position.longitude = Angle.Companion.ofDeg(longitude);
        position.retrograde = m.group(5).equals("R");

        m = latPattern.matcher(LonLatRADecl.substring(31, 41));
        m.find();
        final double latitude = Sexagesimal.Companion.parse(m.group(2), m.group(3), m.group(4)).toDecimal();
        position.latitude = Angle.Companion.ofDeg(m.group(1).equals("-") ? -latitude : latitude);

        m = rasPattern.matcher(LonLatRADecl.substring(44, 52));
        m.find();
        final double rightAscension = Sexagesimal.Companion.parse(m.group(1), m.group(2), m.group(3)).toDecimal();
        position.rightAscension = Angle.Companion.ofHrs(rightAscension);

        m = latPattern.matcher(LonLatRADecl.substring(55, 65));
        m.find();
        final double declination = Sexagesimal.Companion.parse(m.group(2), m.group(3), m.group(4)).toDecimal();
        position.declination = Angle.Companion.ofDeg(m.group(1).equals("-") ? -declination : declination);

        return position;
    }

    public static Iterable<Object[]> data(Collection<Body> bodies) {
        final List<Object[]> dataSets = new LinkedList<>();
        final File[] files = new File(DIR_NASA.getFile()).listFiles();

        for (File file : files) {
            final BufferedReader lineReader;
            String line;

            String dateStr;
            boolean geocentric = false;
            double moonPhase = Double.POSITIVE_INFINITY;
            com.mkreidl.ephemeris.time.Instant instant = null;

            try {
                lineReader = new BufferedReader(new FileReader(file.getAbsolutePath()));
                while ((line = lineReader.readLine()) != null) {
                    if (line.startsWith("Date/Time")) {
                        dateStr = line.substring(11, 30);
                        instant = com.mkreidl.ephemeris.time.Instant.ofEpochMilli(NASA_DATE_PARSER.parse(dateStr).getTime());
                    }
                    if (line.equals("Geocentric positions"))
                        geocentric = true;
                    if (line.startsWith("Phase of Moon"))
                        moonPhase = Double.parseDouble(line.substring(15, 20));
                    if (line.startsWith("Planet") && !geocentric)
                        break;
                    try {
                        final EphemerisData ephemeris = parseNASAEphemeris(line);
                        final String objectName = line.substring(0, 15).trim();
                        final Body body = Body.valueOf(objectName.toUpperCase());
                        if (body == MOON)
                            ephemeris.phase = moonPhase * 360;
                        if (bodies.contains(body))
                            dataSets.add(new Object[]{body, instant, ephemeris});
                    } catch (IllegalArgumentException | IllegalStateException | StringIndexOutOfBoundsException e) {
                    }
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
        return dataSets;
    }

    private static final SimpleDateFormat NASA_DATE_PARSER = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.ENGLISH);

    static {
        NASA_DATE_PARSER.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private GeocentricEphemTestUtil() {
    }
}
