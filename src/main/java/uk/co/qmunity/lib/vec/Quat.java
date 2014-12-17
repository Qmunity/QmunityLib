package uk.co.qmunity.lib.vec;

public class Quat {

    private double x, y, z, w;

    public Quat(double x, double y, double z, double w) {

        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Quat(Vec3d axis, double angle) {

        double sinHalfAngle = Math.sin(angle / 2D);

        x = axis.getX() * sinHalfAngle;
        y = axis.getY() * sinHalfAngle;
        z = axis.getZ() * sinHalfAngle;
        w = Math.cos(angle / 2D);
    }

    public double getX() {

        return x;
    }

    public double getY() {

        return y;
    }

    public double getZ() {

        return z;
    }

    public double getW() {

        return w;
    }

    public double length() {

        return Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public Quat normalize() {

        double len = length();

        x /= len;
        y /= len;
        z /= len;
        w /= len;

        return this;
    }

    public Quat conjugate() {

        return new Quat(-x, -y, -z, w);
    }

    public Quat mul(Quat r) {

        double w_ = w * r.getW() - x * r.getX() - y * r.getY() - z * r.getZ();
        double x_ = x * r.getW() + w * r.getX() + y * r.getZ() - z * r.getY();
        double y_ = y * r.getW() + w * r.getY() + z * r.getX() - x * r.getZ();
        double z_ = z * r.getW() + w * r.getZ() + x * r.getY() - y * r.getX();

        return new Quat(x_, y_, z_, w_);
    }

    public Vec3d mul(Vec3d vec) {

        double d = -x * vec.getX() - y * vec.getY() - z * vec.getZ();
        double d1 = w * vec.getX() + y * vec.getZ() - z * vec.getY();
        double d2 = w * vec.getY() - x * vec.getZ() + z * vec.getX();
        double d3 = w * vec.getZ() + x * vec.getY() - y * vec.getX();

        return vec.set(d1 * w - d * x - d2 * z + d3 * y, d2 * w - d * y + d1 * z - d3 * x, d3 * w - d * z - d1 * y + d2 * x);
    }

    public static Quat getRotation(double x, double y, double z) {

        Quat rx = new Quat(new Vec3d(1, 0, 0), Math.toRadians(x));
        Quat ry = new Quat(new Vec3d(0, 1, 0), Math.toRadians(y));
        Quat rz = new Quat(new Vec3d(0, 0, 1), Math.toRadians(z));

        return rx.mul(ry.mul(rz));
    }

}