package uk.co.qmunity.lib.part;

import net.minecraft.client.renderer.RenderBlocks;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IPartRenderable extends IPart {

    @SideOnly(Side.CLIENT)
    public boolean renderBreaking(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass, QMovingObjectPosition mop);

    @SideOnly(Side.CLIENT)
    public boolean renderStatic(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass);

    @SideOnly(Side.CLIENT)
    public void renderDynamic(Vec3d translation, double delta, int pass);

    @SideOnly(Side.CLIENT)
    public boolean shouldRenderOnPass(int pass);

    @SideOnly(Side.CLIENT)
    public Vec3dCube getRenderBounds();

}
