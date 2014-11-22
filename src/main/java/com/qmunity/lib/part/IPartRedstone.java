package com.qmunity.lib.part;

import net.minecraftforge.common.util.ForgeDirection;

public interface IPartRedstone extends IPart {

    public int getStrongPower(ForgeDirection side);

    public int getWeakPower(ForgeDirection side);

    public boolean canConnectRedstone(ForgeDirection side);

}
