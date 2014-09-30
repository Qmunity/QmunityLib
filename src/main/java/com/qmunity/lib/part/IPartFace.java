package com.qmunity.lib.part;

import net.minecraftforge.common.util.ForgeDirection;

public interface IPartFace {

    public ForgeDirection getFace();

    public boolean canStay();

}
