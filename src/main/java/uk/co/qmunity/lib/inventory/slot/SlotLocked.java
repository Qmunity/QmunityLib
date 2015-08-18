package uk.co.qmunity.lib.inventory.slot;

import net.minecraft.inventory.IInventory;

public class SlotLocked extends SlotFiltered {

    public SlotLocked(IInventory inventory, int slot, int x, int y) {

        super(inventory, slot, x, y, ISlotFilter.NONE);
    }

}
