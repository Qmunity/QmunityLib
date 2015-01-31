package uk.co.qmunity.lib.part.compat;

import uk.co.qmunity.lib.network.packet.PacketCAddPart;
import uk.co.qmunity.lib.network.packet.PacketCRemovePart;
import uk.co.qmunity.lib.network.packet.PacketCUpdatePart;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.ITilePartHolder;

public class PartUpdateManager {

    private static boolean enabled = true;

    public static void sendPartUpdate(ITilePartHolder holder, IPart part, int channel) {

        if (part.getWorld().isRemote)
            return;
        if (!enabled)
            return;

        new PacketCUpdatePart(part.getParent(), part, channel).send();
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
