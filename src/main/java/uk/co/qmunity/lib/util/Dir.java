package uk.co.qmunity.lib.util;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.Vec3d;

public enum Dir {

    FRONT(ForgeDirection.NORTH), RIGHT(ForgeDirection.EAST), BACK(ForgeDirection.SOUTH), LEFT(ForgeDirection.WEST), TOP(ForgeDirection.UP), BOTTOM(
            ForgeDirection.DOWN);

    private ForgeDirection d;

    private Dir(ForgeDirection d) {

        this.d = d;
    }

    public ForgeDirection toForgeDirection(ForgeDirection face, int rotation) {

        ForgeDirection dir = d;
        for (int i = 0; i < rotation; i++)
            dir = dir.getRotation(face.getOpposite());

        Vec3d v = new Vec3d(0, 0, 0).add(dir);
        v.rotateUndo(face, Vec3d.center);

        return v.toForgeDirection();
    }

    public ForgeDirection getFD() {

        return d;
    }

    public static Dir getDirection(ForgeDirection direction, ForgeDirection face, int rotation) {

        Vec3d v = new Vec3d(0, 0, 0).add(direction);
        v.rotate(face, Vec3d.center);

        ForgeDirection dir = v.toForgeDirection();
        for (int i = 0; i < rotation; i++)
            dir = dir.getRotation(face);

        return fromFD(dir);
    }

    private static Dir fromFD(ForgeDirection forgeDirection) {

        for (Dir d : values())
            if (d.d == forgeDirection)
                return d;

        return null;
    }

    public Dir getOpposite() {

        switch (this) {
        case BACK:
            return FRONT;
        case FRONT:
            return BACK;
        case LEFT:
            return RIGHT;
        case RIGHT:
            return LEFT;
        case TOP:
            return BOTTOM;
        default:
            return TOP;
        }
    }

    public String getLocalizedName() {

        return I18n.format("direction." + name().toLowerCase());
    }

}
