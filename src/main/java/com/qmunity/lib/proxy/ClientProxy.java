package com.qmunity.lib.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import com.qmunity.lib.client.render.RenderMultipart;
import com.qmunity.lib.client.render.RenderParticle;
import com.qmunity.lib.effect.EntityFXParticle;
import com.qmunity.lib.tile.TileMultipart;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerRenders() {

        RenderingRegistry.registerEntityRenderingHandler(EntityFXParticle.class, new RenderParticle());

        RenderMultipart multipartRenderer = new RenderMultipart();
        RenderingRegistry.registerBlockHandler(multipartRenderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileMultipart.class, multipartRenderer);
    }

    @Override
    public EntityPlayer getPlayer() {

        return Minecraft.getMinecraft().thePlayer;
    }

}
