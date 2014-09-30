package com.qmunity.lib.part;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.qmunity.lib.raytrace.QMovingObjectPosition;

public interface IPart {

    public World getWorld();

    public int getX();

    public int getY();

    public int getZ();

    public ITilePartHolder getParent();

    public void setParent(ITilePartHolder parent);

    public String getType();

    public void writeToNBT(NBTTagCompound tag);

    public void readFromNBT(NBTTagCompound tag);

    public void writeUpdateToNBT(NBTTagCompound tag);

    public void readUpdateFromNBT(NBTTagCompound tag);

    public void sendUpdatePacket();

    public ItemStack getItem();

    public ItemStack getPickedItem(QMovingObjectPosition mop);

    public List<ItemStack> getDrops();

    public void breakAndDrop(boolean creative);

}
