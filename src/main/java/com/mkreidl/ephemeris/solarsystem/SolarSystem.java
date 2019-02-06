package com.mkreidl.ephemeris.solarsystem;

import com.mkreidl.ephemeris.Position;
import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.sky.coordinates.Ecliptical;
import com.mkreidl.ephemeris.sky.coordinates.Equatorial;
import com.mkreidl.ephemeris.sky.coordinates.Horizontal;
import com.mkreidl.math.Vector3;

import java.util.EnumMap;
import java.util.Map;

public abstract class SolarSystem {
    
    protected final Map<Body, Ecliptical.Cart> positions = new EnumMap<>(Body.class);
    private final Map<Body, Ecliptical.Cart> velocities = new EnumMap<>(Body.class);
    private final Map<Body, Position> planetsEphemerides = new EnumMap<>(Body.class);
    private final EnumMap<Body, Double> geocentricDistances = new EnumMap<>(Body.class);

    final Map<Body, OrbitalModel> models = new EnumMap<>(Body.class);

    SolarSystem() {
        for (Body body : Body.values()) {
            planetsEphemerides.put(body, new Position());
            positions.put(body, new Ecliptical.Cart());
            velocities.put(body, new Ecliptical.Cart());
        }
    }

    public Cartesian getHeliocentric(final Body body, final Cartesian output) {
        output.set(positions.get(body));
        if (models.get(body).getType() == OrbitalModel.Type.GEOCENTRIC)
            output.add(positions.get(Body.EARTH));
        return output;
    }

    public Position getEphemerides(final Body body, final Position position) {
        switch (models.get(body).getType()) {
            case HELIOCENTRIC:
                position.setHeliocentricPosition(positions.get(body), positions.get(Body.EARTH));
                position.setHeliocentricVelocity(velocities.get(body), velocities.get(Body.EARTH));
                break;
            case GEOCENTRIC:
                position.setGeocentricPosition(positions.get(body), positions.get(Body.EARTH));
                position.setGeocentricVelocity(velocities.get(body), velocities.get(Body.EARTH));
                break;
        }
        position.correctAberration();
        return position;
    }

    public double getGeocentricDistance(Body body) {
        return geocentricDistances.get(body);
    }

    public void compute(final Time time, final Body body) {
        final OrbitalModel model = models.get(body);
        final PhaseCartesian phase = model.computeCartesian(time);
        final Vector3 pos = phase.getPosition().times(model.getDistanceUnit().toMeters());
        final Vector3 vel = phase.getVelocity().times(model.getDistanceUnit().toMeters());
        positions.get(body).set(pos.getX(), pos.getY(), pos.getZ());
        velocities.get(body).set(vel.getX(), vel.getY(), vel.getZ());
    }

    public void compute(final Time time) {
        for (final Body body : Body.values()) {
            compute(time, body);
        }
        final Equatorial.Cart geocentric = new Equatorial.Cart();
        for (final Body body : Body.values()) {
            getEphemerides(body, planetsEphemerides.get(body));
            planetsEphemerides.get(body).get(geocentric, Position.CoordinatesCenter.GEOCENTRIC);
            geocentricDistances.put(body, geocentric.length());
        }
    }

    public void setTimeLocation(final Time time, double longitudeRad, double latitudeRad) {
        final double localSiderealTimeRad = time.getMeanSiderealTimeRadians() + longitudeRad;
        final double currentEclipticRad = new Ecliptic(time.getTime()).getMeanObliquity();
        setTimeLocation(currentEclipticRad, localSiderealTimeRad, latitudeRad);
    }

    public void setTimeLocation(double currentEclipticRadians, double localSiderealTimeRadians, double latitudeRadians) {
        for (final Body body : Body.values())
            planetsEphemerides.get(body).setTimeLocation(currentEclipticRadians, localSiderealTimeRadians, latitudeRadians);
    }

    public void getPosition(Body body, Position.CoordinatesCenter center, Ecliptical.Cart out) {
        planetsEphemerides.get(body).get(out, center);
    }

    public void getPosition(Body body, Position.CoordinatesCenter center, Equatorial.Cart out) {
        planetsEphemerides.get(body).get(out, center);
    }

    public void getPosition(Body body, Position.CoordinatesCenter center, Horizontal.Cart out) {
        planetsEphemerides.get(body).get(out, center);
    }
}
