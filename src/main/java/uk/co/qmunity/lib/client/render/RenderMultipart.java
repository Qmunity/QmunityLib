package uk.co.qmunity.lib.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.block.BlockMultipart;
import uk.co.qmunity.lib.model.IVertexConsumer;
import uk.co.qmunity.lib.part.IQLPart;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.tile.TileMultipart;
import uk.co.qmunity.lib.vec.BlockPos;
import uk.co.qmunity.lib.vec.Vector3;

public class RenderMultipart extends TileEntitySpecialRenderer implements IQLStaticRenderer {

    public static int pass = 0;
    public static int RENDER_ID;

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float frame) {

        TileMultipart te = (TileMultipart) tile;

        for (IQLPart p : te.getParts()) {
            if (p.getParent() != null) {
                GL11.glPushMatrix();
                p.renderDynamic(new Vector3(x, y, z), pass, frame);
                GL11.glPopMatrix();
            }
        }
    }

    @Override
    public boolean renderStatic(IBlockAccess world, BlockPos position, RenderContext context, IVertexConsumer consumer) {

        TileMultipart te = BlockMultipart.findTile(world, position.x, position.y, position.z);
        if (te == null || te.getParts().isEmpty())
            return false;

        boolean rendered = false;
        for (IQLPart p : te.getParts())
            rendered |= p.renderStatic(context, consumer, pass);

        return rendered;
    }

    @Override
    public boolean renderBreaking(IBlockAccess world, BlockPos position, RenderContext context, IVertexConsumer consumer, IIcon overrideIcon) {

        TileMultipart te = BlockMultipart.findTile(world, position.x, position.y, position.z);
        if (te == null || te.getParts().isEmpty())
            return false;
        MovingObjectPosition mop = Minecraft.getMinecraft().objectMouseOver;
        if (mop != null && mop instanceof QMovingObjectPosition && ((QMovingObjectPosition) mop).part != null)
            return ((QMovingObjectPosition) mop).part.renderBreaking(context, consumer, (QMovingObjectPosition) mop, overrideIcon);
        return false;
    }
}
