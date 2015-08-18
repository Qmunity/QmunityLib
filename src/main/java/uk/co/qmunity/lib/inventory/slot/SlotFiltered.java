package uk.co.qmunity.lib.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotFiltered extends QLSlot {

    protected ISlotFilter filter;

    public SlotFiltered(IInventory inventory, int slot, int x, int y, ISlotFilter filter) {

        super(inventory, slot, x, y);

        this.filter = filter;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {

        return filter.matches(stack);
    }

}
