package uk.co.qmunity.lib.inventory.slot;

import net.minecraft.item.ItemStack;

public interface ISlotFilter {

    public static final ISlotFilter NONE = new ISlotFilter() {

        @Override
        public boolean matches(ItemStack stack) {

            return false;
        }
    };

    public static final ISlotFilter ANY = new ISlotFilter() {

        @Override
        public boolean matches(ItemStack stack) {

            return true;
        }
    };

    public boolean matches(ItemStack stack);

}
