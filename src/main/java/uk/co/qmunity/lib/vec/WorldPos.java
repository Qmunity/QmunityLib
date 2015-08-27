package uk.co.qmunity.lib.vec;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class WorldPos extends BlockPos implements IWorldLocation {

    public World world;

    public WorldPos(World world, int x, int y, int z) {

        super(x, y, z);
        this.world = world;
    }

    public WorldPos(World world, Vector3 v) {

        super(v);
        this.world = world;
    }

    public WorldPos(World world, BlockPos v) {

        super(v);
        this.world = world;
    }

    public WorldPos(WorldPos coord) {

        this(coord.world, coord);
    }

    public WorldPos(TileEntity te) {

        this(te.getWorldObj(), te.xCoord, te.yCoord, te.zCoord);
    }

    @Override
    public World getWorld() {

        return world;
    }

    @Override
    public WorldPos copy() {

        return new WorldPos(this);
    }

    public Block getBlock() {

        return world.getBlock(x, y, z);
    }

    public int getMetadata() {

        return world.getBlockMetadata(x, y, z);
    }

    public TileEntity getTileEntity() {

        return world.getTileEntity(x, y, z);
    }

    @Override
    public WorldPos offset(ForgeDirection side) {

        super.offset(side, 1);
        return this;
    }

    @Override
    public WorldPos offset(ForgeDirection side, int amount) {

        super.offset(side, amount);
        return this;
    }

}
