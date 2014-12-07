package uk.co.qmunity.lib.part;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * Implemented by parts with some kind of redstone behaviour.
 *
 * @author amadornes
 */
public interface IPartRedstone extends IPart {

    /**
     * Gets the strength of the strong redstone signal outputted on the specified side.
     */
    public int getStrongPower(ForgeDirection side);

    /**
     * Gets the strength of the weak redstone signal outputted on the specified side.
     */
    public int getWeakPower(ForgeDirection side);

    /**
     * Checks whether or not redstone can connect to the specified side of this part.
     */
    public boolean canConnectRedstone(ForgeDirection side);

}
