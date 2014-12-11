package uk.co.qmunity.lib.part.compat;

import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.part.MicroblockShape;
import uk.co.qmunity.lib.vec.Vec3dCube;

public class OcclusionHelper {

    public static Vec3dCube getCornerMicroblockBox(int size, int location) {

        ForgeDirection s1 = ForgeDirection.getOrientation(location & 0x00F);
        ForgeDirection s2 = ForgeDirection.getOrientation((location & 0x0F0) >> 4);
        ForgeDirection s3 = ForgeDirection.getOrientation((location & 0xF00) >> 8);

        boolean x = s1.offsetX > 0 || s2.offsetX > 0 || s3.offsetX > 0;
        boolean y = s1.offsetY > 0 || s2.offsetY > 0 || s3.offsetY > 0;
        boolean z = s1.offsetZ > 0 || s2.offsetZ > 0 || s3.offsetZ > 0;

        double s = (size * 2) / 16D;

        return new Vec3dCube(x ? 1 - s : 0, y ? 1 - s : 0, z ? 1 - s : 0, x ? 1 : s, y ? 1 : s, z ? 1 : s);
    }

    public static Vec3dCube getEdgeMicroblockBox(int size, int location) {

        ForgeDirection s1 = ForgeDirection.getOrientation(location & 0x0F);
        ForgeDirection s2 = ForgeDirection.getOrientation((location & 0xF0) >> 4);

        boolean x = s1.offsetX > 0 || s2.offsetX > 0;
        boolean y = s1.offsetY > 0 || s2.offsetY > 0;
        boolean z = s1.offsetZ > 0 || s2.offsetZ > 0;

        double s = (size * 2) / 16D;

        return new Vec3dCube((s1.offsetX == 0 && s2.offsetX == 0) ? s : (x ? 1 - s : 0), (s1.offsetY == 0 && s2.offsetY == 0) ? s
                : (y ? 1 - s : 0), (s1.offsetZ == 0 && s2.offsetZ == 0) ? s : (z ? 1 - s : 0), (s1.offsetX == 0 && s2.offsetX == 0) ? 1 - s
                : (x ? 1 : s), (s1.offsetY == 0 && s2.offsetY == 0) ? 1 - s : (y ? 1 : s), (s1.offsetZ == 0 && s2.offsetZ == 0) ? 1 - s
                                : (z ? 1 : s));
    }

    public static Vec3dCube getFaceMicroblockBox(int size, int location) {

        ForgeDirection d = ForgeDirection.getOrientation(location);

        double s = (size * 2) / 16D;

        return new Vec3dCube(d.offsetX > 0 ? 1 - s : 0, d.offsetY > 0 ? 1 - s : 0, d.offsetZ > 0 ? 1 - s : 0, d.offsetX < 0 ? s : 1,
                d.offsetY < 0 ? s : 1, d.offsetZ < 0 ? s : 1);
    }

    public static Vec3dCube getFaceHollowMicroblockBox(int size, int location, int holeSize) {

        ForgeDirection d = ForgeDirection.getOrientation(location);

        double s = (size * 2) / 16D;
        double hs = holeSize / 16D;

        return new Vec3dCube(d.offsetX > 0 ? 1 - s : (d.offsetX == 0 ? 0.5 - hs : 0), d.offsetY > 0 ? 1 - s : (d.offsetY == 0 ? 0.5 - hs
                : 0), d.offsetZ > 0 ? 1 - s : (d.offsetZ == 0 ? 0.5 - hs : 0), d.offsetX < 0 ? s : (d.offsetX == 0 ? 0.5 + hs : 1),
                d.offsetY < 0 ? s : (d.offsetY == 0 ? 0.5 + hs : 1), d.offsetZ < 0 ? s : (d.offsetZ == 0 ? 0.5 + hs : 1));
    }

    public static Vec3dCube getBox(MicroblockShape shape, int size, int location) {

        if (shape == MicroblockShape.CORNER)
            return getCornerMicroblockBox(size, location);
        if (shape == MicroblockShape.EDGE)
            return getEdgeMicroblockBox(size, location);
        if (shape == MicroblockShape.FACE)
            return getFaceMicroblockBox(size, location);

        return null;
    }

}
