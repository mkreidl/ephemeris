package com.mkreidl.ephemeris.solarsystem;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Angle;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Spherical;
import com.mkreidl.ephemeris.sky.coordinates.Ecliptical;

public class Zodiac {
    public double obliquity;
    public final Spherical pole = new Spherical(
            1.0, -Math.PI / 2, Math.PI / 2 - new Ecliptic(new Time()).getMeanObliquity());

    public static Sign getSign(Angle lon) {
        return Sign.values()[(int) lon.get(Angle.Unit.DEGREES) / 30];
    }

    public static Sign getSignFromRad(double lon) {
        int signIndex = (int) (lon / Angle.DEG) / 30;
        while (signIndex < 0)
            signIndex += 12;
        return Sign.values()[signIndex];
    }

    public static double getLongitude(Sign sign, Angle.Unit unit) {
        return sign.enterAngle.get(unit);
    }

    public static Angle convertToRelativeLongitude(Sign sign, Angle angle) {
        return sign.convertToRelativeLongitude(angle);
    }

    public double compute(Time time) {
        obliquity = new Ecliptic(time).getMeanObliquity();
        pole.lat = Math.PI / 2 - obliquity;
        return obliquity;
    }

    public Cartesian getEquatorialDirection(Sign sign, Cartesian direction) {
        return sign.posCart.toEquatorial(obliquity, direction);
    }

    public Cartesian getEquatorialDirectionMiddle(Sign sign, Cartesian direction) {
        return sign.posCartMiddle.toEquatorial(obliquity, direction);
    }

    public enum Sign {
        ARI(1, "Aries", "Ari"),
        TAU(2, "Taurus", "Tau"),
        GEM(3, "Gemini", "Gem"),
        CNC(4, "Cancer", "Cnc"),
        LEO(5, "Leo", "Leo"),
        VIR(6, "Virgo", "Vir"),
        LIB(7, "Libra", "Lib"),
        SCO(8, "Scorpio", "Sco"),
        SGR(9, "Sagittarius", "Sgr"),
        CAP(10, "Capricornus", "Cap"),
        AQR(11, "Aquarius", "Aqr"),
        PSC(12, "Pisces", "Psc");

        Sign(int ordinal, String name, String shortName) {
            this.name = name;
            this.shortName = shortName;
            enterAngle.set((ordinal - 1) * 30, Angle.Unit.DEGREES);
            middleAngle.set((ordinal - 0.5) * 30, Angle.Unit.DEGREES);
            exitAngle.set((ordinal) * 30, Angle.Unit.DEGREES);

            position.set(1.0, (ordinal - 1) * Math.PI / 6, 0.0);
            position.transform(posCart);
            positionMiddle.set(1.0, (ordinal - 0.5) * Math.PI / 6, 0.0);
            positionMiddle.transform(posCartMiddle);
            positionExit.set(1.0, (ordinal) * Math.PI / 6, 0.0);
            positionExit.transform(posCartExit);
        }

        public String getName() {
            return name;
        }

        public String getShortName() {
            return shortName;
        }

        public float getAngle(Angle.Unit unit) {
            return (float) enterAngle.get(unit);
        }

        public Angle convertToRelativeLongitude(Angle angle) {
            final double lonSign = getAngle(Angle.Unit.RADIANS);
            final double lonAbs = angle.get(Angle.Unit.RADIANS);
            final double lonRel = lonAbs - lonSign;
            return angle.set(lonRel, Angle.Unit.RADIANS);
        }

        private final String name;
        private final String shortName;
        private final Angle enterAngle = new Angle();
        private final Angle middleAngle = new Angle();
        private final Angle exitAngle = new Angle();

        private final Ecliptical.Sphe position = new Ecliptical.Sphe();
        private final Ecliptical.Cart posCart = new Ecliptical.Cart();
        private final Ecliptical.Sphe positionMiddle = new Ecliptical.Sphe();
        private final Ecliptical.Cart posCartMiddle = new Ecliptical.Cart();
        private final Ecliptical.Sphe positionExit = new Ecliptical.Sphe();
        private final Ecliptical.Cart posCartExit = new Ecliptical.Cart();
    }
}