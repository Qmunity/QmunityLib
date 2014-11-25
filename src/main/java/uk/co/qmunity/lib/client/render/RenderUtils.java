package uk.co.qmunity.lib.client.render;

import java.nio.DoubleBuffer;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import uk.co.qmunity.lib.vec.Vec3dCube;

public class RenderUtils {

    /**
     * @author K-4U
     *
     */
    public static final void vertexWithTexture(double x, double y, double z, float tx, float ty) {

        GL11.glTexCoord2f(tx, ty);
        GL11.glVertex3d(x, y, z);
    }

    /**
     * @author K-4U
     *
     */
    public static void drawTexturedCube(Vec3dCube cube) {

        // Top side:
        GL11.glNormal3d(0, 1, 0);
        vertexWithTexture(cube.getMinX(), cube.getMaxY(), cube.getMaxZ(), 0.0F, 0.0F);
        vertexWithTexture(cube.getMaxX(), cube.getMaxY(), cube.getMaxZ(), 1.0F, 0.0F);
        vertexWithTexture(cube.getMaxX(), cube.getMaxY(), cube.getMinZ(), 1.0F, 1.0F);
        vertexWithTexture(cube.getMinX(), cube.getMaxY(), cube.getMinZ(), 0.0F, 1.0F);

        // Bottom side:
        GL11.glNormal3d(0, -1, 0);
        vertexWithTexture(cube.getMaxX(), cube.getMinY(), cube.getMaxZ(), 0.0F, 0.0F);
        vertexWithTexture(cube.getMinX(), cube.getMinY(), cube.getMaxZ(), 1.0F, 0.0F);
        vertexWithTexture(cube.getMinX(), cube.getMinY(), cube.getMinZ(), 1.0F, 1.0F);
        vertexWithTexture(cube.getMaxX(), cube.getMinY(), cube.getMinZ(), 0.0F, 1.0F);

        // Draw west side:
        GL11.glNormal3d(-1, 0, 0);
        vertexWithTexture(cube.getMinX(), cube.getMinY(), cube.getMaxZ(), 1.0F, 0.0F);
        vertexWithTexture(cube.getMinX(), cube.getMaxY(), cube.getMaxZ(), 1.0F, 1.0F);
        vertexWithTexture(cube.getMinX(), cube.getMaxY(), cube.getMinZ(), 0.0F, 1.0F);
        vertexWithTexture(cube.getMinX(), cube.getMinY(), cube.getMinZ(), 0.0F, 0.0F);

        // Draw east side:
        GL11.glNormal3d(1, 0, 0);
        vertexWithTexture(cube.getMaxX(), cube.getMinY(), cube.getMinZ(), 1.0F, 0.0F);
        vertexWithTexture(cube.getMaxX(), cube.getMaxY(), cube.getMinZ(), 1.0F, 1.0F);
        vertexWithTexture(cube.getMaxX(), cube.getMaxY(), cube.getMaxZ(), 0.0F, 1.0F);
        vertexWithTexture(cube.getMaxX(), cube.getMinY(), cube.getMaxZ(), 0.0F, 0.0F);

        // Draw north side
        GL11.glNormal3d(0, 0, -1);
        vertexWithTexture(cube.getMinX(), cube.getMinY(), cube.getMinZ(), 1.0F, 0.0F);
        vertexWithTexture(cube.getMinX(), cube.getMaxY(), cube.getMinZ(), 1.0F, 1.0F);
        vertexWithTexture(cube.getMaxX(), cube.getMaxY(), cube.getMinZ(), 0.0F, 1.0F);
        vertexWithTexture(cube.getMaxX(), cube.getMinY(), cube.getMinZ(), 0.0F, 0.0F);

        // Draw south side
        GL11.glNormal3d(0, 0, 1);
        vertexWithTexture(cube.getMinX(), cube.getMinY(), cube.getMaxZ(), 0.0F, 0.0F);
        vertexWithTexture(cube.getMaxX(), cube.getMinY(), cube.getMaxZ(), 1.0F, 0.0F);
        vertexWithTexture(cube.getMaxX(), cube.getMaxY(), cube.getMaxZ(), 1.0F, 1.0F);
        vertexWithTexture(cube.getMinX(), cube.getMaxY(), cube.getMaxZ(), 0.0F, 1.0F);
    }

