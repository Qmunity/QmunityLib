package uk.co.qmunity.lib.inventory;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import uk.co.qmunity.lib.network.NetworkHandler;
import uk.co.qmunity.lib.network.annotation.GuiSynced;
import uk.co.qmunity.lib.network.annotation.NetworkUtils;
import uk.co.qmunity.lib.network.annotation.PacketCUpdateGui;
import uk.co.qmunity.lib.network.annotation.SyncedField;
import uk.co.qmunity.lib.tile.TileBase;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

/**
 * Use this container when you want to use @GuiSynced on your tile entity fields.
 * @author MineMaarten
 */
public abstract class ContainerBase<Tile extends TileBase> extends Container{

    public Tile te;
    private final List<SyncedField> syncedFields;

    public ContainerBase(Tile te){
        this.te = te;
        syncedFields = NetworkUtils.getSyncedFields(te, GuiSynced.class);
    }

    protected void addSyncedField(SyncedField field){
        syncedFields.add(field);
    }

    public void updateField(int index, Object value){
        syncedFields.get(index).setValue(value);
        // te.onGuiUpdate();
    }

    @Override
    public void detectAndSendChanges(){
        super.detectAndSendChanges();
        for(int i = 0; i < syncedFields.size(); i++) {
            if(syncedFields.get(i).update()) {
                sendToCrafters(new PacketCUpdateGui(i, syncedFields.get(i)));
            }
        }
    }

    protected void sendToCrafters(IMessage message){
        for(ICrafting crafter : (List<ICrafting>)crafters) {
            if(crafter instanceof EntityPlayerMP) {
                NetworkHandler.QLIB.sendTo(message, (EntityPlayerMP)crafter);
            }
        }
    }
}
