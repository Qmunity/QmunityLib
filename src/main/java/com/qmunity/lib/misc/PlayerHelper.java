package com.qmunity.lib.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PlayerHelper {
    
    public static EntityPlayer getPlayer() {
    
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) { return getPlayer_(); }
        
        return null;
    }
    
    @SideOnly(Side.CLIENT)
    private static EntityPlayer getPlayer_() {
    
        return Minecraft.getMinecraft().thePlayer;
    }
    
}
