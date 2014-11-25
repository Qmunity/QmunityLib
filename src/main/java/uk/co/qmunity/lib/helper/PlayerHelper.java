package uk.co.qmunity.lib.helper;

import uk.co.qmunity.lib.QmunityLib;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerHelper {

    public static EntityPlayer getPlayer() {

        return QmunityLib.proxy.getPlayer();
    }

}
