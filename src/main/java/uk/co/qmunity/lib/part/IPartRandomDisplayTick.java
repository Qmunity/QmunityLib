package uk.co.qmunity.lib.part;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Interface implemented by parts that do something on a random display tick.
 *
 * @author amadornes
 */
public interface IPartRandomDisplayTick extends IPart {

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(Random rnd);

}
