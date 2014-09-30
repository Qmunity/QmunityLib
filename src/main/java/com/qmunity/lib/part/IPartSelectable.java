package com.qmunity.lib.part;

import java.util.List;

import com.qmunity.lib.raytrace.QMovingObjectPosition;
import com.qmunity.lib.vec.Vec3d;
import com.qmunity.lib.vec.Vec3dCube;

public interface IPartSelectable {

    public QMovingObjectPosition rayTrace(Vec3d start, Vec3d end);

    public List<Vec3dCube> getSelectionBoxes();

}
