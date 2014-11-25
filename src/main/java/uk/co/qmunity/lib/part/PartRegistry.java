package uk.co.qmunity.lib.part;

import java.util.ArrayList;
import java.util.List;

public class PartRegistry {

    private static final List<IPartFactory> factories = new ArrayList<IPartFactory>();

    public static void registerFactory(IPartFactory factory) {

        if (factory == null)
            return;
        if (factories.contains(factory))
            return;

        factories.add(factory);
    }

    public static IPart createPart(String type, boolean client) {

        for (IPartFactory f : factories) {
            IPart p = f.createPart(type, client);
            if (p != null)
                return p;
        }

        return null;
    }

}
