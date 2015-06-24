package uk.co.qmunity.lib.helper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDropper;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockNote;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.block.BlockRedstoneLight;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockTNT;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityComparator;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.part.compat.IMultipartCompat;
import uk.co.qmunity.lib.part.compat.MultipartSystem;
import uk.co.qmunity.lib.vec.Vec3i;

public class RedstoneHelper {

    @Deprecated
    public static int getVanillaSignalStrength(World world, int x, int y, int z, ForgeDirection side, ForgeDirection face) {

        return getVanillaSignalStrength(world, new BlockPos(x, y, z), side, face);
    }

    public static int getVanillaSignalStrength(World world, BlockPos pos, ForgeDirection side, ForgeDirection face) {

        if (face != ForgeDirection.DOWN && face != ForgeDirection.UNKNOWN)
            return 0;

        Block block = world.getBlock(pos.getX(), pos.getY(), pos.getZ());

        if (block == Blocks.redstone_wire) {
            if (side == ForgeDirection.DOWN)
                return world.getBlockMetadata(pos.getX(), pos.getY(), pos.getZ());
            if (side == ForgeDirection.UP)
                return 0;
            int d = Direction.getMovementDirection(side.offsetX, side.offsetZ);
            if (BlockRedstoneWire.isPowerProviderOrWire(world, pos.getX(), pos.getY(), pos.getZ(), d)
                    || BlockRedstoneWire.isPowerProviderOrWire(world, pos.getX() + side.offsetX + side.offsetX, pos.getY() + side.offsetY + side.offsetY, pos.getZ()
                            + side.offsetZ + side.offsetZ, (d + 2) % 4)) {
                return world.getBlockMetadata(pos.getX(), pos.getY(), pos.getZ());
            }
        }
        if (block instanceof BlockRedstoneComparator)

            if (block == Blocks.unpowered_repeater) {
                return 0;
            }
        if (block == Blocks.powered_repeater) {
            if (side == ForgeDirection.DOWN || side == ForgeDirection.UP)
                return 0;
            int d = Direction.getMovementDirection(side.offsetX, side.offsetZ);
            return d == (world.getBlockMetadata(pos.getX(), pos.getY(), pos.getZ()) % 4) ? 15 : 0;
        }
        if (block instanceof BlockRedstoneComparator) {
            if (side == ForgeDirection.DOWN || side == ForgeDirection.UP)
                return 0;
            int d = Direction.getMovementDirection(side.offsetX, side.offsetZ);
            return d == (world.getBlockMetadata(pos.getX(), pos.getY(), pos.getZ()) % 4) ? ((TileEntityComparator) world.getTileEntity(pos.getX(), pos.getY(), pos.getZ())).getOutputSignal() : 0;
        }
        return 0;
    }

    @Deprecated
    public static boolean canConnectVanilla(World world, int x, int y, int z, ForgeDirection side, ForgeDirection face) {

        return canConnectVanilla(world, new BlockPos(x, y, z), side, face);
    }

    public static boolean canConnectVanilla(World world, BlockPos pos, ForgeDirection side, ForgeDirection face) {

        if (side == ForgeDirection.UNKNOWN)
            return false;

        Block block = world.getBlock(pos.getX(), pos.getY(), pos.getZ());
        int meta = world.getBlockMetadata(pos.getX(), pos.getY(), pos.getZ());
        int d = Direction.getMovementDirection(side.offsetX, side.offsetZ);

        if ((block == Blocks.unpowered_repeater || block == Blocks.powered_repeater)
                && (face == ForgeDirection.DOWN || face == ForgeDirection.UNKNOWN))
            if (d % 2 == meta % 2)
                return true;

        if (block instanceof BlockLever) {
            meta = meta % 8;
            ForgeDirection leverFace = ((meta == 0 || meta == 7) ? ForgeDirection.UP : ((meta == 5 || meta == 6) ? ForgeDirection.DOWN
                    : (meta == 1 ? ForgeDirection.WEST : (meta == 2 ? ForgeDirection.EAST : (meta == 3 ? ForgeDirection.NORTH
                            : (meta == 4 ? ForgeDirection.SOUTH : ForgeDirection.UNKNOWN))))));
            if (face != ForgeDirection.UNKNOWN && face != leverFace)
                return false;
            return side != leverFace.getOpposite();
        }

        if (block instanceof BlockRedstoneComparator && (face == ForgeDirection.DOWN || face == ForgeDirection.UNKNOWN))
            return side != ForgeDirection.UP;

        if (block instanceof BlockRedstoneWire)
            return face == ForgeDirection.UNKNOWN || face == ForgeDirection.DOWN;

        return block instanceof BlockDoor || block instanceof BlockRedstoneLight || block instanceof BlockTNT
                || block instanceof BlockDispenser || block instanceof BlockNote
                || block instanceof BlockPistonBase;// true;
    }

    @Deprecated
    private static boolean isVanillaBlock(World world, int x, int y, int z) {

        return isVanillaBlock(world, new BlockPos(x, y, z));
    }

