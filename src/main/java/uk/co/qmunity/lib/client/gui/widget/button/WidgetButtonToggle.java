package uk.co.qmunity.lib.client.gui.widget.button;

import uk.co.qmunity.lib.client.gui.GuiTexture;
import uk.co.qmunity.lib.client.gui.widget.IWidgetContainer;

public class WidgetButtonToggle extends WidgetButton {

    public boolean state = false;

    public WidgetButtonToggle(IWidgetContainer gui, int id, int x, int y, int width, int height, String text) {

        super(gui, id, x, y, width, height, text);
    }

    public WidgetButtonToggle(IWidgetContainer gui, int id, int x, int y, int width, int height) {

        super(gui, id, x, y, width, height);
    }

    public WidgetButtonToggle(IWidgetContainer gui, int id, int x, int y, int width, int height, String text, GuiTexture off, GuiTexture on) {

        this(gui, id, x, y, width, height, text);

        this.icons = new GuiTexture[] { off, on };
    }

    public WidgetButtonToggle(IWidgetContainer gui, int id, int x, int y, int width, int height, GuiTexture off, GuiTexture on) {

        this(gui, id, x, y, width, height);

        this.icons = new GuiTexture[] { off, on };
    }

    @Override
    protected int getTextureY(int mouseX, int mouseY) {

        return !enabled ? 0 : ((isMouseOver(mouseX + x, mouseY + y) ? 60 : 20) + (state ? 20 : 0));
    }

    @Override
    protected GuiTexture getIcon() {

        if (icons == null || icons.length != 2)
            return null;
        return icons[state ? 1 : 0];
    }

    @Override
    public void onWidgetClicked(int mouseX, int mouseY, int button) {

        if (button != 0 || !enabled)
            return;

        if (notifyListeners(new WidgetActionButton.Toggle(mouseX + x, mouseY + y, !state)))
            return;

        state = !state;
        playSound();
    }

}
