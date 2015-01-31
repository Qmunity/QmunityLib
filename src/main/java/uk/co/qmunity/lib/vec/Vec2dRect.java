package uk.co.qmunity.lib.vec;

import java.awt.Rectangle;

public class Vec2dRect {

    private Vec2d min;
    private Vec2d max;

    public Vec2dRect(Vec2d a, Vec2d b) {

        double x1 = Math.min(a.getX(), b.getX());
        double y1 = Math.min(a.getY(), b.getY());
        double x2 = Math.max(a.getX(), b.getX());
        double y2 = Math.max(a.getY(), b.getY());

        min = new Vec2d(x1, y1);
        max = new Vec2d(x2, y2);
    }

    public Vec2dRect(double x1, double y1, double x2, double y2) {

        min = new Vec2d(x1, y1);
        max = new Vec2d(x2, y2);
    }

    public Vec2dRect(Rectangle rect) {

        this(rect.x, rect.y, rect.x + rect.width, rect.y + rect.height);
    }

    public Vec2d getMin() {

        return min;
    }

    public Vec2d getMax() {

        return max;
    }

    public double getMinX() {

        return min.getX();
    }

    public double getMinY() {

        return min.getY();
    }

    public double getMaxX() {

        return max.getX();
    }

    public double getMaxY() {

        return max.getY();
    }

    public Vec2d getCenter() {

        return getMin().clone().add(getMax()).div(2, 2);
    }

    public Vec2dRect rotate(double angle, Vec2d center) {

        min = min.clone().sub(center).rotate(angle).add(center);
        max = max.clone().sub(center).rotate(angle).add(center);

        return this;
    }

    @Override
    public Vec2dRect clone() {

        return new Vec2dRect(min, max);
    }

    public Vec3dCube extrude(double height) {

        return new Vec3dCube(getMinX(), 0, getMinY(), getMaxX(), height, getMaxY());
    }

}
