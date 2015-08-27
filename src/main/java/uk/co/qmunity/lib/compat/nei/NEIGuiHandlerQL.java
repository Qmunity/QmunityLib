package uk.co.qmunity.lib.compat.nei;

import java.awt.Rectangle;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import uk.co.qmunity.lib.client.gui.widget.IGuiWidget;
import uk.co.qmunity.lib.client.gui.widget.IWidgetContainer;
import uk.co.qmunity.lib.client.gui.widget.tab.WidgetTab;
import uk.co.qmunity.lib.inventory.slot.ISlotPhantom;
import codechicken.nei.VisiblityData;
import codechicken.nei.api.INEIGuiHandler;
import codechicken.nei.api.TaggedInventoryArea;

public class NEIGuiHandlerQL implements INEIGuiHandler {

    @Override
    public VisiblityData modifyVisiblity(GuiContainer gc, VisiblityData currentVisibility) {

        if (!(gc instanceof IWidgetContainer))
            return currentVisibility;
        IWidgetContainer gui = (IWidgetContainer) gc;

        for (IGuiWidget widget : gui.getWidgets()) {
            if (widget instanceof WidgetTab) {
                WidgetTab tab = (WidgetTab) widget;
                if (tab.getSide() == 0) {
                } else if (tab.getSide() == 1) {
                    if (tab.isOpening() || tab.isOpen()) {
                        currentVisibility.showStateButtons = false;
                        currentVisibility.showUtilityButtons = false;
                    }
                } else if (tab.getSide() == 2) {
                    currentVisibility.showSearchSection = false;
                } else if (tab.getSide() == 3) {
                    currentVisibility.showSearchSection = false;
                }
            }
        }
        return currentVisibility;
    }

    @Override
    public Iterable<Integer> getItemSpawnSlots(GuiContainer gc, ItemStack item) {

        return null;
    }

    @Override
    public List<TaggedInventoryArea> getInventoryAreas(GuiContainer gc) {

        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean handleDragNDrop(GuiContainer gc, int mousex, int mousey, ItemStack draggedStack, int button) {

        if (!(gc instanceof IWidgetContainer))
            return false;
        IWidgetContainer gui = (IWidgetContainer) gc;

        for (Slot s : (List<Slot>) gc.inventorySlots.inventorySlots) {
            if (mousex >= s.xDisplayPosition + gui.getLeft() && mousex < s.xDisplayPosition + gui.getLeft() + 16
                    && mousey >= s.yDisplayPosition + gui.getTop() && mousey < s.yDisplayPosition + gui.getTop() + 16
                    && s instanceof ISlotPhantom) {
                if (s.isItemValid(draggedStack)) {
                    ItemStack inSlot = s.getStack();
                    ItemStack stack = draggedStack.copy();

                    if (button == 1) {
                        if (inSlot != null && inSlot.isItemEqual(draggedStack) && ItemStack.areItemStackTagsEqual(draggedStack, inSlot))
                            stack.stackSize = Math.min(inSlot.stackSize + 1,
                                    Math.min(inSlot.getMaxStackSize(), s.inventory.getInventoryStackLimit()));
                        else
                            stack.stackSize = 1;
                    } else if (button == 2) {
                        if (inSlot != null)
                            stack.stackSize = inSlot.stackSize;
                        else
                            stack.stackSize = 1;
                    }

                    s.putStack(stack);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean hideItemPanelSlot(GuiContainer gc, int x, int y, int w, int h) {

        if (!(gc instanceof IWidgetContainer))
            return false;
        IWidgetContainer gui = (IWidgetContainer) gc;

        int top = gui.getTop(), left = gui.getLeft(), width = gui.getWidth(), height = gui.getHeight();

        if (new Rectangle(left, top, width, height).intersects(x, y, w, h))
            return true;

        for (IGuiWidget widget : gui.getWidgets())
            if (widget.isMouseOver(x - left, y - top) || widget.isMouseOver(x - left, y - top + h)
                    || widget.isMouseOver(x - left + w, y - top) || widget.isMouseOver(x - left + w, y - top + h))
                return true;

        return false;
    }
}
