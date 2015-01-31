package uk.co.qmunity.lib.network.packet;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.ITilePartHolder;

public class PacketCRemovePart extends PacketCPart {

    public PacketCRemovePart(ITilePartHolder holder, IPart part) {

        super(holder, part);
    }

    public PacketCRemovePart() {

        super();
    }

    @Override
    public void handle(EntityPlayer player) {

        if (holder != null)
            holder.removePart(holder.getPartMap().get(partId));
    }

    @Override
    public void writeData(DataOutput buffer) throws IOException {

    }

    @Override
    public void readData(DataInput buffer) throws IOException {

    }

}
