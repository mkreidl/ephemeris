package com.mkreidl.ephemeris.dynamics;

import com.mkreidl.ephemeris.*;
import com.mkreidl.ephemeris.geometry.*;


public class Sun extends OrbitalModel<Cartesian>
{
    @Override
    public Type getType()
    {
        return Type.HELIOCENTRIC;
    }

    @Override
    public void compute( Time time, Cartesian position, Cartesian velocity )
    {
        position.set( 0.0, 0.0, 0.0 );
        if ( velocity != null )
            velocity.set( 0.0, 0.0, 0.0 );
    }
}