    private static boolean isVanillaBlock(World world, BlockPos pos) {

        Block b = world.getBlock(pos.getX(), pos.getY(), pos.getZ());
        return b instanceof BlockRedstoneRepeater || b instanceof BlockLever || b instanceof BlockRedstoneWire
                || b instanceof BlockRedstoneComparator || b instanceof BlockDoor || b instanceof BlockRedstoneLight
                || b instanceof BlockTNT || b instanceof BlockDispenser || b instanceof BlockNote
                || b instanceof BlockPistonBase;
    }

    @Deprecated
    public static int getOutputWeak(World world, int x, int y, int z, ForgeDirection side, ForgeDirection face) {

        return getOutputWeak(world, new BlockPos(x, y, z), side, face);
    }

    public static int getOutputWeak(World world, BlockPos pos, ForgeDirection side, ForgeDirection face) {

        Vec3i location = new Vec3i(pos);

        for (MultipartSystem s : MultipartSystem.getAvailableSystems()) {
            IMultipartCompat compat = s.getCompat();
            if (compat.isMultipart(world, location))
                return compat.getWeakRedstoneOuput(world, location, side, face);
        }

        Block block = world.getBlock(pos.getX(), pos.getY(), pos.getZ());

        int power = block.isProvidingWeakPower(world, pos.getX(), pos.getY(), pos.getZ(), side.getOpposite().ordinal());
        if (power > 0)
            return power;

        if (block.isNormalCube(world, pos.getX(), pos.getY(), pos.getZ()) && block.isOpaqueCube()) {
            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                if (d == side)
                    continue;
                power = Math.max(power,
                        getOutputStrong(world, pos.getX() + d.offsetX, pos.getY() + d.offsetY, pos.getZ() + d.offsetZ, d.getOpposite(), ForgeDirection.UNKNOWN));
            }
        }

