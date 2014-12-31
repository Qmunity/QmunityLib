package uk.co.qmunity.lib.network.packet;

import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
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

    private String partId = null;

    public PacketCPart(ITilePartHolder holder, IPart part) {

        super(holder);

        this.holder = holder;
        this.part = part;
    }

    public PacketCPart() {

        super();
    }

    public abstract void handle(EntityPlayer player);

    public abstract void writeData(NBTTagCompound tag);

    public abstract void readData(NBTTagCompound tag);

    @Override
    @SideOnly(Side.CLIENT)
    public final void handleClientSide(PacketCPart message, EntityPlayer player) {

        message.holder = MultipartCompatibility.getPartHolder(player.worldObj, x, y, z);
        if (message.holder != null && message.holder.getPartMap().containsKey(message.partId))
            message.part = message.holder.getPartMap().get(message.partId);

        message.handle(player);
    }

    @Override
    public final void handleServerSide(PacketCPart message, EntityPlayer player) {

    }

    @Override
    public final void write(NBTTagCompound tag) {

        super.write(tag);

        NBTTagCompound t = new NBTTagCompound();
        writeData(t);
        tag.setTag("data", t);

        String partId = null;
        Map<String, IPart> parts = holder.getPartMap();
        for (String id : parts.keySet()) {
            if (parts.get(id) == part) {
                partId = id;
                break;
            }
        }
        if (partId == null)
            return;
        tag.setString("partId", partId);
    }

    @Override
    public final void read(NBTTagCompound tag) {

        super.read(tag);

        NBTTagCompound t = tag.getCompoundTag("data");
        readData(t);

        partId = tag.getString("partId");
    }

    public void send() {

        NetworkHandler.sendToAllAround(this, holder.getWorld(), 64);
    }
}