package uk.co.qmunity.lib.raytrace;

import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import net.minecraft.util.MovingObjectPosition;

public class QMovingObjectPosition extends MovingObjectPosition {

    private IPart part;
    private Vec3dCube cube;

    public QMovingObjectPosition(MovingObjectPosition mop) {

        super(mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, mop.hitVec);
    }

    public QMovingObjectPosition(MovingObjectPosition mop, IPart part) {

        this(mop);
        this.part = part;
    }

    public QMovingObjectPosition(MovingObjectPosition mop, Vec3dCube cube) {

        this(mop);
        this.cube = cube;
    }

    public QMovingObjectPosition(MovingObjectPosition mop, IPart part, Vec3dCube cube) {

        this(mop, part);
        this.cube = cube;
    }

    public IPart getPart() {

        return part;
    }

    public Vec3dCube getCube() {

        return cube;
    }

    public double distanceTo(Vec3d pos) {

        return pos.distanceTo(hitVec.xCoord, hitVec.yCoord, hitVec.zCoord);
    }

}
