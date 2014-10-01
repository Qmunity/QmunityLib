package com.qmunity.lib.tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.qmunity.lib.part.IPart;
import com.qmunity.lib.part.IPartCollidable;
import com.qmunity.lib.part.IPartFace;
import com.qmunity.lib.part.IPartLightEmitter;
import com.qmunity.lib.part.IPartOccluding;
import com.qmunity.lib.part.IPartRedstone;
import com.qmunity.lib.part.IPartSelectable;
import com.qmunity.lib.part.IPartSolid;
import com.qmunity.lib.part.IPartTicking;
import com.qmunity.lib.part.IPartUpdateListener;
import com.qmunity.lib.part.ITilePartHolder;
import com.qmunity.lib.part.PartRegistry;
import com.qmunity.lib.raytrace.QMovingObjectPosition;
import com.qmunity.lib.raytrace.RayTracer;
import com.qmunity.lib.vec.Vec3d;
import com.qmunity.lib.vec.Vec3dCube;

public class TileMultipart extends TileEntity implements ITilePartHolder {

    private Map<String, IPart> parts = new HashMap<String, IPart>();
    private List<IPart> toUpdate = new ArrayList<IPart>();
    private List<String> removed = new ArrayList<String>();

    @Override
    public World getWorld() {

        return getWorldObj();
    }

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
    public boolean canAddPart(IPart part) {

        if (part instanceof IPartCollidable) {
            List<Vec3dCube> cubes = new ArrayList<Vec3dCube>();
            ((IPartCollidable) part).addCollisionBoxesToList(cubes, null);
            for (Vec3dCube c : cubes)
                if (!getWorld().checkNoEntityCollision(c.clone().add(getX(), getY(), getZ()).toAABB()))
                    return false;
        }

        if (part instanceof IPartOccluding) {
            List<Vec3dCube> l = getOcclusionBoxes();
            for (Vec3dCube b : ((IPartOccluding) part).getOcclusionBoxes())
                for (Vec3dCube c : l)
                    if (b.toAABB().intersectsWith(c.toAABB()))
                        return false;
        }

        return true;
    }

    @Override
    public void addPart(IPart part) {

        parts.put(genIdentifier(), part);
        part.setParent(this);
        sendPartUpdate(part);
        if (part instanceof IPartUpdateListener)
            ((IPartUpdateListener) part).onAdded();
        for (IPart p : getParts())
            if (p != part && p instanceof IPartUpdateListener)
                ((IPartUpdateListener) p).onPartChanged(part);
    }

    @Override
    public boolean removePart(IPart part) {

        if (part == null)
            return false;
        if (!parts.containsValue(part))
            return false;
        if (removed.contains(part))
            return false;

        if (part instanceof IPartUpdateListener)
            ((IPartUpdateListener) part).onRemoved();
        for (IPart p : getParts())
            if (p != part && p instanceof IPartUpdateListener)
                ((IPartUpdateListener) p).onPartChanged(part);

        String id = getIdentifier(part);
        removed.add(id);
        parts.remove(id);
        part.setParent(null);
        sendUpdatePacket();

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
    public void sendPartUpdate(IPart part) {

        if (!toUpdate.contains(part))
            toUpdate.add(part);
        sendUpdatePacket();
    }

    public int getLightValue() {

        int val = 0;

        for (IPart p : getParts())
            if (p instanceof IPartLightEmitter)
                val = Math.max(val, ((IPartLightEmitter) p).getLightValue());

        return val;
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

    // Saving/syncing parts

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);

        NBTTagList l = new NBTTagList();
        writeParts(l, getParts(), false);
        tag.setTag("parts", l);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);

