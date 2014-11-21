package com.qmunity.lib.misc;

public class Map3D<T> {

    private Object[][][] data = null;

    public Map3D(int sizeX, int sizeY, int sizeZ) {

        data = new Object[sizeX][sizeY][sizeZ];
    }

    public Map3D(int sizeX, int sizeY, int sizeZ, T fillValue) {

        this(sizeX, sizeY, sizeZ);

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                for (int z = 0; z < sizeZ; z++) {
                    set(x, y, z, fillValue);
                }
            }
        }
    }

    public void set(int x, int y, int z, T value) {

        data[x][y][z] = value;
    }

    @SuppressWarnings("unchecked")
    public T get(int x, int y, int z) {

        return (T) data[x][y][z];
    }

}
