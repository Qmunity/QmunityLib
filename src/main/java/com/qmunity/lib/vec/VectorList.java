package com.qmunity.lib.vec;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.World;

public class VectorList extends ArrayList<Vec3d> {

    public VectorList(List<Vec3d> copied) {

        this();
        addAll(copied);
    }

    public VectorList() {

        super();
    }

    private static final long serialVersionUID = 1L;

    public boolean add(double x, double y, double z) {

        return super.add(new Vec3d(x, y, z));
    }

    public boolean add(double x, double y, double z, World w) {

        return super.add(new Vec3d(x, y, z, w));
    }

    public boolean remove(double x, double y, double z) {

        return super.remove(new Vec3d(x, y, z));
    }

    public boolean remove(double x, double y, double z, World w) {

        return super.remove(new Vec3d(x, y, z, w));
    }

}
