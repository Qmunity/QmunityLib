package uk.co.qmunity.lib.helper;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemHelper {

    @Deprecated
    public static void dropItem(World world, int x, int y, int z, ItemStack itemStack) {

        ItemHelper.dropItem(world, new BlockPos(x, y, z), itemStack);
    }

    public static void dropItem(World world, BlockPos pos, ItemStack itemStack) {

        if (world.isRemote)
            return;
        float dX = world.rand.nextFloat() * 0.8F + 0.1F;
        float dY = world.rand.nextFloat() * 0.8F + 0.1F;
        float dZ = world.rand.nextFloat() * 0.8F + 0.1F;

        EntityItem entityItem = new EntityItem(world, pos.getX() + dX, pos.getY() + dY, pos.getZ() + dZ, itemStack.copy());

        float factor = 0.05F;
        entityItem.motionX = world.rand.nextGaussian() * factor;
        entityItem.motionY = world.rand.nextGaussian() * factor + 0.2F;
        entityItem.motionZ = world.rand.nextGaussian() * factor;
        world.spawnEntityInWorld(entityItem);
        itemStack.stackSize = 0;
    }
}
