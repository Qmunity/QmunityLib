package uk.co.qmunity.lib.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.QmunityLib;
import uk.co.qmunity.lib.client.gui.widget.BaseWidget;
import uk.co.qmunity.lib.client.gui.widget.IGuiWidget;
import uk.co.qmunity.lib.client.gui.widget.IWidgetListener;

/**
 * @author MineMaarten
 * @author K-4U
 * @author Amadornes
 */
public class GuiBase extends GuiScreen implements IWidgetListener {

    protected static final int COLOR_TEXT = 0xFFFFFF;
    private final List<IGuiWidget> widgets = new ArrayList<IGuiWidget>();
    private final ResourceLocation resLoc;
    protected int xSize = 176;
    protected int ySize = 166;
    protected String title = null;

    public GuiBase(ResourceLocation _resLoc, int xSize, int ySize) {

        resLoc = _resLoc;
        this.xSize = xSize;
        this.ySize = ySize;
    }

    public GuiBase(ResourceLocation _resLoc, int xSize, int ySize, String title) {

        this(_resLoc, xSize, ySize);

        this.title = title;
    }

    protected void addWidget(IGuiWidget widget) {

        widgets.add(widget);
        widget.setListener(this);
    }

    @Override
    public void setWorldAndResolution(Minecraft par1Minecraft, int par2, int par3) {

        widgets.clear();
        super.setWorldAndResolution(par1Minecraft, par2, par3);
    }

    public static void drawVerticalProgressBar(int xOffset, int yOffset, int h, int w, float value, float max, int color) {

        float perc = value / max;
        int height = (int) (h * perc);
        drawRect(xOffset, yOffset + h - height, xOffset + w, yOffset + h, color);
    }

    public void drawHorizontalAlignedString(int xOffset, int yOffset, int w, String text, boolean useShadow) {

        int stringWidth = fontRendererObj.getStringWidth(text);
        int newX = xOffset;
        if (stringWidth < w) {
            newX = w / 2 - stringWidth / 2 + xOffset;
        }

        fontRendererObj.drawString(text, newX, yOffset, COLOR_TEXT, useShadow);
    }

    public void drawString(int xOffset, int yOffset, String text, boolean useShadow) {

        fontRendererObj.drawString(text, xOffset, yOffset, COLOR_TEXT, useShadow);
    }

    @Override
    public void drawScreen(int x, int y, float partialTick) {

        super.drawScreen(x, y, partialTick);

        // Background
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.renderEngine.bindTexture(resLoc);

            int x_ = (width - xSize) / 2;
            int y_ = (height - ySize) / 2;

            drawTexturedModalRect(x_, y_, 0, 0, xSize, ySize);

            for (IGuiWidget widget : widgets)
                widget.render(x_, y_, partialTick);
        }

        // Foreground
        {
            if (title != null) {
                drawHorizontalAlignedString(((width - xSize) / 2) + 7, ((height - ySize) / 2) + 8, xSize - 14, I18n.format(title), true);
            }
        }

        List<String> tooltip = new ArrayList<String>();
        boolean shift = QmunityLib.proxy.isSneakingInGui();
        for (IGuiWidget widget : widgets) {
            if (widget.getBounds().contains(x, y))
                widget.addTooltip(x, y, tooltip, shift);
        }
        if (!tooltip.isEmpty()) {
            List<String> localizedTooltip = new ArrayList<String>();
            for (String line : tooltip) {
                String localizedLine = I18n.format(line);
                String[] lines = WordUtils.wrap(localizedLine, 50).split(System.getProperty("line.separator"));
                for (String locLine : lines) {
                    localizedTooltip.add(locLine);
                }
            }
            drawHoveringText(localizedTooltip, x, y, fontRendererObj);
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {

        super.mouseClicked(x, y, button);
        for (IGuiWidget widget : widgets) {
            if (widget.getBounds().contains(x, y) && (!(widget instanceof BaseWidget) || ((BaseWidget) widget).enabled))
                widget.onMouseClicked(x, y, button);
        }
    }

    @Override
    public void actionPerformed(IGuiWidget widget) {

    }

    public void redraw() {

        buttonList.clear();
        widgets.clear();
        initGui();
    }

    public IGuiWidget getWidget(int id) {

        for (IGuiWidget widget : widgets)
            if (widget.getID() == id)
                return widget;

        return null;
    }
}
