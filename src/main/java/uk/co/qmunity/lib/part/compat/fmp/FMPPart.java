package uk.co.qmunity.lib.part.compat.fmp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import uk.co.qmunity.lib.part.IPartCollidable;
import uk.co.qmunity.lib.part.IPartFace;
import uk.co.qmunity.lib.part.IPartInteractable;
import uk.co.qmunity.lib.part.IPartOccluding;
import uk.co.qmunity.lib.part.IPartRedstone;
import uk.co.qmunity.lib.part.IPartSelectable;
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
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TNormalOcclusion;

public class FMPPart extends TMultiPart implements ITilePartHolder, TNormalOcclusion, IRedstonePart, INeighborTileChange, IFMPPart,
ISidedHollowConnect {

    private Map<String, IPart> parts = new HashMap<String, IPart>();
    private List<IPart> added = new ArrayList<IPart>();

    private boolean shouldDieInAFire = false;
    private boolean loaded = false;

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

        for (IPart p : getParts()) {
            if (firstTick && loaded) {
                for (IPart p_ : added)
                    if (p_ == p)
                        ((IPartUpdateListener) p).onAdded();
                if (p instanceof IPartUpdateListener) {
                    ((IPartUpdateListener) p).onLoaded();
                }
            }
            if (p instanceof IPartTicking)
                ((IPartTicking) p).update();
        }
        firstTick = false;

        if (!world().isRemote && shouldDieInAFire)
            tile().remPart(this);
    }

    @Override
    public void save(NBTTagCompound tag) {

        super.save(tag);

        NBTTagList l = new NBTTagList();
        writeParts(l, false);
        tag.setTag("parts", l);
    }

    @Override
    public void load(NBTTagCompound tag) {

        super.load(tag);

        NBTTagList l = tag.getTagList("parts", new NBTTagCompound().getId());
        readParts(l, true, false);

        if (getParts().size() == 0)
            shouldDieInAFire = true;

        loaded = true;

        if (tile() != null && getWorld() != null)
            getWorld().markBlockRangeForRenderUpdate(getX(), getY(), getZ(), getX(), getY(), getZ());
    }

    @Override
    public void writeDesc(MCDataOutput packet) {

        super.writeDesc(packet);

        NBTTagCompound tag = new NBTTagCompound();

        NBTTagList l = new NBTTagList();
        writeParts(l, true);
        tag.setTag("parts", l);

        packet.writeNBTTagCompound(tag);
    }

    @Override
    public void readDesc(MCDataInput packet) {

        super.readDesc(packet);

        NBTTagList l = packet.readNBTTagCompound().getTagList("parts", new NBTTagCompound().getId());
        readParts(l, true, true);

        if (tile() != null && getWorld() != null)
            getWorld().markBlockRangeForRenderUpdate(getX(), getY(), getZ(), getX(), getY(), getZ());
    }

    private void writeParts(NBTTagList l, boolean update) {

        for (IPart p : getParts()) {
            String id = getIdentifier(p);

            NBTTagCompound tag = new NBTTagCompound();

            tag.setString("id", id);
            tag.setString("type", p.getType());
            NBTTagCompound data = new NBTTagCompound();
            if (update)
                p.writeUpdateToNBT(data);
            else
                p.writeToNBT(data);
            tag.setTag("data", data);

            l.appendTag(tag);
        }
    }

    private void readParts(NBTTagList l, boolean update, boolean client) {

        for (int i = 0; i < l.tagCount(); i++) {
            NBTTagCompound tag = l.getCompoundTagAt(i);

            String id = tag.getString("id");
            IPart p = getPart(id);
            if (p == null) {
                p = PartRegistry.createPart(tag.getString("type"), client);
                if (p == null)
                    continue;
                p.setParent(this);
                parts.put(id, p);
            }

            NBTTagCompound data = tag.getCompoundTag("data");
            if (update)
                p.readUpdateFromNBT(data);
            else
                p.readFromNBT(data);
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
        }

        return true;
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
                if (!tile().canAddPart(nop))
                    return false;
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

        onUpdate();

        for (IPart p : getParts())
            if (p != null && p instanceof IPartUpdateListener)
                ((IPartUpdateListener) p).onNeighborBlockChange();
    }

    @Override
    public void onPartChanged(TMultiPart part) {

        super.onPartChanged(part);

        if (simulated)
            return;

        onUpdate();

        for (IPart p : getParts())
            if (p != null && p instanceof IPartUpdateListener)
                ((IPartUpdateListener) p).onPartChanged(null);
    }

    private void onUpdate() {

        if (simulated)
            return;

        for (IPart p : getParts())
            if (p != null && p instanceof IPartFace)
                if (!((IPartFace) p).canStay())
                    p.breakAndDrop(false);
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

        super.onChunkLoad();

        if (simulated)
            return;

        for (IPart p : getParts())
            if (p != null && p instanceof IPartUpdateListener)
                ((IPartUpdateListener) p).onLoaded();
    }

    @Override
    public void onChunkUnload() {

        super.onChunkUnload();

        if (simulated)
            return;

        for (IPart p : getParts())
            if (p != null && p instanceof IPartUpdateListener)
                ((IPartUpdateListener) p).onUnloaded();
    }

    @Override
    public void onConverted() {

        for (IPart p : getParts())
            if (p instanceof IPartUpdateListener)
                ((IPartUpdateListener) p).onConverted();
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
        for (IPart p : getParts())
            if (p instanceof IPartThruHole)
                val = Math.max(val, ((IPartThruHole) p).getHollowSize(ForgeDirection.getOrientation(side)));
        if (val <= 0 || val >= 12)
            return val;
        return 4;
    }

}

interface IFMPPart {

    public boolean isSolid(int side);
}
