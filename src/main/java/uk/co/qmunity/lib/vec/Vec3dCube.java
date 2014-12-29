package uk.co.qmunity.lib.vec;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.misc.Pair;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.transform.Transformation;
import uk.co.qmunity.lib.transform.Translation;

public class Vec3dCube {

    private Vec3d min, max;
    private IPart part;

    public Vec3dCube(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {

        this(minX, minY, minZ, maxX, maxY, maxZ, (World) null);
    }

    public Vec3dCube(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, IPart part) {

        this(minX, minY, minZ, maxX, maxY, maxZ, part.getWorld());
        this.part = part;
    }

    public Vec3dCube(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, World world) {

        this(new Vec3d(minX, minY, minZ, world), new Vec3d(maxX, maxY, maxZ, world));
    }

    public Vec3dCube(Vec3d a, Vec3d b) {

        World w = a.getWorld();
        if (w == null)
            w = b.getWorld();

        min = a;
        max = b;

        fix();
    }

    public Vec3dCube(Vec3d a, Vec3d b, IPart part) {

        this(a, b);
        this.part = part;
    }

    public Vec3dCube(AxisAlignedBB aabb) {

        this(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
    }

    public Vec3d getMin() {

        return min;
    }

    public Vec3d getMax() {

        return max;
    }

    public Vec3d getCenter() {

        return new Vec3d((getMinX() + getMaxX()) / 2D, (getMinY() + getMaxY()) / 2D, (getMinZ() + getMaxZ()) / 2D, getMin().getWorld());
    }

    public double getMinX() {

        return min.getX();
    }

    public double getMinY() {

        return min.getY();
    }

    public double getMinZ() {

        return min.getZ();
    }

    public double getMaxX() {

        return max.getX();
    }

    public double getMaxY() {

        return max.getY();
    }

    public double getMaxZ() {

        return max.getZ();
    }

    public IPart getPart() {

        return part;
    }

    public void setPart(IPart p) {

        part = p;
    }

    public AxisAlignedBB toAABB() {

        return AxisAlignedBB.getBoundingBox(getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ());
    }

    @Override
    public Vec3dCube clone() {

        return new Vec3dCube(min.clone(), max.clone(), part);
    }

    public Vec3dCube expand(double size) {

        min.sub(size, size, size);
        max.add(size, size, size);

        return this;
    }

    public Vec3dCube fix() {

        Vec3d a = min.clone();
        Vec3d b = max.clone();

        double minX = Math.min(a.getX(), b.getX());
        double minY = Math.min(a.getY(), b.getY());
        double minZ = Math.min(a.getZ(), b.getZ());

        double maxX = Math.max(a.getX(), b.getX());
        double maxY = Math.max(a.getY(), b.getY());
        double maxZ = Math.max(a.getZ(), b.getZ());

        min = new Vec3d(minX, minY, minZ, a.w);
        max = new Vec3d(maxX, maxY, maxZ, b.w);

        return this;
    }

    public Vec3dCube rotate(int x, int y, int z, Vec3d center) {

        min.sub(center).rotate(x, y, z).add(center);
        max.sub(center).rotate(x, y, z).add(center);

        double mul = 10000000;

        fix();

        min.setX(Math.round(min.getX() * mul) / mul);
        min.setY(Math.round(min.getY() * mul) / mul);
        min.setZ(Math.round(min.getZ() * mul) / mul);

        max.setX(Math.round(max.getX() * mul) / mul);
        max.setY(Math.round(max.getY() * mul) / mul);
        max.setZ(Math.round(max.getZ() * mul) / mul);

        return this;
    }

    public Vec3dCube rotate(ForgeDirection face, Vec3d center) {

        switch (face) {
        case DOWN:
            return this;
        case UP:
            return rotate(0, 0, 2 * 90, center);
        case WEST:
            return rotate(0, 0, -1 * 90, center);
        case EAST:
            return rotate(0, 0, 1 * 90, center);
        case NORTH:
            return rotate(1 * 90, 0, 0, center);
        case SOUTH:
            return rotate(-1 * 90, 0, 0, center);
        default:
            break;
        }

        return this;
    }

    public Vec3dCube add(double x, double y, double z) {

        min.add(x, y, z);
        max.add(x, y, z);

        return this;
    }

    public static final Vec3dCube merge(List<Vec3dCube> cubes) {

        double minx = Double.MAX_VALUE;
        double miny = Double.MAX_VALUE;
        double minz = Double.MAX_VALUE;
        double maxx = Double.MIN_VALUE;
        double maxy = Double.MIN_VALUE;
        double maxz = Double.MIN_VALUE;

        for (Vec3dCube c : cubes) {
            minx = Math.min(minx, c.getMinX());
            miny = Math.min(miny, c.getMinY());
            minz = Math.min(minz, c.getMinZ());
            maxx = Math.max(maxx, c.getMaxX());
            maxy = Math.max(maxy, c.getMaxY());
            maxz = Math.max(maxz, c.getMaxZ());
        }

        if (cubes.size() == 0)
            return new Vec3dCube(0, 0, 0, 0, 0, 0);

        return new Vec3dCube(minx, miny, minz, maxx, maxy, maxz);
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Vec3dCube))
            return false;
        Vec3dCube other = (Vec3dCube) o;
        return other.min.equals(min) && other.max.equals(max) && other.part == part;
    }

