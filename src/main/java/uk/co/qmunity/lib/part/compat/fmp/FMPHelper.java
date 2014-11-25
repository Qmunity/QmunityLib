package uk.co.qmunity.lib.part.compat.fmp;

import uk.co.qmunity.lib.part.compat.IMultipartCompat;
import uk.co.qmunity.lib.ref.Deps;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;

public class FMPHelper {

    public static final boolean isLoaded() {

        return Loader.isModLoaded(Deps.FMP);
    }

    private static IMultipartCompat compat;

    public static final IMultipartCompat getCompat() {

        if (isLoaded())
            initCompat();

        return compat;
    }

    @Optional.Method(modid = Deps.FMP)
    private static void initCompat() {

        if (compat == null)
            compat = new FMPCompat();
    }

}
