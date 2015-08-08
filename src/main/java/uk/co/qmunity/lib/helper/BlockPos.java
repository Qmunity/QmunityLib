package uk.co.qmunity.lib.helper;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;

/**
 * Created by Quetzi on 23/06/15.
 */
public class BlockPos {

    private int x, y, z;

    public BlockPos(int x, int y, int z) {

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockPos(double x, double y, double z) {

        this(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z));
    }

    public BlockPos(ChunkCoordinates coords) {

        this(coords.posX, coords.posY, coords.posZ);
    }

    public BlockPos add(BlockPos pos, int x, int y, int z) {

        return new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
    }

    public BlockPos add(BlockPos pos, double x, double y, double z) {

        return new BlockPos((double)pos.getX() + x, (double)pos.getY() + y, (double)pos.getZ() + z);
    }

    public BlockPos add(int x, int y, int z) {

        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }


    public int getX() {

        return x;
    }

    public int getY() {

        return y;
    }

    public int getZ() {

        return z;
    }
}
