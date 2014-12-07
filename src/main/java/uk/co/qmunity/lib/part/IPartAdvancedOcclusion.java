package uk.co.qmunity.lib.part;

/**
 * This is an advanced version of {@link IPartOccluding} which allows you to define a custom occlusion check.
 *
 * @author amadornes
 */
public interface IPartAdvancedOcclusion extends IPartOccluding {

    /**
     * Checks if the part passed as an argument occludes in any way this part. Return false if it does, true if it doesn't.
     */
    public boolean occlusionTest(IPartOccluding part);

}
