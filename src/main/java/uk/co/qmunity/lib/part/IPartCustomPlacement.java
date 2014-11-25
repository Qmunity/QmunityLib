package uk.co.qmunity.lib.part;

import uk.co.qmunity.lib.vec.Vec3i;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface IPartCustomPlacement extends IPart {

    public IPartPlacement getPlacement(IPart part, World world, Vec3i location, ForgeDirection face, MovingObjectPosition mop,
            EntityPlayer player);

}
