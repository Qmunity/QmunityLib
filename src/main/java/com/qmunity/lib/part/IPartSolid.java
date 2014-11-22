package com.qmunity.lib.part;

import net.minecraftforge.common.util.ForgeDirection;

public interface IPartSolid extends IPart {

    public boolean isSideSolid(ForgeDirection face);

}
