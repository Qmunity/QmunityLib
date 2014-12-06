package uk.co.qmunity.lib.transform;

import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;

public class Translation implements Transformation {

    private double x, y, z;

    public Translation(double x, double y, double z) {

        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public Vec3d apply(Vec3d point) {

        return point.clone().add(x, y, z);
    }

    @Override
    public Vec3dCube apply(Vec3dCube cube) {

        return cube.clone().add(x, y, z);
    }

}
