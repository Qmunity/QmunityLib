package uk.co.qmunity.lib.part;

import java.util.List;

import uk.co.qmunity.lib.vec.Vec3dCube;
import net.minecraft.entity.Entity;

public interface IPartCollidable extends IPart {

    public void addCollisionBoxesToList(List<Vec3dCube> boxes, Entity entity);

}
