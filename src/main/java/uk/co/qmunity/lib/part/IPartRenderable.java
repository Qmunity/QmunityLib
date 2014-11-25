package uk.co.qmunity.lib.part;

import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;
import net.minecraft.client.renderer.RenderBlocks;
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
