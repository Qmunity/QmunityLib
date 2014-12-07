package uk.co.qmunity.lib.part;

/**
 * Interface implemented by microblocks. It contains the methods you need to determine the shape and position of the microblock in its parent.
 *
 * @author amadornes
 */
public interface IMicroblock extends IPart, IPartOccluding {

    /**
     * The shape of the microblock
     */
    public MicroblockShape getShape();

    /**
     * The size of the microblock
     */
    public int getSize();

    /**
     * The position of the microblock:<br>
     * <i> - If it's a face microblock: The side it's on</i><br>
     * <i> - If it's an edge microblock: side1 + side2 << 4</i><br>
     * <i> - If it's a corner microblock: side1 + side2 << 4 + side3 << 8</i><br>
     */
    public int getPosition();

}
