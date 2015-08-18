package uk.co.qmunity.lib.compat;

import uk.co.qmunity.lib.compat.fmp.CompatModuleFMP;
import uk.co.qmunity.lib.compat.nei.CompatModuleNEI;
import uk.co.qmunity.lib.util.IModule;
import uk.co.qmunity.lib.util.ModularRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class QLCompatManager extends ModularRegistry<IModule> {

    public static final QLCompatManager instance = new QLCompatManager();

    public static void preInit(FMLPreInitializationEvent event) {

        for (IModule m : instance)
            m.preInit(event);
    }

    public static void init(FMLInitializationEvent event) {

        for (IModule m : instance)
            m.init(event);
    }

    public static void postInit(FMLPostInitializationEvent event) {

        for (IModule m : instance)
            m.postInit(event);
    }

    static {
        instance.register(Dependency.MOD.on(QLDependencies.NEI), CompatModuleNEI.class);
        instance.register(Dependency.MOD.on(QLDependencies.FMP), CompatModuleFMP.class);
    }

}
