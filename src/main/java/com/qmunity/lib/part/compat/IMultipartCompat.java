package com.qmunity.lib.part.compat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.qmunity.lib.part.IPart;
import com.qmunity.lib.part.ITilePartHolder;
import com.qmunity.lib.vec.Vec3i;

public interface IMultipartCompat {

    public boolean addPartToWorld(IPart part, World world, Vec3i location, EntityPlayer player);

    public boolean isMultipart(World world, Vec3i location);

    public int getStrongRedstoneOuput(World world, Vec3i location, ForgeDirection direction, ForgeDirection face);

    public int getWeakRedstoneOuput(World world, Vec3i location, ForgeDirection direction, ForgeDirection face);

    public ITilePartHolder getPartHolder(World world, Vec3i location);

}