        NBTTagList l = tag.getTagList("parts", new NBTTagCompound().getId());
        readParts(l, false, false);
        toUpdate.addAll(getParts());
    }

    public void writeUpdateToNBT(NBTTagCompound tag) {

        List<IPart> toUpdate = new ArrayList<IPart>();
        toUpdate.addAll(this.toUpdate);

        NBTTagList l = new NBTTagList();
        writeParts(l, toUpdate, true);
        tag.setTag("parts", l);

        NBTTagList rem = new NBTTagList();
        for (String s : removed)
            rem.appendTag(new NBTTagString(s));
        tag.setTag("removed", rem);
    }

    public void readUpdateFromNBT(NBTTagCompound tag) {

        int before = getParts().size();

        NBTTagList l = tag.getTagList("parts", new NBTTagCompound().getId());
        readParts(l, true, true);

        NBTTagList rem = tag.getTagList("removed", new NBTTagString().getId());
        for (int i = 0; i < rem.tagCount(); i++)
            removePart(getPart(rem.getStringTagAt(i)));

        if (getParts().size() != before)
            getWorldObj().markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
    }

    private void writeParts(NBTTagList l, List<IPart> parts, boolean update) {

        for (IPart p : parts) {
            String id = getIdentifier(p);

            if (removed.contains(id))
                continue;

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

    @Override
    public Packet getDescriptionPacket() {

        NBTTagCompound tag = new NBTTagCompound();
        writeUpdateToNBT(tag);
        S35PacketUpdateTileEntity pkt = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);

        toUpdate.clear();
        removed.clear();

        return pkt;
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {

        readUpdateFromNBT(pkt.func_148857_g());
    }

    public void sendUpdatePacket() {

        getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void removePart(EntityPlayer player) {

        QMovingObjectPosition mop = rayTrace(RayTracer.instance().getStartVector(player), RayTracer.instance().getEndVector(player));
        if (mop != null) {
            mop.getPart().breakAndDrop(player.capabilities.isCreativeMode);
        }
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

    public void onNeighborBlockChange() {

        onUpdate();
        for (IPart p : getParts())
            if (p instanceof IPartUpdateListener)
                ((IPartUpdateListener) p).onNeighborBlockChange();
    }

    public void onNeighborChange() {

        onUpdate();
        for (IPart p : getParts())
            if (p instanceof IPartUpdateListener)
                ((IPartUpdateListener) p).onNeighborTileChange();
    }

    private void onUpdate() {

        for (IPart p : getParts())
            if (p instanceof IPartFace)
                if (!((IPartFace) p).canStay())
                    p.breakAndDrop(false);

        if (getParts().size() == 0) {
            getWorldObj().setBlockToAir(xCoord, yCoord, zCoord);
        }
    }

    public boolean isSideSolid(ForgeDirection face) {

        for (IPart p : getParts())
            if (p instanceof IPartSolid)
                if (((IPartSolid) p).isSideSolid(face))
                    return true;

        return false;
    }

    private boolean firstTick = true;

    @Override
    public void updateEntity() {

        for (IPart p : getParts()) {
            if (firstTick) {
                if (p instanceof IPartUpdateListener)
                    ((IPartUpdateListener) p).onLoaded();
                firstTick = false;
            }
            if (p instanceof IPartTicking)
                ((IPartTicking) p).update();
        }
    }

    public List<Vec3dCube> getOcclusionBoxes() {

        List<Vec3dCube> boxes = new ArrayList<Vec3dCube>();

        for (IPart p : getParts())
            if (p instanceof IPartOccluding)
                boxes.addAll(((IPartOccluding) p).getOcclusionBoxes());

        return boxes;
    }

    public int getStrongOutput(ForgeDirection direction, ForgeDirection face) {

        int max = 0;

        for (IPart p : getParts()) {
            if (p instanceof IPartRedstone) {
                if (p instanceof IPartFace) {
                    if (((IPartFace) p).getFace() == face)
                        max = Math.max(max, ((IPartRedstone) p).getStrongPower(direction));
                } else {
                    max = Math.max(max, ((IPartRedstone) p).getStrongPower(direction));
                }
            }
        }

        return max;
    }

    public int getStrongOutput(ForgeDirection direction) {

        int max = 0;

        for (ForgeDirection face : ForgeDirection.VALID_DIRECTIONS)
            max = Math.max(max, getStrongOutput(direction, face));

        return max;
    }

    public int getWeakOutput(ForgeDirection direction, ForgeDirection face) {

        int max = 0;

        for (IPart p : getParts()) {
            if (p instanceof IPartRedstone) {
                if (p instanceof IPartFace) {
                    if (((IPartFace) p).getFace() == face)
                        max = Math.max(max, ((IPartRedstone) p).getWeakPower(direction));
                } else {
                    max = Math.max(max, ((IPartRedstone) p).getWeakPower(direction));
                }
            }
        }

        return max;
    }

    public int getWeakOutput(ForgeDirection direction) {

        int max = 0;

        for (ForgeDirection face : ForgeDirection.VALID_DIRECTIONS)
            max = Math.max(max, getWeakOutput(direction, face));

        return max;
    }

    public boolean canConnect(ForgeDirection direction, ForgeDirection face) {

        for (IPart p : getParts()) {
            if (p instanceof IPartRedstone) {
                if (p instanceof IPartFace) {
                    if (((IPartFace) p).getFace() == face)
                        if (((IPartRedstone) p).canConnectRedstone(direction))
                            return true;
                } else {
                    if (((IPartRedstone) p).canConnectRedstone(direction))
                        return true;
                }
            }
        }

        return false;
    }

    public boolean canConnect(ForgeDirection direction) {

        for (ForgeDirection face : ForgeDirection.VALID_DIRECTIONS)
            if (canConnect(direction, face))
                return true;

        return false;
    }

    @Override
    public void onChunkUnload() {

        for (IPart p : getParts())
            if (p instanceof IPartUpdateListener)
                ((IPartUpdateListener) p).onUnloaded();
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {

        return AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
    }

    public ItemStack pickUp(EntityPlayer player) {

        QMovingObjectPosition mop = rayTrace(RayTracer.instance().getStartVector(player), RayTracer.instance().getEndVector(player));
        if (mop != null) {
            return mop.getPart().getItem();
        }

        return null;
    }

    public Map<String, IPart> getPartMap() {

        return parts;
    }

}
