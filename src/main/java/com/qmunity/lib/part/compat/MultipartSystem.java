package com.qmunity.lib.part.compat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.qmunity.lib.part.compat.fmp.FMPHelper;
import com.qmunity.lib.part.compat.standalone.StandaloneCompat;

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

}
