package com.qmunity.lib.part.compat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.qmunity.lib.part.IPart;
import com.qmunity.lib.vec.Vec3i;

public class MultipartCompatibility {

    public static boolean addPartToWorld(IPart part, World world, Vec3i location, ForgeDirection clickedFace, EntityPlayer player, ItemStack item) {

        if (!player.isSneaking()) {
            for (MultipartSystem s : MultipartSystem.getAvailableSystems()) {
                if (world.isAirBlock(location.getX(), location.getY(), location.getZ()) || s.getCompat().isMultipart(world, location)) {
                    if (s.getCompat().addPartToWorld(part, world, location, player)) {
                        if (!player.capabilities.isCreativeMode)
                            item.stackSize -= 1;
                        return true;
                    }
                }
            }
        }

        location = location.clone().add(clickedFace);

        for (MultipartSystem s : MultipartSystem.getAvailableSystems()) {
            if (world.isAirBlock(location.getX(), location.getY(), location.getZ()) || s.getCompat().isMultipart(world, location)) {
                if (s.getCompat().addPartToWorld(part, world, location, player)) {
                    if (!player.capabilities.isCreativeMode)
                        item.stackSize -= 1;
                    return true;
                }
            }
        }

        return false;
    }

}
