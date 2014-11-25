package uk.co.qmunity.lib.part;

import java.util.List;
import java.util.Map;

import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.vec.IWorldLocation;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public interface ITilePartHolder extends IWorldLocation {

    @Override
    public World getWorld();

    @Override
    public int getX();

    @Override
    public int getY();

    @Override
    public int getZ();

    public List<IPart> getParts();

    public boolean canAddPart(IPart part);

    public void addPart(IPart part);

    public boolean removePart(IPart part);

    public QMovingObjectPosition rayTrace(Vec3d start, Vec3d end);

    public void addCollisionBoxesToList(List<Vec3dCube> boxes, AxisAlignedBB bounds, Entity entity);

    public Map<String, IPart> getPartMap();

}