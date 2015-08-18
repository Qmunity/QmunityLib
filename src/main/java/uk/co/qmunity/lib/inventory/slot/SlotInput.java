package uk.co.qmunity.lib.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotInput extends QLSlot {

    public SlotInput(IInventory inventory, int slot, int x, int y) {

        super(inventory, slot, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {

        return inventory.isItemValidForSlot(getSlotIndex(), itemStack);
    }
}
