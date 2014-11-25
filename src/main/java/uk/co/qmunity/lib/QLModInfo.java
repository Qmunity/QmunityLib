package uk.co.qmunity.lib;

public class QLModInfo{

    public static final String MODID = "qmunitylib";
    public static final String NAME = "QmunityLib";
    private static final String MAJOR = "@MAJOR@";
    private static final String MINOR = "@MINOR@";
    private static final String BUILD = "@BUILD_NUMBER@";
    private static final String MCVERSION = "1.7.10";

    public static String fullVersionString(){

        return String.format("%s-%s.%s.%s", MCVERSION, MAJOR, MINOR, BUILD);
    }
}
