package uk.co.qmunity.lib.part.compat.fmp;

import java.util.Arrays;

import uk.co.qmunity.lib.QLModInfo;
import uk.co.qmunity.lib.block.BlockMultipart;
import uk.co.qmunity.lib.init.QLBlocks;
import uk.co.qmunity.lib.tile.TileMultipart;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.MultiPartRegistry.IPartConverter;
import codechicken.multipart.MultiPartRegistry.IPartFactory;
import codechicken.multipart.TMultiPart;

public class FMPPartFactory implements IPartFactory, IPartConverter {

    public static final void register() {

        FMPPartFactory reg = new FMPPartFactory();

        MultiPartRegistry.registerParts(reg, new String[] { QLModInfo.MODID + "_multipart" });
        MultiPartRegistry.registerConverter(reg);
    }

    @Override
    public Iterable<Block> blockTypes() {

        return Arrays.asList(QLBlocks.multipart);
    }

    @Override
    public TMultiPart convert(World world, BlockCoord loc) {

        TileMultipart te = BlockMultipart.get(world, loc.x, loc.y, loc.z);
        if (te == null)
            return null;

        return new FMPPart(te.getPartMap());
    }

    @Override
    public TMultiPart createPart(String type, boolean client) {

        if (type.equals(QLModInfo.MODID + "_multipart"))
            return new FMPPart();

        return null;
    }

}
