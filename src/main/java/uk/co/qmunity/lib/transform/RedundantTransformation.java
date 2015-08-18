package uk.co.qmunity.lib.transform;

import uk.co.qmunity.lib.model.Vertex;
import uk.co.qmunity.lib.vec.Matrix4;
import uk.co.qmunity.lib.vec.Vector3;

/**
 * Most of this class was made by ChickenBones for CodeChickenLib but has been adapted for use in QmunityLib.<br>
 * You can find the original source at http://github.com/Chicken-Bones/CodeChickenLib
 */
public class RedundantTransformation extends Transformation {

    private final Matrix4 mat = new Matrix4().setIdentity();

    @Override
    public void apply(Vector3 vec) {

    }

    @Override
    public void apply(Matrix4 mat) {

    }

    @Override
    public void applyN(Vector3 normal) {

    }

    @Override
    public Transformation at(Vector3 point) {

        return this;
    }

    @Override
    public Transformation inverse() {

        return this;
    }

    @Override
    public String toString() {

        return "Nothing()";
    }

    @Override
    public void operate(Vertex vertex) {

    }

    @Override
    public Matrix4 getTransformationMatrix() {

        return mat;
    }
}