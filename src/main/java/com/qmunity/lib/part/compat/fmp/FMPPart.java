package com.qmunity.lib.part.compat.fmp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.raytracer.ExtendedMOP;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.INeighborTileChange;
import codechicken.multipart.IRedstonePart;
import codechicken.multipart.NormalOcclusionTest;
import codechicken.multipart.TFacePart;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TNormalOcclusion;

import com.qmunity.lib.QLModInfo;
import com.qmunity.lib.part.IPart;
import com.qmunity.lib.part.IPartCollidable;
import com.qmunity.lib.part.IPartFace;
import com.qmunity.lib.part.IPartOccluding;
import com.qmunity.lib.part.IPartRedstone;
import com.qmunity.lib.part.IPartRenderable;
import com.qmunity.lib.part.IPartSelectable;
import com.qmunity.lib.part.IPartSolid;
import com.qmunity.lib.part.IPartTicking;
import com.qmunity.lib.part.IPartUpdateListener;
import com.qmunity.lib.part.ITilePartHolder;
import com.qmunity.lib.part.PartRegistry;
import com.qmunity.lib.raytrace.QMovingObjectPosition;
import com.qmunity.lib.vec.Vec3d;
import com.qmunity.lib.vec.Vec3dCube;
import com.qmunity.lib.vec.Vec3i;

public class FMPPart extends TMultiPart implements ITilePartHolder, TFacePart, TNormalOcclusion, IRedstonePart, INeighborTileChange {

    private IPart part;

    public FMPPart() {

    }

    public FMPPart(IPart part) {

        this.part = part;
        part.setParent(this);
    }

    @Override
    public String getType() {

        return QLModInfo.MODID + ".multipart";
    }

    public IPart getPart() {

        return part;
    }

    @Override
    public Iterable<IndexedCuboid6> getSubParts() {

        List<IndexedCuboid6> cubes = new ArrayList<IndexedCuboid6>();

        if (part != null && part instanceof IPartSelectable) {
            for (Vec3dCube c : ((IPartSelectable) part).getSelectionBoxes())
                cubes.add(new IndexedCuboid6(0, new Cuboid6(c.toAABB())));
        } else {
            cubes.add(new IndexedCuboid6(0, new Cuboid6(0, 0, 0, 1, 1, 1)));
        }

        return cubes;
    }

    @Override
    public ExtendedMOP collisionRayTrace(Vec3 start, Vec3 end) {

        // return super.collisionRayTrace(start, end);
        if (part != null && part instanceof IPartSelectable) {
            QMovingObjectPosition mop = ((IPartSelectable) part).rayTrace(new Vec3d(start), new Vec3d(end));
            if (mop == null)
                return null;

            new Cuboid6(mop.getCube().toAABB()).setBlockBounds(tile().getBlockType());
            return new ExtendedMOP(mop, 0, start.distanceTo(mop.hitVec));
        }

        return super.collisionRayTrace(start, end);
    }

    @Override
    public void update() {

        if (part != null && part instanceof IPartTicking)
            ((IPartTicking) part).update();

    }

    // Loading/saving parts

    @Override
    public void save(NBTTagCompound tag) {

        boolean client = tile() == null ? false : world().isRemote;

        super.save(tag);

        if (getPart() == null) {
            new Exception("Saved null part! Client: " + client).printStackTrace();
            return;
        }

        tag.setString("part", getPart().getType());

        NBTTagCompound t = new NBTTagCompound();
        getPart().writeToNBT(t);
        tag.setTag("partData", t);
    }

    @Override
    public void load(NBTTagCompound tag) {

        boolean client = tile() == null ? false : world().isRemote;

        super.load(tag);

        if (!tag.hasKey("part"))
            return;

        if (getPart() == null) {
            part = PartRegistry.createPart(tag.getString("part"), client);
            part.setParent(this);
        }

        if (getPart() == null) {
            new Exception("Loaded null part! Client: " + client).printStackTrace();
            return;
        }

        getPart().readFromNBT(tag.getCompoundTag("partData"));
    }

    @Override
    public void writeDesc(MCDataOutput packet) {

        super.writeDesc(packet);

        if (getPart() == null) {
            packet.writeBoolean(true);
            return;
        } else {
            packet.writeBoolean(false);
        }

        packet.writeString(getPart().getType());

        NBTTagCompound t = new NBTTagCompound();
        getPart().writeToNBT(t);
        packet.writeNBTTagCompound(t);
    }

