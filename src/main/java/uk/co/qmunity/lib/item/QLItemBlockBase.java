package uk.co.qmunity.lib.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import uk.co.qmunity.lib.QLModInfo;
import uk.co.qmunity.lib.QmunityLib;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class QLItemBlockBase extends ItemBlock {

    public QLItemBlockBase(Block block) {

        super(block);
    }

    public QLItemBlockBase(Block block, String name) {

        super(block);

        setUnlocalizedName(name);
        setTextureName(getModId() + ":" + name);
    }

    protected abstract String getModId();

    protected String getUnlocalizedTip(ItemStack stack, EntityPlayer player) {

        return getUnlocalizedName(stack).replace("tile.", "tooltip.");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SideOnly(Side.CLIENT)
    protected void addTipInformation(ItemStack stack, EntityPlayer player, List l, String tip) {

        l.addAll(Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(tip, 35));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List l, boolean unused) {

        String unlocalized = getUnlocalizedTip(stack, player);
        String localized = I18n.format(unlocalized);
        if (!hasTooltip(stack, player, unlocalized, localized))
            return;

        if (!QmunityLib.proxy.isShiftDown()) {
            l.add(I18n.format("tooltip." + QLModInfo.MODID + ":shift", EnumChatFormatting.GRAY.toString(),
                    EnumChatFormatting.YELLOW.toString() + EnumChatFormatting.ITALIC.toString(), EnumChatFormatting.RESET.toString()
                            + EnumChatFormatting.GRAY.toString()));
        } else {
            addTipInformation(stack, player, l, localized);
        }
    }

    protected boolean hasTooltip(ItemStack stack, EntityPlayer player, String unlocalized, String localized) {

        return !localized.equals(unlocalized);
    }
}
