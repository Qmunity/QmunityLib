package uk.co.qmunity.lib.part;

import net.minecraftforge.common.util.ForgeDirection;

public interface ISolidPart extends IQLPart {

    public boolean isSideSolid(ForgeDirection face);
}
