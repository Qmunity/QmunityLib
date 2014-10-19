package com.qmunity.lib.part.compat.fmp;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Cuboid6;
import codechicken.multipart.IFaceRedstonePart;
import codechicken.multipart.IRedstonePart;
import codechicken.multipart.NormallyOccludedPart;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TSlottedPart;
import codechicken.multipart.TileMultipart;

import com.qmunity.lib.part.IPart;
import com.qmunity.lib.part.ITilePartHolder;
import com.qmunity.lib.part.compat.IMultipartCompat;
import com.qmunity.lib.vec.Vec3dCube;
import com.qmunity.lib.vec.Vec3i;

public class FMPCompat implements IMultipartCompat {

    @Override
    public boolean addPartToWorld(IPart part, World world, Vec3i location, EntityPlayer player) {

        BlockCoord b = new BlockCoord(location.getX(), location.getY(), location.getZ());
        TileMultipart tmp = TileMultipart.getTile(world, b);
        if (tmp == null)
            return false;

        FMPPart p = (FMPPart) getPartHolder(world, location);
        boolean isNew = false;
        if (p == null) {
            p = new FMPPart();
            isNew = true;
        }

        if (!p.canAddPart(part))
            return false;

        if (!world.isRemote)
            p.addPart(part);

        if (isNew) {
            if (!tmp.canAddPart(p))
                return false;
            if (!world.isRemote)
                TileMultipart.addPart(world, b, p);
        }

        return true;
    }

    @Override
    public boolean isMultipart(World world, Vec3i location) {

        return TileMultipart.getTile(world, new BlockCoord(location.getX(), location.getY(), location.getZ())) != null;
    }

    @Override
    public int getStrongRedstoneOuput(World world, Vec3i location, ForgeDirection direction, ForgeDirection face) {

        TileMultipart tmp = TileMultipart.getTile(world, new BlockCoord(location.getX(), location.getY(), location.getZ()));
        int strong = 0;

        for (TMultiPart p : tmp.jPartList()) {
            if (p instanceof IRedstonePart) {
                if (p instanceof IFaceRedstonePart) {
                    if (((IFaceRedstonePart) p).getFace() == face.ordinal())
                        strong = Math.max(strong, ((IRedstonePart) p).strongPowerLevel(direction.ordinal()));
                } else if (p instanceof TSlottedPart) {
                    if (((TSlottedPart) p).getSlotMask() == 1 << direction.ordinal())
                        strong = Math.max(strong, ((IRedstonePart) p).strongPowerLevel(direction.ordinal()));
                } else {
                    strong = Math.max(strong, ((IRedstonePart) p).strongPowerLevel(direction.ordinal()));
                }
            }
        }

        return strong;
    }

    @Override
    public int getWeakRedstoneOuput(World world, Vec3i location, ForgeDirection direction, ForgeDirection face) {

        TileMultipart tmp = TileMultipart.getTile(world, new BlockCoord(location.getX(), location.getY(), location.getZ()));
        int weak = 0;

        for (TMultiPart p : tmp.jPartList()) {
            if (p instanceof IRedstonePart) {
                if (p instanceof IFaceRedstonePart) {
                    if (((IFaceRedstonePart) p).getFace() == face.ordinal())
                        weak = Math.max(weak, ((IRedstonePart) p).weakPowerLevel(direction.ordinal()));
                } else if (p instanceof TSlottedPart) {
                    if (((TSlottedPart) p).getSlotMask() == 1 << direction.ordinal())
                        weak = Math.max(weak, ((IRedstonePart) p).weakPowerLevel(direction.ordinal()));
                } else {
                    weak = Math.max(weak, ((IRedstonePart) p).weakPowerLevel(direction.ordinal()));
                }
            }
        }

        return weak;
    }

    @Override
    public ITilePartHolder getPartHolder(World world, Vec3i location) {

        TileMultipart tmp = TileMultipart.getTile(world, new BlockCoord(location.getX(), location.getY(), location.getZ()));

        for (TMultiPart p : tmp.jPartList())
            if (p instanceof FMPPart)
                return (ITilePartHolder) p;

        return null;
    }

    @Override
    public boolean checkOcclusion(World world, Vec3i location, Vec3dCube cube) {

        TileMultipart tmp = TileMultipart.getTile(world, new BlockCoord(location.getX(), location.getY(), location.getZ()));
        if (tmp == null)
            return false;

        return !tmp.canAddPart(new NormallyOccludedPart(new Cuboid6(cube.toAABB())));
    }
}
