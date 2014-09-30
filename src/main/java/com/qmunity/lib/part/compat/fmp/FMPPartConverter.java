package com.qmunity.lib.part.compat.fmp;

import java.util.ArrayList;
import java.util.List;

import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;

import com.qmunity.lib.QLModInfo;
import com.qmunity.lib.part.IPart;

public class FMPPartConverter extends TMultiPart {

    private List<IPart> parts = new ArrayList<IPart>();

    public FMPPartConverter(List<IPart> parts) {

        this.parts = parts;
    }

    @Override
    public String getType() {

        return QLModInfo.MODID + ".multipart.converter";
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void update() {

        super.update();

        if (!world().isRemote && parts != null) {
            for (IPart p : parts) {
                TileMultipart.addPart(world(), new BlockCoord(tile()), new FMPPart(p));
            }

            tile().remPart(this);

            parts = null;
        }
    }

    @Override
    public void onWorldJoin() {

        super.onWorldJoin();

        onAdded();
    }
}
