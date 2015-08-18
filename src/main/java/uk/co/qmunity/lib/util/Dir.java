package uk.co.qmunity.lib.util;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.transform.Rotation;
import uk.co.qmunity.lib.vec.BlockPos;
import uk.co.qmunity.lib.vec.Vector3;
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

        return new Vector3(BlockPos.sideOffsets[d.ordinal()]).//
                apply(Rotation.quarterRotations[rotation % 4]).//
                apply(Rotation.sideRotations[face.ordinal()]).//
                toBlockPos().toSide();
    }

    public ForgeDirection getFD() {

        return d;
    }

    public static Dir getDirection(ForgeDirection direction, ForgeDirection face, int rotation) {

        return fromFD(new Vector3(BlockPos.sideOffsets[direction.ordinal()]).//
                apply(Rotation.sideRotations[face.ordinal()].inverse()).//
                apply(Rotation.quarterRotations[rotation % 4].inverse()).//
                toBlockPos().toSide());
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

        return I18n.format("bluepower:direction." + name().toLowerCase());
    }

}
