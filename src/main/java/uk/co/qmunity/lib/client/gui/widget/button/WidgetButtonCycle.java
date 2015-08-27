package uk.co.qmunity.lib.client.gui.widget.button;

import uk.co.qmunity.lib.client.gui.GuiTexture;
import uk.co.qmunity.lib.client.gui.widget.IWidgetContainer;

public class WidgetButtonCycle extends WidgetButton {

    public int modes, mode;

    public WidgetButtonCycle(IWidgetContainer gui, int id, int x, int y, int width, int height, int modes) {

        super(gui, id, x, y, width, height);

        this.modes = modes;
    }

    public WidgetButtonCycle(IWidgetContainer gui, int id, int x, int y, int width, int height, int modes, String text) {

        super(gui, id, x, y, width, height, text);

        this.modes = modes;
    }

    public WidgetButtonCycle(IWidgetContainer gui, int id, int x, int y, int width, int height, int modes, GuiTexture... icons) {

        this(gui, id, x, y, width, height, modes);

        this.icons = icons;
    }

    public WidgetButtonCycle(IWidgetContainer gui, int id, int x, int y, int width, int height, int modes, String text, GuiTexture... icons) {

        this(gui, id, x, y, width, height, modes, text);

        this.icons = icons;
    }

    @Override
    protected int getTextureY(int mouseX, int mouseY) {

        return super.getTextureY(mouseX, mouseY);
    }

    @Override
    protected GuiTexture getIcon() {

        if (icons == null || icons.length != modes)
            return null;

        return icons[mode];
    }

    @Override
    public void onWidgetClicked(int mouseX, int mouseY, int button) {

        if ((button != 0 && button != 1) || !enabled)
            return;

        int old = mode;

        if (button == 0)
            mode++;
        else if (button == 1)
            mode--;

        if (mode >= modes)
            mode = 0;
        else if (mode < 0)
            mode = modes - 1;

        if (notifyListeners(new WidgetActionButton.Cycle(mouseX + x, mouseY + y, id, old, mode))) {
            mode = old;
            return;
        }

        playSound();
    }
}