    /**
     * @author amadornes
     *
     */
    public static void drawTexturedCube(Vec3dCube cube, float minU, float minV, float maxU, float maxV) {

        // Top side:
        GL11.glNormal3d(0, 1, 0);
        vertexWithTexture(cube.getMinX(), cube.getMaxY(), cube.getMaxZ(), minU, minV);
        vertexWithTexture(cube.getMaxX(), cube.getMaxY(), cube.getMaxZ(), maxU, minV);
        vertexWithTexture(cube.getMaxX(), cube.getMaxY(), cube.getMinZ(), maxU, maxV);
        vertexWithTexture(cube.getMinX(), cube.getMaxY(), cube.getMinZ(), minU, maxV);

        // Bottom side:
        GL11.glNormal3d(0, -1, 0);
        vertexWithTexture(cube.getMaxX(), cube.getMinY(), cube.getMaxZ(), minU, minV);
        vertexWithTexture(cube.getMinX(), cube.getMinY(), cube.getMaxZ(), maxU, minV);
        vertexWithTexture(cube.getMinX(), cube.getMinY(), cube.getMinZ(), maxU, maxV);
        vertexWithTexture(cube.getMaxX(), cube.getMinY(), cube.getMinZ(), minU, maxV);

        // Draw west side:
        GL11.glNormal3d(-1, 0, 0);
        vertexWithTexture(cube.getMinX(), cube.getMinY(), cube.getMaxZ(), maxU, minV);
        vertexWithTexture(cube.getMinX(), cube.getMaxY(), cube.getMaxZ(), maxU, maxV);
        vertexWithTexture(cube.getMinX(), cube.getMaxY(), cube.getMinZ(), minU, maxV);
        vertexWithTexture(cube.getMinX(), cube.getMinY(), cube.getMinZ(), minU, minV);

        // Draw east side:
        GL11.glNormal3d(1, 0, 0);
        vertexWithTexture(cube.getMaxX(), cube.getMinY(), cube.getMinZ(), maxU, minV);
        vertexWithTexture(cube.getMaxX(), cube.getMaxY(), cube.getMinZ(), maxU, maxV);
        vertexWithTexture(cube.getMaxX(), cube.getMaxY(), cube.getMaxZ(), minU, maxV);
        vertexWithTexture(cube.getMaxX(), cube.getMinY(), cube.getMaxZ(), minU, minV);

        // Draw north side
        GL11.glNormal3d(0, 0, -1);
        vertexWithTexture(cube.getMinX(), cube.getMinY(), cube.getMinZ(), maxU, minV);
        vertexWithTexture(cube.getMinX(), cube.getMaxY(), cube.getMinZ(), maxU, maxV);
        vertexWithTexture(cube.getMaxX(), cube.getMaxY(), cube.getMinZ(), minU, maxV);
        vertexWithTexture(cube.getMaxX(), cube.getMinY(), cube.getMinZ(), minU, minV);

        // Draw south side
        GL11.glNormal3d(0, 0, 1);
        vertexWithTexture(cube.getMinX(), cube.getMinY(), cube.getMaxZ(), minU, minV);
        vertexWithTexture(cube.getMaxX(), cube.getMinY(), cube.getMaxZ(), maxU, minV);
        vertexWithTexture(cube.getMaxX(), cube.getMaxY(), cube.getMaxZ(), maxU, maxV);
        vertexWithTexture(cube.getMinX(), cube.getMaxY(), cube.getMaxZ(), minU, maxV);
    }

    public static DoubleBuffer planeEquation(double x, double y, double z) {

        return fromArray(new double[] { x, y, z, 0 });
    }

    public static DoubleBuffer fromArray(double... eq) {

        DoubleBuffer b = BufferUtils.createDoubleBuffer(eq.length * 2).put(eq);
        b.flip();
        return b;
    }

