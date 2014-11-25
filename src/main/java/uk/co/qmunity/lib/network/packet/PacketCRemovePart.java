package uk.co.qmunity.lib.network.packet;

import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.ITilePartHolder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PacketCRemovePart extends PacketCPart {

    private String id;

    public PacketCRemovePart(ITilePartHolder holder, IPart part) {

        super(holder, part);
    }

    public PacketCRemovePart() {

        super();
    }

    @Override
    public void handle(EntityPlayer player) {

        holder.removePart(holder.getPartMap().get(id));
    }

    @Override
    public void writeData(NBTTagCompound tag) {

        String partId = null;
        for (String id : holder.getPartMap().keySet())
            if (holder.getPartMap().get(id) == part)
                partId = id;
        tag.setString("id", partId);
    }

    @Override
    public void readData(NBTTagCompound tag) {

        id = tag.getString("id");
    }

}
