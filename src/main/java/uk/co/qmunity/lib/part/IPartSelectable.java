package uk.co.qmunity.lib.part;

import java.util.List;

import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;

/**
 * Interface implemented by parts that have selection boxes.
 *
 * @author amadornes
 */
public interface IPartSelectable extends IPart {

    /**
     * Raytraces the part from the start to the end point.
     */
    public QMovingObjectPosition rayTrace(Vec3d start, Vec3d end);

    /**
     * Gets this part's selection boxes.
     */
    public List<Vec3dCube> getSelectionBoxes();

}
