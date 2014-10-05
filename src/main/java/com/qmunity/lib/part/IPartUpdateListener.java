package com.qmunity.lib.part;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IPartUpdateListener {

    public void onPartChanged(IPart part);

    public void onNeighborBlockChange();

    public void onNeighborTileChange();

    public void onAdded();

    public void onRemoved();

    public void onLoaded();

    public void onUnloaded();

    public boolean onActivated(EntityPlayer player, ItemStack item);

    public void onClicked(EntityPlayer player, ItemStack item);

}
