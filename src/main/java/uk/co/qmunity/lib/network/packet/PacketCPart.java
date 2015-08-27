package uk.co.qmunity.lib.network.packet;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import uk.co.qmunity.lib.network.LocatedPacket;
import uk.co.qmunity.lib.network.MCByteBuf;
import uk.co.qmunity.lib.network.NetworkHandler;
import uk.co.qmunity.lib.part.IPartHolder;
import uk.co.qmunity.lib.part.IQLPart;
import uk.co.qmunity.lib.part.MultipartCompat;
import uk.co.qmunity.lib.part.PartRegistry;

public class PacketCPart extends LocatedPacket<PacketCPart> {

    private int action;

    private String partID;
    private IQLPart part;

    private byte[] data;

    private PacketCPart(int action, IPartHolder holder, IQLPart part) {

        super(holder);

        this.action = action;

        this.partID = holder.getPartID(part);
        this.part = part;
    }

    public PacketCPart() {

    }

    @Override
    public void toBytes(MCByteBuf buf) {

        super.toBytes(buf);

        buf.writeByte(action);
        buf.writeString(partID);
        if (action == 1 || action == 2) {
            MCByteBuf buf_ = new MCByteBuf(Unpooled.buffer());
            if (action == 1)
                buf_.writeString(part.getType());
            part.writeUpdateData(buf_);
            byte[] data = buf_.array();
            buf.writeInt(data.length);
            buf.writeBytes(data);
        }
    }

    @Override
    public void fromBytes(MCByteBuf buf) {

        super.fromBytes(buf);

        action = buf.readByte() & 0xFF;
        partID = buf.readString();
        if (action == 1 || action == 2) {
            data = new byte[buf.readInt()];
            buf.readBytes(data);
        }
    }

    @Override
    public void handleClientSide(EntityPlayer player) {

        if (action == 0) { // Remove part
            IPartHolder holder = MultipartCompat.getHolder(player.worldObj, x, y, z);
            part = holder.findPart(partID);

            holder.removePart(part);
        } else if (action == 1) { // Add part
            MCByteBuf buf_ = new MCByteBuf(Unpooled.copiedBuffer(data));
            part = PartRegistry.createPart(buf_.readString(), true);
            part.readUpdateData(buf_);

            MultipartCompat.addPart(player.worldObj, x, y, z, part, partID);
        } else if (action == 2) { // Update part
            MCByteBuf buf_ = new MCByteBuf(Unpooled.copiedBuffer(data));
            IPartHolder holder = MultipartCompat.getHolder(player.worldObj, x, y, z);

            if (holder != null) {
                part = holder.findPart(partID);
                part.readUpdateData(buf_);
            }
        }
    }

    @Override
    public void handleServerSide(EntityPlayer player) {

    }

    public static void removePart(IPartHolder holder, IQLPart part) {

        NetworkHandler.QLIB.sendToAllAround(new PacketCPart(0, holder, part), holder.getWorld());
    }

    public static void addPart(IPartHolder holder, IQLPart part) {

        NetworkHandler.QLIB.sendToAllAround(new PacketCPart(1, holder, part), holder.getWorld());
    }

    public static void updatePart(IPartHolder holder, IQLPart part) {

        NetworkHandler.QLIB.sendToAllAround(new PacketCPart(2, holder, part), holder.getWorld());
    }

}
