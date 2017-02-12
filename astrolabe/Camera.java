package com.mkreidl.ephemeris.astrolabe;

import com.mkreidl.ephemeris.geometry.*;

/**
 * Created by mkreidl on 01.09.2016.
 */
public class Camera
{
    public Direction direction;
    public Position position;

    private Stereographic stereographic;
    private int reflection;

    public Camera( Direction direction, Position position )
    {
        this.direction = direction;
        this.position = position;
        invalidate();
    }

    public Camera( int direction, int position )
    {
        switch ( direction )
        {
            case 1:
                this.direction = Direction.SOUTH;
                break;
            case -1:
                this.direction = Direction.NORTH;
                break;
        }
        switch ( position )
        {
            case 1:
                this.position = Position.INNER;
                break;
            case -1:
                this.position = Position.OUTER;
                break;
        }
        invalidate();
    }

    public Camera setDirection( Direction direction )
    {
        this.direction = direction;
        invalidate();
        return this;
    }

    public Camera setPosition( Position position )
    {
        this.position = position;
        invalidate();
        return this;
    }

    public double projectLatitude( double lat )
    {
        return stereographic.project1D( lat );
    }

    public Cartesian project( Cartesian input, Cartesian projection )
    {
        stereographic.project( input, projection );
        projection.y = reflection * projection.y;
        return projection;
    }

    public Circle project( Spherical poleOfCircle, double alpha, Circle projection )
    {
        stereographic.project( poleOfCircle, alpha, projection );
        projection.y = reflection * projection.y;
        return projection;
    }

    public double reflect( double input )
    {
        return reflection * input;
    }

    public float reflect( float input )
    {
        return reflection * input;
    }

    public Direction getFinitePole()
    {
        return direction.pole * position.value == -1 ? Direction.NORTH : Direction.SOUTH;
    }

    private void invalidate()
    {
        stereographic = new Stereographic( direction.pole * position.value );
        reflection = direction.pole;
    }

    public enum Direction
    {
        SOUTH( 1 ), NORTH( -1 );
        private final int pole;

        Direction( int pole )
        {
            this.pole = pole;
        }

        public static Direction determineByLatitude( double latitude )
        {
            if ( latitude < 0 )
                return NORTH;
            else
                return SOUTH;
        }

        public int value()
        {
            return pole;
        }
    }

    public enum Position
    {
        INNER( 1 ), OUTER( -1 );
        private final int value;

        Position( int value )
        {
            this.value = value;
        }

        public int value()
        {
            return value;
        }
    }
}
