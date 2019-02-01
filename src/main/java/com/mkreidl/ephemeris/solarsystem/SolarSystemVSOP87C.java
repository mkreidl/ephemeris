package com.mkreidl.ephemeris.solarsystem;

public class SolarSystemVSOP87C extends SolarSystem
{
    public SolarSystemVSOP87C()
    {
        models.put( Body.SUN, new ModelSun() );
        models.put( Body.MERCURY, new ModelVsop87.XYZ(com.mkreidl.ephemeris.solarsystem.vsop87c.Mercury.getCoefficients()) );
        models.put( Body.VENUS, new ModelVsop87.XYZ( com.mkreidl.ephemeris.solarsystem.vsop87c.Venus.getCoefficients() ));
        models.put( Body.EARTH, new ModelVsop87.XYZ( com.mkreidl.ephemeris.solarsystem.vsop87c.Earth.getCoefficients() ));
        models.put( Body.MARS, new ModelVsop87.XYZ( com.mkreidl.ephemeris.solarsystem.vsop87c.Mars.getCoefficients() ));
        models.put( Body.JUPITER, new ModelVsop87.XYZ( com.mkreidl.ephemeris.solarsystem.vsop87c.Jupiter.getCoefficients() ));
        models.put( Body.SATURN, new ModelVsop87.XYZ( com.mkreidl.ephemeris.solarsystem.vsop87c.Saturn.getCoefficients() ));
        models.put( Body.URANUS, new ModelVsop87.XYZ( com.mkreidl.ephemeris.solarsystem.vsop87c.Uranus.getCoefficients() ));
        models.put( Body.NEPTUNE, new ModelVsop87.XYZ( com.mkreidl.ephemeris.solarsystem.vsop87c.Neptune.getCoefficients() ));
        models.put( Body.MOON, new ModelMoon() );
        models.put( Body.PLUTO, new ModelPluto() );
    }
}
