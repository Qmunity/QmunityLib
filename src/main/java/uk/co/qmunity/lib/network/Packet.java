package uk.co.qmunity.lib.network;

import io.netty.buffer.ByteBuf;
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
    public final void fromBytes(ByteBuf buf) {

        fromBytes(new MCByteBuf(buf));
    }

    @Override
    public final void toBytes(ByteBuf buf) {

        toBytes(new MCByteBuf(buf));
    }

    public abstract void fromBytes(MCByteBuf buf);

    public abstract void toBytes(MCByteBuf buf);
}
