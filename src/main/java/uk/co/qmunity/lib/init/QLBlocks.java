package uk.co.qmunity.lib.init;

import uk.co.qmunity.lib.QLModInfo;
import uk.co.qmunity.lib.block.BlockMultipart;
import uk.co.qmunity.lib.ref.Names;
import uk.co.qmunity.lib.tile.TileMultipart;
import net.minecraft.block.Block;
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

        GameRegistry.registerBlock(multipart, Names.Registry.Blocks.MULTIPART);
        GameRegistry.registerTileEntity(TileMultipart.class, Names.Registry.Blocks.MULTIPART);
    }
}
