package uk.co.qmunity.lib.network;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.world.World;
import uk.co.qmunity.lib.vec.IWorldLocation;
import cpw.mods.fml.common.network.NetworkRegistry;

public abstract class LocatedPacketDouble<T extends LocatedPacket<T>> extends Packet<T> {

    protected double x, y, z;

    public LocatedPacketDouble(IWorldLocation location) {

        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
    }

    public LocatedPacketDouble(double x, double y, double z) {

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public LocatedPacketDouble() {

    }

    @Override
    public void read(DataInput buffer) throws IOException {

        x = buffer.readDouble();
        y = buffer.readDouble();
        z = buffer.readDouble();
    }

    @Override
    public void write(DataOutput buffer) throws IOException {

        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
    }

    public NetworkRegistry.TargetPoint getTargetPoint(World world) {

        return getTargetPoint(world, 64);
    }

    public NetworkRegistry.TargetPoint getTargetPoint(World world, double updateDistance) {

        return new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, updateDistance);
    }

}
