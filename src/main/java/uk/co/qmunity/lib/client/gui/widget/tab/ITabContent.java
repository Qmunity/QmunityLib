package uk.co.qmunity.lib.client.gui.widget.tab;

import java.util.List;

public interface ITabContent {

    public IGuiWidgetTab getParent();

    public ITabContent setParent(IGuiWidgetTab parent);

    public void renderContent(int mouseX, int mouseY, float frame);

    public void onMouseClicked(int mouseX, int mouseY, int button);

    public void onMouseReleased(int mouseX, int mouseY, int button);

    public void onMouseMoved(int mouseX, int mouseY);

    public void onMouseDragged(int mouseX, int mouseY, int button, long ticks);

    public void onKeyTyped(int key, char c);

    public void addTooltip(int mouseX, int mouseY, List<String> tip);

    public void update();

    public boolean canHaveFocus();

}
