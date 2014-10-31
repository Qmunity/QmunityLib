package com.qmunity.lib.part.compat.fmp;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Direction;
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
    public int getStrongRedstoneOuput(World world, Vec3i location, ForgeDirection side, ForgeDirection face) {

        TileMultipart tmp = TileMultipart.getTile(world, new BlockCoord(location.getX(), location.getY(), location.getZ()));
        if (tmp == null)
            return 0;

        if (face == ForgeDirection.UNKNOWN)
            return tmp.strongPowerLevel(side.ordinal());

        int strong = 0;

        for (TMultiPart p : tmp.jPartList()) {
            if (p instanceof IRedstonePart) {
                if (p instanceof IFaceRedstonePart) {
                    if (((IFaceRedstonePart) p).getFace() == face.ordinal())
                        strong = Math.max(strong, ((IRedstonePart) p).strongPowerLevel(side.ordinal()));
                } else if (p instanceof TSlottedPart) {
                    if (((TSlottedPart) p).getSlotMask() == 1 << side.ordinal())
                        strong = Math.max(strong, ((IRedstonePart) p).strongPowerLevel(side.ordinal()));
                } else {
                    strong = Math.max(strong, ((IRedstonePart) p).strongPowerLevel(side.ordinal()));
                }
            }
        }

        return strong;
    }

    @Override
    public int getWeakRedstoneOuput(World world, Vec3i location, ForgeDirection side, ForgeDirection face) {

        TileMultipart tmp = TileMultipart.getTile(world, new BlockCoord(location.getX(), location.getY(), location.getZ()));
        if (tmp == null)
            return 0;

        if (face == ForgeDirection.UNKNOWN)
            return tmp.weakPowerLevel(side.ordinal());

        int weak = 0;

        for (TMultiPart p : tmp.jPartList()) {
            if (p instanceof IRedstonePart) {
                if (p instanceof IFaceRedstonePart) {
                    if (((IFaceRedstonePart) p).getFace() == face.ordinal())
                        weak = Math.max(weak, ((IRedstonePart) p).weakPowerLevel(side.ordinal()));
                } else if (p instanceof TSlottedPart) {
                    if (((TSlottedPart) p).getSlotMask() == 1 << side.ordinal())
                        weak = Math.max(weak, ((IRedstonePart) p).weakPowerLevel(side.ordinal()));
                } else {
                    weak = Math.max(weak, ((IRedstonePart) p).weakPowerLevel(side.ordinal()));
                }
            }
        }

        return weak;
    }

    @Override
    public boolean canConnectRedstone(World world, Vec3i location, ForgeDirection side, ForgeDirection face) {

        int s = Direction.getMovementDirection(side.offsetX, side.offsetZ);
        int f = face.ordinal();

        TileMultipart tmp = TileMultipart.getTile(world, new BlockCoord(location.getX(), location.getY(), location.getZ()));
        if (tmp == null)
            return false;

        if (face == ForgeDirection.UNKNOWN)
            return tmp.canConnectRedstone(s);

        if (!tmp.canConnectRedstone(s))
            return false;

        for (TMultiPart p : tmp.jPartList()) {
            if (p instanceof IRedstonePart) {
                if (p instanceof IFaceRedstonePart) {
                    if (((IFaceRedstonePart) p).getFace() == f && ((IRedstonePart) p).canConnectRedstone(s))
                        return true;
                } else if (p instanceof TSlottedPart) {
                    if (((TSlottedPart) p).getSlotMask() == 1 << s && ((IRedstonePart) p).canConnectRedstone(s))
                        return true;
                } else {
                    if (((IRedstonePart) p).canConnectRedstone(s))
                        return true;
                }
            }
        }

        return false;
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
