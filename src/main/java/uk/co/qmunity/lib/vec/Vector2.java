package uk.co.qmunity.lib.vec;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import uk.co.qmunity.lib.helper.MathHelper;
import uk.co.qmunity.lib.model.Vertex;
import uk.co.qmunity.lib.transform.ITransformation;

public class Vector2 {

    public static Vector2 zero = new Vector2();
    public static Vector2 one = new Vector2(1, 1);
    public static Vector2 center = new Vector2(0.5, 0.5);

    public double x;
    public double y;

    public Vector2() {

    }

    public Vector2(double x, double y) {

        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 vec) {

        this.x = vec.x;
        this.y = vec.y;
    }

    public Vector2(Vertex vert) {

        this.x = vert.position.x;
        this.y = vert.position.y;
    }

    public Vector2 copy() {

        return new Vector2(this);
    }

    public Vector2 set(double x, double y) {

        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2 set(Vector2 vec) {

        return set(vec.x, vec.y);
    }

    public Vector2 add(double x, double y) {

        this.x += x;
        this.y += y;
        return this;
    }

    public Vector2 add(Vector2 vec) {

        return add(vec.x, vec.y);
    }

    public Vector2 sub(double x, double y) {

        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vector2 sub(Vector2 vec) {

        return sub(vec.x, vec.y);
    }

    public Vector2 mul(double f) {

        return mul(f, f);
    }

    public Vector2 mul(Vector2 f) {

        return mul(f.x, f.y);
    }

    public Vector2 mul(double fx, double fy) {

        x *= fx;
        y *= fy;
        return this;
    }

    public Vector2 div(double f) {

        return div(f, f);
    }

    public Vector2 div(Vector2 f) {

        return div(f.x, f.y);
    }

    public Vector2 div(double fx, double fy) {

        x /= fx;
        y /= fy;
        return this;
    }

    public double mag() {

        return Math.sqrt(x * x + y * y);
    }

    public double magSquared() {

        return x * x + y * y;
    }

    public Vector2 normalize() {

        double d = mag();
        if (d != 0)
            mul(1 / d);
        return this;
    }

    public Vector2 rotate(double angle) {

        double theta = MathHelper.torad * angle;
        double cs = MathHelper.cos(theta);
        double sn = MathHelper.sin(theta);

        double px = x * cs - y * sn;
        double py = x * sn + y * cs;

        x = px;
        y = py;

        return this;
    }

    @Override
    public String toString() {

        MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
        return "Vector2(" + new BigDecimal(x, cont) + ", " + new BigDecimal(y, cont) + ")";
    }

    public boolean isZero() {

        return x == 0 && y == 0;
    }

    public Vector2 inverse() {

        x = -x;
        y = -y;
        return this;
    }

    public Vector2 negate(Vector2 vec) {

        x = 2 * vec.x - x;
        y = 2 * vec.y - y;
        return this;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Vector2))
            return false;
        Vector2 v = (Vector2) o;
        return x == v.x && y == v.y;
    }

    /**
     * Equals method with tolerance
     *
     * @return true if this is equal to v within +-1E-5
     */
    public boolean equalsT(Vector2 v) {

        return MathHelper.isBetween(x - 1E-5, v.x, x + 1E-5) && MathHelper.isBetween(y - 1E-5, v.y, y + 1E-5);
    }

    public Vector2 apply(ITransformation<Vector2, ?> t) {

        t.apply(this);
        return this.simplify();
    }

    public Vector2 simplify() {

        x = Math.round(x * 100000) / 100000D;
        y = Math.round(y * 100000) / 100000D;
        return this;
    }

}
