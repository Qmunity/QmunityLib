package uk.co.qmunity.lib.part;

import java.util.List;

import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;

public interface IPartSelectable extends IPart {

    public QMovingObjectPosition rayTrace(Vec3d start, Vec3d end);

    public List<Vec3dCube> getSelectionBoxes();

}
