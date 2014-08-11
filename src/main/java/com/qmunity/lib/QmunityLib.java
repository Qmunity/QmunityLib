package com.qmunity.lib;

import com.qmunity.lib.proxy.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = QLModInfo.MODID, name = QLModInfo.NAME, version = QLModInfo.VERSION)
public class QmunityLib {

    @SidedProxy(serverSide = "com.qmunity.lib.proxy.CommonProxy", clientSide = "com.qmunity.lib.proxy.ClientProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void init(FMLInitializationEvent ev) {

        proxy.registerRenders();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent ev) {

    }
}