    public static final void drawTexturedRect(double x, double y, double u, double v, double width, double height) {

        GL11.glPushMatrix();
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glTexCoord2d(0, 1);
            GL11.glVertex3d(x, y + height, 0);
            GL11.glTexCoord2d(1, 1);
            GL11.glVertex3d(x + width, y + height, 0);
            GL11.glTexCoord2d(1, 0);
            GL11.glVertex3d(x + width, y, 0);
            GL11.glTexCoord2d(0, 0);
            GL11.glVertex3d(x, y, 0);
        }
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public static final void drawColoredRect(double x, double y, double width, double height, int color) {

        float red = (color >> 16 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float alpha = (color >> 24 & 255) / 255.0F;

        GL11.glPushMatrix();

        boolean isTextureEnabled = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glColor4f(red, green, blue, alpha);

        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glVertex3d(x, y + height, 0);
            GL11.glVertex3d(x + width, y + height, 0);
            GL11.glVertex3d(x + width, y, 0);
            GL11.glVertex3d(x, y, 0);
        }
        GL11.glEnd();

        GL11.glColor4f(1, 1, 1, 1);

        if (isTextureEnabled)
            GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glPopMatrix();
    }

    private static float zLevel = 0;

    public static final void drawHoveringText(List<String> text, int x, int y, FontRenderer font) {

        if (!text.isEmpty()) {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int k = 0;
            Iterator<String> iterator = text.iterator();

            while (iterator.hasNext()) {
                String s = iterator.next();
                int l = font.getStringWidth(s);

                if (l > k) {
                    k = l;
                }
            }

            int j2 = x + 12;
            int k2 = y - 12;
            int i1 = 8;

            if (text.size() > 1) {
                i1 += 2 + (text.size() - 1) * 10;
            }

            zLevel = 300.0F;
            int j1 = -267386864;
            drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
            drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
            drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
            drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
            drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
            int k1 = 1347420415;
            int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
            drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
            drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
            drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
            drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

            for (int i2 = 0; i2 < text.size(); ++i2) {
                String s1 = text.get(i2);
                font.drawStringWithShadow(s1, j2, k2, -1);

                if (i2 == 0) {
                    k2 += 2;
                }

                k2 += 10;
            }

            zLevel = 0.0F;
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }

    public static final void drawGradientRect(int x1, int y1, int x2, int y2, int c1, int c2) {

        float f = (c1 >> 24 & 255) / 255.0F;
        float f1 = (c1 >> 16 & 255) / 255.0F;
        float f2 = (c1 >> 8 & 255) / 255.0F;
        float f3 = (c1 & 255) / 255.0F;
        float f4 = (c2 >> 24 & 255) / 255.0F;
        float f5 = (c2 >> 16 & 255) / 255.0F;
        float f6 = (c2 >> 8 & 255) / 255.0F;
        float f7 = (c2 & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        tessellator.addVertex(x2, y1, zLevel);
        tessellator.addVertex(x1, y1, zLevel);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex(x1, y2, zLevel);
        tessellator.addVertex(x2, y2, zLevel);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private static RenderItem customRenderItem;

    public static void renderItem(ItemStack item) {

        if (customRenderItem == null) {
            customRenderItem = new RenderItem() {

                @Override
                public boolean shouldSpreadItems() {

                    return false;
                }
            };
            customRenderItem.setRenderManager(RenderManager.instance);
        }
        EntityItem ghostEntityItem = new EntityItem(Minecraft.getMinecraft().theWorld);
        ghostEntityItem.hoverStart = 0.0F;
        ghostEntityItem.setEntityItemStack(item);
        GL11.glColor3d(1, 1, 1);
        if (item.getItem() instanceof ItemBlock) {
            ItemBlock testItem = (ItemBlock) item.getItem();
            Block testBlock = testItem.field_150939_a;
            if (RenderBlocks.renderItemIn3d(testBlock.getRenderType())) {
                GL11.glScaled(1.2, 1.2, 1.2);
            }
        }
        customRenderItem.doRender(ghostEntityItem, 0, 0, 0, 0, 0);
    }
}
