package com.qmunity.lib.init;

import net.minecraft.block.Block;

import com.qmunity.lib.QLModInfo;
import com.qmunity.lib.block.BlockMultipart;
import com.qmunity.lib.ref.Names;
import com.qmunity.lib.tile.TileMultipart;

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
