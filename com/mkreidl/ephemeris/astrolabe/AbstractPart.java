package com.mkreidl.ephemeris.astrolabe;


public abstract class AbstractPart
{
    private long lastSynchronizedMillis;
    protected final Astrolabe astrolabe;
    protected OnRecalculateListener onRecalculateListener = new OnRecalculateListener()
    {
        @Override
        public void onRecalculate()
        {
            // Null object pattern
        }
    };

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

    public boolean isTimedOut( int timeoutMillis )
    {
        return Math.abs( astrolabe.getTimeInMillis() - lastSynchronizedMillis ) > timeoutMillis;
    }

    public void localize()
    {
        onChangeObserverParams();
        recalculate();
        onRecalculateListener.onRecalculate();
    }

    public void view()
    {
        onChangeObserverParams();
        recalculate();
        onRecalculateListener.onRecalculate();
    }

    public void synchronize()
    {
        lastSynchronizedMillis = astrolabe.time.getTime();
        onSynchronize();
        recalculate();
        onRecalculateListener.onRecalculate();
    }

    public void setRecalculateListener( OnRecalculateListener onRecalculateListener )
    {
        this.onRecalculateListener = onRecalculateListener;
    }

    public interface OnRecalculateListener
    {
        void onRecalculate();
    }
}
