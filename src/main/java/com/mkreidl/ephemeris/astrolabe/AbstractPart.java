package com.mkreidl.ephemeris.astrolabe;


import com.mkreidl.ephemeris.geometry.Spherical;

public abstract class AbstractPart
{
    private long lastSynchronizedMillis;
    protected final Astrolabe astrolabe;
    protected OnRecomputeListener onRecomputeListener = () ->
    { /* Null object pattern*/ };

    AbstractPart( Astrolabe astrolabe )
    {
        this.astrolabe = astrolabe;
    }

    public void setRecomputeListener( OnRecomputeListener onRecomputeListener )
    {
        this.onRecomputeListener = onRecomputeListener;
    }

    protected abstract void onRecomputeProjection();

    protected abstract void onSynchronize();

    protected abstract void onChangeViewParameters();


    public Spherical getGeographicLocation()
    {
        return astrolabe.getLocation();
    }

    boolean isTimedOut( int timeoutMillis )
    {
        return Math.abs( astrolabe.getTimeInMillis() - lastSynchronizedMillis ) > timeoutMillis;
    }

    synchronized void changeObserverLocation()
    {
        onChangeViewParameters();
        onRecomputeProjection();
        onRecomputeListener.onRecompute();
    }

    synchronized void synchronize()
    {
        lastSynchronizedMillis = astrolabe.getTimeInMillis();
        onSynchronize();
        onRecomputeProjection();
        onRecomputeListener.onRecompute();
    }

    public interface OnRecomputeListener
    {
        void onRecompute();
    }
}
