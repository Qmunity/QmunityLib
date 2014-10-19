package com.qmunity.lib.part;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import com.qmunity.lib.raytrace.QMovingObjectPosition;
import com.qmunity.lib.vec.Vec3d;
import com.qmunity.lib.vec.Vec3dCube;

public interface ITilePartHolder {

    public World getWorld();

    public int getX();

    public int getY();

    public int getZ();

    public List<IPart> getParts();

    public boolean canAddPart(IPart part);

    public void addPart(IPart part);

    public boolean removePart(IPart part);

    public void sendPartUpdate(IPart part);

    public QMovingObjectPosition rayTrace(Vec3d start, Vec3d end);

    public void addCollisionBoxesToList(List<Vec3dCube> boxes, AxisAlignedBB bounds, Entity entity);

    public boolean checkOcclusion(Vec3dCube cube);

}
