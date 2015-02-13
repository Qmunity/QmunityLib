package uk.co.qmunity.lib.part.compat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.part.IMicroblock;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartOccluding;
import uk.co.qmunity.lib.part.ITilePartHolder;
import uk.co.qmunity.lib.part.MicroblockShape;
import uk.co.qmunity.lib.part.PartNormallyOccluded;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

public class OcclusionHelper {

    public static PartNormallyOccluded getMicroblockPart(MicroblockShape shape, int size, ForgeDirection... sides) {

        if (shape == MicroblockShape.FACE)
            return new PartNormallyOccluded(getFaceMicroblockBox(size, sides[0]));
        if (shape == MicroblockShape.FACE_HOLLOW)
            return new PartNormallyOccluded(getHollowFaceMicroblockBox(size, sides[0]));
        if (shape == MicroblockShape.EDGE)
            return new PartNormallyOccluded(getEdgeMicroblockBox(size, sides[0], sides[1]));
        if (shape == MicroblockShape.CORNER)
            return new PartNormallyOccluded(getCornerMicroblockBox(size, sides[0], sides[1], sides[2]));

        return new PartNormallyOccluded(new Vec3dCube(0, 0, 0, 0, 0, 0));
    }

    public static boolean microblockOcclusionTest(Vec3i block, MicroblockShape shape, int size, ForgeDirection... sides) {

        return microblockOcclusionTest(block.getWorld(), block, shape, size, sides);
    }

    public static boolean microblockOcclusionTest(World world, Vec3i block, MicroblockShape shape, int size, ForgeDirection... sides) {

        List<IPart> parts = new ArrayList<IPart>();
        for (IPart part : MultipartCompatibility.getMicroblocks(world, block))
            parts.add(part);

        return occlusionTest(parts, getMicroblockPart(shape, size, sides));
    }

    public static boolean microblockOcclusionTest(ITilePartHolder holder, MicroblockShape shape, int size, ForgeDirection... sides) {

        List<IPart> parts = new ArrayList<IPart>();
        for (IPart part : holder.getMicroblocks())
            parts.add(part);

        return occlusionTest(parts, getMicroblockPart(shape, size, sides));
    }

    public static boolean microblockOcclusionTest(ITilePartHolder holder, IMicroblock microblock) {

        List<IPart> parts = new ArrayList<IPart>();
        for (IPart part : holder.getMicroblocks())
            parts.add(part);

        return occlusionTest(parts, microblock);
    }

    public static Vec3dCube getFaceMicroblockBox(int size, ForgeDirection face) {

        double s = (size * 2) / 16D;

        return new Vec3dCube(face.offsetX > 0 ? 1 - s : 0, face.offsetY > 0 ? 1 - s : 0, face.offsetZ > 0 ? 1 - s : 0, face.offsetX < 0 ? s
                : 1, face.offsetY < 0 ? s : 1, face.offsetZ < 0 ? s : 1);
    }

    public static Vec3dCube getHollowFaceMicroblockBox(int size, ForgeDirection face) {

        double s = 2 / 16D;
        double d = size / 32D;

        return new Vec3dCube(face.offsetX > 0 ? 1 - s : (face.offsetX < 0 ? 0 : 0.5 - d), face.offsetY > 0 ? 1 - s : (face.offsetY < 0 ? 0
                : 0.5 - d), face.offsetZ > 0 ? 1 - s : (face.offsetZ < 0 ? 0 : 0.5 - d), face.offsetX < 0 ? s : (face.offsetX > 0 ? 1
                : 0.5 + d), face.offsetY < 0 ? s : (face.offsetY > 0 ? 1 : 0.5 + d), face.offsetZ < 0 ? s
                : (face.offsetZ > 0 ? 1 : 0.5 + d));
    }

    public static Vec3dCube getEdgeMicroblockBox(int size, ForgeDirection side1, ForgeDirection side2) {

        boolean x = side1.offsetX > 0 || side2.offsetX > 0;
        boolean y = side1.offsetY > 0 || side2.offsetY > 0;
        boolean z = side1.offsetZ > 0 || side2.offsetZ > 0;

        double s = (size * 2) / 16D;

        return new Vec3dCube((side1.offsetX == 0 && side2.offsetX == 0) ? s : (x ? 1 - s : 0),
                (side1.offsetY == 0 && side2.offsetY == 0) ? s : (y ? 1 - s : 0), (side1.offsetZ == 0 && side2.offsetZ == 0) ? s
                        : (z ? 1 - s : 0), (side1.offsetX == 0 && side2.offsetX == 0) ? 1 - s : (x ? 1 : s),
                (side1.offsetY == 0 && side2.offsetY == 0) ? 1 - s : (y ? 1 : s), (side1.offsetZ == 0 && side2.offsetZ == 0) ? 1 - s
                        : (z ? 1 : s));
    }

