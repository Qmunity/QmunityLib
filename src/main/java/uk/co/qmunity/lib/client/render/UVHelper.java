package uk.co.qmunity.lib.client.render;

import net.minecraft.util.IIcon;
import uk.co.qmunity.lib.vec.Vec2d;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UVHelper {

    public static Vec2d rotateUV(Vec2d uv, double angle) {

        Vec2d c = new Vec2d(0.5, 0.5);
        return new Vec2d(uv.getX(), uv.getY()).sub(c).rotate(angle).add(c);
    }

    public static Vec2d rotateUV(Vec2d uv, double angle, IIcon icon) {

        Vec2d rotated = rotateUV(uv, angle);
        return new Vec2d(icon.getInterpolatedU(rotated.getX() * 16), icon.getInterpolatedV(rotated.getY() * 16));
    }

}
