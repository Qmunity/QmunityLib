package uk.co.qmunity.lib.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public abstract class BlockBase extends Block {

    public BlockBase(Material material) {

        super(material);
        setStepSound(soundTypeStone);
        blockHardness = 3.0F;
    }

    @Override
    public String getUnlocalizedName() {

        return String.format("tile.%s:%s", getModId(), getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    protected abstract String getModId();

    protected String getUnwrappedUnlocalizedName(String name) {

        return name.substring(name.indexOf(".") + 1);
    }

}
