package uk.co.qmunity.lib.part;

import java.util.List;

/**
 * Interface implemented by the parts that want to add a tooltip to WAILA.
 *
 * @author amadornes
 */
public interface IPartWAILAProvider extends IPart {

    /**
     * Adds content to the WAILA tooltip.
     */
    public void addWAILABody(List<String> text);

}
