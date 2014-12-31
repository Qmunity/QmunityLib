package uk.co.qmunity.lib.util;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.Vec3d;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public enum Dir {

    FRONT(ForgeDirection.NORTH), RIGHT(ForgeDirection.EAST), BACK(ForgeDirection.SOUTH), LEFT(ForgeDirection.WEST), TOP(ForgeDirection.UP), BOTTOM(
            ForgeDirection.DOWN);

    private ForgeDirection d;

    private Dir(ForgeDirection d) {

        this.d = d;
    }

    public ForgeDirection toForgeDirection(ForgeDirection face, int rotation) {

        ForgeDirection d = this.d;
        for (int i = 0; i < rotation; i++)
            d = d.getRotation(ForgeDirection.UP);

        d = new Vec3d(0, 0, 0).add(d).rotate(face, new Vec3d(0, 0, 0)).toForgeDirection();

        return d;
    }

    public ForgeDirection getFD() {

        return d;
    }

    public static Dir getDirection(ForgeDirection direction, ForgeDirection face, int rotation) {

        ForgeDirection d = new Vec3d(0, 0, 0).add(direction).rotateUndo(face, new Vec3d(0, 0, 0)).toForgeDirection();

        for (int i = 0; i < rotation; i++)
            d = d.getRotation(ForgeDirection.DOWN);

        return fromFD(d);
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

    @SideOnly(Side.CLIENT)
    public String getLocalizedName() {

        return I18n.format("direction." + name().toLowerCase());
    }

}
