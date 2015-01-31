package uk.co.qmunity.lib.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class Packet<REQ extends Packet<REQ>> implements IMessage, IMessageHandler<REQ, REQ> {

    @Override
    public REQ onMessage(REQ message, MessageContext ctx) {

        if (ctx.side == Side.SERVER) {
            if (message.getClass() == getClass())
                message.handleServerSide(ctx.getServerHandler().playerEntity);
            else
                message.handleServerSide(ctx.getServerHandler().playerEntity);
        } else {
            if (message.getClass() == getClass())
                message.handleClientSide(getPlayerClient());
            else
                message.handleClientSide(getPlayerClient());
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public EntityPlayer getPlayerClient() {

        return Minecraft.getMinecraft().thePlayer;
    }

    @SideOnly(Side.CLIENT)
    public abstract void handleClientSide(EntityPlayer player);

    public abstract void handleServerSide(EntityPlayer player);

    @Override
    public void fromBytes(ByteBuf buf) {

        try {
            ByteBufInputStream bbis = new ByteBufInputStream(buf);
            read(bbis);
            bbis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {

        try {
            ByteBufOutputStream bbos = new ByteBufOutputStream(buf);
            write(bbos);
            bbos.flush();
            bbos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void read(DataInput buffer) throws IOException;

    public abstract void write(DataOutput buffer) throws IOException;
}