    public static Vec3dCube getCornerMicroblockBox(int size, ForgeDirection side1, ForgeDirection side2, ForgeDirection side3) {

        boolean x = side1.offsetX > 0 || side2.offsetX > 0 || side3.offsetX > 0;
        boolean y = side1.offsetY > 0 || side2.offsetY > 0 || side3.offsetY > 0;
        boolean z = side1.offsetZ > 0 || side2.offsetZ > 0 || side3.offsetZ > 0;

        double s = (size * 2) / 16D;

        return new Vec3dCube(x ? 1 - s : 0, y ? 1 - s : 0, z ? 1 - s : 0, x ? 1 : s, y ? 1 : s, z ? 1 : s);
    }

    public static boolean occlusionTest(Vec3i block, Collection<Vec3dCube> boxes) {

        return occlusionTest(block.getWorld(), block, boxes);
    }

    public static boolean occlusionTest(World world, Vec3i block, Collection<Vec3dCube> boxes) {

        ITilePartHolder holder = MultipartCompatibility.getPartHolder(world, block);

        if (holder != null)
            return occlusionTest(holder, boxes);

        return true;
    }

    public static boolean occlusionTest(ITilePartHolder holder, Collection<Vec3dCube> boxes) {

        for (Vec3dCube box : boxes)
            if (!occlusionTest(holder, box))
                return false;

        return true;
    }

    public static boolean occlusionTest(Collection<IPart> parts, Collection<Vec3dCube> boxes) {

        for (IPart p : parts)
            for (Vec3dCube box : boxes)
                if (!occlusionTest(p, box))
                    return false;

        return true;
    }

    public static boolean occlusionTest(IPart part, Collection<Vec3dCube> boxes) {

        for (Vec3dCube box : boxes)
            if (!occlusionTest(part, box))
                return false;

        return true;
    }

    public static boolean occlusionTest(Vec3i block, Vec3dCube box) {

        return occlusionTest(block.getWorld(), block, box);
    }

    public static boolean occlusionTest(World world, Vec3i block, Vec3dCube box) {

        ITilePartHolder holder = MultipartCompatibility.getPartHolder(world, block);

        if (holder != null)
            return occlusionTest(holder, box);

        return true;
    }

    public static boolean occlusionTest(ITilePartHolder holder, Vec3dCube box) {

        return occlusionTest(holder, new PartNormallyOccluded(box));
    }

    public static boolean occlusionTest(Collection<IPart> parts, Vec3dCube box) {

        return occlusionTest(parts, new PartNormallyOccluded(box));
    }

    public static boolean occlusionTest(IPart part, Vec3dCube box) {

        return occlusionTest(part, new PartNormallyOccluded(box));
    }

    public static boolean occlusionTest(Vec3i block, IPart part) {

        return occlusionTest(block.getWorld(), block, part);
    }

    public static boolean occlusionTest(World world, Vec3i block, IPart part) {

        ITilePartHolder holder = MultipartCompatibility.getPartHolder(world, block);

        if (holder != null)
            return occlusionTest(holder, part);

        return true;
    }

    public static boolean occlusionTest(ITilePartHolder holder, IPart part) {

        return occlusionTest(holder.getParts(), part);
    }

    public static boolean occlusionTest(Collection<IPart> parts, IPart part) {

        for (IPart p : parts)
            if (!p.occlusionTest(part) || !part.occlusionTest(p))
                return false;

        return true;
    }

    public static boolean occlusionTest(IPart part, IPart part2) {

        if (part instanceof IPartOccluding && part2 instanceof IPartOccluding) {
            IPartOccluding p1 = (IPartOccluding) part;
            IPartOccluding p2 = (IPartOccluding) part2;

            for (Vec3dCube c1 : p1.getOcclusionBoxes())
                for (Vec3dCube c2 : p2.getOcclusionBoxes())
                    if (!c1.occlusionTest(c2))
                        return false;
        }

        return true;
    }
}
