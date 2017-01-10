package com.mkreidl.ephemeris.astrolabe;

/**
 * Created by mkreidl on 08.09.2016.
 */
public abstract class AbstractPart
{
    protected final Astrolabe astrolabe;
    private long lastSynchronizedMillis;

    AbstractPart( Astrolabe astrolabe )
    {
        this.astrolabe = astrolabe;
    }

    protected abstract void recalculate();

    protected abstract void onSynchronize();

    protected abstract void onChangeObserverParams();

    public Camera getCamera()
    {
        return astrolabe.camera;
    }

    public long getLastSynchronizedMillis()
    {
        return lastSynchronizedMillis;
    }

    void localize()
    {
        onChangeObserverParams();
        recalculate();
    }

    public void view()
    {
        onChangeObserverParams();
        recalculate();
    }

    void synchronize()
    {
        lastSynchronizedMillis = astrolabe.time.getTime();
        onSynchronize();
        recalculate();
    }
}
