package uk.co.qmunity.lib.part;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IWailaProviderPart extends IQLPart {

    @SideOnly(Side.CLIENT)
    public void addWAILABody(List<String> text);
}
