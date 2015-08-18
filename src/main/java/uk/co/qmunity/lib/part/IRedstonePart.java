package uk.co.qmunity.lib.part;

import net.minecraftforge.common.util.ForgeDirection;

public interface IRedstonePart extends IQLPart {

    // Redstone
    public boolean canConnectRedstone(ForgeDirection side);

    public int getWeakPower(ForgeDirection side);

    public int getStrongPower(ForgeDirection side);

}
