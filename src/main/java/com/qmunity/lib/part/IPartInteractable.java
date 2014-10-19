package com.qmunity.lib.part;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.qmunity.lib.raytrace.QMovingObjectPosition;

public interface IPartInteractable {

    public boolean onActivated(EntityPlayer player, QMovingObjectPosition hit, ItemStack item);

    public void onClicked(EntityPlayer player, QMovingObjectPosition hit, ItemStack item);

}
