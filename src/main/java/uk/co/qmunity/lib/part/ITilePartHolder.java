package uk.co.qmunity.lib.part;

import java.util.List;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.vec.IWorldLocation;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;

/**
 * Inteface implemented by objects that want to handle parts
 *
 * @author amadornes
 */
public interface ITilePartHolder extends IWorldLocation {

    /**
     * Gets the world this part holder is in.
     */
    @Override
    public World getWorld();

    /**
     * Gets the X coordinate of this part holder.
     */
    @Override
    public int getX();

    /**
     * Gets the Y coordinate of this part holder.
     */
    @Override
    public int getY();

    /**
     * Gets the Z coordinate of this part holder.
     */
    @Override
    public int getZ();

    /**
     * Gets a list with all the parts in this part holder.
     */
    public List<IPart> getParts();

    /**
     * Checks whether or not the specified part can be added to this part holder.
     */
    public boolean canAddPart(IPart part);

    /**
     * Adds the specified part to this holder.
     */
    public void addPart(IPart part);

    /**
     * Removes the specified part from this holder.
     */
    public boolean removePart(IPart part);

    /**
     * Raytraces all the parts in this holder.
     */
    public QMovingObjectPosition rayTrace(Vec3d start, Vec3d end);

    /**
     * Gets all the collision boxes in this holder.
     */
    public void addCollisionBoxesToList(List<Vec3dCube> boxes, AxisAlignedBB bounds, Entity entity);

    /**
     * Gets a map of the part IDs and parts in this holder.
     */
    public Map<String, IPart> getPartMap();

    /**
     * Gets a list of microblocks in this holder. They should also be included in getParts() and getPartMap() if they're part of this part holder. In
     * the case of FMP, where the microblocks are parts outside this holder, it's not needed.
     */
    public List<IMicroblock> getMicroblocks();

    /**
     * Gets whether or not this tile is simulated (not in world). Used for part placement and displaying a ghost image of the part before placing it.
     */
    public boolean isSimulated();

}