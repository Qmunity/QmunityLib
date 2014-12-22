package uk.co.qmunity.lib.part;

/**
 * Interface implemented by any part that wants to be notified when something changes in its surroundings.
 *
 * @author amadornes
 */
public interface IPartUpdateListener extends IPart {

    /**
     * Called when a part in this block changes.
     */
    public void onPartChanged(IPart part);

    /**
     * Called when a neighbor block changes.
     */
    public void onNeighborBlockChange();

    /**
     * Called when a neighbor TileEntity changes.
     */
    public void onNeighborTileChange();

    /**
     * Called when this part is added to the world.
     */
    public void onAdded();

    /**
     * Called when this part is removed from the world.
     */
    public void onRemoved();

    /**
     * Called when the chunk this part is in gets loaded.
     */
    public void onLoaded();

    /**
     * Called when the chunk this part is in gets unloaded.
     */
    public void onUnloaded();

    /**
     * Called when the part container has been converted to another multipart system
     */
    public void onConverted();

}
