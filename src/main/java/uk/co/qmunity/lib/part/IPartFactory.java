package uk.co.qmunity.lib.part;

/**
 * Interface used to generate part instances from a part type. This interface shall NOT be implemented by a part.
 *
 * @author amadornes
 */
public interface IPartFactory {

    /**
     * Creates a new part of the specified type and for the specified side.
     */
    public IPart createPart(String type, boolean client);

}
