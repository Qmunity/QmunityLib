package uk.co.qmunity.lib.transform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import uk.co.qmunity.lib.model.IVertexOperation;
import uk.co.qmunity.lib.model.Vertex;
import uk.co.qmunity.lib.vec.Matrix4;
import uk.co.qmunity.lib.vec.Vector3;

/**
 * Most of this class was made by ChickenBones for CodeChickenLib but has been adapted for use in QmunityLib.<br>
 * You can find the original source at http://github.com/Chicken-Bones/CodeChickenLib
 */
public class TransformationList extends Transformation implements Iterable<Transformation> {

    private final List<Transformation> transformations;

    public TransformationList(Iterable<Transformation> transformations) {

        this.transformations = new ArrayList<Transformation>();
        for (Transformation t : transformations)
            add(t);
    }

    public TransformationList(Transformation... transformations) {

        this.transformations = new ArrayList<Transformation>();
        for (Transformation t : transformations)
            add(t);
    }

    public TransformationList(Transformation transformation, Transformation... transformations) {

        this.transformations = new ArrayList<Transformation>();
        add(transformation);
        for (Transformation t : transformations)
            add(t);
    }

    private TransformationList(List<Transformation> transformations) {

        this.transformations = transformations;
    }

    public TransformationList add(Transformation... transformations) {

        this.transformations.addAll(Arrays.asList(transformations));

        return this;
    }

    @Override
    public Iterator<Transformation> iterator() {

        return transformations.iterator();
    }

    @Override
    public void apply(Vector3 vector) {

        // for (int i = transformations.size() - 1; i >= 0; i--)
        // transformations.get(i).apply(vector);
        for (Transformation t : transformations)
            t.apply(vector);
    }

    @Override
    public void apply(Matrix4 matrix) {

        // for (int i = transformations.size() - 1; i >= 0; i--)
        // transformations.get(i).apply(matrix);
        for (Transformation t : transformations)
            t.apply(matrix);
    }

    @Override
    public TransformationList inverse() {

        List<Transformation> reversed = new ArrayList<Transformation>(transformations);
        Collections.reverse(reversed);
        return new TransformationList(reversed);
    }

    @Override
    public TransformationList with(Transformation... transformations) {

        add(transformations);

        return this;
    }

    @Override
    public Matrix4 getTransformationMatrix() {

        Matrix4 matrix = new Matrix4().setIdentity();
        apply(matrix);
        return matrix;
    }

    @Override
    public void operate(Vertex vertex) {

        for (int i = transformations.size() - 1; i >= 0; i--)
            if (transformations.get(i) instanceof IVertexOperation)
                ((IVertexOperation) transformations.get(i)).operate(vertex);
    }

}
