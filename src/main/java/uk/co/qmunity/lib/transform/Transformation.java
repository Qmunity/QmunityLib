package uk.co.qmunity.lib.transform;

import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;

public interface Transformation {

    public Vec3d apply(Vec3d point);

    public Vec3dCube apply(Vec3dCube cube);

}
