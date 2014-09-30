package com.qmunity.lib.ref;

import com.qmunity.lib.QLModInfo;

public class Names {

    private static final String BASE = QLModInfo.MODID;

    public static class Registry {

        public static class Blocks {

            private static final String BASE = Names.BASE + ".blocks.";

            public static final String MULTIPART = BASE + "multipart";

        }

        public static class Items {

            private static final String BASE = Names.BASE + ".items.";

            public static final String MULTIPART = BASE + "multipart";

        }
    }

    public static class Unlocalized {

        public static class Blocks {

            private static final String BASE = Names.BASE + ":";

            public static final String MULTIPART = BASE + "multipart";

        }

        public static class Items {

            private static final String BASE = Names.BASE + ":";

            public static final String MULTIPART = BASE + "multipart";

        }
    }

}
