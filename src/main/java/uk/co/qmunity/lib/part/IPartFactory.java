package uk.co.qmunity.lib.part;

public interface IPartFactory {

    IPart createPart(String type, boolean client);

}
