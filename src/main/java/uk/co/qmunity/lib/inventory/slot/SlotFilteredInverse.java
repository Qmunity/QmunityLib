package uk.co.qmunity.lib.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotFilteredInverse extends SlotFiltered {

    public SlotFilteredInverse(IInventory inventory, int slot, int x, int y, ISlotFilter filter) {

        super(inventory, slot, x, y, filter);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {

        return !filter.matches(stack);
    }

}
