package uk.co.qmunity.lib.compat.fmp;

import uk.co.qmunity.lib.compat.QLDependencies;
import uk.co.qmunity.lib.part.MultipartCompat;
import uk.co.qmunity.lib.util.IModule;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CompatModuleFMP implements IModule {

    @Override
    public String getIdentifier() {

        return QLDependencies.FMP;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Override
    public void init(FMLInitializationEvent event) {

        FMPPartFactory.register();
        MultipartCompat.registerMultipartSystem(new MultipartSystemFMP());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }

}
