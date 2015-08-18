package uk.co.qmunity.lib.inventory.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

/**
 * This class was made by CovertJaguar for BuildCraft but has been adapted for use in QmunityLib.<br>
 * You can find the original source at http://github.com/BuildCraft/BuildCraft
 */
public class SlotPhantom extends QLSlot implements ISlotPhantom {

    public SlotPhantom(IInventory inventory, int slot, int x, int y) {

        super(inventory, slot, x, y);
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {

        return false;
    }

    @Override
    public boolean canAdjust() {

        return true;
    }

}
