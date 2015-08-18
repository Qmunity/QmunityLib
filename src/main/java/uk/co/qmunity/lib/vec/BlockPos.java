package uk.co.qmunity.lib.vec;

import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.Copyable;
import uk.co.qmunity.lib.helper.MathHelper;

public class BlockPos implements ILocation, Comparable<BlockPos>, Copyable<BlockPos> {

    public static final BlockPos[] sideOffsets = new BlockPos[] { new BlockPos(0, -1, 0), new BlockPos(0, 1, 0), new BlockPos(0, 0, -1),
            new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0) };

    public int x;
    public int y;
    public int z;

    public BlockPos(int x, int y, int z) {

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockPos(Vector3 vec) {

        this(MathHelper.floor(vec.x), MathHelper.floor(vec.y), MathHelper.floor(vec.z));
    }

    public BlockPos(BlockPos coord) {

        this(coord.x, coord.y, coord.z);
    }

    public BlockPos(ILocation location) {

        this(location.getX(), location.getY(), location.getZ());
    }

    public BlockPos() {

    }

    @Override
    public int getX() {

        return x;
    }

    @Override
    public int getY() {

        return y;
    }

    @Override
    public int getZ() {

        return z;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof BlockPos))
            return false;
        BlockPos o2 = (BlockPos) obj;
        return x == o2.x && y == o2.y && z == o2.z;
    }

    @Override
    public int hashCode() {

        return (x ^ z) * 31 + y;
    }

    @Override
    public int compareTo(BlockPos vec) {

        int val = 0;
        if (x != vec.x)
            val += (x < vec.x ? 1 : -1);
        if (y != vec.y)
            val += (y < vec.y ? 1 : -1);
        if (z != vec.z)
            val += (z < vec.z ? 1 : -1);
        return val;
    }

    public Vector3 center() {

        return new Vector3(x + 0.5, y + 0.5, z + 0.5);
    }

    public BlockPos add(int x, int y, int z) {

        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public BlockPos add(BlockPos vec) {

        return add(vec.x, vec.y, vec.z);
    }

    public BlockPos sub(int x, int y, int z) {

        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public BlockPos sub(BlockPos vec) {

        return sub(vec.x, vec.y, vec.z);
    }

    public BlockPos mul(int f) {

        return mul(f, f, f);
    }

    public BlockPos mul(int fx, int fy, int fz) {

        x *= fx;
        y *= fy;
        z *= fz;
        return this;
    }

    public double mag() {

        return Math.sqrt(x * x + y * y + z * z);
    }

    public int magSq() {

        return x * x + y * y + z * z;
    }

    public boolean isZero() {

        return x == 0 && y == 0 && z == 0;
    }

    public boolean isAxial() {

        return x == 0 ? (y == 0 || z == 0) : (y == 0 && z == 0);
    }

    public BlockPos offset(ForgeDirection side) {

        return offset(side, 1);
    }

    public BlockPos offset(ForgeDirection side, int amount) {

        BlockPos offset = sideOffsets[side.ordinal()];
        x += offset.x * amount;
        y += offset.y * amount;
        z += offset.z * amount;
        return this;
    }

    public int getSide(ForgeDirection side) {

        switch (side) {
        case DOWN:
        case UP:
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

    public BlockPos setSide(ForgeDirection side, int value) {

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

    @Override
    public BlockPos copy() {

        return new BlockPos(x, y, z);
    }

    public BlockPos set(int x, int y, int z) {

        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public BlockPos set(BlockPos vec) {

        return set(vec.x, vec.y, vec.z);
    }

    public ForgeDirection toSide() {

        if (!isAxial())
            return ForgeDirection.UNKNOWN;
        if (y < 0)
            return ForgeDirection.DOWN;
        if (y > 0)
            return ForgeDirection.UP;
        if (z < 0)
            return ForgeDirection.NORTH;
        if (z > 0)
            return ForgeDirection.SOUTH;
        if (x < 0)
            return ForgeDirection.WEST;
        if (x > 0)
            return ForgeDirection.EAST;

        return ForgeDirection.UNKNOWN;
    }

    public int sum() {

        return x + y + z;
    }

    public int absSum() {

        return (x < 0 ? -x : x) + (y < 0 ? -y : y) + (z < 0 ? -z : z);
    }

    @Override
    public String toString() {

        return "(" + x + ", " + y + ", " + z + ")";
    }
}
