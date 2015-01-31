package uk.co.qmunity.lib.network.packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.ITilePartHolder;
import uk.co.qmunity.lib.part.PartRegistry;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;
import uk.co.qmunity.lib.vec.Vec3i;

public class PacketCAddPart extends PacketCPart {

    private String type;
    private byte[] data;

    public PacketCAddPart(ITilePartHolder holder, IPart part) {

        super(holder, part);
    }

    public PacketCAddPart() {

        super();
    }

    @Override
    public void handle(EntityPlayer player) {

        part = PartRegistry.createPart(type, true);

        MultipartCompatibility.addPartToWorldBruteforce(part, player.worldObj, new Vec3i(x, y, z, player.worldObj));

        holder = part.getParent();
        if (holder == null)
            return;
        Map<String, IPart> map = holder.getPartMap();

        String oldId = null;
        for (String id : holder.getPartMap().keySet())
            if (holder.getPartMap().get(id) == part)
                oldId = id;

        map.remove(oldId);
        map.put(partId, part);

        try {
            part.readUpdateData(new DataInputStream(new ByteArrayInputStream(data)), -1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutput buffer) throws IOException {

        buffer.writeUTF(part.getType());

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        part.writeUpdateData(new DataOutputStream(os), -1);
        data = os.toByteArray();

        buffer.writeInt(data.length);
        buffer.write(data);
    }

    @Override
    public void readData(DataInput buffer) throws IOException {

        type = buffer.readUTF();

        data = new byte[buffer.readInt()];
        buffer.readFully(data);
    }

}
