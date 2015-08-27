package uk.co.qmunity.lib.tile;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.network.MCByteBuf;
import uk.co.qmunity.lib.network.NetworkHandler;
import uk.co.qmunity.lib.network.annotation.DescSynced;
import uk.co.qmunity.lib.network.annotation.SyncNetworkUtils;
import uk.co.qmunity.lib.network.annotation.SyncedField;
import uk.co.qmunity.lib.network.packet.PacketCUpdateTile;
import uk.co.qmunity.lib.util.INBTSaveable;
import uk.co.qmunity.lib.util.ISyncable;
import uk.co.qmunity.lib.vec.IWorldLocation;

@SuppressWarnings("rawtypes")
public class QLTileBase extends TileEntity implements IWorldLocation, ISyncable, INBTSaveable {

    /*
     * IWorldLocation
     */

    @Override
    public int getX() {

        return xCoord;
    }

    @Override
    public int getY() {

        return yCoord;
    }

    @Override
    public int getZ() {

        return zCoord;
    }

    @Override
    public World getWorld() {

        return getWorldObj();
    }

    /*
     * ISyncable
     */

    @Override
    public void writeUpdateData(MCByteBuf buf) {

    }

    @Override
    public void readUpdateData(MCByteBuf buf) {

    }

    @Override
    public void sendUpdatePacket() {

        if (getWorld() != null)
            NetworkHandler.QLIB.sendToAllAround(new PacketCUpdateTile(this), getWorld());
    }

    /*
     * Synchronization bridge for description packets (so the client can get a description of the TE when it loads without requesting a packet).
     */

    @Override
    public Packet getDescriptionPacket() {

        NBTTagCompound tag = new NBTTagCompound();

        ByteBuf buf = Unpooled.buffer();
        writeUpdateData(new MCByteBuf(buf));
        tag.setByteArray("data", buf.array());

        return new S35PacketUpdateTileEntity(getX(), getY(), getZ(), 2, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {

        ByteBuf buf = Unpooled.copiedBuffer(pkt.func_148857_g().getByteArray("data"));
        readUpdateData(new MCByteBuf(buf));
        markRender();
    }

    /*
     * Synchronization tags
     */

    private List<SyncedField> descriptionFields;

    public List<SyncedField> getDescriptionFields() {

        if (descriptionFields == null) {
            descriptionFields = SyncNetworkUtils.getSyncedFields(this, DescSynced.class);
            for (SyncedField field : descriptionFields) {
                field.update();
            }
        }
        return descriptionFields;
    }

    /*
     * Helper methods
     */

    public void notifyBlockChange() {

        if (getWorld() == null || getWorld().isRemote)
            return;
        getWorld().notifyBlockChange(getX(), getY(), getZ(), getBlockType());
    }

    public void notifyTileChange() {

        if (getWorld() == null || getWorld().isRemote)
            return;
        getWorld().func_147453_f(getX(), getY(), getZ(), getBlockType());
    }

    public void recalculateLighting() {

        if (getWorld() == null)
            return;
        getWorld().func_147451_t(getX(), getY(), getZ());
    }

    public void markRender() {

        if (getWorld() == null || !getWorld().isRemote)
            return;
        getWorld().func_147479_m(getX(), getY(), getZ());
    }

    public boolean onActivated(EntityPlayer player, MovingObjectPosition mop, ItemStack stack) {

        return false;
    }

    public void onClicked(EntityPlayer player, ItemStack stack) {

    }

    public void onPlacedBy(EntityLivingBase entity, ItemStack stack) {

    }

    public void onNeighborChange(Block block) {

    }

    public void onNeighborTileChange(TileEntity tile) {

    }

    public void onFirstTick() {

    }

    public ArrayList<ItemStack> getDrops() {

        ArrayList<ItemStack> l = new ArrayList<ItemStack>();
        l.add(new ItemStack(getBlockType()));
        return l;
    }

    public boolean canConnectRedstone(ForgeDirection side) {

        return false;
    }

    public int getStrongRedstoneOutput(ForgeDirection side) {

        return 0;
    }

    public int getWeakRedstoneOutput(ForgeDirection side) {

        return 0;
    }

    protected int ticker = 0;

    @Override
    public final void updateEntity() {

        if (ticker == 0)
            onFirstTick();
        update();
        ticker++;
    }

    public void update() {

    }

}
