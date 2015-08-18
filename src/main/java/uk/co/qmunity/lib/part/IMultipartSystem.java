package uk.co.qmunity.lib.part;

import net.minecraft.world.World;

public interface IMultipartSystem {

    public int getPriority();

    public boolean canAddPart(World world, int x, int y, int z, IQLPart part);

    public void addPart(World world, int x, int y, int z, IQLPart part);

    public void addPart(World world, int x, int y, int z, IQLPart part, String partID);

    public IPartHolder getHolder(World world, int x, int y, int z);

}
