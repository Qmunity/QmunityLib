package com.qmunity.lib.vec;

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

}
