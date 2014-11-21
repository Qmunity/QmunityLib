package com.qmunity.lib.part.compat.standalone;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.qmunity.lib.block.BlockMultipart;
import com.qmunity.lib.init.QLBlocks;
import com.qmunity.lib.part.IPart;
import com.qmunity.lib.part.ITilePartHolder;
import com.qmunity.lib.part.PartNormallyOccluded;
import com.qmunity.lib.part.compat.IMultipartCompat;
import com.qmunity.lib.raytrace.RayTracer;
import com.qmunity.lib.tile.TileMultipart;
import com.qmunity.lib.vec.Vec3dCube;
import com.qmunity.lib.vec.Vec3i;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

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

        if (!world.isRemote) {
            if (newTe) {
                world.setBlock(location.getX(), location.getY(), location.getZ(), QLBlocks.multipart);
                world.setTileEntity(location.getX(), location.getY(), location.getZ(), te);
            }
            te.addPart(part);
        }

        return true;
    }

    @Override
    public boolean addPartToWorldBruteforce(IPart part, World world, Vec3i location, EntityPlayer player) {

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

        if (!world.isRemote) {
            if (newTe) {
                world.setBlock(location.getX(), location.getY(), location.getZ(), QLBlocks.multipart);
                world.setTileEntity(location.getX(), location.getY(), location.getZ(), te);
            }
        }
        te.addPart(part);

        return true;
    }

    @Override
    public boolean placePartInWorld(IPart part, World world, Vec3i location, ForgeDirection clickedFace, EntityPlayer player,
            ItemStack item, int pass) {

        if (pass == 0 && player.isSneaking())
            return false;

        MovingObjectPosition mop = world.getBlock(location.getX(), location.getY(), location.getZ()).collisionRayTrace(world,
                location.getX(), location.getY(), location.getZ(), RayTracer.instance().getStartVector(player).toVec3(),
                RayTracer.instance().getEndVector(player).toVec3());
        if (mop == null)
            return false;

        boolean solidFace = false;
        double x = mop.hitVec.xCoord - mop.blockX;
        double y = mop.hitVec.yCoord - mop.blockY;
        double z = mop.hitVec.zCoord - mop.blockZ;
        if (x < 0)
            x += 1;
        if (y < 0)
            y += 1;
        if (z < 0)
            z += 1;

        switch (clickedFace) {
        case DOWN:
            if (y == 0)
                solidFace = true;
            break;
        case UP:
            if (y == 1)
                solidFace = true;
            break;
        case WEST:
            if (x == 0)
                solidFace = true;
            break;
        case EAST:
            if (x == 1)
                solidFace = true;
            break;
        case NORTH:
            if (z == 0)
                solidFace = true;
            break;
        case SOUTH:
            if (z == 1)
                solidFace = true;
            break;
        default:
            break;
        }

        if (pass == 1 || solidFace)
            location.add(clickedFace);

        if (world.isAirBlock(location.getX(), location.getY(), location.getZ()) || isMultipart(world, location))
            if (addPartToWorld(part, world, location, player))
                return true;

        return false;
    }

    @Override
    public int getPlacementPasses() {

        return 2;
    }

    @Override
    public boolean isMultipart(World world, Vec3i location) {

        return BlockMultipart.get(world, location.getX(), location.getY(), location.getZ()) != null;
    }

    @Override
    public int getStrongRedstoneOuput(World world, Vec3i location, ForgeDirection side, ForgeDirection face) {

        TileMultipart te = BlockMultipart.get(world, location.getX(), location.getY(), location.getZ());
        if (te == null)
            return 0;

        if (face == ForgeDirection.UNKNOWN)
            return te.getStrongOutput(side);

        return te.getStrongOutput(side, face);
    }

    @Override
    public int getWeakRedstoneOuput(World world, Vec3i location, ForgeDirection side, ForgeDirection face) {

        TileMultipart te = BlockMultipart.get(world, location.getX(), location.getY(), location.getZ());
        if (te == null)
            return 0;

        if (face == ForgeDirection.UNKNOWN)
            return te.getWeakOutput(side);

        return te.getWeakOutput(side, face);
    }

    @Override
    public boolean canConnectRedstone(World world, Vec3i location, ForgeDirection side, ForgeDirection face) {

        TileMultipart te = BlockMultipart.get(world, location.getX(), location.getY(), location.getZ());
        if (te == null)
            return false;

        if (face == ForgeDirection.UNKNOWN)
            return te.canConnect(side);

        return te.canConnect(side, face);
    }

    @Override
    public ITilePartHolder getPartHolder(World world, Vec3i location) {

        return BlockMultipart.get(world, location.getX(), location.getY(), location.getZ());
    }

    @Override
    public boolean checkOcclusion(World world, Vec3i location, Vec3dCube cube) {

        TileMultipart te = BlockMultipart.get(world, location.getX(), location.getY(), location.getZ());
        if (te == null)
            return false;

        return !te.canAddPart(new PartNormallyOccluded(cube));
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Override
    public void init(FMLInitializationEvent event) {

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }

}
