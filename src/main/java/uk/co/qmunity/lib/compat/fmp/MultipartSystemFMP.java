package uk.co.qmunity.lib.compat.fmp;

import net.minecraft.world.World;
import uk.co.qmunity.lib.part.IMultipartSystem;
import uk.co.qmunity.lib.part.IQLPart;
import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;

public class MultipartSystemFMP implements IMultipartSystem {

    @Override
    public int getPriority() {

        return -1000;
    }

    @Override
    public boolean canAddPart(World world, int x, int y, int z, IQLPart part) {

        FMPPart holder = getHolder(world, x, y, z);
        if (holder != null)
            return holder.canAddPart(part);
        return TileMultipart.canPlacePart(world, new BlockCoord(x, y, z), new FMPPart(part));
    }

    @Override
    public void addPart(World world, int x, int y, int z, IQLPart part) {

        addPart(world, x, y, z, part, null);
    }

    @Override
    public void addPart(World world, int x, int y, int z, IQLPart part, String partID) {

        FMPPart holder = getHolder(world, x, y, z);
        boolean isNew = false;

        if (holder == null) {
            if (world.isRemote)
                return;
            holder = new FMPPart();
            isNew = true;
            holder.tile_$eq(new TileMultipart());
            holder.tile().setWorldObj(world);
            holder.tile().xCoord = x;
            holder.tile().yCoord = y;
            holder.tile().zCoord = z;
        }

        if (partID != null)
            holder.addPart(partID, part, false);
        else
            holder.addPart(part);

        if (isNew) {
            holder.firstTick = false;
            TileMultipart.addPart(world, new BlockCoord(x, y, z), holder);
        }
    }

    @Override
    public FMPPart getHolder(World world, int x, int y, int z) {

        TileMultipart tmp = TileMultipart.getTile(world, new BlockCoord(x, y, z));
        if (tmp == null)
            return null;
        for (TMultiPart p : tmp.jPartList())
            if (p instanceof FMPPart)
                return (FMPPart) p;
        return null;
    }

}
