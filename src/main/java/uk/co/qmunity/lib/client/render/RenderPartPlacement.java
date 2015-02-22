package uk.co.qmunity.lib.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.item.ItemMultipart;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartRenderPlacement;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3i;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPartPlacement {

    private Framebuffer fb = null;
    private int width = 0, height = 0;

    @SubscribeEvent
    public void onRenderTick(RenderWorldLastEvent event) {

        World world = Minecraft.getMinecraft().theWorld;
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        ItemStack item = player.getCurrentEquippedItem();
        if (item == null)
            return;
        if (!(item.getItem() instanceof ItemMultipart))
            return;
        if (Minecraft.getMinecraft().gameSettings.hideGUI && Minecraft.getMinecraft().currentScreen == null)
            return;

        MovingObjectPosition mop = player.rayTrace(player.capabilities.isCreativeMode ? 5 : 4, 0);
        if (mop == null || mop.typeOfHit != MovingObjectType.BLOCK)
            return;

        IPart part = ((ItemMultipart) item.getItem()).createPart(item, player, world, mop);

        if (part == null)
            return;
        if (!(part instanceof IPartRenderPlacement))
            return;

        ForgeDirection faceHit = ForgeDirection.getOrientation(mop.sideHit);
        Vec3i location = new Vec3i(mop.blockX, mop.blockY, mop.blockZ);

        if (!MultipartCompatibility.placePartInWorld(part, world, location, faceHit, player, item, true))
            return;

        if (fb == null || width != Minecraft.getMinecraft().displayWidth || height != Minecraft.getMinecraft().displayHeight) {
            width = Minecraft.getMinecraft().displayWidth;
            height = Minecraft.getMinecraft().displayHeight;
            fb = new Framebuffer(width, height, true);
        }

        GL11.glPushMatrix();
        {

            Minecraft.getMinecraft().getFramebuffer().unbindFramebuffer();
            GL11.glPushMatrix();
            {
                GL11.glLoadIdentity();
                fb.bindFramebuffer(true);

                GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glLoadIdentity();
                GL11.glClearColor(0, 0, 0, 0);

                net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();

                GL11.glPushMatrix();
                {
                    Vec3 playerPos = player.getPosition(event.partialTicks);
                    double x = part.getX() - playerPos.xCoord;
                    double y = part.getY() - playerPos.yCoord;
                    double z = part.getZ() - playerPos.zCoord;

                    GL11.glRotated(player.rotationPitch, 1, 0, 0);
                    GL11.glRotated(player.rotationYaw - 180, 0, 1, 0);

                    GL11.glTranslated(x, y, z);

                    Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
                    Tessellator.instance.addTranslation(-part.getX(), -part.getY(), -part.getZ());
                    Tessellator.instance.startDrawingQuads();
                    RenderHelper.instance.setRenderCoords(world, part.getX(), part.getY(), part.getZ());
                    RenderBlocks.getInstance().blockAccess = world;

                    if (part.shouldRenderOnPass(0))
                        part.renderStatic(new Vec3i(part.getX(), part.getY(), part.getZ()), RenderHelper.instance,
                                RenderBlocks.getInstance(), 0);
                    if (part.shouldRenderOnPass(1))
                        part.renderStatic(new Vec3i(part.getX(), part.getY(), part.getZ()), RenderHelper.instance,
                                RenderBlocks.getInstance(), 1);

                    RenderBlocks.getInstance().blockAccess = null;
                    RenderHelper.instance.reset();
                    Tessellator.instance.draw();
                    Tessellator.instance.addTranslation(part.getX(), part.getY(), part.getZ());

                    if (part.shouldRenderOnPass(0))
                        part.renderDynamic(new Vec3d(0, 0, 0), event.partialTicks, 0);
                    if (part.shouldRenderOnPass(1))
                        part.renderDynamic(new Vec3d(0, 0, 0), event.partialTicks, 1);
                }
                GL11.glPopMatrix();

                net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

                fb.unbindFramebuffer();
            }
            GL11.glPopMatrix();

            Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);

            GL11.glPushMatrix();
            {
                Minecraft mc = Minecraft.getMinecraft();

                ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
                GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
                GL11.glMatrixMode(GL11.GL_PROJECTION);
                GL11.glLoadIdentity();
                GL11.glOrtho(0, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0, 0.1, 10000D);
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glLoadIdentity();
                GL11.glTranslatef(0.0F, 0.0F, -2000.0F);

                fb.bindFramebufferTexture();
                {
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                    Tessellator tessellator = Tessellator.instance;

                    int w = scaledresolution.getScaledWidth();
                    int h = scaledresolution.getScaledHeight();

                    tessellator.startDrawingQuads();
                    tessellator.setColorRGBA_F(1, 1, 1, 0.5F);
                    tessellator.addVertexWithUV(w, h, 0.0D, 1.0D, 0.0D);
                    tessellator.addVertexWithUV(w, 0, 0.0D, 1.0D, 1.0D);
                    tessellator.addVertexWithUV(0, 0, 0.0D, 0.0D, 1.0D);
                    tessellator.addVertexWithUV(0, h, 0.0D, 0.0D, 0.0D);
                    tessellator.draw();

                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glEnable(GL11.GL_LIGHTING);
                }
                fb.unbindFramebufferTexture();

                GL11.glDisable(GL11.GL_BLEND);
            }
            GL11.glPopMatrix();

            fb.framebufferClear();

            Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);
        }
        GL11.glPopMatrix();
    }
}
