package com.qmunity.lib.vec;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ImmutableVec3i extends Vec3i {

    private Block b;
    private int meta;
    private World w;
    private TileEntity te;

    protected ImmutableVec3i(Vec3i v) {

        super(v.x, v.y, v.z, v.getWorld());
        if (v instanceof ImmutableVec3i) {
            b = ((ImmutableVec3i) v).b;
            meta = ((ImmutableVec3i) v).meta;
            w = ((ImmutableVec3i) v).w;
            te = ((ImmutableVec3i) v).te;
        } else {
            b = v.getBlock();
            meta = v.getBlockMeta();
            w = v.getWorld();
            te = v.getTileEntity();
        }
    }

    @Override
    public Vec3i add(int x, int y, int z) {

        return this;
    }

    @Override
    public Vec3i add(ForgeDirection dir) {

        return this;
    }

    @Override
    public Vec3i add(Vec3i vec) {

        return this;
    }

    @Override
    public Vec3i divide(int multiplier) {

        return this;
    }

    @Override
    public Vec3i divide(int x, int y, int z) {

        return this;
    }

    @Override
    public Vec3i divide(ForgeDirection direction) {

        return this;
    }

    @Override
    public Block getBlock() {

        return b;
    }

    @Override
    public int getBlockMeta() {

        return meta;
    }

    @Override
    public TileEntity getTileEntity() {

        return te;
    }

    @Override
    public World getWorld() {

        return w;
    }

    @Override
    public boolean hasTileEntity() {

        return getTileEntity() != null;
    }

    @Override
    public boolean hasWorld() {

        return getWorld() != null;
    }

    @Override
    public boolean isBlock(Block b) {

        return super.isBlock(b);
    }

    @Override
    public boolean isBlock(Block b, boolean checkAir) {

        if (hasWorld()) {
            Block bl = this.b;

            if (b == null && bl == Blocks.air)
                return true;
            if (b == null && checkAir && bl.getMaterial() == Material.air)
                return true;
            if (b == null && checkAir && bl.isAir(w, x, y, z))
                return true;

            return bl.getClass().isInstance(b);
        }
        return false;
    }

    @Override
    public Vec3i multiply(int multiplier) {

        return this;
    }

    @Override
    public Vec3i multiply(int x, int y, int z) {

        return this;
    }

    @Override
    public Vec3i multiply(ForgeDirection direction) {

        return this;
    }

    @Override
    public Vec3i setWorld(World world) {

        return this;
    }

    @Override
    public Vec3i subtract(int x, int y, int z) {

        return this;
    }

    @Override
    public Vec3i subtract(ForgeDirection dir) {

        return this;
    }

    @Override
    public Vec3i subtract(Vec3i vec) {

        return this;
    }

}