    @Override
    public int hashCode() {

        return min.hashCode() << 8 + max.hashCode();
    }

    public Vec2dRect getFace(ForgeDirection face) {

        switch (face) {
        case DOWN:
        case UP:
            return new Vec2dRect(getMinX(), getMinZ(), getMaxX(), getMaxZ());
        case WEST:
        case EAST:
            return new Vec2dRect(getMinY(), getMinZ(), getMaxY(), getMaxZ());
        case NORTH:
        case SOUTH:
            return new Vec2dRect(getMinX(), getMinY(), getMaxX(), getMaxY());
        default:
            break;
        }

        return null;
    }

    public Vec3dCube transform(Transformation transformation) {

        return transformation.apply(this);
    }

    public boolean occlusionTest(Vec3dCube cube) {

        return !toAABB().intersectsWith(cube.toAABB());
    }

    public List<Pair<Pair<Vec3dCube, Translation>, boolean[]>> splitInto1x1() {

        List<Pair<Pair<Vec3dCube, Translation>, boolean[]>> cubes = new ArrayList<Pair<Pair<Vec3dCube, Translation>, boolean[]>>();

        int minx = (int) Math.floor(getMinX());
        int miny = (int) Math.floor(getMinY());
        int minz = (int) Math.floor(getMinZ());
        int maxx = (int) Math.ceil(getMaxX());
        int maxy = (int) Math.ceil(getMaxY());
        int maxz = (int) Math.ceil(getMaxZ());

        for (int x = minx; x < maxx; x++) {
            for (int y = miny; y < maxy; y++) {
                for (int z = minz; z < maxz; z++) {
                    Translation t = new Translation(x, y, z);
                    Vec3dCube cube = new Vec3dCube(0, 0, 0, 1, 1, 1);
                    boolean[] sides = new boolean[6];

                    if (x == minx) {
                        cube.getMin().setX(getMinX() - minx);
                        sides[4] = true;
                    }
                    if (y == miny) {
                        cube.getMin().setY(getMinY() - miny);
                        sides[0] = true;
                    }
                    if (z == minz) {
                        cube.getMin().setZ(getMinZ() - minz);
                        sides[2] = true;
                    }

                    if (x == maxx - 1) {
                        cube.getMax().setX(getMaxX() - (maxx - 1));
                        sides[5] = true;
                    }
                    if (y == maxy - 1) {
                        cube.getMax().setY(getMaxY() - (maxy - 1));
                        sides[1] = true;
                    }
                    if (z == maxz - 1) {
                        cube.getMax().setZ(getMaxZ() - (maxz - 1));
                        sides[3] = true;
                    }
                    
                    cube.fix();

                    cubes.add(new Pair<Pair<Vec3dCube, Translation>, boolean[]>(new Pair<Vec3dCube, Translation>(cube, t), sides));
                }
            }
        }

        return cubes;
    }
}
