package uk.co.qmunity.lib.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3i;

/**
 * Most of the code in this class was made by ChickenBones. All credits go to him!
 *
 * @author ChickenBones
 */
public class LightingHelper {

    private float[][] ao = new float[13][4];
    private int[][] brightness = new int[13][4];

    private int sampledSides = 0;
    private int computedSides = 0;

    private float[] aoSamples = new float[27];
    private int[] brightnessSamples = new int[27];

    private static final int[][] ssamplem = new int[][] { { 0, 1, 2, 3, 4, 5, 6, 7, 8 }, { 18, 19, 20, 21, 22, 23, 24, 25, 26 },
            { 0, 9, 18, 1, 10, 19, 2, 11, 20 }, { 6, 15, 24, 7, 16, 25, 8, 17, 26 }, { 0, 3, 6, 9, 12, 15, 18, 21, 24 },
            { 2, 5, 8, 11, 14, 17, 20, 23, 26 }, { 9, 10, 11, 12, 13, 14, 15, 16, 17 }, { 9, 10, 11, 12, 13, 14, 15, 16, 17 },
            { 3, 12, 21, 4, 13, 22, 5, 14, 23 }, { 3, 12, 21, 4, 13, 22, 5, 14, 23 }, { 1, 4, 7, 10, 13, 16, 19, 22, 25 },
            { 1, 4, 7, 10, 13, 16, 19, 22, 25 }, { 13, 13, 13, 13, 13, 13, 13, 13, 13 } };
    private static final int[][] qsamplem = new int[][] { { 0, 1, 3, 4 }, { 5, 1, 2, 4 }, { 6, 7, 3, 4 }, { 5, 7, 8, 4 } };
    private static final float[] sideao = new float[] { 0.5F, 1F, 0.8F, 0.8F, 0.6F, 0.6F, 0.5F, 1F, 0.8F, 0.8F, 0.6F, 0.6F, 1F };

    private IBlockAccess access;
    private Vec3i pos;

    public LightingHelper(IBlockAccess world, Vec3i location) {

        access = world;
        pos = location;
    }

    public int[] getBrightness(int side) {

        sideSample(side);
        return brightness[side];
    }

    public int[] getBrightness(Vec3d normal) {

        normal = normal.normalize();
        double x = normal.getX(), y = normal.getY(), z = normal.getZ();
        int[] brightness = new int[4];

        if (x < 0) {
            int[] b = getBrightness(ForgeDirection.WEST.ordinal());
            for (int i = 0; i < 4; i++)
                brightness[i] += getProportion(b[i], -x);
        }
        if (x > 0) {
            int[] b = getBrightness(ForgeDirection.EAST.ordinal());
            for (int i = 0; i < 4; i++)
                brightness[i] += getProportion(b[i], x);
        }

        if (y < 0) {
            int[] b = getBrightness(ForgeDirection.DOWN.ordinal());
            for (int i = 0; i < 4; i++)
                brightness[i] += getProportion(b[i], -y);
        }
        if (y > 0) {
            int[] b = getBrightness(ForgeDirection.UP.ordinal());
            for (int i = 0; i < 4; i++)
                brightness[i] += getProportion(b[i], y);
        }

        if (z < 0) {
            int[] b = getBrightness(ForgeDirection.NORTH.ordinal());
            for (int i = 0; i < 4; i++)
                brightness[i] += getProportion(b[i], -z);
        }
        if (z > 0) {
            int[] b = getBrightness(ForgeDirection.SOUTH.ordinal());
            for (int i = 0; i < 4; i++)
                brightness[i] += getProportion(b[i], z);
        }

        return brightness;
    }

    public float[] getAo(int side) {

        sideSample(side);
        return ao[side];
    }

