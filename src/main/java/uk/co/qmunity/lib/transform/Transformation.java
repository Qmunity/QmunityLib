package uk.co.qmunity.lib.transform;

import uk.co.qmunity.lib.model.IVertexOperation;
import uk.co.qmunity.lib.vec.Matrix4;
import uk.co.qmunity.lib.vec.Vector3;

/**
 * Most of this class was made by ChickenBones for CodeChickenLib but has been adapted for use in QmunityLib.<br>
 * You can find the original source at http://github.com/Chicken-Bones/CodeChickenLib
 */
public abstract class Transformation implements ITransformation<Vector3, Transformation>, IVertexOperation {

    @Override
    public Transformation at(Vector3 point) {

        return point.translation().with(this).with(point.translation().inverse());
    }

    @Override
    public TransformationList with(Transformation... transformations) {

        return new TransformationList(this, transformations);
    }

    public abstract Matrix4 getTransformationMatrix();

    @Override
    public void apply(Vector3 vector) {

        getTransformationMatrix().apply(vector);
    }

    @Override
    public void applyN(Vector3 vector) {

    }

    public void apply(Matrix4 matrix) {

        getTransformationMatrix().apply(matrix);
    }

}
