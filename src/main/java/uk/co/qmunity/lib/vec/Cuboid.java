package uk.co.qmunity.lib.vec;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.Copyable;
import uk.co.qmunity.lib.transform.Rotation;
import uk.co.qmunity.lib.transform.Transformation;

/**
 * Most of this class was made by ChickenBones for CodeChickenLib but has been adapted for use in QmunityLib.<br>
 * You can find the original source at http://github.com/Chicken-Bones/CodeChickenLib
 */
public class Cuboid implements Copyable<Cuboid> {

    public static Cuboid full = new Cuboid(0, 0, 0, 1, 1, 1);

    public Vector3 min;
    public Vector3 max;

    public Cuboid() {

        this(new Vector3(), new Vector3());
    }

    public Cuboid(Vector3 min, Vector3 max) {

        this.min = min;
        this.max = max;
    }

    public Cuboid(Cuboid cuboid) {

        min = cuboid.min.copy();
        max = cuboid.max.copy();
    }

    public Cuboid(double minx, double miny, double minz, double maxx, double maxy, double maxz) {

        min = new Vector3(minx, miny, minz);
        max = new Vector3(maxx, maxy, maxz);
    }

    public Cuboid(AxisAlignedBB aabb) {

        min = new Vector3(aabb.minX, aabb.minY, aabb.minZ);
        max = new Vector3(aabb.maxX, aabb.maxY, aabb.maxZ);
    }

    @Override
    public Cuboid copy() {

        return new Cuboid(this);
    }

    public Cuboid set(Cuboid c) {

        return set(c.min, c.max);
    }

    public Cuboid set(Vector3 min, Vector3 max) {

        this.min.set(min);
        this.max.set(max);
        return this;
    }

    public Cuboid set(double minx, double miny, double minz, double maxx, double maxy, double maxz) {

        min.set(minx, miny, minz);
        max.set(maxx, maxy, maxz);
        return this;
    }

    public Cuboid add(Vector3 vec) {

        min.add(vec);
        max.add(vec);
        return this;
    }

    public Cuboid sub(Vector3 vec) {

        min.sub(vec);
        max.sub(vec);
        return this;
    }

    public Cuboid expand(double amt) {

        return expand(new Vector3(amt, amt, amt));
    }

    public Cuboid expand(Vector3 vec) {

        min.sub(vec);
        max.add(vec);
        return this;
    }

    public boolean intersects(Cuboid cuboid) {

        return max.x - 1E-5 > cuboid.min.x && cuboid.max.x - 1E-5 > min.x && max.y - 1E-5 > cuboid.min.y && cuboid.max.y - 1E-5 > min.y
                && max.z - 1E-5 > cuboid.min.z && cuboid.max.z - 1E-5 > min.z;
    }

    public Cuboid offset(Cuboid cuboid) {

        min.add(cuboid.min);
        max.add(cuboid.max);
        return this;
    }

    public Vector3 center() {

        return min.copy().add(max).mul(0.5);
    }

    @Override
    public String toString() {

        MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
        return "Cuboid: (" + new BigDecimal(min.x, cont) + ", " + new BigDecimal(min.y, cont) + ", " + new BigDecimal(min.z, cont)
                + ") -> (" + new BigDecimal(max.x, cont) + ", " + new BigDecimal(max.y, cont) + ", " + new BigDecimal(max.z, cont) + ")";
    }

    public Cuboid enclose(Vector3 vec) {

        if (min.x > vec.x)
            min.x = vec.x;
        if (min.y > vec.y)
            min.y = vec.y;
        if (min.z > vec.z)
            min.z = vec.z;
        if (max.x < vec.x)
            max.x = vec.x;
        if (max.y < vec.y)
            max.y = vec.y;
        if (max.z < vec.z)
            max.z = vec.z;
        return this;
    }

    public Cuboid enclose(Cuboid cuboid) {

        if (min.x > cuboid.min.x)
            min.x = cuboid.min.x;
        if (min.y > cuboid.min.y)
            min.y = cuboid.min.y;
        if (min.z > cuboid.min.z)
            min.z = cuboid.min.z;
        if (max.x < cuboid.max.x)
            max.x = cuboid.max.x;
        if (max.y < cuboid.max.y)
            max.y = cuboid.max.y;
        if (max.z < cuboid.max.z)
            max.z = cuboid.max.z;
        return this;
    }

