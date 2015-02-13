package uk.co.qmunity.lib.part.compat.fmp;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import uk.co.qmunity.lib.part.IMicroblock;
import uk.co.qmunity.lib.part.MicroblockShape;
import uk.co.qmunity.lib.part.PartBase;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.vec.Vec3dCube;
import codechicken.lib.vec.Cuboid6;
import codechicken.microblock.CommonMicroblock;
import codechicken.microblock.CornerMicroblock;
import codechicken.microblock.EdgeMicroblock;
import codechicken.microblock.FaceMicroblock;
import codechicken.microblock.HollowMicroblock;

public class FMPMicroblock extends PartBase implements IMicroblock {

    private CommonMicroblock microblock;

    public FMPMicroblock(CommonMicroblock microblock) {

        this.microblock = microblock;
    }

    @Override
    public MicroblockShape getShape() {

        if (microblock instanceof HollowMicroblock)
            return MicroblockShape.FACE_HOLLOW;
        if (microblock instanceof FaceMicroblock)
            return MicroblockShape.FACE;
        if (microblock instanceof CornerMicroblock)
            return MicroblockShape.CORNER;
        if (microblock instanceof EdgeMicroblock)
            return MicroblockShape.EDGE;

        return null;
    }

    @Override
    public int getSize() {

        return microblock.getSize();
    }

    @Override
    public int getPosition() {

        if (microblock instanceof FaceMicroblock || microblock instanceof HollowMicroblock)
            return microblock.getShape();

        return -1;// TODO
    }

    @Override
    public String getType() {

        return "fmpmicroblock";
    }

    @Override
    public ItemStack getItem() {

        return null;
    }

    @Override
    public void breakAndDrop(boolean creative) {

    }

    @Override
    public World getWorld() {

        return microblock.world();
    }

    @Override
    public int getX() {

        return microblock.x();
    }

    @Override
    public int getY() {

        return microblock.y();
    }

    @Override
    public int getZ() {

        return microblock.z();
    }

    @Override
    public List<Vec3dCube> getOcclusionBoxes() {

        List<Vec3dCube> boxes = new ArrayList<Vec3dCube>();

        for (Cuboid6 c : microblock.getSubParts())
            boxes.add(new Vec3dCube(c.toAABB()));

        return boxes;
    }

    @Override
    public double getHardness(EntityPlayer player, QMovingObjectPosition mop) {

        return 0;
    }

}
