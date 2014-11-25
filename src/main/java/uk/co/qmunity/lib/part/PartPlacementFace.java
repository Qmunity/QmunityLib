package uk.co.qmunity.lib.part;

import uk.co.qmunity.lib.part.compat.IMultipartCompat;
import uk.co.qmunity.lib.vec.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class PartPlacementFace implements IPartPlacement {

    protected ForgeDirection face;

    public PartPlacementFace(ForgeDirection face) {

        this.face = face;
    }

    @Override
    public boolean placePart(IPart part, World world, Vec3i location, IMultipartCompat multipartSystem, boolean simulated) {

        if (part instanceof IPartFace)
            ((IPartFace) part).setFace(face);

        return multipartSystem.addPartToWorld(part, world, location, simulated);
    }

}
