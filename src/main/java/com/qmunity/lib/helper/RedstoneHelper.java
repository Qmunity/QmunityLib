package com.qmunity.lib.helper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.qmunity.lib.part.compat.IMultipartCompat;
import com.qmunity.lib.part.compat.MultipartSystem;
import com.qmunity.lib.vec.Vec3i;

public class RedstoneHelper {

    public static int getRedstoneWireSignalStrength(World world, int x, int y, int z, ForgeDirection dir, ForgeDirection face) {

        if (face != ForgeDirection.DOWN)
            return 0;

        if (world.getBlock(x, y, z) == Blocks.redstone_wire) {
            if (dir == ForgeDirection.DOWN)
                return world.getBlockMetadata(x, y, z);
            int d = Direction.getMovementDirection(dir.offsetX, dir.offsetZ);
            if (BlockRedstoneWire.isPowerProviderOrWire(world, x, y, z, d)
                    || BlockRedstoneWire.isPowerProviderOrWire(world, x + dir.offsetX + dir.offsetX, y + dir.offsetY + dir.offsetY, z + dir.offsetZ
                            + dir.offsetZ, (d + 2) % 4)) {
                return world.getBlockMetadata(x, y, z);
            }
        }

        return 0;
    }

    public static int getBlockOutputStrong(World world, int x, int y, int z, ForgeDirection direction) {

        return world.isBlockProvidingPowerTo(x, y, z, direction.ordinal());
    }

    public static int getBlockOutputWeak(World world, int x, int y, int z, ForgeDirection direction) {

        int max = world.getBlock(x, y, z).isProvidingWeakPower(world, x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ,
                direction.getOpposite().ordinal());

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            if (d == direction)
                continue;

            int x_ = x + d.offsetX;
            int y_ = y + d.offsetY;
            int z_ = z + d.offsetZ;
            int p = 0;
            if (d != ForgeDirection.DOWN)
                p = getRedstoneWireSignalStrength(world, x_, y_, z_, d.getOpposite(), ForgeDirection.DOWN);
            if (p == 0)
                p = getBlockOutputStrong(world, x_, y_, z_, d);
            max = Math.max(max, p);
        }

        return max;
    }

    public static int getOutputStrong(World world, int x, int y, int z, ForgeDirection direction, ForgeDirection face) {

        int redstoneWire = getRedstoneWireSignalStrength(world, x, y, z, direction, face);
        if (redstoneWire > 0)
            return redstoneWire;

        int power = 0;
        boolean found = false;

        Vec3i location = new Vec3i(x, y, z);
        for (MultipartSystem s : MultipartSystem.getAvailableSystems()) {
            IMultipartCompat c = s.getCompat();
            if (found = c.isMultipart(world, location))
                power = Math.max(power, c.getStrongRedstoneOuput(world, location, direction, face));
        }

        if (found)
            return power;

        return getBlockOutputStrong(world, x, y, z, direction);
    }

    public static int getOutputStrong(World world, int x, int y, int z, ForgeDirection direction) {

        int max = 0;

        for (ForgeDirection f : ForgeDirection.VALID_DIRECTIONS)
            max = Math.max(max, getOutputStrong(world, x, y, z, direction, f));

        return max;
    }

    public static int getOutputWeak(World world, int x, int y, int z, ForgeDirection direction, ForgeDirection face) {

        int redstoneWire = getRedstoneWireSignalStrength(world, x, y, z, direction, face);
        if (redstoneWire > 0)
            return redstoneWire;

        int power = 0;
        boolean found = false;

        Vec3i location = new Vec3i(x, y, z);
        for (MultipartSystem s : MultipartSystem.getAvailableSystems()) {
            IMultipartCompat c = s.getCompat();
            if (found = c.isMultipart(world, location))
                power = Math.max(power, c.getWeakRedstoneOuput(world, location, direction, face));
        }

        if (found)
            return power;

        return getBlockOutputWeak(world, x, y, z, direction);
    }

    public static int getOutputWeak(World world, int x, int y, int z, ForgeDirection direction) {

        int max = 0;

        for (ForgeDirection f : ForgeDirection.VALID_DIRECTIONS)
            max = Math.max(max, getOutputWeak(world, x, y, z, direction, f));

        return max;
    }

    public static int getOutput(World world, int x, int y, int z, ForgeDirection direction, ForgeDirection face) {

        return Math.max(getOutputStrong(world, x, y, z, direction, face), getOutputWeak(world, x, y, z, direction, face));
    }

    public static int getOutput(World world, int x, int y, int z, ForgeDirection direction) {

        return Math.max(getOutputStrong(world, x, y, z, direction), getOutputWeak(world, x, y, z, direction));
    }

    public static int getInputStrong(World world, int x, int y, int z, ForgeDirection direction, ForgeDirection face) {

        return getOutputStrong(world, x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ, direction.getOpposite(), face);
    }

    public static int getInputStrong(World world, int x, int y, int z, ForgeDirection direction) {

        return getOutputStrong(world, x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ, direction.getOpposite());
    }

    public static int getInputWeak(World world, int x, int y, int z, ForgeDirection direction, ForgeDirection face) {

        return getInputWeak(world, x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ, direction.getOpposite(), face);
    }

    public static int getInputWeak(World world, int x, int y, int z, ForgeDirection direction) {

        return getInputWeak(world, x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ, direction.getOpposite());
    }

    public static int getInput(World world, int x, int y, int z, ForgeDirection direction, ForgeDirection face) {

        return getOutput(world, x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ, direction.getOpposite(), face);
    }

    public static int getInput(World world, int x, int y, int z, ForgeDirection direction) {

        return getOutput(world, x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ, direction.getOpposite());
    }

    public static void notifyRedstoneUpdate(World world, int x, int y, int z, ForgeDirection direction, boolean strong) {

        int x_ = x + direction.offsetX;
        int y_ = y + direction.offsetY;
        int z_ = z + direction.offsetZ;

        // Weak/strong
        world.notifyBlockOfNeighborChange(x_, y_, z_, world.getBlock(x, y, z));

        // Strong
        if (strong) {
            Block b = world.getBlock(x_, y_, z_);
            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
                if (d != direction.getOpposite())
                    world.notifyBlockOfNeighborChange(x_ + d.offsetX, y_ + d.offsetY, z_ + d.offsetZ, b);
        }
    }

}
