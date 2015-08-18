package uk.co.qmunity.lib.client.render;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.model.LightMatrix;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.vec.BlockPos;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RenderHooks {

    public static void initRenderHooks() {

        MinecraftForge.EVENT_BUS.register(new RenderHooks());
    }

    private static final List<IQLPlacementRenderer> placementRenderers = new ArrayList<IQLPlacementRenderer>();

    public static int registerStaticRenderer(IQLStaticRenderer renderer) {

        int id = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new WrappedStaticRenderer(id, renderer));
        return id;
    }

    public static void registerItemRenderer(Item item, IQLItemRenderer renderer) {

        MinecraftForgeClient.registerItemRenderer(item, new WrappedItemRenderer(renderer));
    }

    public static void registerTileEntityRenderer(Class<? extends TileEntity> tile, TileEntitySpecialRenderer tesr) {

        ClientRegistry.bindTileEntitySpecialRenderer(tile, tesr);
    }

    public static void registerPlacementRenderer(IQLPlacementRenderer renderer) {

        placementRenderers.add(renderer);
    }

    // private int highlightShader = -1;
    // private int depthShader = -1;
    // private Framebuffer fb;
    // private ProjectionHelper projH = new ProjectionHelper();

    @SubscribeEvent
    public void onDrawHighlight(DrawBlockHighlightEvent event) {

        if (event.target == null || event.target.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK)
            return;

        // if (highlightShader != -1) {
        // ARBShaderObjects.glDeleteObjectARB(highlightShader);
        // highlightShader = -1;
        // }
        // if (depthShader != -1) {
        // ARBShaderObjects.glDeleteObjectARB(depthShader);
        // depthShader = -1;
        // }
        //
        // if (highlightShader == -1)
        // highlightShader = ShaderHelper.createProgram("/assets/" + QLModInfo.MODID + "/shaders/placement.vp", "/assets/"
        // + QLModInfo.MODID + "/shaders/placement.fp");
        // if (depthShader == -1)
        // depthShader = ShaderHelper.createProgram("/assets/" + QLModInfo.MODID + "/shaders/depth.vp", "/assets/" + QLModInfo.MODID
        // + "/shaders/depth.fp");
        //
        // projH.refreshMatrices();
        //
        // if (fb == null || fb.framebufferTextureWidth != Minecraft.getMinecraft().displayWidth
        // || fb.framebufferTextureHeight != Minecraft.getMinecraft().displayHeight)
        // fb = new Framebuffer(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, true);
        // fb.framebufferClear();
        // fb.bindFramebuffer(true);
        // {
        // GL11.glPushMatrix();
        // // projH.updateMatrices();
        // GL11.glDisable(GL11.GL_BLEND);
        // ShaderHelper.useShader(depthShader);
        // for (IQLPlacementRenderer renderer : placementRenderers) {
        // VertexConsumerTessellator.instance.overrideAlpha = -1.0F;
        // renderer.renderPlacement(event, VertexConsumerTessellator.instance.context, VertexConsumerTessellator.instance);
        // }
        // ShaderHelper.releaseShader();
        // GL11.glPopMatrix();
        // }
        // fb.unbindFramebuffer();
        // Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);
        //
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        // GL11.glDisable(GL11.GL_DEPTH_TEST);
        // ShaderHelper.useShader(highlightShader, new IShaderCallback() {
        //
        // @Override
        // public void call(int shader, boolean newFrame) {
        //
        // int tex0 = ARBShaderObjects.glGetUniformLocationARB(shader, "texture0");
        // ARBShaderObjects.glUniform1iARB(tex0, 0);
        //
        // int tex1 = ARBShaderObjects.glGetUniformLocationARB(shader, "texture1");
        // ARBShaderObjects.glUniform1iARB(tex1, 1);
        // }
        // });
        // GL13.glActiveTexture(GL13.GL_TEXTURE1);
        // fb.bindFramebufferTexture();
        // GL13.glActiveTexture(GL13.GL_TEXTURE0);
        for (IQLPlacementRenderer renderer : placementRenderers) {
            VertexConsumerTessellator.instance.overrideAlpha = 0.75F;
            renderer.renderPlacement(event, VertexConsumerTessellator.instance.context.set(true, true, true, true),
                    VertexConsumerTessellator.instance);
            VertexConsumerTessellator.instance.overrideAlpha = -1.0F;
        }
        // GL13.glActiveTexture(GL13.GL_TEXTURE1);
        // GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        // GL13.glActiveTexture(GL13.GL_TEXTURE0);
        // ShaderHelper.releaseShader();
        //
        // GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private static class WrappedStaticRenderer implements ISimpleBlockRenderingHandler {

        private final int id;
        private final IQLStaticRenderer renderer;

        public WrappedStaticRenderer(int id, IQLStaticRenderer renderer) {

            this.id = id;
            this.renderer = renderer;
        }

        @Override
        public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

        }

        @Override
        public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks rb) {

            RenderContext context = VertexConsumerTessellator.instance.context.set(true, true, true, true);

            if (rb.overrideBlockTexture != null) {
                MovingObjectPosition mop = Minecraft.getMinecraft().objectMouseOver;
                if (mop != null && mop instanceof QMovingObjectPosition && ((QMovingObjectPosition) mop).part != null) {
                    Tessellator.instance.addTranslation(x, y, z);
                    LightMatrix.instance.locate(world, x, y, z);
                    boolean rendered = renderer.renderBreaking(world, new BlockPos(x, y, z), context, VertexConsumerTessellator.instance,
                            rb.overrideBlockTexture);
                    Tessellator.instance.addTranslation(-x, -y, -z);
                    return rendered;
                }
                return false;
            }

            Tessellator.instance.addTranslation(x, y, z);
            LightMatrix.instance.locate(world, x, y, z);
            boolean rendered = renderer.renderStatic(world, new BlockPos(x, y, z), context, VertexConsumerTessellator.instance);
            Tessellator.instance.addTranslation(-x, -y, -z);

            return rendered;
        }

        @Override
        public boolean shouldRender3DInInventory(int modelId) {

            return false;
        }

        @Override
        public int getRenderId() {

            return id;
        }

    }

    private static class WrappedItemRenderer implements IItemRenderer {

        private final IQLItemRenderer renderer;

        public WrappedItemRenderer(IQLItemRenderer renderer) {

            this.renderer = renderer;
        }

        @Override
        public boolean handleRenderType(ItemStack item, ItemRenderType type) {

            return true;
        }

        @Override
        public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {

            return true;
        }

        @Override
        public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

            GL11.glPushMatrix();

            if (type == ItemRenderType.ENTITY) {
                GL11.glScaled(0.5, 0.5, 0.5);
                GL11.glTranslated(-0.5, -0.5, -0.5);
            } else if (type == ItemRenderType.INVENTORY) {
                GL11.glTranslated(0, -0.125, 0);
            }

            Tessellator.instance.startDrawingQuads();
            renderer.renderItem(item, type, VertexConsumerTessellator.instance.context.set(true, false, true, true),
                    VertexConsumerTessellator.instance);
            Tessellator.instance.draw();

            GL11.glPopMatrix();
        }

    }

}
