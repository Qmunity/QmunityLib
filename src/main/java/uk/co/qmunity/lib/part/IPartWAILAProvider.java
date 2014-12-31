package uk.co.qmunity.lib.part;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Interface implemented by the parts that want to add a tooltip to WAILA.
 *
 * @author amadornes
 */
public interface IPartWAILAProvider extends IPart {

    /**
     * Adds content to the WAILA tooltip.
     */
    @SideOnly(Side.CLIENT)
    public void addWAILABody(List<String> text);

}
