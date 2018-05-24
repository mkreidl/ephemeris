package com.mkreidl.ephemeris.solarsystem;

import com.mkreidl.ephemeris.solarsystem.meeus.Earth;
import com.mkreidl.ephemeris.solarsystem.meeus.Jupiter;
import com.mkreidl.ephemeris.solarsystem.meeus.Mars;
import com.mkreidl.ephemeris.solarsystem.meeus.Mercury;
import com.mkreidl.ephemeris.solarsystem.meeus.Neptune;
import com.mkreidl.ephemeris.solarsystem.meeus.Saturn;
import com.mkreidl.ephemeris.solarsystem.meeus.Uranus;
import com.mkreidl.ephemeris.solarsystem.meeus.Venus;

public class SolarSystemMeeus extends SolarSystem
{
    public SolarSystemMeeus()
    {
        models.put( Body.SUN, new ModelSun() );
        models.put( Body.MERCURY, new Mercury() );
        models.put( Body.VENUS, new Venus() );
        models.put( Body.EARTH, new Earth() );
        models.put( Body.MARS, new Mars() );
        models.put( Body.JUPITER, new Jupiter() );
        models.put( Body.SATURN, new Saturn() );
        models.put( Body.URANUS, new Uranus() );
        models.put( Body.NEPTUNE, new Neptune() );
        models.put( Body.MOON, new ModelMoon() );
        models.put( Body.PLUTO, new ModelPluto() );
    }
}
