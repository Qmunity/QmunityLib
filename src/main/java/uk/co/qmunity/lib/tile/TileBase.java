package uk.co.qmunity.lib.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.network.annotation.DescPacketHandler;
import uk.co.qmunity.lib.network.annotation.DescSynced;
import uk.co.qmunity.lib.network.annotation.NetworkUtils;
import uk.co.qmunity.lib.network.annotation.PacketDescription;
import uk.co.qmunity.lib.network.annotation.SyncedField;
import uk.co.qmunity.lib.util.QLog;
import uk.co.qmunity.lib.vec.IWorldLocation;

/**
 * Base tile entity which provides you a few options. Notably, if you mark a field with @DescSynced, it will be automatically synchronize
 * with the client if the field its value gets changed. If this causes too much traffic, you can additionally mark it with @LazySynced. This will
 * not cause synchronization to occur when this particular field changes, but it will get send along when other fields marked with @DescSynced
 * change, or when sendUpdatePacket() is called manually. Marking @GuiSynced can be used to synchronize fields only with players that have
 * the container open that is associated with this tile entity. Be sure to extend your Container to ContainerBase!
 * @author MineMaarten
 */
public class TileBase extends TileEntity implements IRotatable, IWorldLocation{

    private boolean isRedstonePowered;
    private int outputtingRedstone;
    private int ticker = 0;
    private ForgeDirection rotation = ForgeDirection.UP;
    private List<SyncedField> descriptionFields;

    /*************** BASIC TE FUNCTIONS **************/

    /**
     * This function gets called whenever the world/chunk loads
     */
    @Override
    public void readFromNBT(NBTTagCompound tCompound){

        super.readFromNBT(tCompound);
        isRedstonePowered = tCompound.getBoolean("isRedstonePowered");
        readFromPacketNBT(tCompound);
    }

    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    public void writeToNBT(NBTTagCompound tCompound){

        super.writeToNBT(tCompound);
        tCompound.setBoolean("isRedstonePowered", isRedstonePowered);

        writeToPacketNBT(tCompound);
    }

    /**
     * Tags written in here are synced upon markBlockForUpdate.
     * 
     * @param tCompound
     */
    public void writeToPacketNBT(NBTTagCompound tCompound){

        tCompound.setByte("rotation", (byte)rotation.ordinal());
        tCompound.setByte("outputtingRedstone", (byte)outputtingRedstone);
    }

    public void readFromPacketNBT(NBTTagCompound tCompound){

        rotation = ForgeDirection.getOrientation(tCompound.getByte("rotation"));
        if(rotation.ordinal() > 5) {
            QLog.warning("invalid rotation!");
            rotation = ForgeDirection.UP;
        }
        outputtingRedstone = tCompound.getByte("outputtingRedstone");
        if(worldObj != null) markForRenderUpdate();
    }

    public List<SyncedField> getDescriptionFields(){
        if(descriptionFields == null) {
            descriptionFields = NetworkUtils.getSyncedFields(this, DescSynced.class);
            for(SyncedField field : descriptionFields) {
                field.update();
            }
        }
        return descriptionFields;
    }

    @Override
    public Packet getDescriptionPacket(){
        return DescPacketHandler.getPacket(new PacketDescription(this));
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt){

        readFromPacketNBT(pkt.func_148857_g());
    }

    protected void sendUpdatePacket(){

        if(!worldObj.isRemote) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    protected void markForRenderUpdate(){

        if(worldObj != null) worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
    }

    protected void notifyNeighborBlockUpdate(){

        worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType());
    }

    /**
     * Function gets called every tick. Do not forget to call the super method!
     */
    @Override
    public void updateEntity(){

        if(ticker == 0) {
            onTileLoaded();
        }
        super.updateEntity();
        ticker++;

        if(!worldObj.isRemote) {
            boolean descriptionPacketScheduled = false;
            if(descriptionFields == null) descriptionPacketScheduled = true;
            for(SyncedField field : getDescriptionFields()) {
                if(field.update()) {
                    descriptionPacketScheduled = true;
                }
            }

            if(descriptionPacketScheduled) {
                sendUpdatePacket();
            }
        }
    }

    /**
     * ************** ADDED FUNCTIONS ****************
     */

    public void onBlockNeighbourChanged(){

        checkRedstonePower();
    }

    /**
     * Checks if redstone has changed.
     */
    public void checkRedstonePower(){

        boolean isIndirectlyPowered = getWorldObj().isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
        if(isIndirectlyPowered && !getIsRedstonePowered()) {
            redstoneChanged(true);
        } else if(getIsRedstonePowered() && !isIndirectlyPowered) {
            redstoneChanged(false);
        }
    }

    /**
     * Before being able to use this, remember to mark the block as redstone emitter by calling BlockContainerBase#emitsRedstone()
     * 
     * @param newValue
     */
    public void setOutputtingRedstone(boolean newValue){

        setOutputtingRedstone(newValue ? 15 : 0);
    }

    /**
     * Before being able to use this, remember to mark the block as redstone emitter by calling BlockContainerBase#emitsRedstone()
     * 
     * @param value
     */
    public void setOutputtingRedstone(int value){

        value = Math.max(0, value);
        value = Math.min(15, value);
        if(outputtingRedstone != value) {
            outputtingRedstone = value;
            notifyNeighborBlockUpdate();
        }
    }

    public int getOutputtingRedstone(){

        return outputtingRedstone;
    }

    /**
     * This method can be overwritten to get alerted when the redstone level has changed.
     * 
     * @param newValue
     *            The redstone level it is at now
     */
    protected void redstoneChanged(boolean newValue){

        isRedstonePowered = newValue;
    }

    /**
     * Check whether or not redstone level is high
     */
    public boolean getIsRedstonePowered(){

        return isRedstonePowered;
    }

    /**
     * Returns the ticker of the Tile, this number wll increase every tick
     * 
     * @return the ticker
     */
    public int getTicker(){

        return ticker;
    }

    /**
     * Gets called when the TileEntity ticks for the first time, the world is accessible and updateEntity() has not been ran yet
     */
    protected void onTileLoaded(){

        if(!worldObj.isRemote) onBlockNeighbourChanged();
    }

    public List<ItemStack> getDrops(){

        return new ArrayList<ItemStack>();
    }

    @Override
    public void setFacingDirection(ForgeDirection dir){

        rotation = dir;
        if(worldObj != null) {
            sendUpdatePacket();
            notifyNeighborBlockUpdate();
        }
    }

    @Override
    public ForgeDirection getFacingDirection(){

        return rotation;
    }

    public boolean canConnectRedstone(){

        return false;
    }

    public void onNeighborBlockChanged(){

    }

    @Override
    public World getWorld(){
        return worldObj;
    }

    @Override
    public int getX(){
        return xCoord;
    }

    @Override
    public int getY(){
        return yCoord;
    }

    @Override
    public int getZ(){
        return zCoord;
    }
}
