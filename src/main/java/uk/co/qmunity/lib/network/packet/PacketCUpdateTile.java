package uk.co.qmunity.lib.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import uk.co.qmunity.lib.QmunityLib;
import uk.co.qmunity.lib.network.LocatedPacket;
import uk.co.qmunity.lib.network.MCByteBuf;
import uk.co.qmunity.lib.tile.TileBase;

public class PacketCUpdateTile extends LocatedPacket<PacketCUpdateTile> {

    private TileBase tile;

    public PacketCUpdateTile(TileBase tile) {

        super(tile);
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
        if (te == null || !(te instanceof TileBase))
            return;
        tile = (TileBase) te;
        tile.readUpdateData(buf);
    }

}
