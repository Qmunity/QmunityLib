package uk.co.qmunity.lib.raytrace;

import net.minecraft.util.MovingObjectPosition;
import uk.co.qmunity.lib.part.IQLPart;
import uk.co.qmunity.lib.vec.Cuboid;

public class QMovingObjectPosition extends MovingObjectPosition {

    public IQLPart part;
    public Cuboid cube;

    public QMovingObjectPosition(MovingObjectPosition mop) {

        super(mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, mop.hitVec);
        hitInfo = mop.hitInfo;
    }

    public QMovingObjectPosition(MovingObjectPosition mop, IQLPart part) {

        this(mop);
        this.part = part;
    }

    public QMovingObjectPosition(MovingObjectPosition mop, Cuboid cube) {

        this(mop);
        this.cube = cube;
    }

    public QMovingObjectPosition(MovingObjectPosition mop, IQLPart part, Cuboid cube) {

        this(mop, part);
        this.cube = cube;
    }

}
