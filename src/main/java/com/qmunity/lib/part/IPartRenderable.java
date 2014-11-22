package com.qmunity.lib.part;

import net.minecraft.client.renderer.RenderBlocks;

import com.qmunity.lib.client.render.RenderHelper;
import com.qmunity.lib.vec.Vec3d;
import com.qmunity.lib.vec.Vec3dCube;
import com.qmunity.lib.vec.Vec3i;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IPartRenderable extends IPart {

    @SideOnly(Side.CLIENT)
    public boolean renderStatic(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass);

    @SideOnly(Side.CLIENT)
    public void renderDynamic(Vec3d translation, double delta, int pass);

    public boolean shouldRenderOnPass(int pass);

    public Vec3dCube getRenderBounds();

}
