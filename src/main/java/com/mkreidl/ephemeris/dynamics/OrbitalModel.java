package com.mkreidl.ephemeris.dynamics;

import com.mkreidl.ephemeris.Distance;
import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Coordinates;

public abstract class OrbitalModel<T extends Coordinates<T>>
{
    public enum Type
    {
        HELIOCENTRIC, GEOCENTRIC
    }
  
    public Type getType() {
      return Type.HELIOCENTRIC;
    }
  
    public Distance getDistanceUnit() {
      return Distance.AU;
    }

    public abstract void compute( Time time, T position, T velocity );

    public T compute( Time time, T position )
    {
        compute( time, position, null );
        return position;
    }
}
