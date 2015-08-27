package uk.co.qmunity.lib.model;

import uk.co.qmunity.lib.vec.Vector4;

public class ColorMultiplier implements IVertexOperation {

    public int color;

    public ColorMultiplier(int color) {

        this.color = color;
    }

    @Override
    public void operate(Vertex vertex) {

        vertex.color.mul(Vector4.colorRGB(color));
    }

}
