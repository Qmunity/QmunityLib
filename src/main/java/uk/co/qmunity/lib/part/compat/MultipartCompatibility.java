package uk.co.qmunity.lib.part.compat;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartCustomPlacement;
import uk.co.qmunity.lib.part.IPartPlacement;
import uk.co.qmunity.lib.part.ITilePartHolder;
import uk.co.qmunity.lib.part.PartPlacementDefault;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

public class MultipartCompatibility {

    public static boolean addPartToWorld(IPart part, World world, Vec3i location) {

        return addPartToWorld(part, world, location, false);
    }

    public static boolean addPartToWorld(IPart part, World world, Vec3i location, boolean simulated) {

        for (MultipartSystem s : MultipartSystem.getAvailableSystems())
            if (world.isAirBlock(location.getX(), location.getY(), location.getZ()) || s.getCompat().isMultipart(world, location))
                if (s.getCompat().addPartToWorld(part, world, location, simulated))
                    return true;
        return false;
    }

    public static boolean addPartToWorldBruteforce(IPart part, World world, Vec3i location) {

        for (MultipartSystem s : MultipartSystem.getAvailableSystems())
            if (world.isAirBlock(location.getX(), location.getY(), location.getZ()) || s.getCompat().isMultipart(world, location))
                if (s.getCompat().addPartToWorldBruteforce(part, world, location))
                    return true;
        return false;
    }

    public static boolean placePartInWorld(IPart part, World world, Vec3i location, ForgeDirection clickedFace, EntityPlayer player,
            ItemStack item) {

        return placePartInWorld(part, world, location, clickedFace, player, item, false);
    }

    public static boolean placePartInWorld(IPart part, World world, Vec3i location, ForgeDirection clickedFace, EntityPlayer player,
            ItemStack item, boolean simulated) {

        if (simulated)
            PartUpdateManager.setUpdatesEnabled(false);

        Map<IMultipartCompat, Integer> passes = new HashMap<IMultipartCompat, Integer>();
        int totalPasses = 0;
        for (MultipartSystem s : MultipartSystem.getAvailableSystems()) {
            IMultipartCompat c = s.getCompat();
            int p = c.getPlacementPasses();
            passes.put(c, p);
            totalPasses += p;
        }

        for (int pass = 0; pass < totalPasses; pass++) {
            for (IMultipartCompat c : passes.keySet()) {
                if (pass >= passes.get(c))
                    continue;

                if (c.placePartInWorld(part, world, location.clone(), clickedFace, player, item, pass, simulated)) {
                    if (!player.capabilities.isCreativeMode)
                        item.stackSize -= 1;

                    if (simulated)
                        PartUpdateManager.setUpdatesEnabled(true);
                    return true;
                }
            }
        }

        if (simulated)
            PartUpdateManager.setUpdatesEnabled(true);

        return false;
    }

    public static ITilePartHolder getPartHolder(World world, Vec3i location) {

        if (world == null)
            return null;

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

    public static IPartPlacement getPlacementForPart(IPart part, World world, Vec3i location, ForgeDirection face,
            MovingObjectPosition mop, EntityPlayer player) {

        IPartPlacement placement = null;

        if (!(part instanceof IPartCustomPlacement))
            return new PartPlacementDefault();
        placement = ((IPartCustomPlacement) part).getPlacement(part, world, location, face, mop, player);
        if (placement != null)
            return placement;

        return null;
    }
}
