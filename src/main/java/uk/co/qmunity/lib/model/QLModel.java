package uk.co.qmunity.lib.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.Copyable;
import uk.co.qmunity.lib.transform.Rotation;
import uk.co.qmunity.lib.transform.Transformation;
import uk.co.qmunity.lib.vec.Cuboid;
import uk.co.qmunity.lib.vec.Vector3;

public class QLModel implements IVertexSource, Copyable<QLModel> {

    public static final QLModel create(int vertices) {

        return new QLModel(new Vertex[vertices]);
    }

    private Vertex[] vertices;
    private Map<String, QLModel> subModels = new HashMap<String, QLModel>();
    private ModelProperties properties = new ModelProperties();

    public QLModel(Vertex[] vertices) {

        this.vertices = vertices;
    }

    public QLModel(IVertexSource model) {

        Vertex[] originalVertices = model.getVertices();
        vertices = new Vertex[originalVertices.length];
        for (int i = 0; i < vertices.length; i++)
            vertices[i] = originalVertices[i].copy().setOwner(this);

        properties.set(model.getProperties());
    }

    @Override
    public Vertex[] getVertices() {

        return vertices;
    }

    @Override
    public ModelProperties getProperties() {

        return properties;
    }

    public List<QLModel> getSubModels() {

        return new ArrayList<QLModel>(subModels.values());
    }

    public QLModel getSubModel(String identifier) {

        if (subModels.containsKey(identifier))
            return subModels.get(identifier);
        return null;
    }

    public QLModel apply(Transformation... transformations) {

        for (Transformation t : transformations)
            for (Vertex v : vertices)
                t.operate(v);
        return this;
    }

    public QLModel apply(IVertexOperation... operations) {

        for (IVertexOperation o : operations)
            for (Vertex v : vertices)
                o.operate(v);
        return this;
    }

    public QLModel generateCuboid(int start, Cuboid cuboid) {

        return generateCuboid(start, cuboid, 0);
    }

    public QLModel generateCuboid(int start, Cuboid cuboid, int mask) {

        return generateCuboid(start, cuboid.min.x, cuboid.min.y, cuboid.min.z, cuboid.max.x, cuboid.max.y, cuboid.max.z, mask);
    }

    public QLModel generateCuboid(int start, double x1, double y1, double z1, double x2, double y2, double z2) {

        return generateCuboid(start, x1, y1, z1, x2, y2, z2, 0);
    }

    public QLModel generateCuboid(int start, double x1, double y1, double z1, double x2, double y2, double z2, int mask) {

        int i = start;
        double u1, v1, u2, v2;

        if ((mask & 1) == 0) {// bottom face
            u1 = x1;
            v1 = z1;
            u2 = x2;
            v2 = z2;
            vertices[i++] = new Vertex(this, x1, y1, z2, u1, v2, 0);
            vertices[i++] = new Vertex(this, x1, y1, z1, u1, v1, 0);
            vertices[i++] = new Vertex(this, x2, y1, z1, u2, v1, 0);
            vertices[i++] = new Vertex(this, x2, y1, z2, u2, v2, 0);
        }

        if ((mask & 2) == 0) {// top face
            u1 = x1;
            v1 = z1;
            u2 = x2;
            v2 = z2;
            vertices[i++] = new Vertex(this, x2, y2, z2, u2, v2, 1);
            vertices[i++] = new Vertex(this, x2, y2, z1, u2, v1, 1);
            vertices[i++] = new Vertex(this, x1, y2, z1, u1, v1, 1);
            vertices[i++] = new Vertex(this, x1, y2, z2, u1, v2, 1);
        }

        if ((mask & 4) == 0) {// east face
            u1 = 1 - x1;
            v1 = 1 - y2;
            u2 = 1 - x2;
            v2 = 1 - y1;
            vertices[i++] = new Vertex(this, x1, y1, z1, u1, v2, 2);
            vertices[i++] = new Vertex(this, x1, y2, z1, u1, v1, 2);
            vertices[i++] = new Vertex(this, x2, y2, z1, u2, v1, 2);
            vertices[i++] = new Vertex(this, x2, y1, z1, u2, v2, 2);
        }

        if ((mask & 8) == 0) {// west face
            u1 = x1;
            v1 = 1 - y2;
            u2 = x2;
            v2 = 1 - y1;
            vertices[i++] = new Vertex(x2, y1, z2, u2, v2, 3);
            vertices[i++] = new Vertex(x2, y2, z2, u2, v1, 3);
            vertices[i++] = new Vertex(x1, y2, z2, u1, v1, 3);
            vertices[i++] = new Vertex(x1, y1, z2, u1, v2, 3);
        }

        if ((mask & 0x10) == 0) {// north face
            u1 = z1;
            v1 = 1 - y2;
            u2 = z2;
            v2 = 1 - y1;
            vertices[i++] = new Vertex(this, x1, y1, z2, u2, v2, 4);
            vertices[i++] = new Vertex(this, x1, y2, z2, u2, v1, 4);
            vertices[i++] = new Vertex(this, x1, y2, z1, u1, v1, 4);
            vertices[i++] = new Vertex(this, x1, y1, z1, u1, v2, 4);
        }

        if ((mask & 0x20) == 0) {// south face
            u1 = 1 - z1;
            v1 = 1 - y2;
            u2 = 1 - z2;
            v2 = 1 - y1;
            vertices[i++] = new Vertex(this, x2, y1, z1, u1, v2, 5);
            vertices[i++] = new Vertex(this, x2, y2, z1, u1, v1, 5);
            vertices[i++] = new Vertex(this, x2, y2, z2, u2, v1, 5);
            vertices[i++] = new Vertex(this, x2, y1, z2, u2, v2, 5);
        }

        return this;
    }

