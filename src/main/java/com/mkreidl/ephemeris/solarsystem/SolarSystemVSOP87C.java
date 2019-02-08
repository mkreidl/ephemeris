package com.mkreidl.ephemeris.solarsystem;

import com.mkreidl.ephemeris.solarsystem.vsop87c.*;

public class SolarSystemVSOP87C extends SolarSystem {
    public SolarSystemVSOP87C() {
        models.put(Body.MERCURY, new MercuryVsop87C());
        models.put(Body.VENUS, new VenusVsop87C());
        models.put(Body.EARTH, new EarthVsop87C());
        models.put(Body.MARS, new MarsVsop87C());
        models.put(Body.JUPITER, new JupiterVsop87C());
        models.put(Body.SATURN, new SaturnVsop87C());
        models.put(Body.URANUS, new UranusVsop87C());
        models.put(Body.NEPTUNE, new NeptuneVsop87C());
        models.put(Body.MOON, new ModelMoon());
        models.put(Body.PLUTO, new ModelPluto());
    }
}