    @Override
    public void readDesc(MCDataInput packet) {

        boolean client = tile() == null ? false : world().isRemote;

        super.readDesc(packet);

        if (packet.readBoolean())
            return;

        String type = packet.readString();
        NBTTagCompound tag = packet.readNBTTagCompound();

        if (getPart() == null) {
            part = PartRegistry.createPart(type, client);
            part.setParent(this);
        }

        if (getPart() == null) {
            new Exception("Received null part data! Client: " + client).printStackTrace();
            return;
        }

        getPart().readFromNBT(tag);
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
    public List<IPart> getParts() {

        return Arrays.asList(part);
    }

    @Override
    public void addPart(IPart part) {

    }

    @Override
    public boolean removePart(IPart part) {

        if (part == this.part) {
            if (!world().isRemote)
                tile().remPart(this);
            return true;
        }
        return false;
    }

    @Override
    public void sendPartUpdate(IPart part) {

        sendDescUpdate();
    }

    @Override
    public boolean canAddPart(IPart part) {

        return false;
    }

    @Override
    public QMovingObjectPosition rayTrace(Vec3d start, Vec3d end) {

        return null;
    }

    @Override
    public boolean renderStatic(Vector3 pos, int pass) {

        if (part == null)
            return false;

        if (part instanceof IPartRenderable) {
            if (((IPartRenderable) part).shouldRenderOnPass(pass)) {
                RenderBlocks.getInstance().blockAccess = world();
                boolean rendered = ((IPartRenderable) part).renderStatic(new Vec3i((int) pos.x, (int) pos.y, (int) pos.z),
                        RenderBlocks.getInstance(), pass);
                RenderBlocks.getInstance().blockAccess = null;
                return rendered;
            }
        }

        return false;
    }

    @Override
    public void renderDynamic(Vector3 pos, float frame, int pass) {

        if (part == null)
            return;

        GL11.glPushMatrix();
        {
            GL11.glTranslated(pos.x, pos.y, pos.z);

            if (part instanceof IPartRenderable)
                if (((IPartRenderable) part).shouldRenderOnPass(pass))
                    ((IPartRenderable) part).renderDynamic(new Vec3d(0, 0, 0), frame, pass);
        }
        GL11.glPopMatrix();
    }

    @Override
    public void addCollisionBoxesToList(List<Vec3dCube> boxes, AxisAlignedBB bounds, Entity entity) {

        if (part != null && part instanceof IPartCollidable) {
            List<Vec3dCube> boxes_ = new ArrayList<Vec3dCube>();
            ((IPartCollidable) part).addCollisionBoxesToList(boxes_, entity);
            for (Vec3dCube c : boxes) {
                Vec3dCube cube = c.clone();
                cube.setPart(part);
                boxes.add(cube);
            }
            boxes_.clear();
        }
    }

    @Override
    public Iterable<Cuboid6> getCollisionBoxes() {

        List<Cuboid6> cubes = new ArrayList<Cuboid6>();

        List<Vec3dCube> boxes = new ArrayList<Vec3dCube>();
        addCollisionBoxesToList(boxes, AxisAlignedBB.getBoundingBox(x(), y(), z(), x() + 1, y() + 1, z() + 1), null);
        for (Vec3dCube c : boxes)
            cubes.add(new Cuboid6(c.toAABB()));

        return cubes;
    }

    @Override
    public void onNeighborChanged() {

        super.onNeighborChanged();
        onUpdate();

        if (part != null && part instanceof IPartUpdateListener)
            ((IPartUpdateListener) part).onNeighborBlockChange();
    }

    @Override
    public void onPartChanged(TMultiPart part) {

        super.onPartChanged(part);
        onUpdate();

        IPart p = null;

        if (part instanceof FMPPart)
            p = ((FMPPart) part).getPart();

        if (this.part != null && this.part instanceof IPartUpdateListener)
            ((IPartUpdateListener) this.part).onPartChanged(p);
    }

    private void onUpdate() {

        if (part != null && part instanceof IPartFace) {
            if (!((IPartFace) part).canStay()) {
                part.breakAndDrop(false);
            }
        }
    }

    @Override
    public Iterable<ItemStack> getDrops() {

        return part.getDrops();
    }

    @Override
    public int getSlotMask() {

        if (part != null && part instanceof IPartFace)
            return 1 << ((IPartFace) part).getFace().ordinal();

        return 0;
    }

    @Override
    public int redstoneConductionMap() {

        return 0;
    }

    @Override
    public boolean solid(int f) {

        if (part != null && part instanceof IPartSolid)
            return ((IPartSolid) part).isSideSolid(ForgeDirection.getOrientation(f));

        return false;
    }

    @Override
    public Iterable<Cuboid6> getOcclusionBoxes() {

        List<Cuboid6> cubes = new ArrayList<Cuboid6>();

        if (part != null && part instanceof IPartOccluding) {
            for (Vec3dCube c : ((IPartOccluding) part).getOcclusionBoxes())
                cubes.add(new IndexedCuboid6(0, new Cuboid6(c.toAABB())));
        }

        return cubes;
    }

    @Override
    public boolean occlusionTest(TMultiPart npart) {

        return NormalOcclusionTest.apply(this, npart);
    }

    @Override
    public boolean canConnectRedstone(int side) {

        if (part != null && part instanceof IPartRedstone)
            return ((IPartRedstone) part).canConnectRedstone(ForgeDirection.getOrientation(side));

        return false;
    }

    @Override
    public int strongPowerLevel(int side) {

        if (part != null && part instanceof IPartRedstone)
            return ((IPartRedstone) part).getStrongPower(ForgeDirection.getOrientation(side));

        return 0;
    }

    @Override
    public int weakPowerLevel(int side) {

        if (part != null && part instanceof IPartRedstone)
            return ((IPartRedstone) part).getWeakPower(ForgeDirection.getOrientation(side));

        return 0;
    }

    @Override
    public void onNeighborTileChanged(int arg0, boolean arg1) {

        if (part != null && part instanceof IPartUpdateListener)
            ((IPartUpdateListener) part).onNeighborTileChange();
    }

    @Override
    public boolean weakTileChanges() {

        return true;
    }

    @Override
    public void onAdded() {

        if (part != null && part instanceof IPartUpdateListener)
            ((IPartUpdateListener) part).onAdded();
    }

    @Override
    public void onRemoved() {

        if (part != null && part instanceof IPartUpdateListener)
            ((IPartUpdateListener) part).onRemoved();
    }

    @Override
    public void onChunkLoad() {

        super.onChunkLoad();

        if (part != null && part instanceof IPartUpdateListener)
            ((IPartUpdateListener) part).onLoaded();
    }

    @Override
    public void onChunkUnload() {

        super.onChunkUnload();

        if (part != null && part instanceof IPartUpdateListener)
            ((IPartUpdateListener) part).onUnloaded();
    }

    @Override
    public ItemStack pickItem(MovingObjectPosition hit) {

        return part != null ? part.getItem() : null;
    }

}
