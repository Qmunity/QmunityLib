package uk.co.qmunity.lib.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import uk.co.qmunity.lib.network.NetworkHandler;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.ITilePartHolder;

public class PacketCUpdatePart extends PacketCPart {

    private NBTTagCompound data;

    public PacketCUpdatePart(ITilePartHolder holder, IPart part) {

        super(holder, part);
    }

    public PacketCUpdatePart() {

        super();
    }

    @Override
    public void handle(EntityPlayer player) {

        if (part == null)
            return;

        part.readUpdateFromNBT(data);

        holder.getWorld().markBlockRangeForRenderUpdate(x, y, z, x, y, z);
    }

    @Override
    public void writeData(NBTTagCompound tag) {

        NBTTagCompound data = new NBTTagCompound();
        part.writeUpdateToNBT(data);
        tag.setTag("data", data);
    }

    @Override
    public void readData(NBTTagCompound tag) {

        data = tag.getCompoundTag("data");
    }

    public void sendTo(EntityPlayer player) {

        NetworkHandler.sendTo(this, (EntityPlayerMP) player);
    }

}
