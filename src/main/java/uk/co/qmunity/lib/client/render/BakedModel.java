package uk.co.qmunity.lib.client.render;

import java.util.LinkedList;

import uk.co.qmunity.lib.vec.Vec3d;

public class BakedModel extends RenderHelper {

    private LinkedList<Vertex> vertices = new LinkedList<Vertex>();

    public void render() {

        RenderHelper.instance.renderBakedModel(this);
    }

    public void render(RenderHelper renderHelper) {

        for (Vertex v : vertices)
            // renderHelper.addVertex(v.vertex.getX(), v.vertex.getY(), v.vertex.getZ(), v.u, v.v);
            renderHelper.addVertex_do(v.vertex, v.normal, renderHelper.getColor(), v.opacity, -1, v.u, v.v);
    }

    @Override
    public void addVertex_do(Vec3d vertex, Vec3d normal, int color, int opacity, int brightness, double u, double v) {

        Vertex vert = new Vertex();
        vert.vertex = vertex;
        vert.normal = normal;
        vert.color = color;
        vert.opacity = opacity;
        vert.u = u;
        vert.v = v;
        vertices.add(vert);
    }

    private static class Vertex {

        protected Vec3d vertex, normal;
        protected int color, opacity;
        protected double u, v;
    }

    public void destroy() {

        vertices.clear();
        transformations.clear();
    }

}
