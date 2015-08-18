package uk.co.qmunity.lib.part;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import uk.co.qmunity.lib.client.render.RenderContext;
import uk.co.qmunity.lib.helper.ItemHelper;
import uk.co.qmunity.lib.model.IVertexConsumer;
import uk.co.qmunity.lib.network.MCByteBuf;
import uk.co.qmunity.lib.network.packet.PacketCPart;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.raytrace.RayTracer;
import uk.co.qmunity.lib.vec.Cuboid;
import uk.co.qmunity.lib.vec.Vector3;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class QLPart implements IQLPart {

    private IPartHolder parent;

    @Override
    public World getWorld() {

        return getParent() != null ? getParent().getWorld() : null;
    }

    @Override
    public int getX() {

        return getParent() != null ? getParent().getX() : 0;
    }

    @Override
    public int getY() {

        return getParent() != null ? getParent().getY() : 0;
    }

    @Override
    public int getZ() {

        return getParent() != null ? getParent().getZ() : 0;
    }

    @Override
    public IPartHolder getParent() {

        return parent;
    }

    @Override
    public void setParent(IPartHolder parent) {

        this.parent = parent;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

    }

    @Override
    public void writeUpdateData(MCByteBuf buffer) {

    }

    @Override
    public void readUpdateData(MCByteBuf buffer) {

    }

    @Override
    public void sendUpdatePacket() {

        if (getWorld() != null && !getWorld().isRemote)
            PacketCPart.updatePart(getParent(), this);
    }

    @Override
    public boolean renderStatic(RenderContext context, IVertexConsumer consumer, int pass) {

        return false;
    }

    @Override
    public void renderDynamic(Vector3 translation, int pass, float frame) {

    }

    @Override
    public boolean renderBreaking(RenderContext context, IVertexConsumer consumer, QMovingObjectPosition hit, IIcon overrideIcon) {

        return renderStatic(context, consumer, 0) || renderStatic(context, consumer, 1);
    }

    @Override
    public boolean drawHighlight(QMovingObjectPosition hit, EntityPlayer player, float partialTicks) {

        return false;
    }

    @Override
    public Cuboid getRenderBounds() {

        return Cuboid.full;
    }

    @Override
    public void addDestroyEffects(QMovingObjectPosition hit, EffectRenderer effectRenderer) {

    }

    @Override
    public void addHitEffects(QMovingObjectPosition hit, EffectRenderer effectRenderer) {

    }

    @Override
    public List<Cuboid> getCollisionBoxes() {

        return Arrays.asList();
    }

    @Override
    public List<Cuboid> getSelectionBoxes() {

        return Arrays.asList();
    }

    @Override
    public boolean occlusionTest(IQLPart part) {

        return true;
    }

    @Override
    public QMovingObjectPosition rayTrace(Vec3 start, Vec3 end) {

        return RayTracer.instance().rayTracePart(this, new Vector3(start), new Vector3(end));
    }

    @Override
    public int getLightValue() {

        return 0;
    }

    @Override
    public ItemStack getPickBlock(EntityPlayer player, QMovingObjectPosition hit) {

        return null;
    }

    @Override
    public List<ItemStack> getDrops() {

        return Arrays.asList(new ItemStack(GameRegistry.findItem("qltest", "testpartql")));
    }

    @Override
    public void harvest(EntityPlayer player, QMovingObjectPosition hit) {

        if (!player.capabilities.isCreativeMode)
            for (ItemStack item : getDrops())
                ItemHelper.dropItem(getWorld(), getX(), getY(), getZ(), item);
        getParent().removePart(this);
    }

    @Override
    public float getHardness(EntityPlayer player, QMovingObjectPosition hit) {

        return 10;
    }

    @Override
    public boolean onActivated(EntityPlayer player, QMovingObjectPosition hit, ItemStack item) {

        return false;
    }

    @Override
    public void onClicked(EntityPlayer player, QMovingObjectPosition hit, ItemStack item) {

    }

    @Override
    public void update() {

    }

    @Override
    public void randomDisplayTick(Random rnd) {

    }

    @Override
    public void onPartChanged(IQLPart part) {

    }

    @Override
    public void onNeighborBlockChange() {

    }

    @Override
    public void onNeighborTileChange() {

    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRemoved() {

    }

    @Override
    public void onLoaded() {

    }

    @Override
    public void onUnloaded() {

    }

    @Override
    public void onConverted() {

    }

    public void notifyBlockChange() {

        if (getParent() != null)
            getParent().notifyBlockChange();
    }

    public void notifyTileChange() {

        if (getParent() != null)
            getParent().notifyTileChange();
    }

    public void markDirty() {

        if (getParent() != null)
            getParent().markDirty();
    }

    public void recalculateLighting() {

        if (getParent() != null)
            getParent().recalculateLighting();
    }

    public void markRender() {

        if (getParent() != null)
            getParent().markRender();
    }

    public static class QLPartNormallyOccluded extends QLPart implements IOccludingPart {

        private List<Cuboid> boxes;

        public QLPartNormallyOccluded(List<Cuboid> boxes) {

            this.boxes = boxes;
        }

        @Override
        public String getType() {

            return "normally_occluded_qlpart";
        }

        @Override
        public List<Cuboid> getOcclusionBoxes() {

            return boxes;
        }

        @Override
        public boolean occlusionTest(IQLPart part) {

            if (!(part instanceof IOccludingPart))
                return true;

            List<Cuboid> self = getOcclusionBoxes();
            List<Cuboid> p = ((IOccludingPart) part).getOcclusionBoxes();
            for (Cuboid c1 : self)
                for (Cuboid c2 : p)
                    if (c1.intersects(c2))
                        return false;
            return true;
        }
    }

}
