package uk.co.qmunity.lib.compat.fmp;

import java.util.ArrayList;
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
import uk.co.qmunity.lib.model.IVertexConsumer;
import uk.co.qmunity.lib.network.MCByteBuf;
import uk.co.qmunity.lib.part.IPartHolder;
import uk.co.qmunity.lib.part.IQLPart;
import uk.co.qmunity.lib.part.ISlottedPart;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.vec.Cuboid;
import uk.co.qmunity.lib.vec.Vector3;
import codechicken.lib.raytracer.ExtendedMOP;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.vec.Cuboid6;
import codechicken.multipart.NormallyOccludedPart;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TSlottedPart;

public class FMPWrappedPart implements IQLPart {

    public static FMPWrappedPart wrap(IPartHolder parent, TMultiPart part) {

        if (part instanceof TSlottedPart)
            return new FMPWrappedSlottedPart(parent, part);

        return new FMPWrappedPart(parent, part);
    }

    protected IPartHolder parent;
    protected TMultiPart part;

    protected FMPWrappedPart(IPartHolder parent, TMultiPart part) {

        this.parent = parent;
        this.part = part;
    }

    @Override
    public World getWorld() {

        return part.world();
    }

    @Override
    public int getX() {

        return part.x();
    }

    @Override
    public int getY() {

        return part.y();
    }

    @Override
    public int getZ() {

        return part.z();
    }

    @Override
    public String getType() {

        return "fmp_" + part.getType();
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
    public void writeUpdateData(MCByteBuf buf) {

    }

    @Override
    public void readUpdateData(MCByteBuf buf) {

    }

    @Override
    public void sendUpdatePacket() {

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

        return false;
    }

    @Override
    public boolean drawHighlight(QMovingObjectPosition hit, EntityPlayer player, float partialTicks) {

        return false;
    }

    @Override
    public Cuboid getRenderBounds() {

        return new Cuboid(part.getRenderBounds().toAABB());
    }

    @Override
    public void addDestroyEffects(QMovingObjectPosition hit, EffectRenderer effectRenderer) {

    }

    @Override
    public void addHitEffects(QMovingObjectPosition hit, EffectRenderer effectRenderer) {

    }

    @Override
    public List<Cuboid> getCollisionBoxes() {

        List<Cuboid> boxes = new ArrayList<Cuboid>();
        for (Cuboid6 box : part.getCollisionBoxes())
            boxes.add(new Cuboid(box.toAABB()));
        return boxes;
    }

    @Override
    public List<Cuboid> getSelectionBoxes() {

        List<Cuboid> boxes = new ArrayList<Cuboid>();
        for (IndexedCuboid6 box : part.getSubParts())
            boxes.add(new Cuboid(box.toAABB()));
        return boxes;
    }

    @Override
    public boolean occlusionTest(IQLPart part) {

        List<Cuboid6> boxes = new ArrayList<Cuboid6>();
        for (Cuboid box : part.getSelectionBoxes())
            boxes.add(new Cuboid6(box.toAABB()));
        return this.part.occlusionTest(new NormallyOccludedPart(boxes));
    }

    @Override
    public QMovingObjectPosition rayTrace(Vec3 start, Vec3 end) {

        ExtendedMOP emop = part.collisionRayTrace(start, end);
        if (emop == null)
            return null;
        return new QMovingObjectPosition(emop, this);
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

        return new ArrayList<ItemStack>();
    }

    @Override
    public void harvest(EntityPlayer player, QMovingObjectPosition hit) {

        part.harvest(hit, player);
    }

    @Override
    public float getHardness(EntityPlayer player, QMovingObjectPosition hit) {

        return -1F;
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

    private static class FMPWrappedSlottedPart extends FMPWrappedPart implements ISlottedPart {

        protected FMPWrappedSlottedPart(IPartHolder parent, TMultiPart part) {

            super(parent, part);
        }

        @Override
        public int getSlotMask() {

            return ((TSlottedPart) part).getSlotMask();
        }

    }

}
