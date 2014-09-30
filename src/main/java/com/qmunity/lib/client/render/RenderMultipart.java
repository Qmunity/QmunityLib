package com.qmunity.lib.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import com.qmunity.lib.block.BlockMultipart;
import com.qmunity.lib.part.IPart;
import com.qmunity.lib.part.IPartRenderable;
import com.qmunity.lib.tile.TileMultipart;
import com.qmunity.lib.vec.Vec3d;
import com.qmunity.lib.vec.Vec3i;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderMultipart extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {

    public static int PASS = 0;
    public static final int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float delta) {

        TileMultipart te = (TileMultipart) tile;

        GL11.glPushMatrix();
        {
            GL11.glTranslated(x, y, z);
            for (IPart p : te.getParts()) {
                if (p.getParent() != null && p instanceof IPartRenderable) {
                    GL11.glPushMatrix();

                    if (((IPartRenderable) p).shouldRenderOnPass(PASS))
                        ((IPartRenderable) p).renderDynamic(new Vec3d(0, 0, 0), delta, PASS);

                    GL11.glPopMatrix();
                }
            }
        }
        GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

        TileMultipart te = BlockMultipart.get(world, x, y, z);
        if (te != null)
            for (IPart p : te.getParts())
                if (p.getParent() != null && p instanceof IPartRenderable)
                    if (((IPartRenderable) p).shouldRenderOnPass(PASS))
                        ((IPartRenderable) p).renderStatic(new Vec3i(0, 0, 0), renderer, PASS);

        return false;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {

        return false;
    }

    @Override
    public int getRenderId() {

        return RENDER_ID;
    }

}
