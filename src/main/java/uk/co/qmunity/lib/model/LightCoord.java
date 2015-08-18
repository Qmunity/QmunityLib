package uk.co.qmunity.lib.model;

import uk.co.qmunity.lib.Copyable;
import uk.co.qmunity.lib.transform.Rotation;
import uk.co.qmunity.lib.vec.Vector3;

public class LightCoord implements Copyable<LightCoord> {

    public int side;
    public float fa;
    public float fb;
    public float fc;
    public float fd;

    public LightCoord() {

        this(0, 0, 0, 0, 0);
    }

    public LightCoord(int s, float a, float b, float c, float d) {

        side = s;
        fa = a;
        fb = b;
        fc = c;
        fd = d;
    }

    public LightCoord set(int s, float a, float b, float c, float d) {

        side = s;
        fa = a;
        fb = b;
        fc = c;
        fd = d;
        return this;
    }

    public LightCoord set(LightCoord lc) {

        return set(lc.side, lc.fa, lc.fb, lc.fc, lc.fd);
    }

    public LightCoord compute(Vector3 vec, Vector3 normal) {

        int side = QLModel.findSide(normal);
        if (side < 0)
            return set(12, 1, 0, 0, 0);
        return compute(vec, side);
    }

    public LightCoord compute(Vector3 vec, int side) {

        boolean offset = false;
        switch (side) {
        case 0:
            offset = vec.y <= 0;
            break;
        case 1:
            offset = vec.y >= 1;
            break;
        case 2:
            offset = vec.z <= 0;
            break;
        case 3:
            offset = vec.z >= 1;
            break;
        case 4:
            offset = vec.x <= 0;
            break;
        case 5:
            offset = vec.x >= 1;
            break;
        }
        if (!offset)
            side += 6;
        return computeO(vec, side);
    }

    public LightCoord computeO(Vector3 vec, int side) {

        Vector3 v1 = Rotation.axes[((side & 0xE) + 3) % 6];
        Vector3 v2 = Rotation.axes[((side & 0xE) + 5) % 6];
        float d1 = vec.scalarProject(v1);
        float d2 = 1 - d1;
        float d3 = vec.scalarProject(v2);
        float d4 = 1 - d3;
        return set(side, d2 * d4, d2 * d3, d1 * d4, d1 * d3);
    }

    @Override
    public LightCoord copy() {

        return new LightCoord(side, fa, fb, fc, fd);
    }
}