/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
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
