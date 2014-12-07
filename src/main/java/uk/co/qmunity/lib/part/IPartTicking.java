package uk.co.qmunity.lib.part;

/**
 * Interface implemented by parts that should do something every tick.
 *
 * @author amadornes
 */
public interface IPartTicking extends IPart {

    /**
     * Called every game tick.
     */
    public void update();

}
