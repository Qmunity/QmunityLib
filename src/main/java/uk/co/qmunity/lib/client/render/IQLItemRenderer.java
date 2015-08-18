package uk.co.qmunity.lib.client.render;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import uk.co.qmunity.lib.model.IVertexConsumer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IQLItemRenderer {

    @SideOnly(Side.CLIENT)
    public void renderItem(ItemStack stack, ItemRenderType type, RenderContext context, IVertexConsumer consumer);

}
