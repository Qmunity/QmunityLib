package com.qmunity.lib.vec;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ImmutableVec3d extends Vec3d {

    private Block b;
    private int meta;
    private World w;
    private TileEntity te;

    protected ImmutableVec3d(Vec3d v) {

        super(v.x, v.y, v.z, v.getWorld());
        if (v instanceof ImmutableVec3d) {
            b = ((ImmutableVec3d) v).b;
            meta = ((ImmutableVec3d) v).meta;
            w = ((ImmutableVec3d) v).w;
            te = ((ImmutableVec3d) v).te;
        } else {
            b = v.getBlock();
            meta = v.getBlockMeta();
            w = v.getWorld();
            te = v.getTileEntity();
        }
    }

    @Override
    public Vec3d add(double x, double y, double z) {

        return this;
    }

    @Override
    public Vec3d add(ForgeDirection dir) {

        return this;
    }

    @Override
    public Vec3d add(Vec3d vec) {

        return this;
    }

    @Override
    public Vec3d div(double multiplier) {

        return this;
    }

    @Override
    public Vec3d div(double x, double y, double z) {

        return this;
    }

    @Override
    public Vec3d div(ForgeDirection direction) {

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
            if (b == null && checkAir && bl.isAir(w, (int) x, (int) y, (int) z))
                return true;

            return bl.getClass().isInstance(b);
        }
        return false;
    }

    @Override
    public Vec3d mul(double multiplier) {

        return this;
    }

    @Override
    public Vec3d mul(double x, double y, double z) {

        return this;
    }

    @Override
    public Vec3d mul(ForgeDirection direction) {

        return this;
    }

    @Override
    public Vec3d setWorld(World world) {

        return this;
    }

    @Override
    public Vec3d sub(double x, double y, double z) {

        return this;
    }

    @Override
    public Vec3d sub(ForgeDirection dir) {

        return this;
    }

    @Override
    public Vec3d sub(Vec3d vec) {

        return this;
    }

}
