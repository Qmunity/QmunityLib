package com.qmunity.lib.part;

import net.minecraftforge.common.util.ForgeDirection;

public interface IPartFace extends IPart {

    public void setFace(ForgeDirection face);

    public ForgeDirection getFace();

    public boolean canStay();

}
