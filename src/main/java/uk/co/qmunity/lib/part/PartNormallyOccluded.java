package uk.co.qmunity.lib.part;

import java.util.Arrays;
import java.util.List;

import uk.co.qmunity.lib.vec.Vec3dCube;
import net.minecraft.item.ItemStack;

public final class PartNormallyOccluded extends PartBase implements IPartOccluding {

    private Vec3dCube cube;

    public PartNormallyOccluded(Vec3dCube cube) {

        this.cube = cube;
    }

    @Override
    public String getType() {

        return null;
    }

    @Override
    public ItemStack getItem() {

        return null;
    }

    @Override
    public List<Vec3dCube> getOcclusionBoxes() {

        return Arrays.asList(cube);
    }

}
