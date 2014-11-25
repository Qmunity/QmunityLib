package uk.co.qmunity.lib.part;

import java.util.List;

import uk.co.qmunity.lib.vec.Vec3dCube;

public interface IPartOccluding extends IPart {

    public List<Vec3dCube> getOcclusionBoxes();

}
