package uk.co.qmunity.lib.model.uv;

import uk.co.qmunity.lib.Copyable;

public class UV implements Copyable<UV> {

    public double u, v;
    public int texID;

    public UV(double u, double v) {

        this.u = u;
        this.v = v;
    }

    public UV(double u, double v, int texID) {

        this(u, v);
        this.texID = texID;
    }

    public UV(UV uv) {

        this(uv.u, uv.v, uv.texID);
    }

    public void set(double u, double v) {

        this.u = u;
        this.v = v;
    }

    @Override
    public UV copy() {

        return new UV(this);
    }

}
