package uk.co.qmunity.lib.client.gui.widget.tab;

import static uk.co.qmunity.lib.client.gui.GuiRenderingUtils.*;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.gui.widget.SimpleWidget;
import uk.co.qmunity.lib.client.helper.InputHelper;
import uk.co.qmunity.lib.client.texture.CustomIcon;

public class TabContentText extends SimpleTabContent<TabContentText> {

    protected List<String> text = new ArrayList<String>();
    protected double scale = 0.5;

    protected List<String> displayedText = new ArrayList<String>();
    protected int displayHeight, realHeight;
    protected int lastW, lastH;

    protected int scroll = 0;
    private ScaledResolution sr;

    public TabContentText(List<String> text) {

        this.text = text;

        sr = new ScaledResolution(mc(), mc().displayWidth, mc().displayHeight);
        if (sr.getScaleFactor() >= 4)
            scale = 0.5;
        else if (sr.getScaleFactor() == 3)
            scale = 0.75;
        else
            scale = 1;
    }

    public TabContentText(List<String> text, double scale) {

        this.text = text;
        this.scale = scale;
    }

    protected void computeDisplayedList(int w, int h) {

        int th = computeDisplayedList_do(w, h);
        if (th * scale > h)
            computeDisplayedList_do(w - getParent().getMinWidth(), h);
    }

    private int computeDisplayedList_do(int w, int h) {

        displayedText.clear();

        int height = 0;
        int extraHeight = 0;
        for (String line : text) {
            if (line == null)
                continue;
            for (String lp : I18n.format(line).split("\n")) {
                if (fontRenderer().getStringWidth(lp) * scale < w) {
                    displayedText.add(lp);
                    height += fontRenderer().FONT_HEIGHT;
                } else {
                    if (((int) Math.floor(w / scale)) > 5) {
                        for (Object s : fontRenderer().listFormattedStringToWidth(lp, (int) Math.floor(w / scale))) {
                            displayedText.add((String) s);
                            height += fontRenderer().FONT_HEIGHT;
                        }
                    } else {
                        displayedText.add(fontRenderer().trimStringToWidth(lp, (int) Math.floor(w / scale)));
                        height += fontRenderer().FONT_HEIGHT;
                    }
                }
            }
            displayedText.add(null);
            height += 4;
            extraHeight += 4;
        }

        realHeight = height - extraHeight;
        return displayHeight = Math.max(0, height - 4);
    }

    @Override
    public void renderContent(int mouseX, int mouseY, float frame) {

        int w = getParent().getInnerWidth(frame), h = getParent().getInnerHeight(frame);
        if (w < 5 || h < fontRenderer().FONT_HEIGHT * scale)
            return;

        if (lastW != w || lastH != h) {
            computeDisplayedList(w, h);
            lastW = w;
            lastH = h;
        }

        GL11.glPushMatrix();
        {
            GL11.glColor4d(1, 1, 1, 1);
            GL11.glScaled(scale, scale, 1);
            int y = 0;
            int i = 0;
            for (String line : displayedText) {
                if (i < scroll) {
                    if (line != null)
                        i++;
                    continue;
                }
                if (line != null) {
                    if ((y + fontRenderer().FONT_HEIGHT) * scale > h)
                        break;
                    drawString(0, y, line, true);

                    y += fontRenderer().FONT_HEIGHT;
                } else {
                    if ((y + 4) * scale > h)
                        break;
                    y += 4;
                }
            }
        }
        GL11.glPopMatrix();

        if (h > 16 && displayHeight * scale > h) {
            bindTexture(SimpleWidget.WIDGET_TEXTURE_SHEET);

            int startUp = mouseX >= w - 14 + 4 && mouseX < w + 2 - 4 && mouseY >= -5 + 4 && mouseY < 11 - 4 ? 48 : 16;
            int startDown = mouseX >= w - 14 + 4 && mouseX < w + 2 - 4 && mouseY >= h - 14 + 4 && mouseY < h + 2 - 4 ? 32 : 0;

            GL11.glColor4d(1, 1, 1, 1);
            drawTexturedRect(w - 14, -5, new CustomIcon(startUp / 256F, 112 / 256F, (startUp + 16) / 256F, (112 + 16) / 256F, 16, 16));
            drawTexturedRect(w - 14, h - 14, new CustomIcon(startDown / 256F, 112 / 256F, (startDown + 16) / 256F, (112 + 16) / 256F, 16,
                    16));
        }
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {

        super.onMouseClicked(mouseX, mouseY, button);

        int w = lastW, h = lastH;
        if (w == 0 || h < fontRenderer().FONT_HEIGHT * scale)
            return;
        if (h < 16 || displayHeight * scale < h)
            return;

        if (mouseX >= w - 14 + 4 && mouseX < w + 2 - 4 && mouseY >= -5 + 4 && mouseY < 11 - 4)
            scrollDo(false);
        else if (mouseX >= w - 14 + 4 && mouseX < w + 2 - 4 && mouseY >= h - 14 + 4 && mouseY < h + 2 - 4)
            scrollDo(true);
    }

    @Override
    public void update() {

        super.update();

        if (sr == null)
            sr = new ScaledResolution(mc(), mc().displayWidth, mc().displayHeight);

        if (getParent().isMouseOver((Mouse.getX() / sr.getScaleFactor()) - getParent().getParent().getLeft(),
                ((mc().displayHeight - Mouse.getY()) / sr.getScaleFactor()) - getParent().getParent().getTop())) {
            int dWheel = InputHelper.getDWheel();
            if (dWheel < 0)
                scrollDo(true);
            else if (dWheel > 0)
                scrollDo(false);
        }
    }

    protected void scrollDo(boolean direction) {

        int w = lastW, h = lastH;
        if (w == 0 || h < fontRenderer().FONT_HEIGHT * scale)
            return;
        if (h < 16 || displayHeight * scale < h)
            return;

        if (direction) {
            int cnt = 0;
            int actuallyDisplayed = 0;
            int y = 0;
            for (String line : displayedText) {
                if (line != null) {
                    if ((y + fontRenderer().FONT_HEIGHT) * scale <= h) {
                        y += fontRenderer().FONT_HEIGHT;
                        actuallyDisplayed++;
                    }
                    cnt++;
                } else {
                    if ((y + 4) * scale > h)
                        break;
                    y += 4;
                }
            }
            scroll = Math.min(scroll + 1, cnt - actuallyDisplayed);
        } else {
            scroll = Math.max(0, scroll - 1);
        }
    }

}
