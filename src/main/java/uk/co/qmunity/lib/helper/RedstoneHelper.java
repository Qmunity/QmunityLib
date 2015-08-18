package uk.co.qmunity.lib.helper;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class RedstoneHelper {

    public static interface IQLRedstoneProvider {

        public boolean canProvideRedstoneFor(World world, int x, int y, int z);

        public boolean canConnectRedstone(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side);

        public int getWeakRedstoneOutput(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side);

        public int getStrongRedstoneOutput(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side);

    }

    private static List<IQLRedstoneProvider> providers = new ArrayList<IQLRedstoneProvider>();

    public static void registerProvider(IQLRedstoneProvider provider) {

        if (provider == null)
            throw new NullPointerException("Attempted to register a null redstone provider!");
        if (providers.contains(provider))
            throw new IllegalStateException("Attempted to register a redstone provider that was already registered!");

        providers.add(provider);
    }

    public static boolean canConnectRedstone(World world, int x, int y, int z, ForgeDirection side) {

        return canConnectRedstone(world, x, y, z, ForgeDirection.UNKNOWN, side);
    }

    public static boolean canConnectRedstone(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

        boolean provided = false;
        for (IQLRedstoneProvider provider : providers) {
            if (provider.canProvideRedstoneFor(world, x, y, z)) {
                if (provider.canConnectRedstone(world, x, y, z, face, side))
                    return true;
                provided = true;
            }
        }
        if (!provided)
            return world.getBlock(x, y, z).canConnectRedstone(world, x, y, z,
                    Direction.getMovementDirection(side.getOpposite().offsetX, side.getOpposite().offsetZ));
        return false;
    }

    public static int getWeakRedstoneOutput(World world, int x, int y, int z, ForgeDirection side) {

        return getWeakRedstoneOutput(world, x, y, z, ForgeDirection.UNKNOWN, side);
    }

    public static int getWeakRedstoneOutput(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

        int pow = 0;
        boolean provided = false;
        for (IQLRedstoneProvider provider : providers) {
            if (provider.canProvideRedstoneFor(world, x, y, z)) {
                pow = Math.max(pow, provider.getWeakRedstoneOutput(world, x, y, z, face, side));
                provided = true;
            }
        }
        if (!provided) {
            Block b = world.getBlock(x, y, z);
            pow = b.isProvidingWeakPower(world, x, y, z, side.ordinal() ^ 1);
            if (b.isBlockNormalCube())
                for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
                    pow = Math.max(
                            pow,
                            getStrongRedstoneOutput(world, x + d.offsetX, y + d.offsetY, z + d.offsetZ, ForgeDirection.UNKNOWN,
                                    d.getOpposite()));
        }
        return pow;
    }

    public static int getStrongRedstoneOutput(World world, int x, int y, int z, ForgeDirection side) {

        return getStrongRedstoneOutput(world, x, y, z, ForgeDirection.UNKNOWN, side);
    }

    public static int getStrongRedstoneOutput(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

        int pow = 0;
        boolean provided = false;
        for (IQLRedstoneProvider provider : providers) {
            if (provider.canProvideRedstoneFor(world, x, y, z)) {
                pow = Math.max(pow, provider.getStrongRedstoneOutput(world, x, y, z, face, side));
                provided = true;
            }
        }
        if (!provided)
            return world.getBlock(x, y, z).isProvidingWeakPower(world, x, y, z, side.ordinal() ^ 1);
        return pow;
    }

    public static int getWeakRedstoneInput(World world, int x, int y, int z) {

        int pow = 0;
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS)
            pow = Math.max(pow, getWeakRedstoneInput(world, x, y, z, side));
        return pow;
    }

    public static int getWeakRedstoneInput(World world, int x, int y, int z, ForgeDirection side) {

        return getWeakRedstoneOutput(world, x + side.offsetX, y + side.offsetY, z + side.offsetZ, side.getOpposite());
    }

    public static int getWeakRedstoneInput(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

        return getWeakRedstoneOutput(world, x + side.offsetX, y + side.offsetY, z + side.offsetZ, face, side.getOpposite());
    }

    public static int getStrongRedstoneInput(World world, int x, int y, int z) {

        int pow = 0;
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS)
            pow = Math.max(pow, getStrongRedstoneInput(world, x, y, z, side));
        return pow;
    }

    public static int getStrongRedstoneInput(World world, int x, int y, int z, ForgeDirection side) {

        return getStrongRedstoneOutput(world, x + side.offsetX, y + side.offsetY, z + side.offsetZ, side.getOpposite());
    }

    public static int getStrongRedstoneInput(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

        return getStrongRedstoneOutput(world, x + side.offsetX, y + side.offsetY, z + side.offsetZ, face, side.getOpposite());
    }

}
