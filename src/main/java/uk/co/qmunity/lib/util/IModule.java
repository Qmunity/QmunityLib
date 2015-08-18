package uk.co.qmunity.lib.util;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public interface IModule {

    public String getIdentifier();

    public void preInit(FMLPreInitializationEvent event);

    public void init(FMLInitializationEvent event);

    public void postInit(FMLPostInitializationEvent event);

    public static interface IConfigurableModule extends IModule {

        public void loadConfig(Configuration config);
    }

}
