package uk.co.qmunity.lib.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotOutput extends QLSlot {

    public SlotOutput(IInventory inventory, int slot, int x, int y) {

        super(inventory, slot, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {

        return false;
    }
}
