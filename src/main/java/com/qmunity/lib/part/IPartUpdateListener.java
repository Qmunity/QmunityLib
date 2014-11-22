package com.qmunity.lib.part;

public interface IPartUpdateListener extends IPart {

    public void onPartChanged(IPart part);

    public void onNeighborBlockChange();

    public void onNeighborTileChange();

    public void onAdded();

    public void onRemoved();

    public void onLoaded();

    public void onUnloaded();

}
