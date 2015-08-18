package uk.co.qmunity.lib.util;

import net.minecraft.nbt.NBTTagCompound;

public interface INBTSaveable {

    public void writeToNBT(NBTTagCompound tag);

    public void readFromNBT(NBTTagCompound tag);

    public void markDirty();

}
