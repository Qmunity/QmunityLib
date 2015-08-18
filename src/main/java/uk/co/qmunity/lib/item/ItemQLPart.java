package uk.co.qmunity.lib.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.QLModInfo;
import uk.co.qmunity.lib.part.IQLPart;
import uk.co.qmunity.lib.part.MultipartCompat;
import uk.co.qmunity.lib.transform.Rotation;
import uk.co.qmunity.lib.vec.BlockPos;
import uk.co.qmunity.lib.vec.Vector3;

public abstract class ItemQLPart extends Item {

    public ItemQLPart() {

        setUnlocalizedName(QLModInfo.MODID + ":multipart");
    }

    private double getHitDepth(Vector3 vhit, int side) {

        return vhit.copy().scalarProject(Rotation.axes[side]) + (side % 2 ^ 1);
    }

    private boolean place(World world, int x, int y, int z, ForgeDirection side, Vector3 hit, ItemStack item, EntityPlayer player) {

        IQLPart part = newPart(world, x, y, z, side, hit, item, player);
        if (part == null || !MultipartCompat.canAddPart(world, x, y, z, part))
            return false;

        if (!world.isRemote)
            MultipartCompat.addPart(world, x, y, z, part);
        if (!player.capabilities.isCreativeMode)
            item.stackSize -= 1;
        return true;
    }

    @Override
    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY,
            float hitZ) {

        BlockPos pos = new BlockPos(x, y, z);
        Vector3 hit = new Vector3(hitX, hitY, hitZ);
        double d = getHitDepth(hit, side);
        ForgeDirection s = ForgeDirection.getOrientation(side);

        if (d < 1 && place(world, pos.x, pos.y, pos.z, s, hit, item, player))
            return true;

        pos.offset(s);
        return place(world, pos.x, pos.y, pos.z, s, hit, item, player);
    }

    /**
     * Create a new part based on the placement information parameters.
     */
    public abstract IQLPart newPart(World world, int x, int y, int z, ForgeDirection side, Vector3 hit, ItemStack item, EntityPlayer player);

}
