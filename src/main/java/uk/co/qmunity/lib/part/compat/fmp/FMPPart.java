package uk.co.qmunity.lib.part.compat.fmp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.QLModInfo;
import uk.co.qmunity.lib.QmunityLib;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.client.render.RenderMultipart;
import uk.co.qmunity.lib.part.IMicroblock;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartCenter;
import uk.co.qmunity.lib.part.IPartCollidable;
import uk.co.qmunity.lib.part.IPartInteractable;
import uk.co.qmunity.lib.part.IPartOccluding;
import uk.co.qmunity.lib.part.IPartRedstone;
import uk.co.qmunity.lib.part.IPartSelectable;
import uk.co.qmunity.lib.part.IPartSelectableCustom;
import uk.co.qmunity.lib.part.IPartSolid;
import uk.co.qmunity.lib.part.IPartThruHole;
import uk.co.qmunity.lib.part.IPartTicking;
import uk.co.qmunity.lib.part.IPartUpdateListener;
import uk.co.qmunity.lib.part.ITilePartHolder;
import uk.co.qmunity.lib.part.PartRegistry;
import uk.co.qmunity.lib.part.compat.MultipartSystem;
import uk.co.qmunity.lib.part.compat.OcclusionHelper;
import uk.co.qmunity.lib.part.compat.PartUpdateManager;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.raytrace.RayTracer;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;
import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.raytracer.ExtendedMOP;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.microblock.ISidedHollowConnect;
import codechicken.multipart.INeighborTileChange;
import codechicken.multipart.IRedstonePart;
import codechicken.multipart.NormalOcclusionTest;
import codechicken.multipart.NormallyOccludedPart;
import codechicken.multipart.PartMap;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TNormalOcclusion;
import codechicken.multipart.TSlottedPart;
import codechicken.multipart.scalatraits.TSlottedTile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FMPPart extends TMultiPart implements ITilePartHolder, TNormalOcclusion, IRedstonePart, INeighborTileChange, IFMPPart,
ISidedHollowConnect, TSlottedPart {

    private Map<String, IPart> parts = new HashMap<String, IPart>();
    private List<IPart> added = new ArrayList<IPart>();

    private boolean shouldDieInAFire = false;
    private boolean loaded = false;
    private boolean converted = false;

    private final boolean simulated;

    public FMPPart(boolean simulated) {

        this.simulated = simulated;
    }

    public FMPPart() {

        this(false);
    }

    public FMPPart(Map<String, IPart> parts) {

        this();

        this.parts = parts;
        for (String s : parts.keySet())
            parts.get(s).setParent(this);
    }

    @Override
    public String getType() {

        return QLModInfo.MODID + "_multipart";
    }

    @Override
    public List<IPart> getParts() {

        List<IPart> parts = new ArrayList<IPart>();

        for (String s : this.parts.keySet()) {
            IPart p = this.parts.get(s);
            if (p.getParent() != null)
                parts.add(p);
        }

        return parts;
    }

    @Override
    public Iterable<IndexedCuboid6> getSubParts() {

        List<IndexedCuboid6> cubes = new ArrayList<IndexedCuboid6>();

        for (IPart p : getParts())
            if (p instanceof IPartSelectable)
                for (Vec3dCube c : ((IPartSelectable) p).getSelectionBoxes())
                    cubes.add(new IndexedCuboid6(0, new Cuboid6(c.toAABB())));

        if (cubes.size() == 0)
            cubes.add(new IndexedCuboid6(0, new Cuboid6(0, 0, 0, 1, 1, 1)));

        return cubes;
    }

    @Override
    public ExtendedMOP collisionRayTrace(Vec3 start, Vec3 end) {

        QMovingObjectPosition qmop = rayTrace(new Vec3d(start), new Vec3d(end));
        if (qmop == null)
            return null;
        new Cuboid6(qmop.getCube().toAABB()).setBlockBounds(tile().getBlockType());
        Vec3 v = qmop.hitVec.subtract(start);
        return new ExtendedMOP(qmop, 0, v.xCoord * v.xCoord + v.yCoord * v.yCoord + v.zCoord * v.zCoord);
    }

    private boolean firstTick = true;

    @Override
    public void update() {

        if (firstTick) {
            if (converted) {
                for (IPart p : getParts())
                    if (p instanceof IPartUpdateListener)
                        ((IPartUpdateListener) p).onConverted();
            } else {
                if (!loaded) {
                    for (IPart p : added)
                        if (p instanceof IPartUpdateListener)
                            ((IPartUpdateListener) p).onAdded();
                } else {
                    for (IPart p : added)
                        ((IPartUpdateListener) p).onLoaded();
                }
            }

            if (!world().isRemote)
                sendDescUpdate();
            firstTick = false;
        }
        for (IPart p : getParts()) {
            if (p instanceof IPartTicking)
                ((IPartTicking) p).update();
        }

        if (!world().isRemote && (shouldDieInAFire || getParts().size() == 0))
            tile().remPart(this);
    }

    @Override
    public void save(NBTTagCompound tag) {

        super.save(tag);

        NBTTagList l = new NBTTagList();
        for (Entry<String, IPart> e : getPartMap().entrySet()) {
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
            IPart p = getPart(id);
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

        if (getParts().size() == 0)
            shouldDieInAFire = true;

        loaded = true;

        if (tile() != null && getWorld() != null)
            getWorld().markBlockRangeForRenderUpdate(getX(), getY(), getZ(), getX(), getY(), getZ());
    }

    @Override
    public void writeDesc(MCDataOutput packet) {

        super.writeDesc(packet);

        FMPDataOutput buffer = new FMPDataOutput(packet);

        try {
            buffer.writeInt(getPartMap().size());

            for (Entry<String, IPart> e : getPartMap().entrySet()) {
                buffer.writeUTF(e.getKey());
                buffer.writeUTF(e.getValue().getType());
                e.getValue().writeUpdateData(buffer, -1);
            }
        } catch (Exception ex) {
        }
    }

    @Override
    public void readDesc(MCDataInput packet) {

        super.readDesc(packet);

        FMPDataInput buffer = new FMPDataInput(packet);

        try {
            int amt = buffer.readInt();

            for (int i = 0; i < amt; i++) {
                String id = buffer.readUTF();
                String type = buffer.readUTF();

                IPart p = getPart(id);
                if (p == null) {
                    p = PartRegistry.createPart(type, true);
                    if (p == null)
                        continue;
                    p.setParent(this);
                    parts.put(id, p);
                }

                p.readUpdateData(buffer, -1);
            }
        } catch (Exception ex) {
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
    public void addPart(IPart part) {

        int before = parts.size();

        parts.put(genIdentifier(), part);
        part.setParent(this);

        if (!simulated) {
            if (part instanceof IPartUpdateListener)
                if (tile() != null)
                    ((IPartUpdateListener) part).onAdded();
                else
                    added.add(part);
            for (IPart p : getParts())
                if (p != part && p instanceof IPartUpdateListener)
                    ((IPartUpdateListener) p).onPartChanged(part);

            if (before > 0)
                PartUpdateManager.addPart(this, part);

            if (tile() != null) {
                for (int i = 0; i < 6; i++)
                    tile().notifyNeighborChange(i);
                tile().notifyTileChange();
            }

            refreshSlots();
        }
    }

    @Override
    public boolean removePart(IPart part) {

        if (part == null)
            return false;
        if (!parts.containsValue(part))
            return false;

        if (!simulated) {
            PartUpdateManager.removePart(this, part);

            if (part instanceof IPartUpdateListener)
                ((IPartUpdateListener) part).onRemoved();
        }

        String id = getIdentifier(part);
        parts.remove(id);
        part.setParent(null);

        if (!simulated) {
            for (IPart p : getParts())
                if (p != part && p instanceof IPartUpdateListener)
                    ((IPartUpdateListener) p).onPartChanged(part);

            tile().markDirty();
            getWorld().func_147479_m(getX(), getY(), getZ());
            for (int i = 0; i < 6; i++)
                tile().notifyNeighborChange(i);
            tile().notifyTileChange();

            refreshSlots();
        }

        return true;
    }

    private void refreshSlots() {

        if (tile() instanceof TSlottedTile) {
            TSlottedTile t = (TSlottedTile) tile();
            TMultiPart[] old = t.v_partMap();
            TMultiPart[] parts = new TMultiPart[old.length];

            for (int i = 0; i < old.length; i++)
                if (old[i] != null && old[i] != this)
                    parts[i] = old[i];

            t.v_partMap_$eq(parts);
            t.bindPart(this);
        }
    }

    private String genIdentifier() {

        String s = null;
        do {
            s = UUID.randomUUID().toString();
        } while (parts.containsKey(s));

        return s;
    }

    private String getIdentifier(IPart part) {

        for (String s : parts.keySet())
            if (parts.get(s).equals(part))
                return s;

        return null;
    }

    private IPart getPart(String id) {

        for (String s : parts.keySet())
            if (s.equals(id))
                return parts.get(s);

        return null;
    }

    @Override
    public boolean canAddPart(IPart part) {

        if (tile() == null)
            return true;

        if (part instanceof IPartCollidable) {
            List<Vec3dCube> cubes = new ArrayList<Vec3dCube>();
            ((IPartCollidable) part).addCollisionBoxesToList(cubes, null);
            for (Vec3dCube c : cubes)
                if (!getWorld().checkNoEntityCollision(c.clone().add(getX(), getY(), getZ()).toAABB()))
                    return false;
        }

        if (part instanceof IPartOccluding) {
            for (Vec3dCube b : ((IPartOccluding) part).getOcclusionBoxes()) {
                NormallyOccludedPart nop = new NormallyOccludedPart(new Cuboid6(b.toAABB()));
                try {
                    if (!tile().canAddPart(nop))
                        return false;
                } catch (Exception ex) {
                    return false;
                }
            }
        }

        return !OcclusionHelper.occlusionTest(this, part);
    }

    @Override
    public QMovingObjectPosition rayTrace(Vec3d start, Vec3d end) {

        QMovingObjectPosition closest = null;
        double dist = Double.MAX_VALUE;

        for (IPart p : getParts()) {
            if (p instanceof IPartSelectable) {
                QMovingObjectPosition mop = ((IPartSelectable) p).rayTrace(start, end);
                if (mop == null)
                    continue;
                double d = start.distanceTo(new Vec3d(mop.hitVec));
                if (d < dist) {
                    closest = mop;
                    dist = d;
                }
            }
        }

        return closest;
    }

    @Override
    public void sendUpdatePacket(IPart part, int channel) {

        if (tile() != null && world() != null && getParts().contains(part))
            PartUpdateManager.sendPartUpdate(this, part, channel);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderStatic(Vector3 pos, int pass) {

        boolean did = false;

        RenderBlocks renderer = RenderBlocks.getInstance();

        RenderHelper.instance.setRenderCoords(getWorld(), (int) pos.x, (int) pos.y, (int) pos.z);

        renderer.blockAccess = getWorld();

        for (IPart p : getParts()) {
            if (p.getParent() != null) {
                if (p.shouldRenderOnPass(pass)) {
                    p.renderStatic(new Vec3i((int) pos.x, (int) pos.y, (int) pos.z), RenderHelper.instance, renderer, pass);
                    RenderHelper.instance.resetRenderedSides();
                    RenderHelper.instance.resetTextureRotations();
                    RenderHelper.instance.resetTransformations();
                    RenderHelper.instance.setColor(0xFFFFFF);
                }
            }
        }

        renderer.blockAccess = null;

        RenderHelper.instance.reset();

        return did;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderDynamic(Vector3 pos, float frame, int pass) {

        GL11.glPushMatrix();
        {
            GL11.glTranslated(pos.x, pos.y, pos.z);
            for (IPart p : getParts()) {
                if (p.getParent() != null) {
                    GL11.glPushMatrix();

                    if (p.shouldRenderOnPass(pass))
                        p.renderDynamic(new Vec3d(0, 0, 0), frame, pass);

                    GL11.glPopMatrix();
                }
            }
        }
        GL11.glPopMatrix();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void drawBreaking(RenderBlocks renderBlocks) {

        QMovingObjectPosition mop = rayTrace(RayTracer.instance().getStartVector(Minecraft.getMinecraft().thePlayer), RayTracer.instance()
                .getEndVector(Minecraft.getMinecraft().thePlayer));

        if (mop == null || mop.getPart() == null)
            return;

        RenderHelper.instance.setRenderCoords(getWorld(), getX(), getY(), getZ());

        RenderMultipart.renderBreaking(getWorld(), getX(), getY(), getZ(), renderBlocks, mop);

        RenderHelper.instance.reset();
    }

    @Override
    public boolean drawHighlight(MovingObjectPosition hit, EntityPlayer player, float frame) {

        QMovingObjectPosition mop = rayTrace(RayTracer.instance().getStartVector(Minecraft.getMinecraft().thePlayer), RayTracer.instance()
                .getEndVector(Minecraft.getMinecraft().thePlayer));

        if (mop == null || mop.getPart() == null || !(mop.getPart() instanceof IPartSelectableCustom))
            return false;

        return ((IPartSelectableCustom) mop.getPart()).drawHighlight(mop, player, frame);
    }

    @Override
    public void addCollisionBoxesToList(List<Vec3dCube> l, AxisAlignedBB bounds, Entity entity) {

        List<Vec3dCube> boxes = new ArrayList<Vec3dCube>();

        for (IPart p : getParts()) {
            if (p instanceof IPartCollidable) {
                List<Vec3dCube> boxes_ = new ArrayList<Vec3dCube>();
                ((IPartCollidable) p).addCollisionBoxesToList(boxes_, entity);
                for (Vec3dCube c : boxes_) {
                    Vec3dCube cube = c.clone();
                    cube.add(getX(), getY(), getZ());
                    cube.setPart(p);
                    boxes.add(cube);
                }
                boxes_.clear();
            }
        }

        for (Vec3dCube c : boxes) {
            if (c.toAABB().intersectsWith(bounds))
                l.add(c);
        }
    }

    @Override
    public Iterable<Cuboid6> getCollisionBoxes() {

        List<Cuboid6> cubes = new ArrayList<Cuboid6>();

        List<Vec3dCube> boxes = new ArrayList<Vec3dCube>();
        addCollisionBoxesToList(boxes, AxisAlignedBB.getBoundingBox(x(), y(), z(), x() + 1, y() + 1, z() + 1), null);
        for (Vec3dCube c : boxes)
            cubes.add(new Cuboid6(c.clone().add(-x(), -y(), -z()).toAABB()));

        return cubes;
    }

    @Override
    public void onNeighborChanged() {

        super.onNeighborChanged();

        if (simulated)
            return;

        for (IPart p : getParts())
            if (p != null && p instanceof IPartUpdateListener)
                ((IPartUpdateListener) p).onNeighborBlockChange();
    }

    @Override
    public void onPartChanged(TMultiPart part) {

        for (IPart p : getParts())
            if (p != null && p instanceof IPartUpdateListener)
                ((IPartUpdateListener) p).onPartChanged(null);
    }

    @Override
    public Iterable<ItemStack> getDrops() {

        List<ItemStack> l = new ArrayList<ItemStack>();

        for (IPart p : getParts()) {
            List<ItemStack> d = p.getDrops();
            if (d != null)
                l.addAll(d);
        }

        return l;
    }

    @Override
    public Iterable<Cuboid6> getOcclusionBoxes() {

        List<Cuboid6> cubes = new ArrayList<Cuboid6>();

        for (IPart p : getParts())
            if (p != null && p instanceof IPartOccluding)
                for (Vec3dCube c : ((IPartOccluding) p).getOcclusionBoxes())
                    cubes.add(new IndexedCuboid6(0, new Cuboid6(c.toAABB())));

        return cubes;
    }

    @Override
    public boolean occlusionTest(TMultiPart npart) {

        return NormalOcclusionTest.apply(this, npart);
    }

    @Override
    public boolean canConnectRedstone(int side) {

        for (IPart p : getParts())
            if (p instanceof IPartRedstone)
                if (((IPartRedstone) p).canConnectRedstone(ForgeDirection.getOrientation(side)))
                    return true;
        return false;
    }

    @Override
    public int strongPowerLevel(int side) {

        int max = 0;

        for (IPart p : getParts())
            if (p instanceof IPartRedstone)
                max = Math.max(max, ((IPartRedstone) p).getStrongPower(ForgeDirection.getOrientation(side)));

        return max;
    }

    @Override
    public int weakPowerLevel(int side) {

        int max = 0;

        for (IPart p : getParts())
            if (p instanceof IPartRedstone)
                max = Math.max(max, ((IPartRedstone) p).getWeakPower(ForgeDirection.getOrientation(side)));

        return max;
    }

    @Override
    public void onNeighborTileChanged(int arg0, boolean arg1) {

        if (simulated)
            return;

        for (IPart p : getParts())
            if (p != null && p instanceof IPartUpdateListener)
                ((IPartUpdateListener) p).onNeighborTileChange();
    }

    @Override
    public boolean weakTileChanges() {

        return true;
    }

    @Override
    public void onAdded() {

        if (simulated)
            return;

        for (IPart p : getParts())
            if (p != null && p instanceof IPartUpdateListener)
                ((IPartUpdateListener) p).onAdded();
    }

    @Override
    public void onRemoved() {

        if (simulated)
            return;

        for (IPart p : getParts())
            if (p != null && p instanceof IPartUpdateListener)
                ((IPartUpdateListener) p).onRemoved();
    }

    @Override
    public void onChunkLoad() {

        if (simulated)
            return;

        for (IPart p : getParts())
            if (p != null && p instanceof IPartUpdateListener)
                ((IPartUpdateListener) p).onLoaded();
    }

    @Override
    public void onChunkUnload() {

        if (simulated)
            return;

        for (IPart p : getParts())
            if (p != null && p instanceof IPartUpdateListener)
                ((IPartUpdateListener) p).onUnloaded();
    }

    @Override
    public void onConverted() {

        converted = true;
    }

    @Override
    public void onMoved() {

        onChunkUnload();
        onChunkLoad();
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

        QMovingObjectPosition mop = rayTrace(RayTracer.instance().getStartVector(QmunityLib.proxy.getPlayer()), RayTracer.instance()
                .getEndVector(QmunityLib.proxy.getPlayer()));
        if (mop == null)
            return null;

        return mop.getPart().getItem();
    }

    @Override
    public void harvest(MovingObjectPosition hit, EntityPlayer player) {

        if (world().isRemote)
            return;

        QMovingObjectPosition mop = rayTrace(RayTracer.instance().getStartVector(player), RayTracer.instance().getEndVector(player));
        if (mop != null) {
            mop.getPart().breakAndDrop(player.capabilities.isCreativeMode);

            if (getParts().size() == 0)
                super.harvest(hit, player);
        }
    }

    @Override
    public void click(EntityPlayer player, MovingObjectPosition hit, ItemStack item) {

        QMovingObjectPosition mop = rayTrace(RayTracer.instance().getStartVector(player), RayTracer.instance().getEndVector(player));
        if (mop != null)
            if (mop.getPart() instanceof IPartInteractable)
                ((IPartInteractable) mop.getPart()).onClicked(player, mop, item);
    }

    @Override
    public boolean activate(EntityPlayer player, MovingObjectPosition hit, ItemStack item) {

        QMovingObjectPosition mop = rayTrace(RayTracer.instance().getStartVector(player), RayTracer.instance().getEndVector(player));
        if (mop != null)
            if (mop.getPart() instanceof IPartInteractable)
                return ((IPartInteractable) mop.getPart()).onActivated(player, mop, item);
        return false;
    }

    @Override
    public Map<String, IPart> getPartMap() {

        return parts;
    }

    @Override
    public List<IMicroblock> getMicroblocks() {

        return MultipartSystem.FMP.getCompat().getMicroblocks(getWorld(), new Vec3i(this));
    }

    @Override
    public boolean isSimulated() {

        return simulated;
    }

    public boolean isSolid(ForgeDirection face) {

        for (IPart p : getParts())
            if (p instanceof IPartSolid && ((IPartSolid) p).isSideSolid(face))
                return true;

        return true;
    }

    @Override
    public boolean isSolid(int side) {

        return isSolid(ForgeDirection.getOrientation(side));
    }

    @Override
    public float getStrength(MovingObjectPosition hit, EntityPlayer player) {

        QMovingObjectPosition mop = rayTrace(RayTracer.instance().getStartVector(player), RayTracer.instance().getEndVector(player));

        if (mop != null && mop.getPart() != null)
            return (float) (30 * mop.getPart().getHardness(player, mop));
        return 30;
    }

    @Override
    public int getLightValue() {

        int val = 0;
        for (IPart p : getParts())
            val = Math.max(val, p.getLightValue());
        return val;
    }

    @Override
    public int getHollowSize(int side) {

        int val = 0;
        boolean found = false;
        for (IPart p : getParts()) {
            if (p instanceof IPartThruHole) {
                val = Math.max(val, ((IPartThruHole) p).getHollowSize(ForgeDirection.getOrientation(side)));
                found = true;
            }
        }
        if (found && (val > 0 || val < 12))
            return val;
        return 8;
    }

    @Override
    public int getSlotMask() {

        for (IPart p : getParts())
            if (p instanceof IPartCenter)
                return PartMap.CENTER.mask;

        return 0;
    }

}

interface IFMPPart {

    public boolean isSolid(int side);
}
