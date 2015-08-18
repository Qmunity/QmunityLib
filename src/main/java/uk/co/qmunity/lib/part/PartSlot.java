package uk.co.qmunity.lib.part;

import net.minecraftforge.common.util.ForgeDirection;

public enum PartSlot {
    DOWN, UP, WEST, EAST, NORTH, SOUTH, //
    CENTER, //
    CORNER_NNN, CORNER_NPN, CORNER_NNP, CORNER_NPP, CORNER_PNN, CORNER_PPN, CORNER_PNP, CORNER_PPP, //
    EDGE_NYN, EDGE_NYP, EDGE_PYN, EDGE_PYP, EDGE_NNZ, EDGE_PNZ, EDGE_NPZ, EDGE_PPZ, EDGE_XNN, EDGE_XPN, EDGE_XNP, EDGE_XPP;

    private static int[] edgeBetweenMap = new int[] { -1, -1, 8, 10, 4, 5, -1, -1, 9, 11, 6, 7, -1, -1, -1, -1, 0, 2, -1, -1, -1, -1, 1, 3 };

    public final int mask;

    private PartSlot() {

        this.mask = 1 << ordinal();
    }

    public static PartSlot face(ForgeDirection face) {

        return values()[face.ordinal()];
    }

    public static PartSlot edgeBetween(ForgeDirection side1, ForgeDirection side2) {

        if (side2.ordinal() < side1.ordinal())
            return edgeBetween(side2, side1);
        if ((side1.ordinal() & 6) == (side2.ordinal() & 6))
            throw new IllegalArgumentException("Faces " + side1 + " and " + side2 + " are opposites");
        return values()[15 + edgeBetweenMap[side1.ordinal() * 6 + side2.ordinal()]];
    }

}
