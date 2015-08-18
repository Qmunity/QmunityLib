package uk.co.qmunity.lib.client.texture;

import net.minecraft.util.IIcon;
import uk.co.qmunity.lib.model.IVertexOperation;
import uk.co.qmunity.lib.model.Vertex;

public class IconTransformation implements IVertexOperation {

    public int id;
    public IIcon icon;
    public IIcon override;

    public IconTransformation(int id, IIcon icon) {

        this.id = id;
        this.icon = icon;
    }

    public IconTransformation(IIcon icon) {

        this(-1, icon);
    }

    public IconTransformation setOverride(IIcon override) {

        this.override = override;
        return this;
    }

    @Override
    public void operate(Vertex vertex) {

        IIcon icon = this.override != null ? this.override : this.icon;

        if (id == -1 || vertex.uv.texID == id)
            vertex.uv.set(icon.getInterpolatedU(vertex.uv.u * 16), icon.getInterpolatedV(vertex.uv.v * 16));
        if (vertex.owner != null)
            vertex.owner.getProperties().setHasTextures(true);
    }

}
