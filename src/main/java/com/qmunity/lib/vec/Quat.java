package com.qmunity.lib.vec;

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

    public void setX(double x) {

        this.x = x;
    }

    public void setY(double y) {

        this.y = y;
    }

    public void setZ(double z) {

        this.z = z;
    }

    public void setW(double w) {

        this.w = w;
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

    public Vec3d mul(Vec3d v) {

        double k0 = w * w - 0.5D;
        double k1;
        double rx, ry, rz;

        k1 = v.x * x;
        k1 += v.y * y;
        k1 += v.z * z;

        rx = v.x * k0 + x * k1;
        ry = v.y * k0 + y * k1;
        rz = v.z * k0 + z * k1;

        rx += w * (y * v.z - z * v.y);
        ry += w * (z * v.x - x * v.z);
        rz += w * (x * v.y - y * v.x);

        rx += rx;
        ry += ry;
        rz += rz;

        return new Vec3d(rx, ry, rz);
    }

}