        return power;
    }

    @Deprecated
    public static int getOutputStrong(World world, int x, int y, int z, ForgeDirection side, ForgeDirection face) {

        return getOutputStrong(world, new BlockPos(x, y, z), side, face);
    }

    public static int getOutputStrong(World world, BlockPos pos, ForgeDirection side, ForgeDirection face) {

        Vec3i location = new Vec3i(pos);

        for (MultipartSystem s : MultipartSystem.getAvailableSystems()) {
            IMultipartCompat compat = s.getCompat();
            if (compat.isMultipart(world, location))
                return compat.getStrongRedstoneOuput(world, location, side, face);
        }

        int power = getVanillaSignalStrength(world, pos, side, face);
        if (power > 0)
            return power;

        return world.getBlock(pos.getX(), pos.getY(), pos.getZ()).isProvidingStrongPower(world, pos.getX(), pos.getY(), pos.getZ(), side.getOpposite().ordinal());
    }

    @Deprecated
    public static int getOutputWeak(World world, int x, int y, int z, ForgeDirection side) {

        return getOutputWeak(world, new BlockPos(x, y, z), side, ForgeDirection.UNKNOWN);
    }

    public static int getOutputWeak(World world, BlockPos pos, ForgeDirection side) {

        return getOutputWeak(world, pos, side, ForgeDirection.UNKNOWN);
    }

    @Deprecated
    public static int getOutputStrong(World world, int x, int y, int z, ForgeDirection side) {

        return getOutputStrong(world, new BlockPos(x, y, z), side, ForgeDirection.UNKNOWN);
    }

    public static int getOutputStrong(World world, BlockPos pos, ForgeDirection side) {

        return getOutputStrong(world, pos, side, ForgeDirection.UNKNOWN);
    }

    @Deprecated
    public static int getOutput(World world, int x, int y, int z, ForgeDirection side) {

        return Math.max(getOutputWeak(world, new BlockPos(x, y, z), side), getOutputStrong(world, new BlockPos(x, y, z), side));
    }

    public static int getOutput(World world, BlockPos pos, ForgeDirection side) {

        return Math.max(getOutputWeak(world, pos, side), getOutputStrong(world, pos, side));
    }

    @Deprecated
    public static int getOutput(World world, int x, int y, int z, ForgeDirection side, ForgeDirection face) {

        return Math.max(getOutputWeak(world, new BlockPos(x, y, z), side, face), getOutputStrong(world, new BlockPos(x, y, z), side, face));
    }

    public static int getOutput(World world, BlockPos pos, ForgeDirection side, ForgeDirection face) {

        return Math.max(getOutputWeak(world, pos, side, face), getOutputStrong(world, pos, side, face));
    }

    @Deprecated
    public static int getOutput(World world, int x, int y, int z) {

        return getOutput(world, new BlockPos(x, y, z));
    }

    public static int getOutput(World world, BlockPos pos) {

        int power = 0;
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS)
            power = Math.max(power, getOutput(world, pos, side));
        return power;
    }

    @Deprecated
    public static int getInputWeak(World world, int x, int y, int z, ForgeDirection side, ForgeDirection face) {

        return getOutputWeak(world, new BlockPos(x + side.offsetX, y + side.offsetY, z + side.offsetZ), side.getOpposite(), face);
    }

    public static int getInputWeak(World world, BlockPos pos, ForgeDirection side, ForgeDirection face) {

        return getOutputWeak(world, pos.add(side.offsetX, side.offsetY, side.offsetZ), side.getOpposite(), face);
    }

    @Deprecated
    public static int getInputStrong(World world, int x, int y, int z, ForgeDirection side, ForgeDirection face) {

        return getOutputStrong(world, new BlockPos(x + side.offsetX, y + side.offsetY, z + side.offsetZ), side.getOpposite(), face);
    }

    public static int getInputStrong(World world, BlockPos pos, ForgeDirection side, ForgeDirection face) {

        return getOutputStrong(world, pos.add(side.offsetX, side.offsetY, side.offsetZ), side.getOpposite(), face);
    }

    @Deprecated
    public static int getInputWeak(World world, int x, int y, int z, ForgeDirection side) {

        return getOutputWeak(world, new BlockPos(x + side.offsetX, y + side.offsetY, z + side.offsetZ), side.getOpposite());
    }

    public static int getInputWeak(World world, BlockPos pos, ForgeDirection side) {

        return getOutputWeak(world, pos.add(side.offsetX, side.offsetY, side.offsetZ), side.getOpposite());
    }

    @Deprecated
    public static int getInputStrong(World world, int x, int y, int z, ForgeDirection side) {

        return getOutputStrong(world, new BlockPos(x + side.offsetX, y + side.offsetY, z + side.offsetZ), side.getOpposite());
    }

    public static int getInputStrong(World world, BlockPos pos, ForgeDirection side) {

        return getOutputStrong(world, pos.add(side.offsetX, side.offsetY, side.offsetZ), side.getOpposite());
    }

    @Deprecated
    public static int getInput(World world, int x, int y, int z, ForgeDirection side) {

        return getOutput(world, new BlockPos(x + side.offsetX, y + side.offsetY, z + side.offsetZ), side.getOpposite());
    }

    public static int getInput(World world, BlockPos pos, ForgeDirection side) {

        return getOutput(world, pos.add(side.offsetX, side.offsetY, side.offsetZ), side.getOpposite());
    }

    @Deprecated
    public static int getInput(World world, int x, int y, int z, ForgeDirection side, ForgeDirection face) {

        return getOutput(world, new BlockPos(x + side.offsetX, y + side.offsetY, z + side.offsetZ), side.getOpposite(), face);
    }

    public static int getInput(World world, BlockPos pos, ForgeDirection side, ForgeDirection face) {

        return getOutput(world, pos.add(side.offsetX, side.offsetY, side.offsetZ), side.getOpposite(), face);
    }

    @Deprecated
    public static int getInput(World world, int x, int y, int z) {

        return getInput(world, new BlockPos(x, y, z));
    }

    public static int getInput(World world, BlockPos pos) {

        int power = 0;
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS)
            power = Math.max(power, getInput(world, pos, side));
        return power;
    }

    @Deprecated
    public static boolean canConnect(World world, int x, int y, int z, ForgeDirection side, ForgeDirection face) {

        return canConnect(world, new BlockPos(x, y, z), side, face);
    }

    public static boolean canConnect(World world, BlockPos pos, ForgeDirection side, ForgeDirection face) {

        Vec3i location = new Vec3i(pos);

        if (isVanillaBlock(world, pos))
            return canConnectVanilla(world, pos, side, face);

        for (MultipartSystem s : MultipartSystem.getAvailableSystems()) {
            IMultipartCompat compat = s.getCompat();
            if (compat.isMultipart(world, location))
                return compat.canConnectRedstone(world, location, side, face);
        }

        try {
            return world.getBlock(pos.getX(), pos.getY(), pos.getZ()).canConnectRedstone(world, pos.getX(), pos.getY(), pos.getZ(), Direction.getMovementDirection(side.offsetX, side.offsetZ));
        } catch (Exception ex) {
            // ex.printStackTrace();
        }
        return false;
    }

    @Deprecated
    public static boolean canConnect(World world, int x, int y, int z, ForgeDirection side) {

        return canConnect(world, new BlockPos(x, y, z), side, ForgeDirection.UNKNOWN);
    }

    public static boolean canConnect(World world, BlockPos pos, ForgeDirection side) {

        return canConnect(world, pos, side, ForgeDirection.UNKNOWN);
    }

    @Deprecated
    public static void notifyRedstoneUpdate(World world, int x, int y, int z, ForgeDirection direction, boolean strong) {

        notifyRedstoneUpdate(world, new BlockPos(x, y, z), direction, strong);
    }

    public static void notifyRedstoneUpdate(World world, BlockPos pos, ForgeDirection direction, boolean strong) {

        int x_ = pos.getX() + direction.offsetX;
        int y_ = pos.getY() + direction.offsetY;
        int z_ = pos.getZ() + direction.offsetZ;

        if (world == null)
            return;

        Block block = world.getBlock(pos.getX(), pos.getY(), pos.getZ());

        // Weak/strong
        world.notifyBlockOfNeighborChange(x_, y_, z_, block);

        // Strong
        if (strong)
            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
                if (d != direction.getOpposite())
                    world.notifyBlockOfNeighborChange(x_ + d.offsetX, y_ + d.offsetY, z_ + d.offsetZ, block);
    }

}
