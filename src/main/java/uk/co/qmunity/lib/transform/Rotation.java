package uk.co.qmunity.lib.transform;

import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.Quat;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;

public class Rotation implements Transformation {

    private double x, y, z;
    private Vec3d center = Vec3d.center;
    private Quat r;

    public Rotation(double x, double y, double z) {

        this.x = x;
        this.y = y;
        this.z = z;

        Quat rx = new Quat(new Vec3d(1, 0, 0), Math.toRadians(x));
        Quat ry = new Quat(new Vec3d(0, 1, 0), Math.toRadians(y));
        Quat rz = new Quat(new Vec3d(0, 0, 1), Math.toRadians(z));

        r = rx.mul(ry.mul(rz));
    }

    public Rotation(double x, double y, double z, Vec3d center) {

        this(x, y, z);
        this.center = center;
    }

    public Rotation(ForgeDirection face) {

        switch (face) {
        case DOWN:
            break;
        case UP:
            z = 180;
            break;
        case WEST:
            z = -90;
            break;
        case EAST:
            z = 90;
            break;
        case NORTH:
            x = 90;
            break;
        case SOUTH:
            x = -90;
            break;
        default:
            break;
        }

        Quat rx = new Quat(new Vec3d(1, 0, 0), Math.toRadians(x));
        Quat ry = new Quat(new Vec3d(0, 1, 0), Math.toRadians(y));
        Quat rz = new Quat(new Vec3d(0, 0, 1), Math.toRadians(z));

        r = rx.mul(ry.mul(rz));
    }

    public Rotation(ForgeDirection face, Vec3d center) {

        this(face);
        this.center = center;
    }

    @Override
    public Vec3d apply(Vec3d point) {

        point = point.clone();

        point.sub(center);
        point.rotate(r);
        point.add(center);

        return point;
    }

    @Override
    public Vec3dCube apply(Vec3dCube cube) {

        return new Vec3dCube(apply(cube.getMin()), apply(cube.getMax()));
    }

}
