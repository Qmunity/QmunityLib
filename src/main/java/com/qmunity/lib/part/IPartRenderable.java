package com.qmunity.lib.part;

import net.minecraft.client.renderer.RenderBlocks;

import com.qmunity.lib.vec.Vec3d;
import com.qmunity.lib.vec.Vec3i;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IPartRenderable {

    @SideOnly(Side.CLIENT)
    public boolean renderStatic(Vec3i translation, RenderBlocks renderer, int pass);

    @SideOnly(Side.CLIENT)
    public void renderDynamic(Vec3d translation, double delta, int pass);

    public boolean shouldRenderOnPass(int pass);

}
