package uk.co.qmunity.lib.client.gui.widget;

import java.util.List;

public interface IGuiWidget {

    public int getID();

    public IWidgetContainer getParent();

    public boolean isMouseOver(int mouseX, int mouseY);

    public void render(int mouseX, int mouseY, float partialTick);

    public void onMouseClicked(int mouseX, int mouseY, int button);

    public void onMouseReleased(int mouseX, int mouseY, int button);

    public void onMouseMoved(int mouseX, int mouseY);

    public void onMouseDragged(int mouseX, int mouseY, int button, long ticks);

    public void onKeyTyped(int key, char c);

    public void addTooltip(int mouseX, int mouseY, List<String> tip);

    public void update();

    public IGuiWidget focus();

    public boolean hasFocus();

    public IGuiWidget addListener(IWidgetListener gui);
}
