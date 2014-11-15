package com.qmunity.lib.part;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.qmunity.lib.helper.ItemHelper;
import com.qmunity.lib.raytrace.QMovingObjectPosition;

public abstract class PartBase implements IPart {

    private ITilePartHolder parent;

    @Override
    public World getWorld() {

        return getParent().getWorld();
    }

    @Override
    public int getX() {

        return getParent().getX();
    }

    @Override
    public int getY() {

        return getParent().getY();
    }

    @Override
    public int getZ() {

        return getParent().getZ();
    }

    @Override
    public ITilePartHolder getParent() {

        return parent;
    }

    @Override
    public void setParent(ITilePartHolder parent) {

        this.parent = parent;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

    }

    @Override
    public void writeUpdateToNBT(NBTTagCompound tag) {

    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {

    }

    @Override
    public void sendUpdatePacket() {

        // FIXME PartUpdateManager.sendPartUpdate(this);
    }

    @Override
    public ItemStack getPickedItem(QMovingObjectPosition mop) {

        return getItem();
    }

    @Override
    public List<ItemStack> getDrops() {

        List<ItemStack> items = new ArrayList<ItemStack>();

        ItemStack is = getItem();
        if (is != null) {
            is.stackSize = 1;
            items.add(is);
        }

        return items;
    }

    @Override
    public void breakAndDrop(boolean creative) {

        List<ItemStack> drops = getDrops();
        if (!creative && drops != null && drops.size() > 0)
            for (ItemStack item : drops)
                ItemHelper.dropItem(getWorld(), getX(), getY(), getZ(), item);

        getParent().removePart(this);
    }

}