    public float[] getAo(Vec3d normal) {

        normal = normal.normalize();
        double x = normal.getX(), y = normal.getY(), z = normal.getZ();
        float[] ao = new float[4];

        if (x < 0) {
            float[] b = getAo(ForgeDirection.WEST.ordinal());
            for (int i = 0; i < 4; i++)
                ao[i] += getProportion(b[i], -x);
        }
        if (x > 0) {
            float[] b = getAo(ForgeDirection.EAST.ordinal());
            for (int i = 0; i < 4; i++)
                ao[i] += getProportion(b[i], x);
        }

        if (y < 0) {
            float[] b = getAo(ForgeDirection.DOWN.ordinal());
            for (int i = 0; i < 4; i++)
                ao[i] += getProportion(b[i], -y);
        }
        if (y > 0) {
            float[] b = getAo(ForgeDirection.UP.ordinal());
            for (int i = 0; i < 4; i++)
                ao[i] += getProportion(b[i], y);
        }

        if (z < 0) {
            float[] b = getAo(ForgeDirection.NORTH.ordinal());
            for (int i = 0; i < 4; i++)
                ao[i] += getProportion(b[i], -z);
        }
        if (z > 0) {
            float[] b = getAo(ForgeDirection.SOUTH.ordinal());
            for (int i = 0; i < 4; i++)
                ao[i] += getProportion(b[i], z);
        }

        return ao;
    }

    public int getVertexBrightness(Vec3d vertex, Vec3d normal) {

        int[] b = getBrightness(normal);
        float[] ao = getAo(normal);

        return mixAoBrightness(b[0], b[1], b[2], b[3], ao[0], ao[1], ao[2], ao[3]) & 0xF500F5;
    }

    private void sample(int side) {

        if ((sampledSides & 1 << side) == 0) {
            int x = pos.getX() + (side % 3) - 1;
            int y = pos.getY() + (side / 9) - 1;
            int z = pos.getZ() + (side / 3 % 3) - 1;
            Block b = access.getBlock(x, y, z);
            brightnessSamples[side] = access.getLightBrightnessForSkyBlocks(x, y, z, b.getLightValue(access, x, y, z));
            aoSamples[side] = b.getAmbientOcclusionLightValue();
            sampledSides |= 1 << side;
        }
    }

    private void sideSample(int side) {

        if ((computedSides & 1 << side) == 0) {
            int[] ssample = ssamplem[side];
            for (int q = 0; q < 4; q++) {
                int[] qsample = qsamplem[q];
                if (Minecraft.isAmbientOcclusionEnabled())
                    interpolateSides(side, q, ssample[qsample[0]], ssample[qsample[1]], ssample[qsample[2]], ssample[qsample[3]]);
                else
                    interpolateSides(side, q, ssample[4], ssample[4], ssample[4], ssample[4]);
            }
            computedSides |= 1 << side;
        }
    }

    private void interpolateSides(int s, int q, int a, int b, int c, int d) {

        sample(a);
        sample(b);
        sample(c);
        sample(d);
        ao[s][q] = interpolateAO(aoSamples[a], aoSamples[b], aoSamples[c], aoSamples[d]) * sideao[s];
        brightness[s][q] = interpolateBrightness(brightnessSamples[a], brightnessSamples[b], brightnessSamples[c], brightnessSamples[d]);
    }

    private static float interpolateAO(float a, float b, float c, float d) {

        return (a + b + c + d) / 4F;
    }

    private static int interpolateBrightness(int a, int b, int c, int d) {

        if (a == 0)
            a = d;
        if (b == 0)
            b = d;
        if (c == 0)
            c = d;
        return (a + b + c + d) >> 2 & 0xFF00FF;
    }

    private static int mixAoBrightness(int b1, int b2, int b3, int b4, double ao1, double ao2, double ao3, double ao4) {

        int i1 = (int) ((b1 >> 16 & 255) * ao1 + (b2 >> 16 & 255) * ao2 + (b3 >> 16 & 255) * ao3 + (b4 >> 16 & 255) * ao4) & 255;
        int j1 = (int) ((b1 & 255) * ao1 + (b2 & 255) * ao2 + (b3 & 255) * ao3 + (b4 & 255) * ao4) & 255;
        return i1 << 16 | j1;
    }

    private static int getProportion(int br, double amt) {

        double a = getProportion((float) (br & 0x0000FF), amt);
        double b = getProportion((float) ((br & 0x00FF00) >> 4), amt);
        double c = getProportion((float) ((br & 0xFF0000) >> 8), amt);

        return (int) a + (((int) b) << 4) + (((int) c) << 8);
    }

    private static float getProportion(float ao, double amt) {

        return (float) (ao * amt);
    }

}
