package com.qmunity.lib.network.packet;

import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.qmunity.lib.part.IPart;
import com.qmunity.lib.part.ITilePartHolder;
import com.qmunity.lib.part.PartRegistry;
import com.qmunity.lib.part.compat.MultipartCompatibility;
import com.qmunity.lib.vec.Vec3i;

public class PacketCAddPart extends PacketCPart {

    private String type;
    private String id;
    private NBTTagCompound data;

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

        String newId = null;
        for (String id : holder.getPartMap().keySet())
            if (holder.getPartMap().get(id) == part)
                newId = id;

        map.remove(newId);
        map.put(id, part);

        part.readUpdateFromNBT(data);
    }

    @Override
    public void writeData(NBTTagCompound tag) {

        tag.setString("type", part.getType());

        String partId = null;
        for (String id : holder.getPartMap().keySet())
            if (holder.getPartMap().get(id) == part)
                partId = id;
        tag.setString("id", partId);

        NBTTagCompound data = new NBTTagCompound();
        part.writeUpdateToNBT(data);
        tag.setTag("data", data);
    }

    @Override
    public void readData(NBTTagCompound tag) {

        type = tag.getString("type");
        id = tag.getString("id");
        data = tag.getCompoundTag("data");
    }

}
