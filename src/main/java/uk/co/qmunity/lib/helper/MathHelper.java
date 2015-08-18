package uk.co.qmunity.lib.helper;

import uk.co.qmunity.lib.vec.Vector2;
import uk.co.qmunity.lib.vec.Vector3;
import uk.co.qmunity.lib.vec.Vector4;

public class MathHelper {

    public static final double phi = 1.618033988749894;
    public static final double pi = Math.PI;
    public static final double todeg = 57.29577951308232;
    public static final double torad = 0.017453292519943;
    public static final double sqrt2 = 1.414213562373095;

    public static double[] SIN_TABLE = new double[65536];
    static {
        for (int i = 0; i < 65536; ++i)
            SIN_TABLE[i] = Math.sin(i / 65536D * 2 * Math.PI);

        SIN_TABLE[0] = 0;
        SIN_TABLE[16384] = 1;
        SIN_TABLE[32768] = 0;
        SIN_TABLE[49152] = 1;
    }

    public static double sin(double d) {

        return SIN_TABLE[(int) ((float) d * 10430.378F) & 65535];
    }

    public static double cos(double d) {

        return SIN_TABLE[(int) ((float) d * 10430.378F + 16384.0F) & 65535];
    }

    /**
     * @param a
     *            The value
     * @param b
     *            The value to approach
     * @param max
     *            The maximum step
     * @return the closed value to b no less than max from a
     */
    public static float approachLinear(float a, float b, float max) {

        return (a > b) ? (a - b < max ? b : a - max) : (b - a < max ? b : a + max);
    }

    /**
     * @param a
     *            The value
     * @param b
     *            The value to approach
     * @param max
     *            The maximum step
     * @return the closed value to b no less than max from a
     */
    public static double approachLinear(double a, double b, double max) {

        return (a > b) ? (a - b < max ? b : a - max) : (b - a < max ? b : a + max);
    }

    /**
     * @param a
     *            The first value
     * @param b
     *            The second value
     * @param d
     *            The interpolation factor, between 0 and 1
     * @return a+(b-a)*d
     */
    public static float interpolate(float a, float b, float d) {

        return a + (b - a) * d;
    }

    /**
     * @param a
     *            The first value
     * @param b
     *            The second value
     * @param d
     *            The interpolation factor, between 0 and 1
     * @return a+(b-a)*d
     */
    public static double interpolate(double a, double b, double d) {

        return a + (b - a) * d;
    }

    /**
     * @param a
     *            The value
     * @param b
     *            The value to approach
     * @param ratio
     *            The ratio to reduce the difference by
     * @return a+(b-a)*ratio
     */
    public static double approachExp(double a, double b, double ratio) {

        return a + (b - a) * ratio;
    }

    /**
     * @param a
     *            The value
     * @param b
     *            The value to approach
     * @param ratio
     *            The ratio to reduce the difference by
     * @param cap
     *            The maximum amount to advance by
     * @return a+(b-a)*ratio
     */
    public static double approachExp(double a, double b, double ratio, double cap) {

        double d = (b - a) * ratio;
        if (Math.abs(d) > cap)
            d = Math.signum(d) * cap;
        return a + d;
    }

    /**
     * @param a
     *            The value
     * @param b
     *            The value to approach
     * @param ratio
     *            The ratio to reduce the difference by
     * @param c
     *            The value to retreat from
     * @param kick
     *            The difference when a == c
     * @return
     */
    public static double retreatExp(double a, double b, double c, double ratio, double kick) {

        double d = (Math.abs(c - a) + kick) * ratio;
        if (d > Math.abs(b - a))
            return b;
        return a + Math.signum(b - a) * d;
    }

    public static double clip(double value, double min, double max) {

        if (value > max)
            value = max;
        if (value < min)
            value = min;
        return value;
    }

    public static Vector2 clip(Vector2 vector, double min, double max) {

        clip(vector.x, min, max);
        clip(vector.y, min, max);
        return vector;
    }

    public static Vector3 clip(Vector3 vector, double min, double max) {

        clip(vector.x, min, max);
        clip(vector.y, min, max);
        clip(vector.z, min, max);
        return vector;
    }

    public static Vector4 clip(Vector4 vector, double min, double max) {

        clip(vector.x, min, max);
        clip(vector.y, min, max);
        clip(vector.z, min, max);
        clip(vector.s, min, max);
        return vector;
    }

    public static Vector4 clipColor(Vector4 vector) {

        return clip(vector, 0, 1);
    }

    public static boolean isBetween(double a, double x, double b) {

        return a <= x && x <= b;
    }

    public static int floor(double d) {

        return (int) Math.floor(d);
    }

    public static int ceil(double d) {

        return (int) Math.ceil(d);
    }

    public static int round(double d) {

        return (int) Math.round(d);
    }

    public static int roundAway(double d) {

        return (int) (d < 0 ? Math.floor(d) : Math.ceil(d));
    }

    public static long mean(long[] values) {

        long sum = 0l;
        for (long v : values) {
            sum += v;
        }
        return sum / values.length;
    }
}
