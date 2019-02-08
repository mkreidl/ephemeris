package com.mkreidl.ephemeris.solarsystem;

import com.mkreidl.ephemeris.solarsystem.meeus.*;
import com.mkreidl.ephemeris.solarsystem.vsop87c.EarthVsop87C;

public class SolarSystemMeeus extends SolarSystem {
    public SolarSystemMeeus() {
        models.put(Body.EARTH, new EarthVsop87C());
        models.put(Body.MERCURY, new MercuryMeeus());
        models.put(Body.VENUS, new VenusMeeus());
        models.put(Body.MARS, new MarsMeeus());
        models.put(Body.JUPITER, new JupiterMeeus());
        models.put(Body.SATURN, new SaturnMeeus());
        models.put(Body.URANUS, new UranusMeeus());
        models.put(Body.NEPTUNE, new NeptuneMeeus());
        models.put(Body.MOON, new ModelMoon());
        models.put(Body.PLUTO, new ModelPluto());
    }
}
