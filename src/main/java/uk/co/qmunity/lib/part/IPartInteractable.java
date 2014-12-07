package uk.co.qmunity.lib.part;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;

/**
 * Interface implemented by parts that want to be notified when they're left/right-clicked.
 *
 * @author amadornes
 */
public interface IPartInteractable extends IPart {

    /**
     * Called when the part is right-clicked. Return true if an action occoured, false if nothing happened.
     */
    public boolean onActivated(EntityPlayer player, QMovingObjectPosition hit, ItemStack item);

    /**
     * Called when the part is left-clicked.
     */
    public void onClicked(EntityPlayer player, QMovingObjectPosition hit, ItemStack item);

}
