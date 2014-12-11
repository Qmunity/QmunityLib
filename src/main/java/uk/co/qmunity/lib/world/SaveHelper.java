package uk.co.qmunity.lib.world;

import java.io.File;

import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class SaveHelper {

    public static final File getSaveLocation(World world) {

        File base = DimensionManager.getCurrentSaveRootDirectory();
        return world.provider.dimensionId == 0 ? base : new File(base, world.provider.getSaveFolder());
    }
}
