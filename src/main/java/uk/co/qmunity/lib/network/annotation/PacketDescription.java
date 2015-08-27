package uk.co.qmunity.lib.network.annotation;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import uk.co.qmunity.lib.network.LocatedPacket;
import uk.co.qmunity.lib.network.MCByteBuf;
import uk.co.qmunity.lib.tile.QLTileBase;

/**
 * @author MineMaarten
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PacketDescription extends LocatedPacket<PacketDescription> {
    private byte[] types;
    private Object[] values;

    // private NBTTagCompound extraData;

    public PacketDescription() {

    }

    public PacketDescription(QLTileBase te) {

        super(te.xCoord, te.yCoord, te.zCoord);
        values = new Object[te.getDescriptionFields().size()];
        types = new byte[values.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = te.getDescriptionFields().get(i).getValue();
            types[i] = PacketCUpdateGui.getType(te.getDescriptionFields().get(i));
        }
        // extraData = new NBTTagCompound();
        // te.writeToPacketNBT(extraData);
    }

    @Override
    public void toBytes(MCByteBuf buf) {

        super.toBytes(buf);
        buf.writeInt(values.length);
        for (int i = 0; i < types.length; i++) {
            buf.writeByte(types[i]);
            PacketCUpdateGui.writeField(buf, values[i], types[i]);
        }
        // ByteBufUtils.writeTag(buf, extraData);
    }

    @Override
    public void fromBytes(MCByteBuf buf) {

        super.fromBytes(buf);
        int dataAmount = buf.readInt();
        types = new byte[dataAmount];
        values = new Object[dataAmount];
        for (int i = 0; i < dataAmount; i++) {
            types[i] = buf.readByte();
            values[i] = PacketCUpdateGui.readField(buf, types[i]);
        }
        // extraData = ByteBufUtils.readTag(buf);
    }

    @Override
    public void handleClientSide(EntityPlayer player) {

        TileEntity te = getWorldPos(player.worldObj).getTileEntity();
        if (te instanceof QLTileBase) {
            List<SyncedField> descFields = ((QLTileBase) te).getDescriptionFields();
            if (descFields != null && descFields.size() == types.length) {
                for (int i = 0; i < descFields.size(); i++) {
                    descFields.get(i).setValue(values[i]);
                }
            }
            // ((TileBase) te).readFromPacketNBT(extraData);
        }
    }

    @Override
    public void handleServerSide(EntityPlayer player) {

    }

}
