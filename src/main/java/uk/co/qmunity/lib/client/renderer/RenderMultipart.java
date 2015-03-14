package uk.co.qmunity.lib.client.renderer;

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
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.tile.TileMultipart;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3i;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMultipart extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {

    public static int pass = 0;
    public static final int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float delta) {

        TileMultipart te = (TileMultipart) tile;

        GL11.glPushMatrix();
        {
            GL11.glTranslated(x, y, z);
            for (IPart p : te.getParts()) {
                if (p.getParent() != null) {
                    GL11.glPushMatrix();

                    p.renderDynamic(new Vec3d(0, 0, 0), delta, pass);

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

        boolean rendered = false;

        RenderHelper.instance.fullReset();
        RenderHelper.instance.setRenderCoords(world, x, y, z);

        if (renderer.overrideBlockTexture != null) {
            MovingObjectPosition mop = Minecraft.getMinecraft().objectMouseOver;
            if (mop.blockX == x && mop.blockY == y && mop.blockZ == z && mop instanceof QMovingObjectPosition
                    && ((QMovingObjectPosition) mop).getPart() != null) {
                renderBreaking(world, x, y, z, renderer, ((QMovingObjectPosition) mop));
                rendered = true;
            }
            return false;
        }

        TileMultipart te = BlockMultipart.get(world, x, y, z);
        if (te != null) {
            for (IPart p : te.getParts()) {
                if (p.getParent() != null) {
                    if (p.shouldRenderOnPass(pass)) {
                        if (p.renderStatic(new Vec3i(x, y, z), RenderHelper.instance, renderer, pass))
                            rendered = true;
                        RenderHelper.instance.resetRenderedSides();
                        RenderHelper.instance.resetTextureRotations();
                        RenderHelper.instance.resetTransformations();
                        RenderHelper.instance.setColor(0xFFFFFF);
                    }
                }
            }
        }

        RenderHelper.instance.fullReset();

        return rendered;
    }

    public static void renderBreaking(IBlockAccess world, int x, int y, int z, RenderBlocks renderer, QMovingObjectPosition mop) {

        RenderHelper.instance.setOverrideTexture(renderer.overrideBlockTexture);
        mop.getPart().renderBreaking(new Vec3i(x, y, z), RenderHelper.instance, renderer, pass, mop);
        RenderHelper.instance.setOverrideTexture(null);
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
