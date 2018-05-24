package com.mkreidl.ephemeris.solarsystem;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Cartesian;
import com.mkreidl.ephemeris.geometry.Spherical;

public class ModelSun extends OrbitalModel
{
    @Override
    public void compute( Time time, Cartesian position, Cartesian velocity )
    {
        position.set( 0.0, 0.0, 0.0 );
        if ( velocity != null )
            velocity.set( 0.0, 0.0, 0.0 );
    }

    @Override
    public void compute( Time time, Spherical position, Spherical velocity )
    {
        position.set( 0.0, 0.0, 0.0 );
        if ( velocity != null )
            velocity.set( 0.0, 0.0, 0.0 );
    }
}
