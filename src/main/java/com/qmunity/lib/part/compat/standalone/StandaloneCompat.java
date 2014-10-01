package com.qmunity.lib.part.compat.standalone;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.qmunity.lib.block.BlockMultipart;
import com.qmunity.lib.init.QLBlocks;
import com.qmunity.lib.part.IPart;
import com.qmunity.lib.part.ITilePartHolder;
import com.qmunity.lib.part.compat.IMultipartCompat;
import com.qmunity.lib.tile.TileMultipart;
import com.qmunity.lib.vec.Vec3i;

public class StandaloneCompat implements IMultipartCompat {

    @Override
    public boolean addPartToWorld(IPart part, World world, Vec3i location, EntityPlayer player) {

        TileMultipart te = BlockMultipart.get(world, location.getX(), location.getY(), location.getZ());
        boolean newTe = false;
        if (te == null) {
            te = new TileMultipart();
            te.xCoord = location.getX();
            te.yCoord = location.getY();
            te.zCoord = location.getZ();
            te.setWorldObj(world);
            newTe = true;
        }

        if (!te.canAddPart(part))
            return false;

        if (newTe) {
            te.addPart(part);
            if (te.getParts().size() == 0)
                return false;
        }

        if (!world.isRemote) {
            if (newTe) {
                world.setBlock(location.getX(), location.getY(), location.getZ(), QLBlocks.multipart);
                world.setTileEntity(location.getX(), location.getY(), location.getZ(), te);
            } else {
                te.addPart(part);
            }
        }

        return true;
    }

    @Override
    public boolean isMultipart(World world, Vec3i location) {

        return BlockMultipart.get(world, location.getX(), location.getY(), location.getZ()) != null;
    }

    @Override
    public int getStrongRedstoneOuput(World world, Vec3i location, ForgeDirection direction, ForgeDirection face) {

        TileMultipart te = BlockMultipart.get(world, location.getX(), location.getY(), location.getZ());
        if (te == null)
            return 0;

        return te.getStrongOutput(direction, face);
    }

    @Override
    public int getWeakRedstoneOuput(World world, Vec3i location, ForgeDirection direction, ForgeDirection face) {

        TileMultipart te = BlockMultipart.get(world, location.getX(), location.getY(), location.getZ());
        if (te == null)
            return 0;

        return te.getWeakOutput(direction, face);
    }

    @Override
    public ITilePartHolder getPartHolder(World world, Vec3i location) {

        return BlockMultipart.get(world, location.getX(), location.getY(), location.getZ());
    }

}
