package uk.co.qmunity.lib.transform;

/**
 * Most of this class was made by ChickenBones for CodeChickenLib but has been adapted for use in QmunityLib.<br>
 * You can find the original source at http://github.com/Chicken-Bones/CodeChickenLib
 */
@SuppressWarnings("serial")
public class IrreversibleTransformationException extends RuntimeException {

    public ITransformation<?, ?> t;

    public IrreversibleTransformationException(ITransformation<?, ?> t) {

        this.t = t;
    }

    @Override
    public String getMessage() {

        return "The following transformation is irreversible:\n" + t;
    }

}
