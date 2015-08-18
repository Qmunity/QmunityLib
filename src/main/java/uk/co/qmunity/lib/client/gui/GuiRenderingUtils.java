package uk.co.qmunity.lib.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

/**
 * Do a static import in your GUI/widget to have access to some rendering utilities
 *
 * @author Amadornes
 */
public class GuiRenderingUtils {

    public static final int COLOR_TEXT = 0xFFFFFF;
    public static final int COLOR_TEXT_HOVER = 0xFFFFA0;
    public static final int COLOR_TEXT_DISABLED = 0xA0A0A0;
    private static RenderItem customRenderItem;

    public static Minecraft mc() {

        return Minecraft.getMinecraft();
    }

    public static FontRenderer fontRenderer() {

        return mc().fontRenderer;
    }

    public static void bindTexture(ResourceLocation texture) {

        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
    }

    public static void drawVerticalProgressBar(int xOffset, int yOffset, int h, int w, double value, double max, int color) {

        double perc = value / max;
        drawRect(xOffset, yOffset + h - (int) (h * perc), xOffset + w, yOffset + h, color);
    }

    public static void drawHorizontalProgressBar(int xOffset, int yOffset, int h, int w, double value, double max, int color) {

        double perc = value / max;
        drawRect(xOffset, yOffset + h, xOffset + (int) (w * perc), yOffset + h, color);
    }

    public static void drawHorizontalAlignedString(int xOffset, int yOffset, int w, String text, boolean shadow) {

        drawHorizontalAlignedString(xOffset, yOffset, w, text, COLOR_TEXT, shadow);
    }

    public static void drawHorizontalAlignedString(int xOffset, int yOffset, int w, String text, int color, boolean shadow) {

        int stringWidth = fontRenderer().getStringWidth(text);
        int newX = xOffset;
        if (stringWidth < w)
            newX = w / 2 - stringWidth / 2 + xOffset;

        fontRenderer().drawString(text, newX, yOffset, color, shadow);
    }

    public static void drawString(int xOffset, int yOffset, String text, boolean shadow) {

        drawString(xOffset, yOffset, text, COLOR_TEXT, shadow);
    }

    public static void drawString(int xOffset, int yOffset, String text, int color, boolean shadow) {

        fontRenderer().drawString(text, xOffset, yOffset, color, shadow);
    }

    public static void drawRect(int x1, int y1, int x2, int y2, int color) {

        Gui.drawRect(x1, y1, x2, y2, color);
    }

    public static void drawTexturedRect(int xOffset, int yOffset, int width, int height, float u, float v, float texW, float texH) {

        Gui.func_146110_a(xOffset, yOffset, u, v, width, height, texW, texH);
    }

    public static void drawTexturedRect(int xOffset, int yOffset, int width, int height, float u, float v) {

        drawTexturedRect(xOffset, yOffset, width, height, u, v, 256, 256);
    }

    public static void drawTexturedRect(int xOffset, int yOffset, IIcon icon) {

        int iw = icon.getIconWidth();
        int ih = icon.getIconHeight();
        float tw = iw / (icon.getMaxU() - icon.getMinU());
        float th = ih / (icon.getMaxV() - icon.getMinV());
        drawTexturedRect(xOffset, yOffset, iw, ih, icon.getMinU() * tw, icon.getMinV() * th, tw, th);
    }

    public static void drawCustomSizedTexturedRect(int xOffset, int yOffset, int width, int height, float u, float v, float texWidth,
            float texHeight, int outlineSize) {

        // Corners
        Gui.func_146110_a(xOffset, yOffset, u, v, outlineSize, outlineSize, 256, 256);
        Gui.func_146110_a(xOffset, yOffset + height - outlineSize, u, v + texHeight - outlineSize, outlineSize, outlineSize, 256, 256);
        Gui.func_146110_a(xOffset + width - outlineSize, yOffset, u + texWidth - outlineSize, v, outlineSize, outlineSize, 256, 256);
        Gui.func_146110_a(xOffset + width - outlineSize, yOffset + height - outlineSize, u + texWidth - outlineSize, v + texHeight
                - outlineSize, outlineSize, outlineSize, 256, 256);

        // Borders and inside
        for (int x = outlineSize; x < width - outlineSize; x += texWidth - outlineSize * 2) {
            // Top and bottom borders
            Gui.func_146110_a(xOffset + x, yOffset, u + outlineSize, v,
                    (int) Math.min(width - outlineSize - x, texWidth - outlineSize * 2), outlineSize, 256, 256);
            Gui.func_146110_a(xOffset + x, yOffset + height - outlineSize, u + outlineSize, v + texHeight - outlineSize,
                    (int) Math.min(width - outlineSize - x, texWidth - outlineSize * 2), outlineSize, 256, 256);

            for (int y = outlineSize; y < height - outlineSize; y += texHeight - outlineSize * 2) {
                // Left and right borders, only if x == outlineSize, to prevent over-drawing
                if (x == outlineSize) {
                    Gui.func_146110_a(xOffset, yOffset + y, u, v + outlineSize, outlineSize,
                            (int) Math.min(height - outlineSize - y, texHeight - outlineSize * 2), 256, 256);
                    Gui.func_146110_a(xOffset + width - outlineSize, yOffset + y, u + texWidth - outlineSize, v + outlineSize, outlineSize,
                            (int) Math.min(height - outlineSize - y, texHeight - outlineSize * 2), 256, 256);
                }

                // Inside
                Gui.func_146110_a(xOffset + x, yOffset + y, u + outlineSize, v + outlineSize,
                        (int) Math.min(width - outlineSize - x, texWidth - outlineSize * 2),
                        (int) Math.min(height - outlineSize - y, texHeight - outlineSize * 2), 256, 256);
            }
        }
    }

    public static void drawItem(int xOffset, int yOffset, ItemStack stack) {

        if (customRenderItem == null) {
            customRenderItem = new RenderItem() {

                @Override
                public boolean shouldSpreadItems() {

                    return false;
                }
            };
            customRenderItem.setRenderManager(RenderManager.instance);
        }

        customRenderItem.renderItemIntoGUI(fontRenderer(), mc().renderEngine, stack, xOffset, yOffset);
    }
}
