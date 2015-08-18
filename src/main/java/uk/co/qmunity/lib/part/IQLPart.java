package uk.co.qmunity.lib.part;

import java.util.List;
import java.util.Random;

import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import uk.co.qmunity.lib.client.render.RenderContext;
import uk.co.qmunity.lib.model.IVertexConsumer;
import uk.co.qmunity.lib.network.MCByteBuf;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.util.ISyncable;
import uk.co.qmunity.lib.vec.Cuboid;
import uk.co.qmunity.lib.vec.IWorldLocation;
import uk.co.qmunity.lib.vec.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IQLPart extends IWorldLocation, ISyncable {

    public String getType();

    // Parent

    public IPartHolder getParent();

    public void setParent(IPartHolder parent);

    // NBT

    public void writeToNBT(NBTTagCompound tag);

    public void readFromNBT(NBTTagCompound tag);

    // Update packet

    @Override
    public void writeUpdateData(MCByteBuf buf);

    @Override
    public void readUpdateData(MCByteBuf buf);

    @Override
    public void sendUpdatePacket();

    // Rendering

    @SideOnly(Side.CLIENT)
    public boolean renderStatic(RenderContext context, IVertexConsumer consumer, int pass);

    @SideOnly(Side.CLIENT)
    public void renderDynamic(Vector3 translation, int pass, float frame);

    @SideOnly(Side.CLIENT)
    public boolean renderBreaking(RenderContext context, IVertexConsumer consumer, QMovingObjectPosition hit, IIcon overrideIcon);

    public boolean drawHighlight(QMovingObjectPosition hit, EntityPlayer player, float partialTicks);

    @SideOnly(Side.CLIENT)
    public Cuboid getRenderBounds();

    // Misc rendering

    @SideOnly(Side.CLIENT)
    public void addDestroyEffects(QMovingObjectPosition hit, EffectRenderer effectRenderer);

    @SideOnly(Side.CLIENT)
    public void addHitEffects(QMovingObjectPosition hit, EffectRenderer effectRenderer);

    // Collision/selection

    public List<Cuboid> getCollisionBoxes();

    public List<Cuboid> getSelectionBoxes();

    public boolean occlusionTest(IQLPart part);

    public QMovingObjectPosition rayTrace(Vec3 start, Vec3 end);

    // Misc

    public int getLightValue();

    public ItemStack getPickBlock(EntityPlayer player, QMovingObjectPosition hit);

    public List<ItemStack> getDrops();

    public void harvest(EntityPlayer player, QMovingObjectPosition hit);

    public float getHardness(EntityPlayer player, QMovingObjectPosition hit);

    // Events

    public boolean onActivated(EntityPlayer player, QMovingObjectPosition hit, ItemStack item);

    public void onClicked(EntityPlayer player, QMovingObjectPosition hit, ItemStack item);

    public void update();

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(Random rnd);

    public void onPartChanged(IQLPart part);

    public void onNeighborBlockChange();

    public void onNeighborTileChange();

    public void onAdded();

    public void onRemoved();

    public void onLoaded();

    public void onUnloaded();// TODO: Implement

    public void onConverted();
}
