package uk.co.qmunity.lib.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public abstract class QLBlockBase extends Block {

    private Object modInstance;

    public QLBlockBase(Material material) {

        super(material);
        setStepSound(soundTypeStone);
        setHardness(3.0F);
    }

    public QLBlockBase(Material material, String name) {

        this(material);
        setBlockName(name);
        setBlockTextureName(getModId() + ":" + name);
    }

    @Override
    public String getUnlocalizedName() {

        return String.format("tile.%s:%s", getModId(), getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    protected abstract String getModId();

    protected Object getModInstance() {

        if (modInstance != null)
            return modInstance;

        String modid = getModId();
        for (ModContainer mod : Loader.instance().getActiveModList()) {
            if (mod.getModId().equals(modid)) {
                modInstance = mod.getMod();
                break;
            }
        }

        return modInstance;
    }

    protected String getUnwrappedUnlocalizedName(String name) {

        return name.substring(name.indexOf(".") + 1);
    }

}
