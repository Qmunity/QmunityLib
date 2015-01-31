package uk.co.qmunity.lib.network;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.world.World;
import uk.co.qmunity.lib.vec.IWorldLocation;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

public abstract class LocatedPacket<T extends LocatedPacket<T>> extends Packet<T> {

    protected int x, y, z;

    public LocatedPacket(IWorldLocation location) {

        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
    }

    public LocatedPacket(int x, int y, int z) {

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public LocatedPacket() {

    }

    @Override
    public void read(DataInput buffer) throws IOException {

        x = buffer.readInt();
        y = buffer.readInt();
        z = buffer.readInt();
    }

    @Override
    public void write(DataOutput buffer) throws IOException {

        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
    }

    public TargetPoint getTargetPoint(World world, double range) {

        return new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, range);
    }

}
