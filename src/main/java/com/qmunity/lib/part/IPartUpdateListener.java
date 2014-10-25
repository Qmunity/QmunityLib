package com.qmunity.lib.part;


public interface IPartUpdateListener {

    public void onPartChanged(IPart part);

    public void onNeighborBlockChange();

    public void onNeighborTileChange();

    public void onAdded();

    public void onRemoved();

    public void onLoaded();

    public void onUnloaded();

}
