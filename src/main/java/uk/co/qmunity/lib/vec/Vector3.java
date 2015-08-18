package uk.co.qmunity.lib.vec;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.Copyable;
import uk.co.qmunity.lib.helper.MathHelper;
import uk.co.qmunity.lib.model.Vertex;
import uk.co.qmunity.lib.transform.Transformation;
import uk.co.qmunity.lib.transform.Translation;

/**
 * Most of this class was made by ChickenBones for CodeChickenLib but has been adapted for use in QmunityLib.<br>
 * You can find the original source at http://github.com/Chicken-Bones/CodeChickenLib
 */
public class Vector3 implements Copyable<Vector3> {

    public static Vector3 zero = new Vector3();
    public static Vector3 one = new Vector3(1, 1, 1);
    public static Vector3 center = new Vector3(0.5, 0.5, 0.5);

    public double x;
    public double y;
    public double z;

    public Vector3() {

    }

    public Vector3(double x, double y, double z) {

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(Vector3 vec) {

        x = vec.x;
        y = vec.y;
        z = vec.z;
    }

    public Vector3(BlockPos coord) {

        x = coord.x;
        y = coord.y;
        z = coord.z;
    }

    public Vector3(Vertex vert) {

        x = vert.position.x;
        y = vert.position.y;
        z = vert.position.z;
    }

    public Vector3(Vector2 vec, double z) {

        this.x = vec.x;
        this.y = vec.y;
        this.z = z;
    }

    public Vector3(Vec3 vec) {

        this.x = vec.xCoord;
        this.y = vec.yCoord;
        this.z = vec.zCoord;
    }

    @Override
    public Vector3 copy() {

        return new Vector3(this);
    }

    public Vector3 set(double x, double y, double z) {

        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3 set(Vector3 vec) {

        x = vec.x;
        y = vec.y;
        z = vec.z;
        return this;
    }

    public double getSide(ForgeDirection side) {

        switch (side) {
        case UP:
        case DOWN:
            return y;
        case NORTH:
        case SOUTH:
            return z;
        case WEST:
        case EAST:
            return x;
        default:
            throw new IndexOutOfBoundsException("Switch Falloff");
        }
    }

    public Vector3 setSide(ForgeDirection side, double value) {

        switch (side) {
        case DOWN:
        case UP:
            y = value;
            break;
        case NORTH:
        case SOUTH:
            z = value;
            break;
        case WEST:
        case EAST:
            x = value;
            break;
        default:
            throw new IndexOutOfBoundsException("Switch Falloff");
        }
        return this;
    }

    public double dot(Vector3 vec) {

        double d = vec.x * x + vec.y * y + vec.z * z;

        if (d > 1 && d < 1.00001)
            d = 1;
        else if (d < -1 && d > -1.00001)
            d = -1;
        return d;
    }

    public double dot(double x, double y, double z) {

        return this.x * x + this.y * y + this.z * z;
    }

    public Vector3 crossWith(Vector3 vec) {

        double x_ = y * vec.z - z * vec.y;
        double y_ = z * vec.x - x * vec.z;
        double z_ = x * vec.y - y * vec.x;
        x = x_;
        y = y_;
        z = z_;
        return this;
    }

    public Vector3 cross(Vector3 vec) {

        return copy().crossWith(vec);
    }

    public Vector3 add(double x, double y, double z) {

        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vector3 add(Vector3 vec) {

        x += vec.x;
        y += vec.y;
        z += vec.z;
        return this;
    }

    public Vector3 sub(double x, double y, double z) {

        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Vector3 sub(Vector3 vec) {

        x -= vec.x;
        y -= vec.y;
        z -= vec.z;
        return this;
    }

    public Vector3 mul(double f) {

        x *= f;
        y *= f;
        z *= f;
        return this;
    }

    public Vector3 mul(double fx, double fy, double fz) {

        x *= fx;
        y *= fy;
        z *= fz;
        return this;
    }

    public Vector3 mul(Vector3 f) {

        x *= f.x;
        y *= f.y;
        z *= f.z;
        return this;
    }

    public Vector3 div(double f) {

        x /= f;
        y /= f;
        z /= f;
        return this;
    }

    public Vector3 div(double fx, double fy, double fz) {

        x /= fx;
        y /= fy;
        z /= fz;
        return this;
    }

    public Vector3 div(Vector3 f) {

        x /= f.x;
        y /= f.y;
        z /= f.z;
        return this;
    }

    public double mag() {

        return Math.sqrt(x * x + y * y + z * z);
    }

    public double magSq() {

        return x * x + y * y + z * z;
    }

    public Vector3 normalize() {

        double mag = mag();
        if (mag != 0)
            mul(1 / mag);
        return this;
    }

    @Override
    public String toString() {

        MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
        return "Vector3(" + new BigDecimal(x, cont) + ", " + new BigDecimal(y, cont) + ", " + new BigDecimal(z, cont) + ")";
    }

    public Vector3 perpendicular() {

        if (z == 0)
            return zCrossProduct();
        return xCrossProduct();
    }

    public Vector3 xCrossProduct() {

        double oldZ = z;
        double oldYN = -y;
        x = 0;
        y = oldZ;
        z = oldYN;
        return this;
    }

    public Vector3 zCrossProduct() {

        double oldY = y;
        double oldXN = -x;
        x = oldY;
        y = oldXN;
        z = 0;
        return this;
    }

    public Vector3 yCrossProduct() {

        double oldZN = -z;
        double oldX = x;
        x = oldZN;
        y = 0;
        z = oldX;
        return this;
    }

    public Vector3 rotate(double angle, Vector3 axis) {

        Quat.aroundAxis(axis.copy().normalize(), angle).rotate(this);
        return this;
    }

    public Vector3 rotate(Quat rotator) {

        rotator.rotate(this);
        return this;
    }

    public double angle(Vector3 vec) {

        return Math.acos(copy().normalize().dot(vec.copy().normalize())) * MathHelper.todeg;
    }

    public boolean isZero() {

        return x == 0 && y == 0 && z == 0;
    }

    public boolean isAxial() {

        return x == 0 ? (y == 0 || z == 0) : (y == 0 && z == 0);
    }

    public Vector3 YZintercept(Vector3 end, double px) {

        double dx = end.x - x;
        double dy = end.y - y;
        double dz = end.z - z;

        if (dx == 0)
            return null;

        double d = (px - x) / dx;
        if (MathHelper.isBetween(-1E-5, d, 1E-5))
            return this;

        if (!MathHelper.isBetween(0, d, 1))
            return null;

        x = px;
        y += d * dy;
        z += d * dz;
        return this;
    }

    public Vector3 XZintercept(Vector3 end, double py) {

        double dx = end.x - x;
        double dy = end.y - y;
        double dz = end.z - z;

        if (dy == 0)
            return null;

        double d = (py - y) / dy;
        if (MathHelper.isBetween(-1E-5, d, 1E-5))
            return this;

        if (!MathHelper.isBetween(0, d, 1))
            return null;

        x += d * dx;
        y = py;
        z += d * dz;
        return this;
    }

    public Vector3 XYintercept(Vector3 end, double pz) {

        double dx = end.x - x;
        double dy = end.y - y;
        double dz = end.z - z;

        if (dz == 0)
            return null;

        double d = (pz - z) / dz;
        if (MathHelper.isBetween(-1E-5, d, 1E-5))
            return this;

        if (!MathHelper.isBetween(0, d, 1))
            return null;

        x += d * dx;
        y += d * dy;
        z = pz;
        return this;
    }

    public Vector3 inverse() {

        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    public Vector3 negate(Vector3 vec) {

        x = 2 * vec.x - x;
        y = 2 * vec.y - y;
        z = 2 * vec.z - z;
        return this;
    }

    public float scalarProject(Vector3 vec) {

        double l = vec.mag();
        return (float) (l == 0 ? 0 : dot(vec) / l);
    }

    public Vector3 project(Vector3 vec) {

        double l = vec.magSq();
        if (l == 0)
            return set(0, 0, 0);
        double m = dot(vec) / l;
        set(vec).mul(m);
        return this;
    }

    public Translation translation() {

        return new Translation(this);
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Vector3))
            return false;
        Vector3 v = (Vector3) o;
        return x == v.x && y == v.y && z == v.z;
    }

    /**
     * Equals method with tolerance
     *
     * @return true if this is equal to v within +-1E-5
     */
    public boolean equalsT(Vector3 v) {

        return MathHelper.isBetween(x - 1E-5, v.x, x + 1E-5) && MathHelper.isBetween(y - 1E-5, v.y, y + 1E-5)
                && MathHelper.isBetween(z - 1E-5, v.z, z + 1E-5);
    }

    public Vector3 apply(Transformation tranformation) {

        tranformation.apply(this);
        return this;
    }

    public Vec3 toVec3() {

        return Vec3.createVectorHelper(x, y, z);
    }

    public BlockPos toBlockPos(int operation) {

        if (operation == 1)
            return new BlockPos(MathHelper.ceil(x), MathHelper.ceil(y), MathHelper.ceil(z));
        else if (operation == 2)
            return new BlockPos(MathHelper.round(x), MathHelper.round(y), MathHelper.round(z));
        else
            return new BlockPos(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
    }

    public BlockPos toBlockPos() {

        return toBlockPos(0);
    }
}
