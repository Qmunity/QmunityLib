package com.qmunity.lib.part.compat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.qmunity.lib.part.IPart;
import com.qmunity.lib.part.ITilePartHolder;
import com.qmunity.lib.vec.Vec3dCube;
import com.qmunity.lib.vec.Vec3i;

public class MultipartCompatibility {

    public static boolean addPartToWorld(IPart part, World world, Vec3i location, ForgeDirection clickedFace, EntityPlayer player,
            ItemStack item) {

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

    public static ITilePartHolder getPartHolder(World world, Vec3i location) {

        for (MultipartSystem s : MultipartSystem.getAvailableSystems())
            if (s.getCompat().isMultipart(world, location))
                return s.getCompat().getPartHolder(world, location);

        return null;
    }

    public static ITilePartHolder getPartHolder(World world, int x, int y, int z) {

        return getPartHolder(world, new Vec3i(x, y, z));
    }

    public static IPart getPart(World world, Vec3i location, String type) {

        ITilePartHolder h = getPartHolder(world, location);
        if (h == null)
            return null;

        for (IPart p : h.getParts())
            if (p.getType() == type)
                return p;

        return null;
    }

    public static IPart getPart(World world, int x, int y, int z, String type) {

        return getPart(world, new Vec3i(x, y, z), type);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getPart(World world, Vec3i location, Class<T> type) {

        ITilePartHolder h = getPartHolder(world, location);
        if (h == null)
            return null;

        for (IPart p : h.getParts())
            if (type.isAssignableFrom(p.getClass()))
                return (T) p;

        return null;
    }

    public static <T> T getPart(World world, int x, int y, int z, Class<T> type) {

        return getPart(world, new Vec3i(x, y, z), type);
    }

    public static boolean checkOcclusion(World world, Vec3i location, Vec3dCube cube) {

        for (MultipartSystem s : MultipartSystem.getAvailableSystems())
            if (s.getCompat().isMultipart(world, location))
                return s.getCompat().checkOcclusion(world, location, cube);

        return false;
    }

    public static boolean checkOcclusion(World world, int x, int y, int z, Vec3dCube cube) {

        return checkOcclusion(world, new Vec3i(x, y, z), cube);
    }
}
