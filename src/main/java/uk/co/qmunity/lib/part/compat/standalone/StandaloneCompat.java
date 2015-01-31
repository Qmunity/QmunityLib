package uk.co.qmunity.lib.part.compat.standalone;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.block.BlockMultipart;
import uk.co.qmunity.lib.init.QLBlocks;
import uk.co.qmunity.lib.part.IMicroblock;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartPlacement;
import uk.co.qmunity.lib.part.ITilePartHolder;
import uk.co.qmunity.lib.part.PartNormallyOccluded;
import uk.co.qmunity.lib.part.compat.IMultipartCompat;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;
import uk.co.qmunity.lib.raytrace.RayTracer;
import uk.co.qmunity.lib.tile.TileMultipart;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class StandaloneCompat implements IMultipartCompat {

    @Override
    public boolean addPartToWorld(IPart part, World world, Vec3i location, boolean simulated) {

        TileMultipart te = BlockMultipart.get(world, location.getX(), location.getY(), location.getZ());
        boolean newTe = false;
        if (te == null) {
            te = new TileMultipart(simulated);
            te.xCoord = location.getX();
            te.yCoord = location.getY();
            te.zCoord = location.getZ();
            te.setWorldObj(world);
            newTe = true;
        }

        if (!te.canAddPart(part))
            return false;

        if (!simulated) {
            if (!world.isRemote) {
                if (newTe) {
                    world.setBlock(location.getX(), location.getY(), location.getZ(), QLBlocks.multipart);
                    world.setTileEntity(location.getX(), location.getY(), location.getZ(), te);
                }
                te.addPart(part);
            }
        } else {
            part.setParent(te);
        }

        return true;
    }

    @Override
    public boolean addPartToWorldBruteforce(IPart part, World world, Vec3i location) {

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
            ItemStack item, int pass, boolean simulated) {

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

        if (world.isAirBlock(location.getX(), location.getY(), location.getZ()) || isMultipart(world, location)) {
            IPartPlacement placement = MultipartCompatibility.getPlacementForPart(part, world, location, clickedFace, mop, player);
            if (placement == null)
                return false;
            if (!simulated && !placement.placePart(part, world, location, this, true))
                return false;
            return placement.placePart(part, world, location, this, simulated);
        }

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
    public boolean canBeMultipart(World world, Vec3i location) {

        return false;
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

    @Override
    public List<IMicroblock> getMicroblocks(World world, Vec3i location) {

        TileMultipart tmp = (TileMultipart) getPartHolder(world, location);
        if (tmp != null)
            return tmp.getMicroblocks();

        return new ArrayList<IMicroblock>();
    }
}
