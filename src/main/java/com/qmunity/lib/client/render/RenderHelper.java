package com.qmunity.lib.client.render;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.qmunity.lib.misc.Map3D;
import com.qmunity.lib.vec.Vec3d;
import com.qmunity.lib.vec.Vec3dCube;
import com.qmunity.lib.vec.Vec3i;

public class RenderHelper {

    public static final RenderHelper instance = new RenderHelper();

    private IBlockAccess world = null;
    private Vec3i location = new Vec3i(0, 0, 0);

    private Vec3d rotationPoint = Vec3d.center;
    private Vec3d rotation = new Vec3d(0, 0, 0);

    private Vec3d normal = new Vec3d(0, 0, 0);

    private int[] rotations = new int[] { 0, 0, 0, 0, 0, 0 };

    private boolean[] sides = new boolean[] { true, true, true, true, true, true };

    private Map3D<Integer> lightmap = new Map3D<Integer>(3, 3, 3, 0);

    private boolean renderFromInside = false;

    private int color = 0xFFFFFF;

    private IIcon icon = null;
    private Block fakeBlock = new BlockStone() {

        @Override
        public IIcon getIcon(IBlockAccess w, int x, int y, int z, int side) {

            if (icon != null)
                return icon;

            return Blocks.stone.getIcon(0, 0);
        }

        @Override
        public boolean shouldSideBeRendered(IBlockAccess w, int x, int y, int z, int side) {

            return sides[side];
        }

        @Override
        public int colorMultiplier(IBlockAccess w, int x, int y, int z) {

            return color;
        }

    };
    private RenderBlocks rb = null;

    public void reset() {

        setRenderCoords(null, 0, 0, 0);
        setRotation(0, 0, 0, Vec3d.center);
        setNormal(0, 0, 0);
        resetTextureRotations();
        resetRenderedSides();
        lightmap = new Map3D<Integer>(3, 3, 3, 0);
        renderFromInside = false;
        color = 0xFFFFFF;
    }

    public void resetTextureRotations() {

        rotations = new int[] { 0, 0, 0, 0, 0, 0 };
    }

    public void resetRenderedSides() {

        sides = new boolean[] { true, true, true, true, true, true };
    }

    public void setRenderFromInside(boolean render) {

        renderFromInside = render;
    }

    private int getMixedBrightnessForBlock(int x, int y, int z) {

        if (world == null)
            return 0;

        return world.getBlock(location.getX() + x, location.getY() + y, location.getZ() + z).getMixedBrightnessForBlock(world,
                location.getX() + x, location.getY() + y, location.getZ() + z);
    }

    public void setRenderCoords(IBlockAccess world, int x, int y, int z) {

        this.world = world;
        location = new Vec3i(x, y, z);

        for (int x_ = -1; x_ <= 1; x_++) {
            for (int y_ = -1; y_ <= 1; y_++) {
                for (int z_ = -1; z_ <= 1; z_++) {
                    setLightValue(x_, y_, z_, getMixedBrightnessForBlock(x_, y_, z_));
                }
            }
        }

        if (rb == null)
            rb = new RenderBlocks();
        rb.blockAccess = world;
    }

    public void setRotation(double x, double y, double z, Vec3d rotationPoint) {

        rotation = new Vec3d(x, y, z);
        this.rotationPoint = rotationPoint;
    }

    public void setNormal(double x, double y, double z) {

        normal = new Vec3d(x, y, z);
    }

    public void setTextureRotation(ForgeDirection side, int times) {

        times %= 4;
        if (times < 0)
            times += 4;

        rotations[side.ordinal()] = times;
    }

    public void setTextureRotations(int down, int up, int west, int east, int north, int south) {

        setTextureRotation(ForgeDirection.DOWN, down);
        setTextureRotation(ForgeDirection.UP, up);
        setTextureRotation(ForgeDirection.WEST, west);
        setTextureRotation(ForgeDirection.EAST, east);
        setTextureRotation(ForgeDirection.NORTH, north);
        setTextureRotation(ForgeDirection.SOUTH, south);
    }

    public void setRenderSide(ForgeDirection side, boolean rendered) {

        sides[side.ordinal()] = rendered;
    }

    public void setRenderSides(boolean down, boolean up, boolean west, boolean east, boolean north, boolean south) {

        setRenderSide(ForgeDirection.DOWN, down);
        setRenderSide(ForgeDirection.UP, up);
        setRenderSide(ForgeDirection.WEST, west);
        setRenderSide(ForgeDirection.EAST, east);
        setRenderSide(ForgeDirection.NORTH, north);
        setRenderSide(ForgeDirection.SOUTH, south);
    }

    public void setColor(int color) {

        this.color = color;
    }

    private void setLightValue(int x, int y, int z, int value) {

        lightmap.set(x + 1, y + 1, z + 1, value);
    }

    @SuppressWarnings("unused")
    private int getLightValue(int x, int y, int z) {

        return lightmap.get(x + 1, y + 1, z + 1);
    }

    @SuppressWarnings("unused")
    private void addVertex(double x, double y, double z, double u, double v) {

        setTextureCoords(u, v);
        addVertex(x, y, z);
    }

    private void setTextureCoords(double u, double v) {

        Tessellator.instance.setTextureUV(u, v);
    }

    private void addVertex(double x, double y, double z) {

        Vec3d vertex = new Vec3d(x, y, z);
        vertex.sub(rotationPoint);
        vertex.rotate(rotation.getX(), rotation.getY(), rotation.getZ());
        vertex.add(rotationPoint);

        Vec3d normal = this.normal.clone();
        normal.sub(rotationPoint);
        normal.rotate(rotation.getX(), rotation.getY(), rotation.getZ());
        normal.add(rotationPoint);

        Tessellator.instance.setNormal((float) normal.getX(), (float) normal.getY(), (float) normal.getZ());
        Tessellator.instance.addVertex(vertex.getX(), vertex.getY(), vertex.getZ());
    }

    public void renderBox(Vec3dCube cube) {

        renderBox(cube, null);
    }

    public void renderBox(Vec3dCube cube, IIcon icon) {

        // Rotate cube
        Vec3d pA = cube.getMin().clone().sub(rotationPoint).rotate(rotation.getX(), rotation.getY(), rotation.getZ()).add(rotationPoint);
        Vec3d pB = cube.getMax().clone().sub(rotationPoint).rotate(rotation.getX(), rotation.getY(), rotation.getZ()).add(rotationPoint);
        cube = new Vec3dCube(pA, pB);

        rb.uvRotateBottom = rotations[ForgeDirection.DOWN.ordinal()];
        rb.uvRotateTop = rotations[ForgeDirection.UP.ordinal()];
        rb.uvRotateWest = rotations[ForgeDirection.WEST.ordinal()];
        rb.uvRotateEast = rotations[ForgeDirection.EAST.ordinal()];
        rb.uvRotateNorth = rotations[ForgeDirection.NORTH.ordinal()];
        rb.uvRotateSouth = rotations[ForgeDirection.SOUTH.ordinal()];

        rb.setRenderBounds(cube.getMinX(), cube.getMinY(), cube.getMinZ(), cube.getMaxX(), cube.getMaxY(), cube.getMaxZ());
        rb.setOverrideBlockTexture(null);
        this.icon = icon;
        rb.renderFromInside = renderFromInside;
        rb.renderStandardBlock(fakeBlock, location.getX(), location.getY(), location.getZ());
        rb.renderFromInside = false;
        rb.setRenderBounds(0, 0, 0, 1, 1, 1);
    }
}
