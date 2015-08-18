package uk.co.qmunity.lib.client.render;

import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import uk.co.qmunity.lib.model.IVertexConsumer;
import uk.co.qmunity.lib.vec.BlockPos;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IQLStaticRenderer {

    @SideOnly(Side.CLIENT)
    public boolean renderStatic(IBlockAccess world, BlockPos position, RenderContext context, IVertexConsumer consumer);

    @SideOnly(Side.CLIENT)
    public boolean renderBreaking(IBlockAccess world, BlockPos position, RenderContext context, IVertexConsumer consumer, IIcon overrideIcon);

}
