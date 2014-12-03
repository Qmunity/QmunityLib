package uk.co.qmunity.lib.part.compat.fmp;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartFace;
import uk.co.qmunity.lib.part.IPartPlacement;
import uk.co.qmunity.lib.part.ITilePartHolder;
import uk.co.qmunity.lib.part.compat.IMultipartCompat;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;
import uk.co.qmunity.lib.raytrace.RayTracer;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Cuboid6;
import codechicken.multipart.IFaceRedstonePart;
import codechicken.multipart.IRedstonePart;
import codechicken.multipart.NormallyOccludedPart;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TSlottedPart;
import codechicken.multipart.TileMultipart;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class FMPCompat implements IMultipartCompat {

    @Override
    public boolean addPartToWorld(IPart part, World world, Vec3i location, boolean simulated) {

        BlockCoord b = new BlockCoord(location.getX(), location.getY(), location.getZ());
        TileMultipart tmp = TileMultipart.getOrConvertTile(world, b);
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

        if (!simulated) {
            if (!world.isRemote)
                p.addPart(part);
        } else {
            part.setParent(p);
            TileMultipart te = TileMultipart.getOrConvertTile(world, new BlockCoord(location.getX(), location.getY(), location.getZ()));
            if (te == null) {
                te = new TileMultipart();
                te.xCoord = location.getX();
                te.yCoord = location.getY();
                te.zCoord = location.getZ();
                te.setWorldObj(world);
            }
            p.tile_$eq(te);
        }

        if (isNew) {
            if (!tmp.canAddPart(p))
                return false;
            if (!simulated && !world.isRemote)
                TileMultipart.addPart(world, b, p);
        }

        return true;
    }

    @Override
    public boolean addPartToWorldBruteforce(IPart part, World world, Vec3i location) {

        BlockCoord b = new BlockCoord(location.getX(), location.getY(), location.getZ());
        TileMultipart tmp = TileMultipart.getOrConvertTile(world, b);
        if (tmp == null)
            return false;

        FMPPart p = (FMPPart) getPartHolder(world, location);
        boolean isNew = false;
        if (p == null) {
            p = new FMPPart();
            isNew = true;
        }

        if (!world.isRemote)
            p.addPart(part);

        if (isNew && !world.isRemote)
            TileMultipart.addPart(world, b, p);

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
            if (!simulated) {
                if (!placement.placePart(part, world, location, this, true))
                    return false;
                if (part instanceof IPartFace && !((IPartFace) part).canStay())
                    return false;
                if (placement.placePart(part, world, location, this, false))
                    return true;
            } else {
                if (placement.placePart(part, world, location, this, simulated)) {
                    if (part instanceof IPartFace && !((IPartFace) part).canStay())
                        return false;
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public int getPlacementPasses() {

        return 2;
    }

    @Override
    public boolean isMultipart(World world, Vec3i location) {

        return TileMultipart.getOrConvertTile(world, new BlockCoord(location.getX(), location.getY(), location.getZ())) != null;
    }

    @Override
    public int getStrongRedstoneOuput(World world, Vec3i location, ForgeDirection side, ForgeDirection face) {

        TileMultipart tmp = TileMultipart.getOrConvertTile(world, new BlockCoord(location.getX(), location.getY(), location.getZ()));
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

        TileMultipart tmp = TileMultipart.getOrConvertTile(world, new BlockCoord(location.getX(), location.getY(), location.getZ()));
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

        TileMultipart tmp = TileMultipart.getOrConvertTile(world, new BlockCoord(location.getX(), location.getY(), location.getZ()));
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

        TileMultipart tmp = TileMultipart.getOrConvertTile(world, new BlockCoord(location.getX(), location.getY(), location.getZ()));

        for (TMultiPart p : tmp.jPartList())
            if (p instanceof FMPPart)
                return (ITilePartHolder) p;

        return null;
    }

    @Override
    public boolean checkOcclusion(World world, Vec3i location, Vec3dCube cube) {

        TileMultipart tmp = TileMultipart.getOrConvertTile(world, new BlockCoord(location.getX(), location.getY(), location.getZ()));
        if (tmp == null)
            return false;

        return !tmp.canAddPart(new NormallyOccludedPart(new Cuboid6(cube.toAABB())));
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Override
    public void init(FMLInitializationEvent event) {

        FMPPartFactory.register();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }
}