package uk.co.qmunity.lib;

import uk.co.qmunity.lib.command.CommandQLib;
import uk.co.qmunity.lib.compat.QLCompatManager;
import uk.co.qmunity.lib.helper.RedstoneHelper;
import uk.co.qmunity.lib.helper.SystemInfoHelper;
import uk.co.qmunity.lib.network.NetworkHandler;
import uk.co.qmunity.lib.part.MultipartCompat;
import uk.co.qmunity.lib.part.MultipartSystemStandalone;
import uk.co.qmunity.lib.util.QLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = QLModInfo.MODID, name = QLModInfo.NAME, dependencies = "after:ForgeMultipart")
public class QmunityLib {

    @SidedProxy(serverSide = "uk.co.qmunity.lib.CommonProxy", clientSide = "uk.co.qmunity.lib.client.ClientProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        event.getModMetadata().version = QLModInfo.fullVersionString();
        QLog.logger = event.getModLog();

        QLBlocks.init();

        MultipartSystemStandalone standaloneMultiparts = new MultipartSystemStandalone();
        MultipartCompat.registerMultipartSystem(standaloneMultiparts);
        RedstoneHelper.registerProvider(standaloneMultiparts);

        QLCompatManager.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

        NetworkHandler.initQLib();

        proxy.registerRenders();

        QLCompatManager.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        QLCompatManager.postInit(event);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {

        event.registerServerCommand(new CommandQLib());
    }

    @EventHandler
    public void serverStarted(FMLServerStartedEvent event) {

        SystemInfoHelper.startTime = System.currentTimeMillis();
    }
}