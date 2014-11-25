package uk.co.qmunity.lib.part.compat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import uk.co.qmunity.lib.part.compat.fmp.FMPHelper;
import uk.co.qmunity.lib.part.compat.standalone.StandaloneCompat;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public enum MultipartSystem {

    STANDALONE(0, true, new StandaloneCompat()), //
    FMP(1, FMPHelper.isLoaded(), FMPHelper.getCompat());

    private int priority;
    private boolean isLoaded;
    private IMultipartCompat compat;

    private MultipartSystem(int priority, boolean isLoaded, IMultipartCompat compat) {

        this.priority = priority;
        this.isLoaded = isLoaded;
        this.compat = compat;
    }

    public int getPriority() {

        return priority;
    }

    public boolean isLoaded() {

        return isLoaded;
    }

    public IMultipartCompat getCompat() {

        return compat;
    }

    public static List<MultipartSystem> getAvailableSystems() {

        List<MultipartSystem> l = new ArrayList<MultipartSystem>();

        for (MultipartSystem system : values())
            if (system.isLoaded() && system.getCompat() != null)
                l.add(system);

        Collections.sort(l, new Comparator<MultipartSystem>() {

            @Override
            public int compare(MultipartSystem a, MultipartSystem b) {

                return Integer.compare(a.getPriority(), b.getPriority());
            }
        });

        return l;
    }

    public static void preInit(FMLPreInitializationEvent event) {

        for (MultipartSystem s : getAvailableSystems())
            s.getCompat().preInit(event);
    }

    public static void init(FMLInitializationEvent event) {

        for (MultipartSystem s : getAvailableSystems())
            s.getCompat().init(event);
    }

    public static void postInit(FMLPostInitializationEvent event) {

        for (MultipartSystem s : getAvailableSystems())
            s.getCompat().postInit(event);
    }

}
