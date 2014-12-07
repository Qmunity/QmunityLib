package uk.co.qmunity.lib.part;

import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;

/**
 * NOT IMPLEMENTED YET.<br>
 * <br>
 * Interface implemented by parts that want to draw a custom selection box when their raytrace succeeds.
 *
 * @author amadornes
 */
public interface IPartSelectableCustom extends IPartSelectable {

    /**
     * NOT IMPLEMENTED YET.<br>
     * <br>
     * Draws the custom selection box/es for the specified raytrace.
     */
    public boolean NOTIMPLEMENTED_drawSelectionBoxes(QMovingObjectPosition mop);

}
