package uk.co.qmunity.lib.util;

import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.BlockPos;

public interface IRotatable {

    public boolean rotate(ForgeDirection axis);

    public static interface IRotatableFace {

        public void setFaceRotation(ForgeDirection face, int rotation);

        public int getFaceRotation(ForgeDirection face);

        public boolean canRotateFace(ForgeDirection face);

    }

    public static interface IFacing {

        public void setFacingDirection(ForgeDirection dir);

        public ForgeDirection getFacingDirection();

        public boolean canFaceDirection(ForgeDirection dir);

    }

    public static interface IAxial {

        public void setAxis(EnumAxis axis);

        public EnumAxis getAxis();

        public boolean isValidAxis(EnumAxis axis);

    }

    public static enum EnumAxis {
        X(5), Y(1), Z(3);

        private int positiveDir;

        private EnumAxis(int positiveDir) {

            this.positiveDir = positiveDir;
        }

        public BlockPos getAxis() {

            return BlockPos.sideOffsets[positiveDir];
        }
    }

}
