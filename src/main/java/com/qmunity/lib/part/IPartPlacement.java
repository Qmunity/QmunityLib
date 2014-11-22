package com.qmunity.lib.part;

import net.minecraft.world.World;

import com.qmunity.lib.part.compat.IMultipartCompat;
import com.qmunity.lib.vec.Vec3i;

public interface IPartPlacement {

    public boolean placePart(IPart part, World world, Vec3i location, IMultipartCompat multipartSystem, boolean simulated);

}
