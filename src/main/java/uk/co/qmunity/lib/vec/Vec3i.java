/*******************************************************************************
 * Copyright 2014 amadornes
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations
 * under the License.
 ******************************************************************************/
package uk.co.qmunity.lib.vec;

import java.util.StringTokenizer;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import uk.co.qmunity.lib.helper.BlockPos;

public class Vec3i implements IWorldLocation {

    protected BlockPos pos;
    protected World w = null;

    public Vec3i(BlockPos pos) {

        this.pos = pos;
    }

    public Vec3i(World world, BlockPos pos) {

        this.w = world;
        this.pos = pos;
    }

    public Vec3i(int x, int y, int z) {

        this(new BlockPos(x, y, z));
    }

    public Vec3i(int x, int y, int z, World w) {

        this(w, new BlockPos(x, y, z));
    }

    public Vec3i(TileEntity te) {

        this(te.xCoord, te.yCoord, te.zCoord, te.getWorldObj());
    }

    public Vec3i(Vec3 vec) {

        this((int) vec.xCoord, (int) vec.yCoord, (int) vec.zCoord);
    }

    public Vec3i(Vec3 vec, World w) {

        this((int) vec.xCoord, (int) vec.yCoord, (int) vec.zCoord);
        this.w = w;
    }

    public Vec3i(IWorldLocation loc) {

        this(loc.getX(), loc.getY(), loc.getZ(), loc.getWorld());
    }

    public boolean hasWorld() {

        return w != null;
    }

    public Vec3i add(int x, int y, int z) {

        return new Vec3i(pos.add(x, y, z));
    }

    public Vec3i add(ForgeDirection dir) {

        return add(dir.offsetX, dir.offsetY, dir.offsetZ);
    }

    public Vec3i add(Vec3i vec) {

        return add(vec.pos.getX(), vec.pos.getY(), vec.pos.getZ());
    }

    public Vec3i subtract(int x, int y, int z) {

        return new Vec3i(pos.add(-x, -y, -z));
    }

    public Vec3i subtract(ForgeDirection dir) {

        return subtract(dir.offsetX, dir.offsetY, dir.offsetZ);
    }

    public Vec3i subtract(Vec3i vec) {

        return add(-vec.pos.getX(), -vec.pos.getY(), -vec.pos.getZ());
    }

    public Vec3i multiply(int x, int y, int z) {

        pos = new BlockPos(getX() * x, this.getY() * y, getZ() * z);
        return this;
    }

    public Vec3i multiply(int multiplier) {

        return multiply(multiplier, multiplier, multiplier);
    }

    public Vec3i multiply(ForgeDirection direction) {

        return multiply(direction.offsetX, direction.offsetY, direction.offsetZ);
    }

    public Vec3i divide(int x, int y, int z) {

        this.pos = new BlockPos(getX() / x, this.getY() / y, getZ() / z);
        return this;
    }

    public Vec3i divide(int multiplier) {

        return divide(multiplier, multiplier, multiplier);
    }

    public Vec3i divide(ForgeDirection direction) {

        return divide(direction.offsetX, direction.offsetY, direction.offsetZ);
    }

    public double length() {

        return Math.sqrt(getX() * getX() + getY() * getY() + getZ() * getZ());
    }

    public Vec3i normalize() {

        Vec3i v = clone();
        double len = length();

        v.pos = new BlockPos(v.getX() / len, v.getY() / len, v.getZ() / len);
        return v;
    }

    public Vec3i abs() {

        return new Vec3i(Math.abs(this.getX()), Math.abs(getY()), Math.abs(getZ()));
    }

    public double dot(Vec3i v) {

        return this.getX() * v.getX() + getY() * v.getY() + getZ() * v.getZ();
    }

    public Vec3i cross(Vec3i v) {

        return new Vec3i(getY() * v.getZ() - getZ() * v.getY(), getZ() * v.getZ() - getX() * v.getX(), getX() * v.getY() - getY() * v.getX());
    }

    public Vec3i getRelative(int x, int y, int z) {

        return clone().add(x, y, z);
    }

    public Vec3i getRelative(ForgeDirection dir) {

        return getRelative(dir.offsetX, dir.offsetY, dir.offsetZ);
    }

    public ForgeDirection getDirectionTo(Vec3i vec) {

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
            if (getX() + d.offsetX == vec.getX() && getY() + d.offsetY == vec.getY() && getZ() + d.offsetZ == vec.getZ())
                return d;
        return ForgeDirection.UNKNOWN;
    }

    @Override
    public Vec3i clone() {

        return new Vec3i(w, pos);
    }

    public boolean hasTileEntity() {

        if (hasWorld()) {
            return w.getTileEntity(getX(), getY(), getZ()) != null;
        }
        return false;
    }

