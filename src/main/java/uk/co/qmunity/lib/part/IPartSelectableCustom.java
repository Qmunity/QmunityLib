package uk.co.qmunity.lib.part;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Interface implemented by parts that want to draw a custom selection box when their raytrace succeeds.
 *
 * @author amadornes
 */
public interface IPartSelectableCustom extends IPartSelectable {

    /**
     * Draws the custom selection box/es for the specified raytrace. Return false if you want QmunityLib to handle the rendering by itself.
     */
    @SideOnly(Side.CLIENT)
    public boolean drawHighlight(QMovingObjectPosition mop, EntityPlayer player, float frame);

}
