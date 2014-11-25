package uk.co.qmunity.lib.proxy;

import uk.co.qmunity.lib.client.render.RenderMultipart;
import uk.co.qmunity.lib.client.render.RenderPartPlacement;
import uk.co.qmunity.lib.client.render.RenderParticle;
import uk.co.qmunity.lib.effect.EntityFXParticle;
import uk.co.qmunity.lib.tile.TileMultipart;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerRenders() {

        RenderingRegistry.registerEntityRenderingHandler(EntityFXParticle.class, new RenderParticle());

        RenderMultipart multipartRenderer = new RenderMultipart();
        RenderingRegistry.registerBlockHandler(multipartRenderer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileMultipart.class, multipartRenderer);

        RenderPartPlacement renderPartPlacement = new RenderPartPlacement();
        FMLCommonHandler.instance().bus().register(renderPartPlacement);
        MinecraftForge.EVENT_BUS.register(renderPartPlacement);
    }

    @Override
    public EntityPlayer getPlayer() {

        return Minecraft.getMinecraft().thePlayer;
    }

}
