package uk.co.qmunity.lib.client.gui.widget.tab;

import static uk.co.qmunity.lib.client.gui.GuiRenderingUtils.*;

import java.awt.Color;
import java.util.List;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import uk.co.qmunity.lib.client.gui.GuiTexture;
import uk.co.qmunity.lib.client.gui.widget.IGuiWidget;
import uk.co.qmunity.lib.client.gui.widget.IWidgetContainer;
import uk.co.qmunity.lib.client.gui.widget.SimpleWidget;
import uk.co.qmunity.lib.vec.Vector4;

public class WidgetTab extends SimpleWidget<WidgetTab> implements IGuiWidgetTab {

    public String title;
    public int side, index;
    public int bgColor = 0xFFFFFF;
    public ITabContent content;
    public double speed = 0.2;

    public int minWidth = 24, minHeight = 24, maxWidth = 100, maxHeight = 100;

    public GuiTexture icon;
    public ItemStack itemIcon;

    protected boolean open, opening, closing;
    protected double animationProgress = 1;

    public WidgetTab(IWidgetContainer gui, int id, String title, int side, int index, int bgColor, ITabContent content) {

        super(gui, id);

        this.title = title;
        this.side = side;
        this.index = index;
        this.bgColor = bgColor;
        this.content = content;

        if (content != null)
            content.setParent(this);

        if (side == 0 || side == 1)
            minWidth -= 3;
        else if (side == 2 || side == 3)
            minHeight -= 3;
    }

    public WidgetTab(IWidgetContainer gui, int id, String title, int side, int index, int bgColor, ITabContent content, GuiTexture icon) {

        this(gui, id, title, side, index, bgColor, content);

        this.icon = icon;
    }

    public WidgetTab(IWidgetContainer gui, int id, String title, int side, int index, int bgColor, ITabContent content, ItemStack icon) {

        this(gui, id, title, side, index, bgColor, content);

        this.itemIcon = icon;
    }

    public WidgetTab setSpeed(double speed) {

        this.speed = speed;

        return this;
    }

    public WidgetTab setMinSize(int width, int height) {

        this.minWidth = width;
        this.minHeight = height;

        return this;
    }

    public WidgetTab setMaxSize(int width, int height) {

        this.maxWidth = width;
        this.maxHeight = height;

        return this;
    }

    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {

        int x = getXOffset(0, true), y = getYOffset(0, true), width = getWidth(0), height = getHeight(0);

        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTick) {

        drawBackground(mouseX, mouseY, partialTick);
        drawIcon(mouseX, mouseY, partialTick);
        drawTitle(mouseX, mouseY, partialTick);
        drawContent(mouseX, mouseY, partialTick);
    }

    protected void drawBackground(int mouseX, int mouseY, float partialTick) {

        bindDefaultTextureSheet();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        int side = getSide(), x = getXOffset(partialTick, true), y = getYOffset(partialTick, true);

        Vector4 c = Vector4.colorRGB(bgColor);
        GL11.glColor3d(c.x, c.y, c.z);
        drawCustomSizedTexturedRect(x, y, getWidth(partialTick), getHeight(partialTick), 80 + 16 * side, 112, 16, 16, 4);
        GL11.glColor3d(1, 1, 1);
    }

