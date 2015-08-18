package uk.co.qmunity.lib.client.gui.widget.tab;

import java.util.List;

public abstract class SimpleTabContent<T extends SimpleTabContent<T>> implements ITabContent {

    protected IGuiWidgetTab parent;

    @Override
    public IGuiWidgetTab getParent() {

        return parent;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T setParent(IGuiWidgetTab parent) {

        this.parent = parent;

        return (T) this;
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {

    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int button) {

    }

    @Override
    public void onMouseMoved(int mouseX, int mouseY) {

    }

    @Override
    public void onMouseDragged(int mouseX, int mouseY, int button, long ticks) {

    }

    @Override
    public void onKeyTyped(int key, char c) {

    }

    @Override
    public void addTooltip(int mouseX, int mouseY, List<String> tip) {

    }

    @Override
    public void update() {

    }

    @Override
    public boolean canHaveFocus() {

        return false;
    }

}
