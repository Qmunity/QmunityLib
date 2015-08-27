package uk.co.qmunity.lib.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import uk.co.qmunity.lib.QmunityLib;
import uk.co.qmunity.lib.network.LocatedPacket;
import uk.co.qmunity.lib.network.MCByteBuf;
import uk.co.qmunity.lib.tile.QLTileBase;

public class PacketCUpdateTile extends LocatedPacket<PacketCUpdateTile> {

    private QLTileBase tile;

    public PacketCUpdateTile(QLTileBase tile) {

        super(tile);
        this.tile = tile;
    }

    public PacketCUpdateTile() {

    }

    @Override
    public void handleClientSide(EntityPlayer player) {

    }

    @Override
    public void handleServerSide(EntityPlayer player) {

    }

    @Override
    public void toBytes(MCByteBuf buf) {

        super.toBytes(buf);
        tile.writeUpdateData(buf);
    }

    @Override
    public void fromBytes(MCByteBuf buf) {

        super.fromBytes(buf);
        TileEntity te = QmunityLib.proxy.getWorld().getTileEntity(x, y, z);
        if (te == null || !(te instanceof QLTileBase))
            return;
        tile = (QLTileBase) te;
        tile.readUpdateData(buf);
        tile.markRender();
    }

}
