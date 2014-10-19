package com.qmunity.lib.part;

import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.qmunity.lib.vec.Vec3dCube;

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
