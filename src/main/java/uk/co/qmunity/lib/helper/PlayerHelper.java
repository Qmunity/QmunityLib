package uk.co.qmunity.lib.helper;

import net.minecraft.server.MinecraftServer;
import uk.co.qmunity.lib.QmunityLib;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerHelper {

    public static EntityPlayer getPlayer() {

        return QmunityLib.proxy.getPlayer();
    }

    public static boolean isOpped(String player) {
        for ( String oppedPlayer : MinecraftServer.getServer().getConfigurationManager().func_152603_m().func_152685_a()) {
            if (oppedPlayer.equalsIgnoreCase(player)) {
                return true;
            }
        }
        return false;
    }
}
