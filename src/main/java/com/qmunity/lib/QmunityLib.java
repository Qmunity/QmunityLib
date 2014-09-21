package com.qmunity.lib;

import com.qmunity.lib.proxy.CommonProxy;
import com.qmunity.lib.util.QLog;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = QLModInfo.MODID, name = QLModInfo.NAME)
public class QmunityLib{

    @SidedProxy(serverSide = "com.qmunity.lib.proxy.CommonProxy", clientSide = "com.qmunity.lib.proxy.ClientProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent ev){
        ev.getModMetadata().version = QLModInfo.fullVersionString();
        QLog.logger = ev.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent ev){

        proxy.registerRenders();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent ev){

    }
}
