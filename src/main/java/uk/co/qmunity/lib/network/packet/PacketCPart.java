package uk.co.qmunity.lib.network.packet;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.qmunity.lib.network.LocatedPacket;
import uk.co.qmunity.lib.network.NetworkHandler;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.ITilePartHolder;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class PacketCPart extends LocatedPacket<PacketCPart> {

    protected ITilePartHolder holder = null;
    protected IPart part = null;
    protected String partId = null;

    public PacketCPart(ITilePartHolder holder, IPart part) {

        super(holder);

        this.holder = holder;
        this.part = part;
    }

    public PacketCPart() {

        super();
    }

    public abstract void handle(EntityPlayer player);

    public abstract void writeData(DataOutput buffer) throws IOException;

    public abstract void readData(DataInput buffer) throws IOException;

    @Override
    @SideOnly(Side.CLIENT)
    public final void handleClientSide(EntityPlayer player) {

        if (player == null || player.worldObj == null)
            return;

        holder = MultipartCompatibility.getPartHolder(player.worldObj, x, y, z);
        if (holder != null && holder.getPartMap().containsKey(partId))
            part = holder.getPartMap().get(partId);

        doHandle(player);
    }

    @Override
    public final void handleServerSide(EntityPlayer player) {

    }

    public void doHandle(EntityPlayer player) {

        holder = MultipartCompatibility.getPartHolder(player.worldObj, x, y, z);
        if (holder != null)
            part = holder.getPartMap().get(partId);

        handle(player);
    }

    @Override
    public void write(DataOutput buffer) throws IOException {

        super.write(buffer);

        String partId = null;
        Map<String, IPart> parts = holder.getPartMap();
        for (String id : parts.keySet()) {
            if (parts.get(id) == part) {
                partId = id;
                break;
            }
        }
        if (partId == null) {
            buffer.writeBoolean(false);
            return;
        }
        buffer.writeBoolean(true);
        buffer.writeUTF(partId);

        writeData(buffer);
    }

    @Override
    public void read(DataInput buffer) throws IOException {

        super.read(buffer);
        if (!buffer.readBoolean())
            return;

        partId = buffer.readUTF();

        readData(buffer);
    }

    public void send() {

        NetworkHandler.QLIB.sendToAllAround(this, holder.getWorld(), 64);
    }
}