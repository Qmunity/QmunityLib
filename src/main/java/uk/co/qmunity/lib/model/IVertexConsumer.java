package uk.co.qmunity.lib.model;


public interface IVertexConsumer {

    public boolean consumeVertices(IVertexSource model, IVertexOperation... operations);

}
