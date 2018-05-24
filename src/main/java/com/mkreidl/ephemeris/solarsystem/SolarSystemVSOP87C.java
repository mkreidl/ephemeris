package com.mkreidl.ephemeris.solarsystem;

public class SolarSystemVSOP87C extends SolarSystem
{
    public SolarSystemVSOP87C()
    {
        models.put( Body.SUN, new ModelSun() );
        models.put( Body.MERCURY, new com.mkreidl.ephemeris.solarsystem.vsop87c.Mercury() );
        models.put( Body.VENUS, new com.mkreidl.ephemeris.solarsystem.vsop87c.Venus() );
        models.put( Body.EARTH, new com.mkreidl.ephemeris.solarsystem.vsop87c.Earth() );
        models.put( Body.MARS, new com.mkreidl.ephemeris.solarsystem.vsop87c.Mars() );
        models.put( Body.JUPITER, new com.mkreidl.ephemeris.solarsystem.vsop87c.Jupiter() );
        models.put( Body.SATURN, new com.mkreidl.ephemeris.solarsystem.vsop87c.Saturn() );
        models.put( Body.URANUS, new com.mkreidl.ephemeris.solarsystem.vsop87c.Uranus() );
        models.put( Body.NEPTUNE, new com.mkreidl.ephemeris.solarsystem.vsop87c.Neptune() );
        models.put( Body.MOON, new ModelMoon() );
        models.put( Body.PLUTO, new ModelPluto() );
    }
}
