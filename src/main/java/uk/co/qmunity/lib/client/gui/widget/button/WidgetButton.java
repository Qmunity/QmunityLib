package uk.co.qmunity.lib.client.gui.widget.button;

import static uk.co.qmunity.lib.client.gui.GuiRenderingUtils.*;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.gui.GuiTexture;
import uk.co.qmunity.lib.client.gui.widget.IWidgetContainer;
import uk.co.qmunity.lib.client.gui.widget.SimpleWidget.LocatedWidget;
import uk.co.qmunity.lib.vec.Vector4;

public class WidgetButton extends LocatedWidget<WidgetButton> {

    public int width, height;
    public String text;
    public GuiTexture[] icons;

    public WidgetButton(IWidgetContainer gui, int id, int x, int y, int width, int height) {

        super(gui, id, x, y);

        this.width = width;
        this.height = height;
    }

    public WidgetButton(IWidgetContainer gui, int id, int x, int y, int width, int height, String text) {

        this(gui, id, x, y, width, height);

        this.text = text;
    }

    public WidgetButton(IWidgetContainer gui, int id, int x, int y, int width, int height, GuiTexture icon) {

        this(gui, id, x, y, width, height);

        this.icons = new GuiTexture[] { icon };
    }

    public WidgetButton(IWidgetContainer gui, int id, int x, int y, int width, int height, String text, GuiTexture icon) {

        this(gui, id, x, y, width, height, text);

        this.icons = new GuiTexture[] { icon };
    }

    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {

        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    @Override
    public void renderWidget(int mouseX, int mouseY, float partialTick) {

        bindDefaultTextureSheet();

        int texY = getTextureY(mouseX, mouseY);

        drawCustomSizedTexturedRect(0, 0, width, height, 0, texY, 200, 20, 3);

        drawButtonIcons();

        if (text != null)
            drawHorizontalAlignedString(0, 1 + (height - fontRenderer().FONT_HEIGHT) / 2, width, text, !enabled ? COLOR_TEXT_DISABLED
                    : (isMouseOver(mouseX + x, mouseY + y) ? COLOR_TEXT_HOVER : COLOR_TEXT), true);
    }

    protected int getTextureY(int mouseX, int mouseY) {

        return !enabled ? 0 : (isMouseOver(mouseX + x, mouseY + y) ? 60 : 20);
    }

    protected GuiTexture getIcon() {

        if (icons == null || icons.length != 1)
            return null;
        return icons[0];
    }

    protected void drawButtonIcons() {

        GuiTexture tex = getIcon();
        if (tex == null)
            return;

        IIcon icon = tex.getIcon();
        Vector4 c = Vector4.colorRGB(tex.getTint());

        GL11.glColor3d(c.x, c.y, c.z);
        bindTexture(tex.getTexture());
        drawTexturedRect((width - icon.getIconWidth()) / 2, (height - icon.getIconHeight()) / 2, icon);
        GL11.glColor3d(1, 1, 1);
    }

    @Override
    public void onWidgetClicked(int mouseX, int mouseY, int button) {

        if (button != 0 || !enabled)
            return;

        notifyListeners(new WidgetActionButton.Press(mouseX + x, mouseY + y, id));
        playSound();
    }

    protected void playSound() {

        mc().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
    }

}
