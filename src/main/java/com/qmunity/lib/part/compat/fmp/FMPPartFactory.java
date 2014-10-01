package com.qmunity.lib.part.compat.fmp;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.MultiPartRegistry.IPartConverter;
import codechicken.multipart.MultiPartRegistry.IPartFactory;
import codechicken.multipart.TMultiPart;

import com.qmunity.lib.QLModInfo;
import com.qmunity.lib.block.BlockMultipart;
import com.qmunity.lib.init.QLBlocks;
import com.qmunity.lib.tile.TileMultipart;

public class FMPPartFactory implements IPartFactory, IPartConverter {

    public static final void register() {

        FMPPartFactory reg = new FMPPartFactory();

        MultiPartRegistry.registerParts(reg, new String[] { QLModInfo.MODID + ".multipart" });
        MultiPartRegistry.registerConverter(reg);
    }

    @Override
    public Iterable<Block> blockTypes() {

        return Arrays.asList(QLBlocks.multipart);
    }

    @Override
    public TMultiPart convert(World world, BlockCoord loc) {

        TileMultipart te = BlockMultipart.get(world, loc.x, loc.y, loc.z);
        if (te == null || te.getParts().size() == 0) {
            world.setBlock(loc.x, loc.y, loc.z, Blocks.air);
            return null;
        }

        return new FMPPart(te.getPartMap());
    }

    @Override
    public TMultiPart createPart(String type, boolean client) {

        if (type.equals(QLModInfo.MODID + ".multipart"))
            return new FMPPart();

        return null;
    }

}
