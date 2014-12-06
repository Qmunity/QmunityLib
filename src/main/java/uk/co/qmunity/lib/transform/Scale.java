package uk.co.qmunity.lib.transform;

import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;

public class Scale implements Transformation {

    private double x, y, z;
    private Vec3d center = Vec3d.center;

    public Scale(double x, double y, double z) {

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Scale(double x, double y, double z, Vec3d center) {

        this(x, y, z);
        this.center = center;
    }

    @Override
    public Vec3d apply(Vec3d point) {

        return point.clone().sub(center).mul(x, y, z).add(center);
    }

    @Override
    public Vec3dCube apply(Vec3dCube cube) {

        return new Vec3dCube(apply(cube.getMin()), apply(cube.getMax()));
    }

}
