package uk.co.qmunity.lib.vec;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import uk.co.qmunity.lib.Copyable;
import uk.co.qmunity.lib.helper.MathHelper;
import uk.co.qmunity.lib.transform.ITransformation;

public class Vector4 implements Copyable<Vector4> {

    public static Vector4 zero = new Vector4();
    public static Vector4 one = new Vector4(1, 1, 1, 1);
    public static Vector4 center = new Vector4(0.5, 0.5, 0.5, 0.5);

    public double x;
    public double y;
    public double z;
    public double s;

    public Vector4() {

    }

    public Vector4(double x, double y, double z, double s) {

        this.x = x;
        this.y = y;
        this.z = z;
        this.s = s;
    }

    public Vector4(Vector4 vec) {

        x = vec.x;
        y = vec.y;
        z = vec.z;
        s = vec.s;
    }

    public Vector4(Vector3 vec, double s) {

        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
        this.s = s;
    }

    @Override
    public Vector4 copy() {

        return new Vector4(this);
    }

    public Vector4 set(double x, double y, double z, double s) {

        this.x = x;
        this.y = y;
        this.z = z;
        this.s = s;
        return this;
    }

    public Vector4 set(Vector4 vec) {

        x = vec.x;
        y = vec.y;
        z = vec.z;
        s = vec.s;
        return this;
    }

    public Vector4 add(double x, double y, double z, double s) {

        this.x += x;
        this.y += y;
        this.z += z;
        this.s += s;
        return this;
    }

    public Vector4 add(Vector4 vec) {

        x += vec.x;
        y += vec.y;
        z += vec.z;
        s += vec.s;
        return this;
    }

    public Vector4 sub(double x, double y, double z, double s) {

        this.x -= x;
        this.y -= y;
        this.z -= z;
        this.s -= s;
        return this;
    }

    public Vector4 sub(Vector4 vec) {

        x -= vec.x;
        y -= vec.y;
        z -= vec.z;
        s -= vec.s;
        return this;
    }

    public Vector4 mul(double f) {

        x *= f;
        y *= f;
        z *= f;
        s *= f;
        return this;
    }

    public Vector4 mul(double fx, double fy, double fz, double fs) {

        x *= fx;
        y *= fy;
        z *= fz;
        s *= fs;
        return this;
    }

    public Vector4 mul(Vector4 f) {

        x *= f.x;
        y *= f.y;
        z *= f.z;
        s *= f.s;
        return this;
    }

    public Vector4 div(double f) {

        x /= f;
        y /= f;
        z /= f;
        s /= f;
        return this;
    }

    public Vector4 div(double fx, double fy, double fz, double fs) {

        x /= fx;
        y /= fy;
        z /= fz;
        s /= fs;
        return this;
    }

    public Vector4 div(Vector4 f) {

        x /= f.x;
        y /= f.y;
        z /= f.z;
        s /= f.s;
        return this;
    }

    public double mag() {

        return Math.sqrt(x * x + y * y + z * z + s * s);
    }

    public double magSq() {

        return x * x + y * y + z * z + s * s;
    }

    public Vector4 normalize() {

        double d = mag();
        if (d != 0)
            div(d);
        return this;
    }

    @Override
    public String toString() {

        MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
        return "Vector4(" + new BigDecimal(x, cont) + ", " + new BigDecimal(y, cont) + ", " + new BigDecimal(z, cont) + ", "
                + new BigDecimal(s, cont) + ")";
    }

    public boolean isZero() {

        return x == 0 && y == 0 && z == 0 && s == 0;
    }

    public Vector4 inverse() {

        x = -x;
        y = -y;
        z = -z;
        s = -s;
        return this;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Vector4))
            return false;
        Vector4 v = (Vector4) o;
        return x == v.x && y == v.y && z == v.z && s == v.s;
    }

    /**
     * Equals method with tolerance
     *
     * @return true if this is equal to v within +-1E-5
     */
    public boolean equalsT(Vector4 v) {

        return MathHelper.isBetween(x - 1E-5, v.x, x + 1E-5) && MathHelper.isBetween(y - 1E-5, v.y, y + 1E-5)
                && MathHelper.isBetween(z - 1E-5, v.z, z + 1E-5) && MathHelper.isBetween(s - 1E-5, v.s, s + 1E-5);
    }

    public Vector4 apply(ITransformation<Vector4, ?> t) {

        t.apply(this);
        return this.simplify();
    }

    public Vector4 simplify() {

        x = Math.round(x * 100000) / 100000D;
        y = Math.round(y * 100000) / 100000D;
        z = Math.round(z * 100000) / 100000D;
        s = Math.round(s * 100000) / 100000D;
        return this;
    }

    public Vector4 clipColor() {

        return MathHelper.clipColor(this);
    }

    public int toARGB() {

        return (getAlpha() << 24) + toRGB();
    }

    public int toRGB() {

        return (getRed() << 16) + (getGreen() << 8) + (getBlue() << 0);
    }

    public int getRed() {

        return MathHelper.roundAway(x * 255D);
    }

    public int getGreen() {

        return MathHelper.roundAway(y * 255D);
    }

    public int getBlue() {

        return MathHelper.roundAway(z * 255D);
    }

    public int getAlpha() {

        return MathHelper.roundAway(s * 255D);
    }

    public Quat quat() {

        return new Quat(s, x, y, z);
    }

    public static Vector4 colorRGB(int rgb) {

        return colorARGB(0xFF000000 + (rgb & 0xFFFFFF));
    }

    public static Vector4 colorARGB(int argb) {

        int a = (argb >> 24) & 0xFF;
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = (argb >> 0) & 0xFF;

        return new Vector4(r / 255D, g / 255D, b / 255D, a / 255D);
    }

}
