package uk.co.qmunity.lib.vec;

import java.util.Comparator;

public class VectorSorter implements Comparator<Vec3d> {

    private Vec3d start;

    public VectorSorter(Vec3d start) {

        this.start = start;
    }

    @Override
    public int compare(Vec3d o1, Vec3d o2) {

        return Double.compare(o1.distanceTo(start), o1.distanceTo(start));
    }
}
