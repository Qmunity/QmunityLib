package uk.co.qmunity.lib.part;

import java.util.List;

import net.minecraft.entity.Entity;
import uk.co.qmunity.lib.vec.Vec3dCube;

/**
 * Interface implemented by parts that have a collision box.
 *
 * @author amadornes
 */
public interface IPartCollidable extends IPart {

    /**
     * Adds all of this part's collision boxes to the list. These boxes can depend on the entity that's colliding with them.
     */
    public void addCollisionBoxesToList(List<Vec3dCube> boxes, Entity entity);

}
