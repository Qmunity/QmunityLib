package com.qmunity.lib.part;

import java.util.List;

import net.minecraft.entity.Entity;

import com.qmunity.lib.vec.Vec3dCube;

public interface IPartCollidable {

    public void addCollisionBoxesToList(List<Vec3dCube> boxes, Entity entity);

}
