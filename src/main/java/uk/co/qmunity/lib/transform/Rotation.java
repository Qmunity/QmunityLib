package uk.co.qmunity.lib.transform;

import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;

public class Rotation implements Transformation {

    private double x, y, z;
    private Vec3d center = Vec3d.center;

    public Rotation(double x, double y, double z) {

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Rotation(double x, double y, double z, Vec3d center) {

        this(x, y, z);
        this.center = center;
    }

    @Override
    public Vec3d apply(Vec3d point) {

        point = point.clone();

        point.sub(center);
        point.rotate(x, y, z);
        point.add(center);

        return point;
    }

    @Override
    public Vec3dCube apply(Vec3dCube cube) {

        return new Vec3dCube(apply(cube.getMin()), apply(cube.getMax()));
    }

}
