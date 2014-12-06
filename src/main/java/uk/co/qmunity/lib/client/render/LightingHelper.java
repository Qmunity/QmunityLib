package uk.co.qmunity.lib.client.render;

import net.minecraft.block.Block;
import uk.co.qmunity.lib.misc.Map3D;
import uk.co.qmunity.lib.vec.Vec3d;

public class LightingHelper {

    public static int joinLights(int l1, int l2, int l3, int l4) {

        return (l1 + l2 + l3 + l4) >> 2 & 0xFF00FF;
    }

    public static int joinLights(int l1, int l2) {

        return joinLights(l1, l1, l2, l2);
    }

    private static Map3D<Integer> lightmap = null;

    public static void loadLightmap(Map3D<Integer> lightmap) {

        LightingHelper.lightmap = lightmap;
    }

    private static int getLightValue(int x, int y, int z) {

        return lightmap.get(x + 1, y + 1, z + 1);
    }

    @SuppressWarnings("unused")
    private static Block getBlock(int x, int y, int z) {

        RenderHelper h = RenderHelper.instance;
        return h.getWorld().getBlock(h.getLocation().getX() + x, h.getLocation().getY(), h.getLocation().getZ());
    }

    @SuppressWarnings("unused")
    public static int getBrightnessForVertex(Vec3d vertex, Vec3d normal) {

        if (lightmap == null)
            return 0;
        // normal = normal.normalize();
        // Vec3i inormal = new Vec3i((int) Math.copySign(Math.ceil(Math.abs(normal.getX())), normal.getX()), (int) Math.copySign(
        // Math.ceil(Math.abs(normal.getY())), normal.getY()), (int) Math.copySign(Math.ceil(Math.abs(normal.getZ())), normal.getZ()));
        //
        // int lightingX = 0;
        // int lightingY = 0;
        // int lightingZ = 0;
        // if (inormal.getX() != 0) {
        // int nx = inormal.getX();
        //
        // int X = getLightValue(nx, 0, 0);
        // int YN = getLightValue(nx, -1, 0);
        // int YP = getLightValue(nx, 1, 0);
        // int ZN = getLightValue(nx, 0, -1);
        // int ZP = getLightValue(nx, 0, 1);
        // int YZNN = getLightValue(nx, -1, -1);
        // int YZPN = getLightValue(nx, 1, -1);
        // int YZNP = getLightValue(nx, -1, 1);
        // int YZPP = getLightValue(nx, 1, 1);
        //
        // int surrounding = (int) ((vertex.getY() * (((int) ((YP + (vertex.getZ() * YZPP) + ((1 - vertex.getZ()) * YZPN))) / 2) & 0xFF00FF)) + ((1 -
        // vertex
        // .getY()) * (((int) ((YN + (vertex.getZ() * YZNP) + ((1 - vertex.getZ()) * YZNN))) / 2) & 0xFF00FF))) & 0xFF00FF;
        //
        // lightingX = joinLights(surrounding, X);
        // }
        // if (inormal.getY() != 0) {
        // int ny = inormal.getY();
        //
        // int Y = getLightValue(0, ny, 0);
        // int XN = getLightValue(-1, ny, 0);
        // int XP = getLightValue(1, ny, 0);
        // int ZN = getLightValue(0, ny, -1);
        // int ZP = getLightValue(0, ny, 1);
        // int XZNN = getLightValue(-1, ny, -1);
        // int XZPN = getLightValue(1, ny, -1);
        // int XZNP = getLightValue(-1, ny, 1);
        // int XZPP = getLightValue(1, ny, 1);
        //
        // int surrounding = (int) ((vertex.getX() * (((int) ((XP + (vertex.getZ() * XZPP) + ((1 - vertex.getZ()) * XZPN))) / 2) & 0xFF00FF)) + ((1 -
        // vertex
        // .getX()) * (((int) ((XN + (vertex.getZ() * XZNP) + ((1 - vertex.getZ()) * XZNN))) / 2) & 0xFF00FF))) & 0xFF00FF;
        //
        // lightingY = joinLights(surrounding, Y);
        // }
        // if (inormal.getZ() != 0) {
        // int nz = inormal.getZ();
        //
        // int Z = getLightValue(0, 0, nz);
        // int XN = getLightValue(-1, 0, nz);
        // int XP = getLightValue(1, 0, nz);
        // int ZN = getLightValue(0, -1, nz);
        // int ZP = getLightValue(0, 1, nz);
        // int XYNN = getLightValue(-1, -1, nz);
        // int XYPN = getLightValue(1, -1, nz);
        // int XYNP = getLightValue(-1, 1, nz);
        // int XYPP = getLightValue(1, 1, nz);
        //
        // int surrounding = (int) ((vertex.getX() * (((int) ((XP + (vertex.getY() * XYPP) + ((1 - vertex.getY()) * XYPN))) / 2) & 0xFF00FF)) + ((1 -
        // vertex
        // .getX()) * (((int) ((XN + (vertex.getY() * XYNP) + ((1 - vertex.getY()) * XYNN))) / 2) & 0xFF00FF))) & 0xFF00FF;
        //
        // lightingZ = joinLights(surrounding, Z);
        // }
        //
        // int normalLighting = ((int) ((lightingX * Math.abs(normal.getX())) + (lightingY * Math.abs(normal.getY())) + (lightingZ * Math
        // .abs(normal.getZ())))) & 0xFF00FF;

        return 0xF000F0;
    }
}