    public Cuboid apply(Transformation transformation) {

        transformation.apply(min);
        transformation.apply(max);
        double temp;
        if (min.x > max.x) {
            temp = min.x;
            min.x = max.x;
            max.x = temp;
        }
        if (min.y > max.y) {
            temp = min.y;
            min.y = max.y;
            max.y = temp;
        }
        if (min.z > max.z) {
            temp = min.z;
            min.z = max.z;
            max.z = temp;
        }
        return this;
    }

    public double getSide(ForgeDirection side) {

        switch (side) {
        case DOWN:
            return min.y;
        case UP:
            return max.y;
        case NORTH:
            return min.z;
        case SOUTH:
            return max.z;
        case WEST:
            return min.x;
        case EAST:
            return max.x;
        default:
            break;
        }
        throw new IndexOutOfBoundsException("Switch Falloff");
    }

    public Cuboid setSide(ForgeDirection side, double value) {

        switch (side) {
        case DOWN:
            min.y = value;
            break;
        case UP:
            max.y = value;
            break;
        case NORTH:
            min.z = value;
            break;
        case SOUTH:
            max.z = value;
            break;
        case WEST:
            min.x = value;
            break;
        case EAST:
            max.x = value;
            break;
        default:
            throw new IndexOutOfBoundsException("Switch Falloff");
        }
        return this;
    }

    public Cuboid onFace(ForgeDirection face) {

        return onFace(face, Vector3.center);
    }

    public Cuboid onFace(ForgeDirection face, Vector3 center) {

        return copy().apply(Rotation.sideRotations[face.ordinal()].at(center.copy().inverse()));
    }

    public Cuboid[] computeFaces() {

        return new Cuboid[] {//
        onFace(ForgeDirection.DOWN), onFace(ForgeDirection.UP), onFace(ForgeDirection.NORTH), onFace(ForgeDirection.SOUTH),
                onFace(ForgeDirection.WEST), onFace(ForgeDirection.EAST) };
    }

    public Cuboid[] computeFaces(Vector3 center) {

        return new Cuboid[] {//
        onFace(ForgeDirection.DOWN, center), onFace(ForgeDirection.UP, center), onFace(ForgeDirection.NORTH, center),
                onFace(ForgeDirection.SOUTH, center), onFace(ForgeDirection.WEST, center), onFace(ForgeDirection.EAST, center) };
    }

    public Cuboid quarterRotation(int rot) {

        return quarterRotation(rot, Vector3.center);
    }

    public Cuboid quarterRotation(int rot, Vector3 center) {

        return copy().apply(Rotation.quarterRotations[rot].at(center.copy().inverse()));
    }

    public Cuboid[] computeQuarterRotations() {

        return computeQuarterRotations(Vector3.center);
    }

    public Cuboid[] computeQuarterRotations(Vector3 center) {

        return new Cuboid[] { quarterRotation(0), quarterRotation(1), quarterRotation(2), quarterRotation(3) };
    }

    public Cuboid[][] computeVariants() {

        return computeVariants(Vector3.center);
    }

    public Cuboid[][] computeVariants(Vector3 center) {

        Cuboid[][] variants = new Cuboid[6][4];

        int j = 0;
        for (Cuboid qr : computeQuarterRotations(center)) {
            int i = 0;
            for (Cuboid fr : qr.computeFaces(center)) {
                variants[i][j] = fr;
                i++;
            }
            j++;
        }

        return variants;
    }

    public static boolean intersects(Cuboid a, Cuboid b) {

        return a != null && b != null && a.intersects(b);
    }

    public AxisAlignedBB toAABB() {

        return AxisAlignedBB.getBoundingBox(min.x, min.y, min.z, max.x, max.y, max.z);
    }

    public void setBlockBounds(Block block) {

        block.setBlockBounds((float) min.x, (float) min.y, (float) min.z, (float) max.x, (float) max.y, (float) max.z);
    }
}
