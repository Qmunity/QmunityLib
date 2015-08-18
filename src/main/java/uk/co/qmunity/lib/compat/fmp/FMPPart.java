package uk.co.qmunity.lib.compat.fmp;

import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.QLModInfo;
import uk.co.qmunity.lib.QmunityLib;
import uk.co.qmunity.lib.client.render.VertexConsumerTessellator;
import uk.co.qmunity.lib.model.LightMatrix;
import uk.co.qmunity.lib.network.MCByteBuf;
import uk.co.qmunity.lib.network.packet.PacketCPart;
import uk.co.qmunity.lib.part.IOccludingPart;
import uk.co.qmunity.lib.part.IPartHolder;
import uk.co.qmunity.lib.part.IQLPart;
import uk.co.qmunity.lib.part.ISlottedPart;
import uk.co.qmunity.lib.part.IThruHolePart;
import uk.co.qmunity.lib.part.PartRegistry;
import uk.co.qmunity.lib.part.PartSlot;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.raytrace.RayTracer;
import uk.co.qmunity.lib.vec.Cuboid;
import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.raytracer.ExtendedMOP;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.ISidedHollowConnect;
import codechicken.multipart.INeighborTileChange;
import codechicken.multipart.IRedstonePart;
import codechicken.multipart.NormallyOccludedPart;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TNormalOcclusion;
import codechicken.multipart.TSlottedPart;
import codechicken.multipart.TileMultipart;
import codechicken.multipart.handler.MultipartProxy;
import codechicken.multipart.scalatraits.TSlottedTile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FMPPart extends TMultiPart implements IPartHolder, TSlottedPart, TNormalOcclusion, IRedstonePart, INeighborTileChange,
        ISidedHollowConnect {

    private Map<String, IQLPart> parts = new HashMap<String, IQLPart>();
    private Map<Integer, ISlottedPart> slotMap = new HashMap<Integer, ISlottedPart>();
    private List<FMPWrappedPart> fmpParts = new ArrayList<FMPWrappedPart>();

    private final boolean simulated;

    public FMPPart(boolean simulated) {

        this.simulated = simulated;
    }

    public FMPPart() {

        this(false);
    }

    public FMPPart(Map<String, IQLPart> parts) {

        this();
        this.parts = parts;
    }

    public FMPPart(IQLPart part) {

        this(true);
        String partID;
        do {
            partID = UUID.randomUUID().toString();
        } while (parts.containsKey(partID));
        addPart(partID, part, false);
    }

    @Override
    public String getType() {

        return QLModInfo.MODID + "_multipart";
    }

    @Override
    public Block getBlockType() {

        return MultipartProxy.block();
    }

    @Override
    public Iterable<IndexedCuboid6> getSubParts() {

        List<IndexedCuboid6> cubes = new ArrayList<IndexedCuboid6>();

        for (IQLPart p : getParts())
            for (Cuboid c : p.getSelectionBoxes())
                cubes.add(new IndexedCuboid6(0, new Cuboid6(c.toAABB())));

        if (cubes.size() == 0)
            cubes.add(new IndexedCuboid6(0, new Cuboid6(0, 0, 0, 1, 1, 1)));

        return cubes;
    }

    public QMovingObjectPosition rayTrace(Vec3 start, Vec3 end) {

        QMovingObjectPosition closest = null;
        double dist = Double.MAX_VALUE;

        for (IQLPart p : getParts()) {
            QMovingObjectPosition mop = p.rayTrace(start, end);
            if (mop != null) {
                double d = mop.hitVec.distanceTo(start);
                if (closest == null || d <= dist) {
                    closest = mop;
                    dist = d;
                }
            }
        }
        return closest;
    }

    @Override
    public ExtendedMOP collisionRayTrace(Vec3 start, Vec3 end) {

        QMovingObjectPosition qmop = rayTrace(start, end);
        if (qmop == null || qmop.part == null)
            return null;
        ExtendedMOP emop = new ExtendedMOP(qmop, 0, start.squareDistanceTo(qmop.hitVec));
        emop.hitInfo = qmop;
        return emop;
    }

    @Override
    public boolean occlusionTest(TMultiPart npart) {

        if (npart instanceof FMPPart)
            return false;

        return super.occlusionTest(npart);
    }

    public boolean firstTick = true;

    @Override
    public void update() {

        if (firstTick == true) {
            for (IQLPart p : getParts())
                p.onLoaded();
            firstTick = false;
        }

        for (IQLPart p : getParts())
            p.update();
    }

    @Override
    public void save(NBTTagCompound tag) {

        super.save(tag);

        NBTTagList l = new NBTTagList();
        for (Entry<String, IQLPart> e : parts.entrySet()) {
            NBTTagCompound t = new NBTTagCompound();

            t.setString("id", e.getKey());
            t.setString("type", e.getValue().getType());
            NBTTagCompound data = new NBTTagCompound();
            e.getValue().writeToNBT(data);
            t.setTag("data", data);

            l.appendTag(t);
        }
        tag.setTag("parts", l);
    }

    @Override
    public void load(NBTTagCompound tag) {

        super.load(tag);

        NBTTagList l = tag.getTagList("parts", new NBTTagCompound().getId());
        for (int i = 0; i < l.tagCount(); i++) {
            NBTTagCompound t = l.getCompoundTagAt(i);

            String id = t.getString("id");
            IQLPart p = findPart(id);
            if (p == null) {
                p = PartRegistry.createPart(t.getString("type"), false);
                if (p == null)
                    continue;
                p.setParent(this);
                parts.put(id, p);
            }

            NBTTagCompound data = t.getCompoundTag("data");
            p.readFromNBT(data);
        }

        if (tile() != null && getWorld() != null)
            getWorld().markBlockRangeForRenderUpdate(getX(), getY(), getZ(), getX(), getY(), getZ());
    }

    @Override
    public void writeDesc(MCDataOutput packet) {

        super.writeDesc(packet);

        MCByteBuf buf = new MCByteBuf(Unpooled.buffer());

        buf.writeInt(parts.size());
        for (Entry<String, IQLPart> e : parts.entrySet()) {
            buf.writeString(e.getValue().getType());
            buf.writeString(e.getKey());
            e.getValue().writeUpdateData(buf);
        }

        byte[] bytes = buf.array();
        packet.writeInt(bytes.length);
        packet.writeByteArray(bytes);
    }

    @Override
    public void readDesc(MCDataInput packet) {

        super.readDesc(packet);

        parts.clear();

        int length = packet.readInt();
        MCByteBuf buf = new MCByteBuf(Unpooled.copiedBuffer(packet.readByteArray(length)));

        int amt = buf.readInt();
        for (int i = 0; i < amt; i++) {
            String type = buf.readString();
            String id = buf.readString();
            IQLPart part = PartRegistry.createPart(type, true);
            part.readUpdateData(buf);
            addPart(id, part, false);
        }
    }

    // Part holder methods

    @Override
    public World getWorld() {

        return world();
    }

    @Override
    public int getX() {

        return x();
    }

    @Override
    public int getY() {

        return y();
    }

    @Override
    public int getZ() {

        return z();
    }

    @Override
    public int getSlotMask() {

        int mask = 0;
        for (int i = 0; i < PartSlot.values().length; i++)
            if (slotMap.containsKey(i) && slotMap.get(i) != null)
                mask |= 1 << i;
        return mask;
    }

    @Override
    public boolean canAddPart(IQLPart part) {

        if (part instanceof ISlottedPart) {
            int mask = ((ISlottedPart) part).getSlotMask();
            for (PartSlot s : PartSlot.values())
                if ((mask & s.mask) != 0
                        && ((tile() != null && tile().partMap(s.ordinal()) != null) || (slotMap.containsKey(s.ordinal()) && slotMap.get(s
                                .ordinal()) != null)))
                    return false;
        }
        if (part instanceof IOccludingPart) {
            List<Cuboid6> boxes = new ArrayList<Cuboid6>();
            for (Cuboid c : ((IOccludingPart) part).getOcclusionBoxes())
                boxes.add(new Cuboid6(c.toAABB()));
            if (!tile().canAddPart(new NormallyOccludedPart(boxes)))
                return false;
        }
        return true;
    }

    @Override
    public void addPart(IQLPart part) {

        String partID;
        do {
            partID = UUID.randomUUID().toString();
        } while (parts.containsKey(partID));
        addPart(partID, part, true);
    }

    @Override
    public void addPart(String partID, IQLPart part, boolean notify) {

        // Set parent
        part.setParent(this);

        // Add to the part map
        parts.put(partID, part);
        // Add to the slot map (if needed)
        if (part instanceof ISlottedPart) {
            int slotMask = ((ISlottedPart) part).getSlotMask();
            for (PartSlot s : PartSlot.values())
                if ((slotMask & s.mask) != 0)
                    slotMap.put(s.ordinal(), (ISlottedPart) part);
        }

        if (tile() != null)
            refreshSlots();

        // If we don't want to notify anybody, stop here
        if (!notify || tile() == null)
            return;

        // Notify part
        part.onAdded();
        // Notify other parts
        for (IQLPart p : getParts())
            if (p != part)
                p.onPartChanged(part);
        for (TMultiPart p : tile().jPartList())
            if (p != this)
                p.onPartChanged(this);

        // Tell the client the part has been placed
        if (getWorld() != null && !getWorld().isRemote)
            PacketCPart.addPart(this, part);

        // Notify neighbors
        notifyBlockChange();
        notifyTileChange();
        // Recalculate lighting
        recalculateLighting();
        // Mark chunk for saving
        markDirty();
        // Call rendering update
        markRender();
    }

    @Override
    public void removePart(IQLPart part) {

        // Tell the client the part has been removed
        if (getWorld() != null && !getWorld().isRemote)
            PacketCPart.removePart(this, part);

        String id = getPartID(part);

        // Remove from the part map
        parts.remove(id);
        // Remove from the slot map (if needed)
        if (part instanceof ISlottedPart) {
            int slotMask = ((ISlottedPart) part).getSlotMask();
            for (PartSlot s : PartSlot.values())
                if ((slotMask & s.mask) != 0 && getPartInSlot(s.ordinal()) == part)
                    slotMap.remove(s.ordinal());
        }
        refreshSlots();

        // Notify part
        part.onRemoved();
        // Notify other parts
        for (IQLPart p : getParts())
            if (p != part)
                p.onPartChanged(part);
        for (TMultiPart p : tile().jPartList())
            if (p != this)
                p.onPartChanged(this);

        // Remove parent
        part.setParent(null);

        // Remove part if needed
        TileMultipart tmp = tile();
        if (getParts().isEmpty() && tile() != null && !getWorld().isRemote)
            tile().remPart(this);
        tile_$eq(tmp);

        // Notify neighbors
        if (!getParts().isEmpty()) {
            notifyBlockChange();
            notifyTileChange();
        }
        // Recalculate lighting
        recalculateLighting();
        // Mark chunk for saving
        if (!getParts().isEmpty())
            markDirty();
        // Call rendering update
        markRender();
    }

    private void refreshSlots() {

        if (tile() instanceof TSlottedTile) {
            TSlottedTile t = (TSlottedTile) tile();
            t.v_partMap_$eq(new TMultiPart[t.v_partMap().length]);
            for (TMultiPart p : tile().jPartList())
                if (p != this)
                    t.bindPart(p);
            t.bindPart(this);
        }
    }

    private void refreshWrappers() {

        if (tile() == null)
            return;

        boolean update = false;
        if (fmpParts.size() != tile().jPartList().size()) {
            update = true;
        } else {
            for (int i = 0; i < fmpParts.size(); i++) {
                FMPWrappedPart p = fmpParts.get(i);
                if ((tile().jPartList().get(i) == this && p != null) || (p != null && tile().jPartList().get(i) != p.part))
                    update = true;
            }
        }

        if (!update)
            return;

        fmpParts.clear();
        for (TMultiPart p : tile().jPartList())
            if (p == this)
                fmpParts.add(null);
            else
                fmpParts.add(FMPWrappedPart.wrap(this, p));
    }

    @Override
    public Collection<IQLPart> getParts() {

        return Collections.unmodifiableCollection(parts.values());
    }

    @Override
    public String getPartID(IQLPart part) {

        for (Entry<String, IQLPart> e : parts.entrySet())
            if (e.getValue() == part)
                return e.getKey();
        return null;
    }

    @Override
    public IQLPart findPart(String partID) {

        if (!parts.containsKey(partID))
            return null;
        return parts.get(partID);
    }

    @Override
    public ISlottedPart getPartInSlot(int slot) {

        if (slotMap.containsKey(slot))
            return slotMap.get(slot);

        TMultiPart part = tile() != null ? tile().partMap(slot) : null;
        if (part != null)
            return (ISlottedPart) wrap(part);

        return null;
    }

    public IQLPart wrap(TMultiPart part) {

        for (FMPWrappedPart p : fmpParts)
            if (p != null && p.part == part)
                return p;

        if (tile().jPartList().contains(part)) {
            refreshWrappers();
            return wrap(part);
        }

        return null;
    }

    @Override
    public void markDirty() {

        if (tile() != null)
            tile().markDirty();
    }

    @Override
    public void notifyBlockChange() {

        if (getWorld() == null || getWorld().isRemote)
            return;
        getWorld().notifyBlockChange(getX(), getY(), getZ(), tile().getBlockType());
    }

    @Override
    public void notifyTileChange() {

        if (getWorld() == null || getWorld().isRemote)
            return;
        getWorld().func_147453_f(getX(), getY(), getZ(), tile().getBlockType());
    }

    @Override
    public void recalculateLighting() {

        if (getWorld() == null)
            return;
        getWorld().func_147451_t(getX(), getY(), getZ());
    }

    @Override
    public void markRender() {

        if (getWorld() == null || !getWorld().isRemote)
            return;
        getWorld().func_147479_m(getX(), getY(), getZ());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderStatic(Vector3 pos, int pass) {

        LightMatrix.instance.locate(getWorld(), getX(), getY(), getZ());
        boolean rendered = false;

        Tessellator.instance.addTranslation((float) pos.x, (float) pos.y, (float) pos.z);
        for (IQLPart p : getParts())
            rendered |= p.renderStatic(VertexConsumerTessellator.instance.context.set(true, true, true, true),
                    VertexConsumerTessellator.instance, pass);
        Tessellator.instance.addTranslation((float) -pos.x, (float) -pos.y, (float) -pos.z);

        return rendered;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderDynamic(Vector3 pos, float frame, int pass) {

        for (IQLPart p : getParts())
            p.renderDynamic(new uk.co.qmunity.lib.vec.Vector3(pos.x, pos.y, pos.z), pass, frame);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void drawBreaking(RenderBlocks renderBlocks) {

        QMovingObjectPosition mop = rayTrace(RayTracer.getStartVec(QmunityLib.proxy.getPlayer()),
                RayTracer.getEndVec(QmunityLib.proxy.getPlayer()));
        if (mop != null) {
            LightMatrix.instance.locate(getWorld(), getX(), getY(), getZ());
            mop.part.renderBreaking(VertexConsumerTessellator.instance.context.set(true, true, true, true),
                    VertexConsumerTessellator.instance, mop, renderBlocks.overrideBlockTexture);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean drawHighlight(MovingObjectPosition hit, EntityPlayer player, float frame) {

        QMovingObjectPosition mop = rayTrace(RayTracer.getStartVec(player), RayTracer.getEndVec(player));
        if (mop == null || mop.part == null)
            return true;
        return mop.part.drawHighlight(mop, player, frame);
    }

    @Override
    public Iterable<Cuboid6> getCollisionBoxes() {

        List<Cuboid6> cubes = new ArrayList<Cuboid6>();

        for (IQLPart p : getParts())
            for (Cuboid c : p.getCollisionBoxes())
                cubes.add(new Cuboid6(c.toAABB()));

        return cubes;
    }

    @Override
    public void onNeighborChanged() {

        super.onNeighborChanged();

        if (simulated)
            return;

        for (IQLPart p : getParts())

            p.onNeighborBlockChange();
    }

    @Override
    public Iterable<ItemStack> getDrops() {

        List<ItemStack> l = new ArrayList<ItemStack>();

        for (IQLPart p : getParts()) {
            List<ItemStack> d = p.getDrops();
            if (d != null)
                l.addAll(d);
        }

        return l;
    }

    @Override
    public Iterable<Cuboid6> getOcclusionBoxes() {

        List<Cuboid6> cubes = new ArrayList<Cuboid6>();

        for (IQLPart p : getParts())
            if (p instanceof IOccludingPart)
                for (Cuboid c : ((IOccludingPart) p).getOcclusionBoxes())
                    cubes.add(new IndexedCuboid6(0, new Cuboid6(c.toAABB())));

        return cubes;
    }

    @Override
    public boolean canConnectRedstone(int side) {

        for (IQLPart p : getParts())
            if (p instanceof uk.co.qmunity.lib.part.IRedstonePart)
                if (((uk.co.qmunity.lib.part.IRedstonePart) p).canConnectRedstone(ForgeDirection.getOrientation(side)))
                    return true;
        return false;
    }

    @Override
    public int strongPowerLevel(int side) {

        int max = 0;

        for (IQLPart p : getParts())
            if (p instanceof uk.co.qmunity.lib.part.IRedstonePart)
                max = Math.max(max, ((uk.co.qmunity.lib.part.IRedstonePart) p).getStrongPower(ForgeDirection.getOrientation(side)));

        return max;
    }

    @Override
    public int weakPowerLevel(int side) {

        int max = 0;

        for (IQLPart p : getParts())
            if (p instanceof uk.co.qmunity.lib.part.IRedstonePart)
                max = Math.max(max, ((uk.co.qmunity.lib.part.IRedstonePart) p).getWeakPower(ForgeDirection.getOrientation(side)));

        return max;
    }

    @Override
    public void onNeighborTileChanged(int arg0, boolean arg1) {

        if (simulated)
            return;

        for (IQLPart p : getParts())

            p.onNeighborTileChange();
    }

    @Override
    public boolean weakTileChanges() {

        return true;
    }

    @Override
    public void onAdded() {

        if (simulated)
            return;

        refreshSlots();
        refreshWrappers();

        for (IQLPart p : getParts())
            p.onAdded();
    }

    @Override
    public void onRemoved() {

        if (simulated)
            return;

        refreshSlots();
        refreshWrappers();

        for (IQLPart p : getParts())
            p.onRemoved();
    }

    @Override
    public void onChunkLoad() {

        if (simulated)
            return;

        for (IQLPart p : getParts())
            p.onLoaded();
    }

    @Override
    public void onChunkUnload() {

        if (simulated)
            return;

        for (IQLPart p : getParts())
            p.onUnloaded();
    }

    @Override
    public void invalidateConvertedTile() {

        super.invalidateConvertedTile();

        for (String s : parts.keySet())
            parts.get(s).setParent(this);
    }

    @Override
    public void onConverted() {

        for (IQLPart p : getParts())
            p.onConverted();
    }

    @Override
    public void onMoved() {

        for (IQLPart p : getParts()) {
            p.onUnloaded();
            p.onLoaded();
            p.onNeighborBlockChange();
        }
    }

    @Override
    public void onWorldJoin() {

        super.onWorldJoin();

        onChunkLoad();
    }

    @Override
    public void onWorldSeparate() {

        super.onWorldSeparate();

        onChunkUnload();
    }

    @Override
    public ItemStack pickItem(MovingObjectPosition hit) {

        QMovingObjectPosition mop = rayTrace(RayTracer.getStartVec(QmunityLib.proxy.getPlayer()),
                RayTracer.getEndVec(QmunityLib.proxy.getPlayer()));
        if (mop == null || mop.part == null)
            return null;
        return mop.part.getPickBlock(null, mop);
    }

    @Override
    public void harvest(MovingObjectPosition hit, EntityPlayer player) {

        if (world().isRemote)
            return;

        QMovingObjectPosition mop = rayTrace(RayTracer.getStartVec(player), RayTracer.getEndVec(player));
        if (mop == null || mop.part == null) {
            super.harvest(hit, player);
            return;
        }

        mop.part.harvest(player, mop);
    }

    @Override
    public void click(EntityPlayer player, MovingObjectPosition hit, ItemStack item) {

        QMovingObjectPosition mop = rayTrace(RayTracer.getStartVec(player), RayTracer.getEndVec(player));
        if (mop == null || mop.part == null)
            return;
        mop.part.onClicked(player, mop, item);
    }

    @Override
    public boolean activate(EntityPlayer player, MovingObjectPosition hit, ItemStack item) {

        QMovingObjectPosition mop = rayTrace(RayTracer.getStartVec(player), RayTracer.getEndVec(player));
        if (mop == null || mop.part == null)
            return false;
        return mop.part.onActivated(player, mop, item);
    }

    @Override
    public float getStrength(MovingObjectPosition hit, EntityPlayer player) {

        QMovingObjectPosition mop = rayTrace(RayTracer.getStartVec(player), RayTracer.getEndVec(player));
        if (mop == null || mop.part == null)
            return 30;
        return 30 * mop.part.getHardness(player, mop);
    }

    @Override
    public int getLightValue() {

        int val = 0;
        for (IQLPart p : getParts())
            val = Math.max(val, p.getLightValue());
        return val;
    }

    @Override
    public int getHollowSize(int side) {

        int val = 0;
        boolean found = false;
        for (IQLPart p : getParts()) {
            if (p instanceof IThruHolePart) {
                val = Math.max(val, ((IThruHolePart) p).getHollowSize(ForgeDirection.getOrientation(side)));
                found = true;
            }
        }
        if (found && (val > 0 || val < 12))
            return val;
        return 8;
    }

    @Override
    public boolean canConnectRedstone(ForgeDirection face, ForgeDirection side) {

        return false;
    }

    @Override
    public int getWeakRedstoneOutput(ForgeDirection face, ForgeDirection side) {

        return 0;
    }

    @Override
    public int getStrongRedstoneOutput(ForgeDirection face, ForgeDirection side) {

        return 0;
    }
}
