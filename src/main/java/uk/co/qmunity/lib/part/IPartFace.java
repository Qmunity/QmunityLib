package uk.co.qmunity.lib.part;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * Interface implemented by parts that are placed on the face of a block, such as some microblocks.
 *
 * @author amadornes
 */
public interface IPartFace extends IPart {

    /**
     * Sets the face this part is placed on. Mainly used when placing it.
     */
    public void setFace(ForgeDirection face);

    /**
     * Gets the face this part is placed on.
     */
    public ForgeDirection getFace();

}
