package uk.co.qmunity.lib.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.block.BlockMultipart;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartRenderable;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.tile.TileMultipart;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3i;
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

                    ((IPartRenderable) p).renderDynamic(new Vec3d(0, 0, 0), delta, PASS);

                    GL11.glPopMatrix();
                }
            }
        }
        GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

        renderer.setRenderBounds(0, 0, 0, 0, 0, 0);
        renderer.renderStandardBlock(Blocks.stone, x, y, z);
        renderer.setRenderBounds(0, 0, 0, 1, 1, 1);

        RenderHelper.instance.setRenderCoords(world, x, y, z);

        if (renderer.overrideBlockTexture != null) {
            MovingObjectPosition mop = Minecraft.getMinecraft().objectMouseOver;
            if (mop.blockX == x && mop.blockY == y && mop.blockZ == z && mop instanceof QMovingObjectPosition
                    && ((QMovingObjectPosition) mop).getPart() != null)
                renderBreaking(world, x, y, z, renderer, ((QMovingObjectPosition) mop));
            return false;
        }

        TileMultipart te = BlockMultipart.get(world, x, y, z);
        if (te != null)
            for (IPart p : te.getParts())
                if (p.getParent() != null && p instanceof IPartRenderable)
                    if (((IPartRenderable) p).shouldRenderOnPass(PASS))
                        ((IPartRenderable) p).renderStatic(new Vec3i(x, y, z), RenderHelper.instance, renderer, PASS);

        RenderHelper.instance.fullReset();

        return true;
    }

    public static void renderBreaking(IBlockAccess world, int x, int y, int z, RenderBlocks renderer, QMovingObjectPosition mop) {

        if (mop.getPart() instanceof IPartRenderable) {
            RenderHelper.instance.setOverrideTexture(renderer.overrideBlockTexture);
            ((IPartRenderable) mop.getPart()).renderBreaking(new Vec3i(x, y, z), RenderHelper.instance, renderer, PASS, mop);
            RenderHelper.instance.setOverrideTexture(null);
        }
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
