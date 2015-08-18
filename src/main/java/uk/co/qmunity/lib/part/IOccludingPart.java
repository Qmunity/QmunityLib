package uk.co.qmunity.lib.part;

import java.util.List;

import uk.co.qmunity.lib.vec.Cuboid;

public interface IOccludingPart extends IQLPart {

    public List<Cuboid> getOcclusionBoxes();

}
