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
            dir = dir.getRotation(ForgeDirection.DOWN);

        return new Vec3d(0, 0, 0).add(dir).rotateUndo(face, new Vec3d(0, 0, 0)).toForgeDirection();
    }

    public ForgeDirection getFD() {

        return d;
    }

    public static Dir getDirection(ForgeDirection direction, ForgeDirection face, int rotation) {

        ForgeDirection dir = new Vec3d(0, 0, 0).add(direction).rotate(face, new Vec3d(0, 0, 0)).toForgeDirection();
        for (int i = 0; i < rotation; i++)
            dir = dir.getRotation(ForgeDirection.UP);

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
