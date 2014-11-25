package uk.co.qmunity.lib.vec;

public class Vec2d {

    private double x, y;

    public Vec2d(double x, double y) {

        this.x = x;
        this.y = y;
    }

    public double getX() {

        return x;
    }

    public double getY() {

        return y;
    }

    public Vec2d setX(double x) {

        this.x = x;

        return this;
    }

    public Vec2d setY(double y) {

        this.y = y;

        return this;
    }

    public Vec2d set(double x, double y) {

        this.x = x;
        this.y = y;

        return this;
    }

    public Vec2d add(double x, double y) {

        this.x += x;
        this.y += y;

        return this;
    }

    public Vec2d add(Vec2d v) {

        x += v.getX();
        y += v.getY();

        return this;
    }

    public Vec2d sub(double x, double y) {

        this.x -= x;
        this.y -= y;

        return this;
    }

    public Vec2d sub(Vec2d v) {

        x -= v.getX();
        y -= v.getY();

        return this;
    }

    public Vec2d mul(double x, double y) {

        this.x *= x;
        this.y *= y;

        return this;
    }

    public Vec2d mul(Vec2d v) {

        x *= v.getX();
        y *= v.getY();

        return this;
    }

    public Vec2d div(double x, double y) {

        this.x /= x;
        this.y /= y;

        return this;
    }

    public Vec2d div(Vec2d v) {

        x /= v.getX();
        y /= v.getY();

        return this;
    }

    public double length() {

        return Math.sqrt(x * x + y * y);
    }

    public double dot(Vec2d v) {

        return x * v.getX() + y * v.getY();
    }

    public Vec2d normalize() {

        double len = length();

        x /= len;
        y /= len;

        return this;
    }

    public Vec2d normalizeCopgetY() {

        return clone().normalize();
    }

    public double distance(Vec2d to) {

        return (clone().sub(to)).length();
    }

    public Vec2d rotate(double angle) {

        double rad = Math.toRadians(angle);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);

        return new Vec2d(x * cos - y * sin, x * sin + y * cos);
    }

    @Override
    public Vec2d clone() {

        return new Vec2d(x, y);
    }

    @Override
    public String toString() {

        return "Vec2d [x=" + x + ", y=" + y + "]";
    }

}