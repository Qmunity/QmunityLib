package uk.co.qmunity.lib.transform;

import uk.co.qmunity.lib.model.Vertex;
import uk.co.qmunity.lib.vec.Matrix4;
import uk.co.qmunity.lib.vec.Quat;
import uk.co.qmunity.lib.vec.Vector3;

/**
 * Most of this class was made by ChickenBones for CodeChickenLib but has been adapted for use in QmunityLib.<br>
 * You can find the original source at http://github.com/Chicken-Bones/CodeChickenLib
 */
public class Rotation extends Transformation {

    public static Vector3[] axes = new Vector3[] { new Vector3(0, -1, 0), new Vector3(0, 1, 0), new Vector3(0, 0, -1),
            new Vector3(0, 0, 1), new Vector3(-1, 0, 0), new Vector3(1, 0, 0) };

    public static Transformation[] quarterRotations = new Transformation[] { new RedundantTransformation(),
            new VariableTransformation(new Matrix4(0, 0, -1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1)) {
                @Override
                public void apply(Vector3 vec) {

                    double d1 = vec.x;
                    double d2 = vec.z;
                    vec.x = -d2;
                    vec.z = d1;
                }

                @Override
                public Transformation inverse() {

                    return quarterRotations[3];
                }
            }, new VariableTransformation(new Matrix4(-1, 0, 0, 0, 0, 1, 0, 0, 0, 0, -1, 0, 0, 0, 0, 1)) {
                @Override
                public void apply(Vector3 vec) {

                    vec.x = -vec.x;
                    vec.z = -vec.z;
                }

                @Override
                public Transformation inverse() {

                    return this;
                }
            }, new VariableTransformation(new Matrix4(0, 0, 1, 0, 0, 1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 1)) {
                @Override
                public void apply(Vector3 vec) {

                    double d1 = vec.x;
                    double d2 = vec.z;
                    vec.x = d2;
                    vec.z = -d1;
                }

                @Override
                public Transformation inverse() {

                    return quarterRotations[1];
                }
            } };

    public static Transformation[] sideRotations = new Transformation[] { new RedundantTransformation(),
            new VariableTransformation(new Matrix4(1, 0, 0, 0, 0, -1, 0, 0, 0, 0, -1, 0, 0, 0, 0, 1)) {
                @Override
                public void apply(Vector3 vec) {

                    vec.y = -vec.y;
                    vec.z = -vec.z;
                }

                @Override
                public Transformation inverse() {

                    return this;
                }
            }, new VariableTransformation(new Matrix4(1, 0, 0, 0, 0, 0, -1, 0, 0, 1, 0, 0, 0, 0, 0, 1)) {
                @Override
                public void apply(Vector3 vec) {

                    double d1 = vec.y;
                    double d2 = vec.z;
                    vec.y = -d2;
                    vec.z = d1;
                }

                @Override
                public Transformation inverse() {

                    return sideRotations[3];
                }
            }, new VariableTransformation(new Matrix4(1, 0, 0, 0, 0, 0, 1, 0, 0, -1, 0, 0, 0, 0, 0, 1)) {
                @Override
                public void apply(Vector3 vec) {

                    double d1 = vec.y;
                    double d2 = vec.z;
                    vec.y = d2;
                    vec.z = -d1;
                }

                @Override
                public Transformation inverse() {

                    return sideRotations[2];
                }
            }, new VariableTransformation(new Matrix4(0, 1, 0, 0, -1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1)) {
                @Override
                public void apply(Vector3 vec) {

                    double d0 = vec.x;
                    double d1 = vec.y;
                    vec.x = d1;
                    vec.y = -d0;
                }

                @Override
                public Transformation inverse() {

                    return sideRotations[5];
                }
            }, new VariableTransformation(new Matrix4(0, -1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1)) {
                @Override
                public void apply(Vector3 vec) {

                    double d0 = vec.x;
                    double d1 = vec.y;
                    vec.x = -d1;
                    vec.y = d0;
                }

                @Override
                public Transformation inverse() {

                    return sideRotations[4];
                }
            } };

    private double angle;
    private Vector3 axis;

    private Matrix4 mat;

    public Rotation(double angle, double x, double y, double z) {

        this(angle, new Vector3(x, y, z));
    }

    public Rotation(double angle, Vector3 axis) {

        this.angle = angle;
        this.axis = axis;
        mat = new Matrix4().setIdentity().rotate(-angle, axis);
    }

    public Rotation(Quat quat) {

        angle = Math.acos(quat.s) * 2;
        if (angle == 0) {
            axis = new Vector3(0, 1, 0);
        } else {
            double sa = Math.sin(angle * 0.5);
            axis = new Vector3(quat.x / sa, quat.y / sa, quat.z / sa);
        }
        mat = new Matrix4().setIdentity().rotate(-angle, axis);
    }

    @Override
    public Transformation inverse() {

        return new Rotation(-angle, axis);
    }

    @Override
    public Matrix4 getTransformationMatrix() {

        return mat;
    }

    @Override
    public void operate(Vertex vertex) {

        apply(vertex.position);
        applyN(vertex.normal);
    }

    @Override
    public void applyN(Vector3 vector) {

        apply(vector);
    }

}
