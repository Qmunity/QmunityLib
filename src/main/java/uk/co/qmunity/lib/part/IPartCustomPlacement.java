package uk.co.qmunity.lib.part;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.Vec3i;

/**
 * Inteface implemented by parts that require custom placement, such as face parts or parts that need to know the user that placed them.
 *
 * @author amadornes
 */
public interface IPartCustomPlacement extends IPart {

    /**
     * Gets the placement for a part that's been placed at the specified location with the rest of the placement data.
     */
    public IPartPlacement getPlacement(IPart part, World world, Vec3i location, ForgeDirection face, MovingObjectPosition mop,
            EntityPlayer player);

}
