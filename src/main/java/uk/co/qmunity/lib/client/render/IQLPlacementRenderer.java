package uk.co.qmunity.lib.client.render;

import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import uk.co.qmunity.lib.model.IVertexConsumer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IQLPlacementRenderer {

    @SideOnly(Side.CLIENT)
    public void renderPlacement(DrawBlockHighlightEvent event, RenderContext context, IVertexConsumer consumer);

}
