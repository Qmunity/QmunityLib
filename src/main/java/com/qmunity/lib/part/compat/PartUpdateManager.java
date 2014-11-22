package com.qmunity.lib.part.compat;

import com.qmunity.lib.network.packet.PacketCAddPart;
import com.qmunity.lib.network.packet.PacketCRemovePart;
import com.qmunity.lib.network.packet.PacketCUpdatePart;
import com.qmunity.lib.part.IPart;
import com.qmunity.lib.part.ITilePartHolder;

public class PartUpdateManager {

    private static boolean enabled = true;

    public static void sendPartUpdate(ITilePartHolder holder, IPart part) {

        if (part.getWorld().isRemote)
            return;
        if (!enabled)
            return;

        new PacketCUpdatePart(part.getParent(), part).send();
    }

    public static void addPart(ITilePartHolder holder, IPart part) {

        if (holder.getWorld().isRemote)
            return;
        if (!enabled)
            return;

        new PacketCAddPart(holder, part).send();
    }

    public static void removePart(ITilePartHolder holder, IPart part) {

        if (holder.getWorld().isRemote)
            return;
        if (!enabled)
            return;

        new PacketCRemovePart(holder, part).send();
    }

    public static void setUpdatesEnabled(boolean enabled) {

        PartUpdateManager.enabled = enabled;
    }

}