    public TileEntity getTileEntity() {

        if (hasTileEntity()) {
            return w.getTileEntity(getX(), getY(), getZ());
        }
        return null;
    }

    public boolean isBlock(Block b) {

        return isBlock(b, false);
    }

    public boolean isBlock(Block b, boolean checkAir) {

        if (hasWorld()) {
            Block bl = w.getBlock(getX(), getY(), getZ());

            if (b == null && bl == Blocks.air)
                return true;
            if (b == null && checkAir && bl.getMaterial() == Material.air)
                return true;
            if (b == null && checkAir && bl.isAir(w, getX(), getY(), getZ()))
                return true;

            return bl.getClass().isInstance(b);
        }
        return false;
    }

    public int getBlockMeta() {

        if (hasWorld()) {
            return w.getBlockMetadata(getX(), getY(), getZ());
        }
        return -1;
    }

    public Block getBlock() {

        return getBlock(false);
    }

    public Block getBlock(boolean airIsNull) {

        if (hasWorld()) {
            if (airIsNull && isBlock(null, true))
                return null;
            return w.getBlock(getX(), getY(), getZ());

        }
        return null;
    }

    @Override
    public World getWorld() {

        return w;
    }

    public Vec3i setWorld(World world) {

        w = world;

        return this;
    }

    @Override
    public int getX() {

        return pos.getX();
    }

    @Override
    public int getY() {

        return pos.getY();
    }

    @Override
    public int getZ() {

        return pos.getZ();
    }

    public Vec3i getImmutableCopy() {

        return new ImmutableVec3i(this);
    }

    public int distanceTo(Vec3i vec) {

        return distanceTo(vec.getX(), vec.getY(), vec.getZ());
    }

    public int distanceTo(BlockPos pos) {

        int dx = pos.getX() - getX();
        int dy = pos.getY() - getY();
        int dz = pos.getZ() - getZ();
        return dx * dx + dy * dy + dz * dz;
    }

    public int distanceTo(int x, int y, int z) {

        return distanceTo(new BlockPos(x, y, z));
    }

    public void setPos(BlockPos pos) {

        this.pos = pos;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Vec3i) {
            Vec3i vec = (Vec3i) obj;
            return vec.w == w && vec.getX() == getX() && vec.getY() == getY() && vec.getZ() == getZ();
        }
        return false;
    }

    public Vec3 toVec3() {

        return Vec3.createVectorHelper(getX(), getY(), getZ());
    }

    @Override
    public String toString() {

        String s = "Vector3{";
        if (hasWorld())
            s += "w=" + w.provider.dimensionId + ";";
        s += "x=" + getX() + ";y=" + getY() + ";z=" + getZ() + "}";
        return s;
    }

    public ForgeDirection toForgeDirection() {

        if (getZ() >= getX() && getZ() >= getY())
            return ForgeDirection.SOUTH;
        if (getZ() <= getX() && getZ() <= getY())
            return ForgeDirection.NORTH;

        if (getX() >= getY() && getX() >= getZ())
            return ForgeDirection.EAST;
        if (getX() <= getY() && getX() <= getZ())
            return ForgeDirection.WEST;

        if (getY() >= getX() && getY() >= getZ())
            return ForgeDirection.UP;
        if (getY() <= getX() && getY() <= getZ())
            return ForgeDirection.DOWN;

        return ForgeDirection.UNKNOWN;
    }

    public static Vec3i fromString(String s) {

        if (s.startsWith("Vector3{") && s.endsWith("}")) {
            World w = null;
            int x = 0, y = 0, z = 0;
            String s2 = s.substring(s.indexOf("{") + 1, s.lastIndexOf("}"));
            StringTokenizer st = new StringTokenizer(s2, ";");
            while (st.hasMoreTokens()) {
                String t = st.nextToken();

                if (t.toLowerCase().startsWith("w")) {
                    int world = Integer.parseInt(t.split("=")[1]);
                    if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
                        for (World wo : MinecraftServer.getServer().worldServers) {
                            if (wo.provider.dimensionId == world) {
                                w = wo;
                                break;
                            }
                        }
                    } else {
                        w = getClientWorld(world);
                    }
                }

                if (t.toLowerCase().startsWith("x"))
                    x = Integer.parseInt(t.split("=")[1]);
                if (t.toLowerCase().startsWith("y"))
                    y = Integer.parseInt(t.split("=")[1]);
                if (t.toLowerCase().startsWith("z"))
                    z = Integer.parseInt(t.split("=")[1]);
            }

            if (w != null) {
                return new Vec3i(x, y, z, w);
            } else {
                return new Vec3i(x, y, z);
            }
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    private static World getClientWorld(int world) {

        if (Minecraft.getMinecraft().theWorld.provider.dimensionId != world)
            return null;
        return Minecraft.getMinecraft().theWorld;
    }

}