    @Override
    public QLModel copy() {

        Vertex[] newVertices = new Vertex[vertices.length];
        QLModel model = new QLModel(newVertices);
        for (int i = 0; i < vertices.length; i++)
            newVertices[i] = vertices[i].copy().setOwner(model);
        model.properties.set(properties);
        return model;
    }

    public QLModel backfacedCopy() {

        Vertex[] newVertices = new Vertex[vertices.length];
        QLModel model = new QLModel(newVertices);
        for (int i = 0; i < vertices.length; i++)
            newVertices[i] = vertices[(i % 2 == 1) ? ((i % 4) == 1 ? i + 2 : i - 2) : i].copy().setOwner(model);
        model.properties.set(properties);
        return model;
    }

    public QLModel twoFacedCopy() {

        Vertex[] newVertices = new Vertex[vertices.length * 2];
        QLModel model = new QLModel(newVertices);
        for (int i = 0; i < vertices.length; i++)
            newVertices[i] = vertices[i].copy().setOwner(model);
        for (int i = 0; i < vertices.length; i++)
            newVertices[vertices.length + i] = vertices[(i % 2 == 1) ? ((i % 4) == 1 ? i + 2 : i - 2) : i].copy().setOwner(model);
        model.properties.set(properties);
        return model;
    }

    public static int findSide(Vector3 normal) {

        if (normal.y <= -0.99)
            return 0;
        if (normal.y >= 0.99)
            return 1;
        if (normal.z <= -0.99)
            return 2;
        if (normal.z >= 0.99)
            return 3;
        if (normal.x <= -0.99)
            return 4;
        if (normal.x >= 0.99)
            return 5;
        return -1;
    }

    public QLModel computeLighting() {

        return computeNormals().computeLightCoords();
    }

    public QLModel computeNormals() {

        // Compute normals
        for (int i = 0; i < vertices.length; i += 4) {
            Vector3 diff1 = vertices[i + 1].position.copy().sub(vertices[i].position);
            Vector3 diff2 = vertices[i + 3].position.copy().sub(vertices[i].position);
            vertices[i].normal = diff1.cross(diff2).normalize();
            for (int d = 1; d < 4; d++)
                vertices[i + d].normal = vertices[i].normal.copy();
        }
        properties.setHasNormals(true);

        return this;
    }

    public QLModel computeLightCoords() {

        // Compute light coords
        for (int i = 0; i < vertices.length; i++)
            vertices[i].lc = new LightCoord().compute(vertices[i].position, vertices[i].normal);
        properties.setHasLighting(true);

        return this;
    }

    public QLModel onFace(ForgeDirection face) {

        return onFace(face, Vector3.center);
    }

    public QLModel onFace(ForgeDirection face, Vector3 center) {

        return copy().apply(Rotation.sideRotations[face.ordinal()].at(center));
    }

    public QLModel[] computeFaces() {

        return computeFaces(Vector3.center);
    }

    public QLModel[] computeFaces(Vector3 center) {

        return new QLModel[] {//
        onFace(ForgeDirection.DOWN, center), onFace(ForgeDirection.UP, center), onFace(ForgeDirection.NORTH, center),
                onFace(ForgeDirection.SOUTH, center), onFace(ForgeDirection.WEST, center), onFace(ForgeDirection.EAST, center) };
    }

    public QLModel quarterRotation(int rot) {

        return quarterRotation(rot, Vector3.center);
    }

    public QLModel quarterRotation(int rot, Vector3 center) {

        return copy().apply(Rotation.quarterRotations[rot].at(center));
    }

    public QLModel[] computeQuarterRotations() {

        return computeQuarterRotations(Vector3.center);
    }

    public QLModel[] computeQuarterRotations(Vector3 center) {

        return new QLModel[] { quarterRotation(0), quarterRotation(1), quarterRotation(2), quarterRotation(3) };
    }

    public QLModel[][] computeVariants() {

        return computeVariants(Vector3.center);
    }

    public QLModel[][] computeVariants(Vector3 center) {

        QLModel[][] variants = new QLModel[6][4];

        int j = 0;
        for (QLModel qr : computeQuarterRotations(center)) {
            int i = 0;
            for (QLModel fr : qr.computeFaces(center)) {
                variants[i][j] = fr;
                i++;
            }
            j++;
        }

        return variants;
    }
}
