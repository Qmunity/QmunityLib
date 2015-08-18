package uk.co.qmunity.lib.transform;

import uk.co.qmunity.lib.model.Vertex;
import uk.co.qmunity.lib.vec.Matrix4;
import uk.co.qmunity.lib.vec.Vector3;

/**
 * Most of this class was made by ChickenBones for CodeChickenLib but has been adapted for use in QmunityLib.<br>
 * You can find the original source at http://github.com/Chicken-Bones/CodeChickenLib
 */
public class Scale extends Transformation {

    private Vector3 vector;
    private Matrix4 mat;

    public Scale(double scale) {

        this(scale, scale, scale);
    }

    public Scale(double x, double y, double z) {

        this(new Vector3(x, y, z));
    }

    public Scale(Vector3 vector) {

        this.vector = vector;
        mat = new Matrix4().setIdentity().scale(vector);
    }

    @Override
    public Transformation inverse() {

        return new Scale(vector.copy().inverse());
    }

    @Override
    public Matrix4 getTransformationMatrix() {

        return mat;
    }

    @Override
    public void operate(Vertex vertex) {

        mat.operate(vertex);
    }
}
