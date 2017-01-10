package com.mkreidl.ephemeris.dynamics;

import com.mkreidl.ephemeris.Time;
import com.mkreidl.ephemeris.geometry.Cartesian;

/**
 * Created by mkreidl on 20.08.2016.
 */
public class Sun extends OrbitalModel<Cartesian>
{

    @Override
    public Type getType()
    {
        return Type.HELIOCENTRIC;
    }

    @Override
    public void calculate( Time time, Cartesian position, Cartesian velocity )
    {
        position.set( 0.0, 0.0, 0.0 );
        if ( velocity != null )
            velocity.set( 0.0, 0.0, 0.0 );
    }
}
