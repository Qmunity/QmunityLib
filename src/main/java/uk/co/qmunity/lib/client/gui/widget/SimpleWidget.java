package uk.co.qmunity.lib.client.gui.widget;

import static uk.co.qmunity.lib.client.gui.GuiRenderingUtils.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.QLModInfo;
import uk.co.qmunity.lib.client.gui.widget.IWidgetAction.WidgetActionClick;

@SuppressWarnings("unchecked")
public abstract class SimpleWidget<T extends SimpleWidget<T>> implements IGuiWidget {

    public static final ResourceLocation WIDGET_TEXTURE_SHEET = new ResourceLocation(QLModInfo.MODID + ":textures/gui/widgets.png");

    public final IWidgetContainer gui;
    public final int id;

    protected Set<IWidgetListener> listeners = new HashSet<IWidgetListener>();
    protected List<String> tip = null;

    public boolean enabled = true;

    public SimpleWidget(IWidgetContainer gui, int id) {

        this.gui = gui;
        this.id = id;
    }

    @Override
    public int getID() {

        return id;
    }

    @Override
    public IWidgetContainer getParent() {

        return gui;
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {

        IWidgetAction action = new WidgetActionClick(mouseX, mouseY, button);
        for (IWidgetListener listener : listeners)
            listener.actionPerformed(this, action);
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

        if (this.tip != null)
            tip.addAll(this.tip);
    }

    @Override
    public void update() {

    }

    @Override
    public T focus() {

        gui.setFocus(this);
        return (T) this;
    }

    @Override
    public boolean hasFocus() {

        return gui.getFocused() == this;
    }

    @Override
    public T addListener(IWidgetListener gui) {

        listeners.add(gui);
        return (T) this;
    }

    public T setEnabled(boolean enabled) {

        this.enabled = enabled;
        return (T) this;
    }

    public T addTooltipLine(String tip) {

        return addTooltipLines(tip);
    }

    public T addTooltipLines(String... tip) {

        if (this.tip == null)
            this.tip = new ArrayList<String>();
        for (String t : tip)
            this.tip.add(t);

        return (T) this;
    }

    protected void bindDefaultTextureSheet() {

        bindTexture(WIDGET_TEXTURE_SHEET);
    }

    protected boolean notifyListeners(IWidgetAction action) {

        for (IWidgetListener listener : listeners) {
            listener.actionPerformed(this, action);
            if (action.isCanceled())
                return true;
        }
        return false;
    }

    public static abstract class LocatedWidget<T extends LocatedWidget<T>> extends SimpleWidget<T> {

        public int x, y;

        public LocatedWidget(IWidgetContainer gui, int id, int x, int y) {

            super(gui, id);

            this.x = x;
            this.y = y;
        }

        @Override
        public final void render(int mouseX, int mouseY, float partialTick) {

            GL11.glPushMatrix();
            GL11.glTranslated(x, y, 0);
            renderWidget(mouseX - x, mouseY - y, partialTick);
            GL11.glPopMatrix();
        }

        @Override
        public final void onMouseClicked(int mouseX, int mouseY, int button) {

            onWidgetClicked(mouseX - x, mouseY - y, button);
        }

        @Override
        public final void onMouseReleased(int mouseX, int mouseY, int button) {

            onWidgetMouseReleased(mouseX - x, mouseY - y, button);
        }

        @Override
        public final void onMouseMoved(int mouseX, int mouseY) {

            onWidgetMouseMoved(mouseX - x, mouseY - y);
        }

        @Override
        public final void onMouseDragged(int mouseX, int mouseY, int button, long ticks) {

            onWidgetMouseDragged(mouseX - x, mouseY - y, button, ticks);
        }

        @Override
        public final void addTooltip(int mouseX, int mouseY, List<String> tip) {

            addWidgetTooltip(mouseX - x, mouseY - y, tip);
        }

        public abstract void renderWidget(int mouseX, int mouseY, float partialTick);

        public void onWidgetClicked(int mouseX, int mouseY, int button) {

            IWidgetAction action = new WidgetActionClick(mouseX + x, mouseY + y, button);
            for (IWidgetListener listener : listeners)
                listener.actionPerformed(this, action);
        }

        public void onWidgetMouseReleased(int mouseX, int mouseY, int button) {

        }

        public void onWidgetMouseMoved(int mouseX, int mouseY) {

        }

        public void onWidgetMouseDragged(int mouseX, int mouseY, int button, long ticks) {

        }

        public void addWidgetTooltip(int mouseX, int mouseY, List<String> tip) {

            if (this.tip != null)
                tip.addAll(this.tip);
        }

    }

}
