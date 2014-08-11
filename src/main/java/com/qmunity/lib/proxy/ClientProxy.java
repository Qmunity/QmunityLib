package com.qmunity.lib.proxy;

import com.qmunity.lib.effect.EntityFXParticle;
import com.qmunity.lib.render.RenderParticle;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerRenders() {

        RenderingRegistry.registerEntityRenderingHandler(EntityFXParticle.class, new RenderParticle());
    }

}
