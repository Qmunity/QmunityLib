package uk.co.qmunity.lib.client.gui.widget.tab;

import uk.co.qmunity.lib.client.gui.widget.IGuiWidget;

public interface IGuiWidgetTab extends IGuiWidget {

    public String getTitle();

    public int getSide();

    public int getIndex();

    public int getBackgroundColor();

    public ITabContent getContent();

    public void open();

    public void close();

    public boolean isOpen();

    public boolean isClosed();

    public boolean isOpening();

    public boolean isClosing();

    public int getWidth(float frame);

    public int getHeight(float frame);

    public int getInnerWidth(float frame);

    public int getInnerHeight(float frame);

    public int getMinWidth();

    public int getMinHeight();

    public int getMaxWidth();

    public int getMaxHeight();

}
