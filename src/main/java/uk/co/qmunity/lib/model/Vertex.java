package uk.co.qmunity.lib.model;

import uk.co.qmunity.lib.Copyable;
import uk.co.qmunity.lib.model.uv.UV;
import uk.co.qmunity.lib.vec.Vector3;
import uk.co.qmunity.lib.vec.Vector4;

public class Vertex implements Copyable<Vertex> {

    public IVertexSource owner;
    public Vector3 position;
    public Vector3 normal;
    public Vector4 color;
    public UV uv;
    public LightCoord lc;
    public int brightness;

    public Vertex(double x, double y, double z, double u, double v, int texID) {

        this(null, x, y, z, u, v, texID);
    }

    public Vertex(IVertexSource owner, double x, double y, double z, double u, double v, int texID) {

        this(owner, new Vector3(x, y, z), new Vector3(0, 0, 0), new Vector4(1, 1, 1, 1), new UV(u, v, texID), null, 0);
    }

    public Vertex(IVertexSource owner, Vector3 position, Vector3 normal, Vector4 color, UV uv, LightCoord lc, int brightness) {

        this.position = position;
        this.normal = normal;
        this.color = color;
        this.uv = uv;
        this.lc = lc;
        this.brightness = brightness;
    }

    @Override
    public Vertex copy() {

        return new Vertex(null, position.copy(), normal.copy(), color.copy(), uv.copy(), lc == null ? null : lc.copy(), brightness);
    }

    public Vertex setOwner(IVertexSource owner) {

        this.owner = owner;
        return this;
    }

}
