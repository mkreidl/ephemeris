package com.mkreidl.ephemeris.solarsystem;

import com.mkreidl.ephemeris.solarsystem.meeus.Jupiter;
import com.mkreidl.ephemeris.solarsystem.meeus.Mars;
import com.mkreidl.ephemeris.solarsystem.meeus.Mercury;
import com.mkreidl.ephemeris.solarsystem.meeus.Neptune;
import com.mkreidl.ephemeris.solarsystem.meeus.Saturn;
import com.mkreidl.ephemeris.solarsystem.meeus.Uranus;
import com.mkreidl.ephemeris.solarsystem.meeus.Venus;
import com.mkreidl.ephemeris.solarsystem.vsop87c.Earth;

public class SolarSystemMeeus extends SolarSystem
{
    public SolarSystemMeeus()
    {
        models.put( Body.EARTH, new ModelVsop87.XYZ(Earth.getCoefficients()) );

        models.put( Body.SUN, new ModelSun() );
        models.put( Body.MERCURY, new ModelVsop87.LBR(Mercury.Companion.getCoefficients()) );
        models.put( Body.VENUS, new ModelVsop87.LBR(Venus.Companion.getCoefficients()) );
        models.put( Body.MARS, new ModelVsop87.LBR(Mars.Companion.getCoefficients()));
        models.put( Body.JUPITER, new ModelVsop87.LBR(Jupiter.Companion.getCoefficients()));
        models.put( Body.SATURN, new ModelVsop87.LBR(Saturn.Companion.getCoefficients()) );
        models.put( Body.URANUS, new ModelVsop87.LBR(Uranus.Companion.getCoefficients()) );
        models.put( Body.NEPTUNE, new ModelVsop87.LBR(Neptune.Companion.getCoefficients()) );
        models.put( Body.MOON, new ModelMoon() );
        models.put( Body.PLUTO, new ModelPluto() );
    }
}
