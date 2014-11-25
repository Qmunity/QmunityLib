package uk.co.qmunity.lib;

import uk.co.qmunity.lib.init.QLBlocks;
import uk.co.qmunity.lib.network.NetworkHandler;
import uk.co.qmunity.lib.part.compat.MultipartSystem;
import uk.co.qmunity.lib.proxy.CommonProxy;
import uk.co.qmunity.lib.util.QLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = QLModInfo.MODID, name = QLModInfo.NAME)
public class QmunityLib {

    @SidedProxy(serverSide = "com.qmunity.lib.proxy.CommonProxy", clientSide = "com.qmunity.lib.proxy.ClientProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        event.getModMetadata().version = QLModInfo.fullVersionString();
        QLog.logger = event.getModLog();

        MultipartSystem.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

        NetworkHandler.init();

        proxy.registerRenders();

        QLBlocks.init();

        MultipartSystem.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        MultipartSystem.postInit(event);

    }
}
