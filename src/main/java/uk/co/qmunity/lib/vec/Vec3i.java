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

public class Vec3i implements IWorldLocation {

    protected int x, y, z;
    protected World w = null;

    public Vec3i(int x, int y, int z) {

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3i(int x, int y, int z, World w) {

        this(x, y, z);
        this.w = w;
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

        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vec3i add(ForgeDirection dir) {

        return add(dir.offsetX, dir.offsetY, dir.offsetZ);
    }

    public Vec3i add(Vec3i vec) {

        return add(vec.x, vec.y, vec.z);
    }

    public Vec3i subtract(int x, int y, int z) {

        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Vec3i subtract(ForgeDirection dir) {

        return subtract(dir.offsetX, dir.offsetY, dir.offsetZ);
    }

    public Vec3i subtract(Vec3i vec) {

        return subtract(vec.x, vec.y, vec.z);
    }

    public Vec3i multiply(int x, int y, int z) {

        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    public Vec3i multiply(int multiplier) {

        return multiply(multiplier, multiplier, multiplier);
    }

    public Vec3i multiply(ForgeDirection direction) {

        return multiply(direction.offsetX, direction.offsetY, direction.offsetZ);
    }

    public Vec3i divide(int x, int y, int z) {

        this.x /= x;
        this.y /= y;
        this.z /= z;
        return this;
    }

    public Vec3i divide(int multiplier) {

        return divide(multiplier, multiplier, multiplier);
    }

    public Vec3i divide(ForgeDirection direction) {

        return divide(direction.offsetX, direction.offsetY, direction.offsetZ);
    }

    public double length() {

        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vec3i normalize() {

        Vec3i v = clone();

        double len = length();

        v.x /= len;
        v.y /= len;
        v.z /= len;

        return v;
    }

    public Vec3i abs() {

        return new Vec3i(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    public double dot(Vec3i v) {

        return x * v.getX() + y * v.getY() + z * v.getZ();
    }

    public Vec3i cross(Vec3i v) {

        return new Vec3i(y * v.getZ() - z * v.getY(), x * v.getZ() - z * v.getX(), x * v.getY() - y * v.getX());
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

        return new Vec3i(x, y, z, w);
    }

    public boolean hasTileEntity() {

        if (hasWorld()) {
            return w.getTileEntity(x, y, z) != null;
        }
        return false;
    }

    public TileEntity getTileEntity() {

        if (hasTileEntity()) {
            return w.getTileEntity(x, y, z);
        }
        return null;
    }

    public boolean isBlock(Block b) {

        return isBlock(b, false);
    }

    public boolean isBlock(Block b, boolean checkAir) {

        if (hasWorld()) {
            Block bl = w.getBlock(x, y, z);

            if (b == null && bl == Blocks.air)
                return true;
            if (b == null && checkAir && bl.getMaterial() == Material.air)
                return true;
            if (b == null && checkAir && bl.isAir(w, x, y, z))
                return true;

            return bl.getClass().isInstance(b);
        }
        return false;
    }

    public int getBlockMeta() {

        if (hasWorld()) {
            return w.getBlockMetadata(x, y, z);
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
            return w.getBlock(x, y, z);

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

        return x;
    }

    @Override
    public int getY() {

        return y;
    }

    @Override
    public int getZ() {

        return z;
    }

    public Vec3i getImmutableCopy() {

        return new ImmutableVec3i(this);
    }

    public int distanceTo(Vec3i vec) {

        return distanceTo(vec.x, vec.y, vec.z);
    }

    public int distanceTo(int x, int y, int z) {

        int dx = x - this.x;
        int dy = y - this.y;
        int dz = z - this.z;
        return dx * dx + dy * dy + dz * dz;
    }

    public void setX(int x) {

        this.x = x;
    }

    public void setY(int y) {

        this.y = y;
    }

    public void setZ(int z) {

        this.z = z;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Vec3i) {
            Vec3i vec = (Vec3i) obj;
            return vec.w == w && vec.x == x && vec.y == y && vec.z == z;
        }
        return false;
    }

    public Vec3 toVec3() {

        return Vec3.createVectorHelper(x, y, z);
    }

    @Override
    public String toString() {

        String s = "Vector3{";
        if (hasWorld())
            s += "w=" + w.provider.dimensionId + ";";
        s += "x=" + x + ";y=" + y + ";z=" + z + "}";
        return s;
    }

    public ForgeDirection toForgeDirection() {

        if (z >= x && z >= y)
            return ForgeDirection.SOUTH;
        if (z <= x && z <= y)
            return ForgeDirection.NORTH;

        if (x >= y && x >= z)
            return ForgeDirection.EAST;
        if (x <= y && x <= z)
            return ForgeDirection.WEST;

        if (y >= x && y >= z)
            return ForgeDirection.UP;
        if (y <= x && y <= z)
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
