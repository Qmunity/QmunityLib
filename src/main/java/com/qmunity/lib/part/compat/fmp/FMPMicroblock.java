package com.qmunity.lib.part.compat.fmp;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import codechicken.lib.vec.Cuboid6;
import codechicken.microblock.CommonMicroblock;
import codechicken.microblock.CornerMicroblock;
import codechicken.microblock.EdgeMicroblock;
import codechicken.microblock.FaceMicroblock;
import codechicken.microblock.HollowMicroblock;

import com.qmunity.lib.part.IMicroblock;
import com.qmunity.lib.part.ITilePartHolder;
import com.qmunity.lib.part.MicroblockShape;
import com.qmunity.lib.raytrace.QMovingObjectPosition;
import com.qmunity.lib.vec.Vec3dCube;

public class FMPMicroblock implements IMicroblock {

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
    public ITilePartHolder getParent() {

        return null;
    }

    @Override
    public void setParent(ITilePartHolder parent) {

    }

    @Override
    public String getType() {

        return "fmpmicroblock";
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

    }

    @Override
    public void writeUpdateToNBT(NBTTagCompound tag) {

    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {

    }

    @Override
    public void sendUpdatePacket() {

    }

    @Override
    public ItemStack getItem() {

        return null;
    }

    @Override
    public ItemStack getPickedItem(QMovingObjectPosition mop) {

        return null;
    }

    @Override
    public List<ItemStack> getDrops() {

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

}
