package uk.co.qmunity.lib.transform;

public interface ITransformation<OBJ, TYPE> {

    public void apply(OBJ vector);

    public void applyN(OBJ vector);

    public TYPE at(OBJ point);

    @SuppressWarnings("unchecked")
    public TYPE with(TYPE... transformations);

    public TYPE inverse();

}
