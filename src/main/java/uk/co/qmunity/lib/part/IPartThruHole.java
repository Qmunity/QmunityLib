package uk.co.qmunity.lib.part;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * Interface implemented by parts that can go through a hollow cover's hole and want it to be resized to fit them.
 *
 * @author amadornes
 */
public interface IPartThruHole extends IPartCenter {

    public int getHollowSize(ForgeDirection side);

}
