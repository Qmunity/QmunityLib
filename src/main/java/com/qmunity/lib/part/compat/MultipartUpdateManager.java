package com.qmunity.lib.part.compat;

import java.util.ArrayList;
import java.util.List;

import com.qmunity.lib.part.IPart;
import com.qmunity.lib.part.ITilePartHolder;

public final class MultipartUpdateManager {

    private static List<ITilePartHolder> tiles = new ArrayList<ITilePartHolder>();

    public static void onValidate(ITilePartHolder holder) {

        tiles.add(holder);
        // Send packet with all the info
    }

    public static void onInvalidate(ITilePartHolder holder) {

        tiles.remove(holder);
    }

    public static void onPartAdded(ITilePartHolder holder, IPart part) {

    }

    public static void onPartRemoved(ITilePartHolder holder, IPart part) {

    }

    // public static void handleUpdatePacket(PacketUpdatePart packet) {
    //
    // }

}
