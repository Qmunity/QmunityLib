package uk.co.qmunity.lib.compat.nei;

import uk.co.qmunity.lib.compat.QLDependencies;
import uk.co.qmunity.lib.util.IModule;
import codechicken.nei.api.API;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CompatModuleNEI implements IModule {

    @Override
    public String getIdentifier() {

        return QLDependencies.NEI;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {

        API.registerNEIGuiHandler(new NEIGuiHandlerQL());
    }

    @Override
    public void init(FMLInitializationEvent event) {

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }

}
