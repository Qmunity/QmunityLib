package com.qmunity.lib.part;

import java.util.List;

import com.qmunity.lib.vec.Vec3dCube;

public interface IPartOccluding {

    public List<Vec3dCube> getOcclusionBoxes();

}
