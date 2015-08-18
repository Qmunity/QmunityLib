package uk.co.qmunity.lib.transform;

import uk.co.qmunity.lib.model.Vertex;
import uk.co.qmunity.lib.vec.Matrix4;
import uk.co.qmunity.lib.vec.Vector3;

/**
 * Most of this class was made by ChickenBones for CodeChickenLib but has been adapted for use in QmunityLib.<br>
 * You can find the original source at http://github.com/Chicken-Bones/CodeChickenLib
 */
public abstract class VariableTransformation extends Transformation {

    public Matrix4 mat;

    public VariableTransformation(Matrix4 mat) {

        this.mat = mat;
    }

    @Override
    public void applyN(Vector3 normal) {

        apply(normal);
    }

    @Override
    public void apply(Matrix4 mat) {

        mat.multiply(this.mat);
    }

    @Override
    public Matrix4 getTransformationMatrix() {

        return this.mat;
    }

    @Override
    public void operate(Vertex vertex) {

        apply(vertex.position);
        applyN(vertex.normal);
    }
}