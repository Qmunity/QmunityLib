package com.qmunity.lib.helper;

import net.minecraft.entity.player.EntityPlayer;

import com.qmunity.lib.QmunityLib;

public class PlayerHelper {

    public static EntityPlayer getPlayer() {

        return QmunityLib.proxy.getPlayer();
    }

}
