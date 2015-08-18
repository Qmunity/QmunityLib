package uk.co.qmunity.lib.inventory.slot;

/**
 * This class is copied from the BuildCraft code, which can be found here: https://github.com/BuildCraft/BuildCraft
 * 
 * @author CovertJaguar
 */
public interface ISlotPhantom {

    /*
     * Phantom Slots don't "use" items, they are used for filters and various other logic slots.
     */
    boolean canAdjust();
}
