package com.qmunity.lib.misc;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.util.ForgeDirection;

public class ForgeDirectionUtils {

    public static int getSide(ForgeDirection dir) {

        for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
            if (ForgeDirection.VALID_DIRECTIONS[i] == dir)
                return i;
        }
        return -1;
    }

    /**
     * Returns the ForgeDirection of the facing of the entity given.
     * 
     * @param entity
     * @param includeUpAndDown
     *            false when UP/DOWN should not be included.
     * @return
     */
    public static ForgeDirection getDirectionFacing(EntityLivingBase entity, boolean includeUpAndDown) {

        double yaw = entity.rotationYaw;
        while (yaw < 0)
            yaw += 360;
        yaw = yaw % 360;
        if (includeUpAndDown) {
            if (entity.rotationPitch > 45)
                return ForgeDirection.DOWN;
            else if (entity.rotationPitch < -45)
                return ForgeDirection.UP;
        }
        if (yaw < 45)
            return ForgeDirection.SOUTH;
        else if (yaw < 135)
            return ForgeDirection.WEST;
        else if (yaw < 225)
            return ForgeDirection.NORTH;
        else if (yaw < 315)
            return ForgeDirection.EAST;

        else
            return ForgeDirection.SOUTH;
    }

    public static ForgeDirection getOnFace(ForgeDirection face, ForgeDirection dir) {

        switch (face) {
        case DOWN:
            return dir;
        case UP:
            if (dir == ForgeDirection.UP || dir == ForgeDirection.DOWN)
                return dir.getOpposite();
            return dir;
        case WEST:
            switch (dir) {
            case DOWN:
                return ForgeDirection.WEST;
            case UP:
                return ForgeDirection.EAST;
            case WEST:
                return ForgeDirection.DOWN;
            case EAST:
                return ForgeDirection.UP;
            case NORTH:
                return ForgeDirection.NORTH;
            case SOUTH:
                return ForgeDirection.SOUTH;
            default:
                break;
            }
            break;
        case EAST:
            switch (dir) {
            case DOWN:
                return ForgeDirection.EAST;
            case UP:
                return ForgeDirection.WEST;
            case WEST:
                return ForgeDirection.DOWN;
            case EAST:
                return ForgeDirection.UP;
            case NORTH:
                return ForgeDirection.NORTH;
            case SOUTH:
                return ForgeDirection.SOUTH;
            default:
                break;
            }
            break;
        case NORTH:
            switch (dir) {
            case DOWN:
                return ForgeDirection.NORTH;
            case UP:
                return ForgeDirection.SOUTH;
            case WEST:
                return ForgeDirection.WEST;
            case EAST:
                return ForgeDirection.EAST;
            case NORTH:
                return ForgeDirection.DOWN;
            case SOUTH:
                return ForgeDirection.UP;
            default:
                break;
            }
            break;
        case SOUTH:
            switch (dir) {
            case DOWN:
                return ForgeDirection.SOUTH;
            case UP:
                return ForgeDirection.NORTH;
            case WEST:
                return ForgeDirection.WEST;
            case EAST:
                return ForgeDirection.EAST;
            case NORTH:
                return ForgeDirection.DOWN;
            case SOUTH:
                return ForgeDirection.UP;
            default:
                break;
            }
            break;
        default:
            break;
        }

        return null;
    }

}
