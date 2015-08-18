package uk.co.qmunity.lib;

import net.minecraft.block.Block;
import uk.co.qmunity.lib.block.BlockMultipart;
import uk.co.qmunity.lib.tile.TileMultipart;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(QLModInfo.MODID)
public class QLBlocks {

    public static Block multipart;

    public static void init() {

        instantiate();
        register();
    }

    private static void instantiate() {

        multipart = new BlockMultipart();
    }

    private static void register() {

        GameRegistry.registerBlock(multipart, QLModInfo.MODID + ".multipart");
        GameRegistry.registerTileEntity(TileMultipart.class, QLModInfo.MODID + ".multipart");
    }
}
