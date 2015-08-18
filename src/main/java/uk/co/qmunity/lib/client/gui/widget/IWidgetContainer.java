package uk.co.qmunity.lib.client.gui.widget;

import java.util.List;

public interface IWidgetContainer {
    
    public void addWidget(IGuiWidget widget);
    
    public List<IGuiWidget> getWidgets();
    
    public void setFocus(IGuiWidget widget);
    
    public IGuiWidget getFocused();
    
    public int getTop();
    
    public int getLeft();
    
    public int getWidth();
    
    public int getHeight();
    
}
