package uk.co.qmunity.lib.part;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.QLBlocks;
import uk.co.qmunity.lib.block.BlockMultipart;
import uk.co.qmunity.lib.helper.RedstoneHelper.IQLRedstoneProvider;
import uk.co.qmunity.lib.tile.TileMultipart;
import uk.co.qmunity.lib.vec.Cuboid;

public class MultipartSystemStandalone implements IMultipartSystem, IQLRedstoneProvider {

    @Override
    public int getPriority() {

        return 1000;
    }

    @Override
    public boolean canAddPart(World world, int x, int y, int z, IQLPart part) {

        for (Cuboid c : part.getCollisionBoxes())
            if (!world.checkNoEntityCollision(c.toAABB().offset(x, y, z)))
                return false;

        TileMultipart te = BlockMultipart.findTile(world, x, y, z);
        if (te != null)
            return te.canAddPart(part);

        Block block = world.getBlock(x, y, z);
        return block.isAir(world, x, y, z) || block.isReplaceable(world, x, y, z);
    }

    @Override
    public void addPart(World world, int x, int y, int z, IQLPart part) {

        addPart(world, x, y, z, part, null);
    }

    @Override
    public void addPart(World world, int x, int y, int z, IQLPart part, String partID) {

        TileMultipart te = BlockMultipart.findTile(world, x, y, z);

        if (te == null) {
            te = new TileMultipart();
            te.setWorldObj(world);
            te.xCoord = x;
            te.yCoord = y;
            te.zCoord = z;

            world.setBlock(x, y, z, QLBlocks.multipart);
            world.setTileEntity(x, y, z, te);
            te.firstTick = false;
        }

        if (partID != null)
            te.addPart(partID, part, true);
        else
            te.addPart(part);
    }

    @Override
    public TileMultipart getHolder(World world, int x, int y, int z) {

        return BlockMultipart.findTile(world, x, y, z);
    }

    @Override
    public boolean canProvideRedstoneFor(World world, int x, int y, int z) {

        return getHolder(world, x, y, z) != null;
    }

    @Override
    public boolean canConnectRedstone(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

        TileMultipart te = getHolder(world, x, y, z);
        if (te != null)
            return te.canConnectRedstone(face, side);
        return false;
    }

    @Override
    public int getWeakRedstoneOutput(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

        TileMultipart te = getHolder(world, x, y, z);
        if (te != null)
            return te.getWeakRedstoneOutput(face, side);
        return 0;
    }

    @Override
    public int getStrongRedstoneOutput(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

        TileMultipart te = getHolder(world, x, y, z);
        if (te != null)
            return te.getStrongRedstoneOutput(face, side);
        return 0;
    }
}
