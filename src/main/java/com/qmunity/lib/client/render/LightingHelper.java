package com.qmunity.lib.client.render;

public class LightingHelper {

    public static int join(int l1, int l2, int l3, int l4) {

        return (l1 + l2 + l3 + l4) >> 2 & 0xFF00FF;
    }

    public static int join(int l1, int l2) {

        return join(l1, l1, l2, l2);
    }

}