    protected void drawIcon(int mouseX, int mouseY, float partialTick) {

        int x = getXOffset(0, false), y = getYOffset(0, false), width = minWidth, height = minHeight;

        if (side == 0)
            width -= 2;
        else if (side == 1)
            x += 2;
        else if (side == 2)
            height -= 2;
        else if (side == 3)
            y += 2;

        if (icon != null) {
            Color c = new Color(icon.getTint());
            GL11.glColor3d(c.getRed() / 256D, c.getGreen() / 256D, c.getBlue() / 256D);
            bindTexture(icon.getTexture());
            drawTexturedRect(x + (width - icon.getIcon().getIconWidth()) / 2, y + (height - icon.getIcon().getIconHeight()) / 2,
                    icon.getIcon());
            GL11.glColor4f(1, 1, 1, 1);
        } else if (itemIcon != null) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glPushMatrix();
            {
                GL11.glColor4f(1, 1, 1, 1);
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);

                drawItem(x + (width - 16) / 2, y + (height - 16) / 2, itemIcon);
            }
            GL11.glPopMatrix();
            GL11.glEnable(GL11.GL_LIGHTING);
            RenderHelper.disableStandardItemLighting();
            GL11.glColor4f(1, 1, 1, 1);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }
    }

    protected void drawTitle(int mouseX, int mouseY, float partialTick) {

        String title = getTitle();
        if (title != null && !isClosed()) {
            int x = getXOffset(0, true), y = getYOffset(0, false), width = minWidth, height = minHeight;
            String wrappedTitle = fontRenderer().trimStringToWidth(title,
                    getWidth(partialTick) - minWidth - (side == 0 || side == 1 ? 9 : 12));
            drawString(x + (side != 1 ? width : 6), y + (height - fontRenderer().FONT_HEIGHT) / 2 + (side == 2 ? -1 : 2), wrappedTitle,
                    true);
        }
    }

    protected void drawContent(int mouseX, int mouseY, float partialTick) {

        if (content == null)
            return;
        GL11.glPushMatrix();
        {
            int x = getXOffset(partialTick, true) + (side != 0 ? 6 : 3);
            int y = getYOffset(partialTick, true) + (side != 3 ? minHeight : 6);
            GL11.glTranslated(x, y, 0);
            content.renderContent(mouseX - x, mouseY - y, partialTick);
        }
        GL11.glPopMatrix();
    }

    @Override
    public void update() {

        if (isOpening() || isClosing()) {
            animationProgress += speed;
            if (animationProgress >= 1) {
                if (isOpening()) {
                    closing = false;
                    opening = false;
                    open = true;
                } else {
                    closing = false;
                    opening = false;
                    open = false;
                }
            }
        }

        if (content != null)
            content.update();
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {

        int x = getXOffset(0, false), y = getYOffset(0, false);

        if (mouseX >= x && mouseX < x + minWidth && mouseY >= y && mouseY < y + minHeight) {
            if (isOpen() || isOpening()) {
                close();
            } else if (isClosed() || isClosing()) {
                open();

                for (IGuiWidget w : gui.getWidgets()) {
                    if (w instanceof IGuiWidgetTab && ((IGuiWidgetTab) w).getSide() == getSide()
                            && ((IGuiWidgetTab) w).getIndex() != getIndex()) {
                        IGuiWidgetTab tab = (IGuiWidgetTab) w;
                        if (tab != null && (tab.isOpen() || tab.isOpening()))
                            tab.close();
                    }
                }
            }
            return;
        }
        if (content != null) {
            x = getXOffset(0, true) + (side != 0 ? 6 : 3);
            y = getYOffset(0, true) + (side != 3 ? minHeight : 6);
            int cWidth = getInnerWidth(0), cHeight = getInnerHeight(0);
            if (mouseX >= x && mouseX < x + cWidth && mouseY >= y && mouseY < y + cHeight) {
                if (content.canHaveFocus())
                    focus();
                content.onMouseClicked(mouseX - x, mouseY - y, button);
            }
        }
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int button) {

        super.onMouseReleased(mouseX, mouseY, button);

        if (content != null) {
            int x = getXOffset(0, true) + (side != 0 ? 6 : 3);
            int y = getYOffset(0, true) + (side != 3 ? minHeight : 6);
            int cWidth = getInnerWidth(0), cHeight = getInnerHeight(0);
            if (mouseX >= x && mouseX < x + cWidth && mouseY >= y && mouseY < y + cHeight)
                content.onMouseReleased(mouseX - x, mouseY - y, button);
        }
    }

    @Override
    public void onMouseMoved(int mouseX, int mouseY) {

        super.onMouseMoved(mouseX, mouseY);

        if (content != null) {
            int x = getXOffset(0, true) + (side != 0 ? 6 : 3);
            int y = getYOffset(0, true) + (side != 3 ? minHeight : 6);
            int cWidth = getInnerWidth(0), cHeight = getInnerHeight(0);
            if (mouseX >= x && mouseX < x + cWidth && mouseY >= y && mouseY < y + cHeight)
                content.onMouseMoved(mouseX - x, mouseY - y);
        }
    }

    @Override
    public void onMouseDragged(int mouseX, int mouseY, int button, long ticks) {

        super.onMouseDragged(mouseX, mouseY, button, ticks);

        if (content != null) {
            int x = getXOffset(0, true) + (side != 0 ? 6 : 3);
            int y = getYOffset(0, true) + (side != 3 ? minHeight : 6);
            int cWidth = getInnerWidth(0), cHeight = getInnerHeight(0);
            if (mouseX >= x && mouseX < x + cWidth && mouseY >= y && mouseY < y + cHeight)
                content.onMouseDragged(mouseX - x, mouseY - y, button, ticks);
        }
    }

    @Override
    public void onKeyTyped(int key, char c) {

        super.onKeyTyped(key, c);

        if (content != null)
            content.onKeyTyped(key, c);
    }

    @Override
    public void addTooltip(int mouseX, int mouseY, List<String> tip) {

        int x = getXOffset(0, false), y = getYOffset(0, false);

        if (mouseX >= x && mouseX < x + minWidth && mouseY >= y && mouseY < y + minHeight)
            super.addTooltip(mouseX, mouseY, tip);
        if (content != null) {
            x = getXOffset(0, true) + (side != 0 ? 6 : 3);
            y = getYOffset(0, true) + (side != 3 ? minHeight : 6);
            int cWidth = getInnerWidth(0), cHeight = getInnerHeight(0);
            if (mouseX >= x && mouseX < x + cWidth && mouseY >= y && mouseY < y + cHeight)
                content.addTooltip(mouseX - x, mouseY - y, tip);
        }
    }

    @Override
    public String getTitle() {

        return title;
    }

    @Override
    public int getSide() {

        return side;
    }

    @Override
    public int getIndex() {

        return index;
    }

    @Override
    public int getBackgroundColor() {

        return bgColor;
    }

    @Override
    public ITabContent getContent() {

        return content;
    }

    @Override
    public void open() {

        if (isOpen())
            return;
        if (content == null)
            return;

        if (!opening) {
            animationProgress = 1 - animationProgress;
            closing = false;
        }
        opening = true;
        open = false;
    }

    @Override
    public void close() {

        if (isClosed())
            return;

        if (!closing) {
            animationProgress = 1 - animationProgress;
            opening = false;
        }
        closing = true;
        open = false;
    }

    @Override
    public boolean isOpen() {

        return open;
    }

    @Override
    public boolean isClosed() {

        return !isOpen() && !isOpening() && !isClosing();
    }

    @Override
    public boolean isOpening() {

        return opening;
    }

    @Override
    public boolean isClosing() {

        return closing;
    }

    @Override
    public int getWidth(float frame) {

        return minWidth
                + ((int) ((isOpening() ? Math.min(animationProgress + speed * frame, 1) : (isClosing() ? 1 - Math.min(animationProgress
                        + speed * frame, 1) : (isOpen() ? 1 : 0))) * (maxWidth - minWidth)));
    }

    @Override
    public int getHeight(float frame) {

        return minHeight
                + ((int) ((isOpening() ? Math.min(animationProgress + speed * frame, 1) : (isClosing() ? 1 - Math.min(animationProgress
                        + speed * frame, 1) : (isOpen() ? 1 : 0))) * (maxHeight - minHeight)));
    }

    @Override
    public int getInnerWidth(float frame) {

        return Math.max(0, getWidth(frame) - (side == 0 || side == 1 ? 9 : 12));
    }

    @Override
    public int getInnerHeight(float frame) {

        return Math.max(0, getHeight(frame) - minHeight - (side == 2 || side == 3 ? 6 : 6));
    }

    protected int getXOffset(float frame, boolean sizeAffected) {

        int side = getSide();

        if (side == 0 || side == 1)
            return side == 0 ? gui.getWidth() : (sizeAffected ? -getWidth(frame) : -minWidth);

        int x = 5;

        for (int i = 0; i < index; i++) {
            IGuiWidgetTab tab = null;
            for (IGuiWidget w : gui.getWidgets()) {
                if (w instanceof IGuiWidgetTab && ((IGuiWidgetTab) w).getSide() == side && ((IGuiWidgetTab) w).getIndex() == i) {
                    tab = (IGuiWidgetTab) w;
                    break;
                }
            }
            if (tab != null)
                x += tab.getWidth(frame) + 3;
        }

        return x;
    }

    protected int getYOffset(float frame, boolean sizeAffected) {

        int side = getSide();

        if (side == 2 || side == 3)
            return side == 2 ? gui.getHeight() : (sizeAffected ? -getHeight(frame) : -minHeight);

        int y = 5;

        for (int i = 0; i < index; i++) {
            IGuiWidgetTab tab = null;
            for (IGuiWidget w : gui.getWidgets()) {
                if (w instanceof IGuiWidgetTab && ((IGuiWidgetTab) w).getSide() == side && ((IGuiWidgetTab) w).getIndex() == i) {
                    tab = (IGuiWidgetTab) w;
                    break;
                }
            }
            if (tab != null)
                y += tab.getHeight(frame) + 3;
        }

        return y;
    }

    @Override
    public int getMinWidth() {

        return minWidth;
    }

    @Override
    public int getMinHeight() {

        return minHeight;
    }

    @Override
    public int getMaxWidth() {

        return maxWidth;
    }

    @Override
    public int getMaxHeight() {

        return maxHeight;
    }

}
