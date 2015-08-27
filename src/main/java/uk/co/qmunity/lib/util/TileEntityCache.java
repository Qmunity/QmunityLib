package uk.co.qmunity.lib.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityCache extends LocationCache<TileEntity> {

    public TileEntityCache(World world, int x, int y, int z) {

        super(world, x, y, z);
    }

    @Override
    protected TileEntity getNewValue(World world, int x, int y, int z, Object... extraArgs) {

        return world.getTileEntity(x, y, z);
    }

}
