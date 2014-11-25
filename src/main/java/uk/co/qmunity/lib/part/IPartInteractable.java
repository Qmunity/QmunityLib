package uk.co.qmunity.lib.part;

import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IPartInteractable extends IPart {

    public boolean onActivated(EntityPlayer player, QMovingObjectPosition hit, ItemStack item);

    public void onClicked(EntityPlayer player, QMovingObjectPosition hit, ItemStack item);

}
