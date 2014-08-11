package com.qmunity.lib.vec;

import java.util.Comparator;

public class VectorSorter implements Comparator<Vector3> {

    private Vector3 start;

    public VectorSorter(Vector3 start) {

        this.start = start;
    }

    @Override
    public int compare(Vector3 o1, Vector3 o2) {

        return Double.compare(o1.distanceTo(start), o1.distanceTo(start));
    }
}
