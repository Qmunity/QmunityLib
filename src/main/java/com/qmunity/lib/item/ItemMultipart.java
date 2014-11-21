package com.qmunity.lib.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.qmunity.lib.part.IPart;
import com.qmunity.lib.part.PartRegistry;
import com.qmunity.lib.part.compat.MultipartCompatibility;
import com.qmunity.lib.ref.Names;
import com.qmunity.lib.vec.Vec3i;

public abstract class ItemMultipart extends Item {

    public ItemMultipart() {

        setUnlocalizedName(Names.Unlocalized.Items.MULTIPART);
    }

    @Override
    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int face, float x_, float y_, float z_) {

        IPart part = createPart(item, player, world,
                new MovingObjectPosition(x, y, z, face, Vec3.createVectorHelper(x + x_, y + y_, z + z_)));

        if (part == null)
            return false;

        ForgeDirection dir = ForgeDirection.getOrientation(face);
        return MultipartCompatibility.placePartInWorld(part, world, new Vec3i(x, y, z), dir, player, item);
    }

    public abstract String getCreatedPartType(ItemStack item, EntityPlayer player, World world, MovingObjectPosition mop);

    public IPart createPart(ItemStack item, EntityPlayer player, World world, MovingObjectPosition mop) {

        return PartRegistry.createPart(getCreatedPartType(item, player, world, mop), world.isRemote);
    }

}
