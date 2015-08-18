package uk.co.qmunity.lib.client.helper;

import org.lwjgl.input.Mouse;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class InputHelper {

    private static int dWheel = 0;

    public static int getDWheel() {

        return dWheel;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onClientTick(ClientTickEvent event) {

        if (event.phase != Phase.END)
            return;

        dWheel = Mouse.getDWheel();
    }

}
