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
package com.qmunity.lib.vec;

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

public class Vec3d {

    public static final Vec3d center = new Vec3d(0.5, 0.5, 0.5).getImmutableCopy();

    protected double x, y, z;
    protected World w = null;

    public Vec3d(double x, double y, double z) {

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3d(double x, double y, double z, World w) {

        this(x, y, z);
        this.w = w;
    }

    public Vec3d(TileEntity te) {

        this(te.xCoord, te.yCoord, te.zCoord, te.getWorldObj());
    }

    public Vec3d(Vec3 vec) {

        this(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public Vec3d(Vec3 vec, World w) {

        this(vec.xCoord, vec.yCoord, vec.zCoord);
        this.w = w;
    }

    public boolean hasWorld() {

        return w != null;
    }

    public Vec3d add(double x, double y, double z) {

        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vec3d add(ForgeDirection dir) {

        return add(dir.offsetX, dir.offsetY, dir.offsetZ);
    }

    public Vec3d add(Vec3d vec) {

        return add(vec.x, vec.y, vec.z);
    }

    public Vec3d sub(double x, double y, double z) {

        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Vec3d sub(ForgeDirection dir) {

        return sub(dir.offsetX, dir.offsetY, dir.offsetZ);
    }

    public Vec3d sub(Vec3d vec) {

        return sub(vec.x, vec.y, vec.z);
    }

    public Vec3d mul(double x, double y, double z) {

        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    public Vec3d mul(double multiplier) {

        return mul(multiplier, multiplier, multiplier);
    }

    public Vec3d mul(ForgeDirection direction) {

        return mul(direction.offsetX, direction.offsetY, direction.offsetZ);
    }

    public Vec3d multiply(Vec3d v) {

        return mul(v.getX(), v.getY(), v.getZ());
    }

    public Vec3d div(double x, double y, double z) {

        this.x /= x;
        this.y /= y;
        this.z /= z;
        return this;
    }

    public Vec3d div(double multiplier) {

        return div(multiplier, multiplier, multiplier);
    }

    public Vec3d div(ForgeDirection direction) {

        return div(direction.offsetX, direction.offsetY, direction.offsetZ);
    }

    public Vec3d rotate(Quat rotation) {

        Vec3d v = rotation.mul(this);

        x = v.x;
        y = v.y;
        z = v.z;

        return this;
    }

    public Vec3d rotate(double x, double y, double z) {

        Quat rx = new Quat(new Vec3d(1, 0, 0), Math.toRadians(x));
        Quat ry = new Quat(new Vec3d(0, 1, 0), Math.toRadians(y));
        Quat rz = new Quat(new Vec3d(0, 0, 1), Math.toRadians(z));

        rotate(rx.mul(ry.mul(rz)));

        return this;
    }

    private Vec3d rotate(int x, int y, int z, Vec3d center) {

        sub(center).rotate(x, y, z).add(center);
        double mul = 10000000;

        setX(Math.round(getX() * mul) / mul);
        setY(Math.round(getY() * mul) / mul);
        setZ(Math.round(getZ() * mul) / mul);

        return this;
    }

    public Vec3d rotate(ForgeDirection face, Vec3d center) {

        switch (face) {
        case DOWN:
            return this;
        case UP:
            return rotate(0, 0, 2 * 90, center);
        case WEST:
            return rotate(0, 0, -1 * 90, center);
        case EAST:
            return rotate(0, 0, 1 * 90, center);
        case NORTH:
            return rotate(1 * 90, 0, 0, center);
        case SOUTH:
            return rotate(-1 * 90, 0, 0, center);
        default:
            break;
        }

        return this;
    }

    public Vec3d rotateUndo(ForgeDirection face, Vec3d center) {

        switch (face) {
        case DOWN:
            return this;
        case UP:
            return rotate(0, 0, -2 * 90, center);
        case WEST:
            return rotate(0, 0, 1 * 90, center);
        case EAST:
            return rotate(0, 0, -1 * 90, center);
        case NORTH:
            return rotate(-1 * 90, 0, 0, center);
        case SOUTH:
            return rotate(1 * 90, 0, 0, center);
        default:
            break;
        }

        return this;
    }

    public double length() {

        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vec3d normalize() {

        Vec3d v = clone();

        double len = length();

        v.x /= len;
        v.y /= len;
        v.z /= len;

        return v;
    }

    public Vec3d abs() {

        return new Vec3d(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    public double dot(Vec3d v) {

        return x * v.getX() + y * v.getY() + z * v.getZ();
    }

    public Vec3d cross(Vec3d v) {

        return new Vec3d(y * v.getZ() - z * v.getY(), x * v.getZ() - z * v.getX(), x * v.getY() - y * v.getX());
    }

    public Vec3d getRelative(double x, double y, double z) {

        return clone().add(x, y, z);
    }

    public Vec3d getRelative(ForgeDirection dir) {

        return getRelative(dir.offsetX, dir.offsetY, dir.offsetZ);
    }

    public ForgeDirection getDirectionTo(Vec3d vec) {

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
            if (getBlockX() + d.offsetX == vec.getBlockX() && getBlockY() + d.offsetY == vec.getBlockY()
            && getBlockZ() + d.offsetZ == vec.getBlockZ())
                return d;
        return null;
    }

    public boolean isZero() {

        return x == 0 && y == 0 && z == 0;
    }

    @Override
    public Vec3d clone() {

        return new Vec3d(x, y, z, w);
    }

    public boolean hasTileEntity() {

        if (hasWorld()) {
            return w.getTileEntity((int) x, (int) y, (int) z) != null;
        }
        return false;
    }

    public TileEntity getTileEntity() {

        if (hasTileEntity()) {
            return w.getTileEntity((int) x, (int) y, (int) z);
        }
        return null;
    }

    public boolean isBlock(Block b) {

        return isBlock(b, false);
    }

    public boolean isBlock(Block b, boolean checkAir) {

        if (hasWorld()) {
            Block bl = w.getBlock((int) x, (int) y, (int) z);

            if (b == null && bl == Blocks.air)
                return true;
            if (b == null && checkAir && bl.getMaterial() == Material.air)
                return true;
            if (b == null && checkAir && bl.isAir(w, (int) x, (int) y, (int) z))
                return true;

            return bl.getClass().isInstance(b);
        }
        return false;
    }

    public int getBlockMeta() {

        if (hasWorld()) {
            return w.getBlockMetadata((int) x, (int) y, (int) z);
        }
        return -1;
    }

    public Block getBlock() {

        return getBlock(true);
    }

    public Block getBlock(boolean airIsNull) {

        if (hasWorld()) {
            if (airIsNull && isBlock(null, true))
                return null;
            return w.getBlock((int) x, (int) y, (int) z);

        }
        return null;
    }

    public World getWorld() {

        return w;
    }

    public Vec3d setWorld(World world) {

        w = world;

        return this;
    }

    public double getX() {

        return x;
    }

    public double getY() {

        return y;
    }

    public double getZ() {

        return z;
    }

    public int getBlockX() {

        return (int) Math.floor(x);
    }

    public int getBlockY() {

        return (int) Math.floor(y);
    }

    public int getBlockZ() {

        return (int) Math.floor(z);
    }

    public Vec3d getImmutableCopy() {

        return new ImmutableVec3d(this);
    }

    public double distanceTo(Vec3d vec) {

        return distanceTo(vec.x, vec.y, vec.z);
    }

    public double distanceTo(double x, double y, double z) {

        double dx = x - this.x;
        double dy = y - this.y;
        double dz = z - this.z;
        return dx * dx + dy * dy + dz * dz;
    }

    public void setX(double x) {

        this.x = x;
    }

    public void setY(double y) {

        this.y = y;
    }

    public void setZ(double z) {

        this.z = z;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Vec3d) {
            Vec3d vec = (Vec3d) obj;
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

        if (z == 1)
            return ForgeDirection.SOUTH;
        if (z == -1)
            return ForgeDirection.NORTH;

        if (x == 1)
            return ForgeDirection.EAST;
        if (x == -1)
            return ForgeDirection.WEST;

        if (y == 1)
            return ForgeDirection.UP;
        if (y == -1)
            return ForgeDirection.DOWN;

        return ForgeDirection.UNKNOWN;
    }

    public static Vec3d fromString(String s) {

        if (s.startsWith("Vector3{") && s.endsWith("}")) {
            World w = null;
            double x = 0, y = 0, z = 0;
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
                    x = Double.parseDouble(t.split("=")[1]);
                if (t.toLowerCase().startsWith("y"))
                    y = Double.parseDouble(t.split("=")[1]);
                if (t.toLowerCase().startsWith("z"))
                    z = Double.parseDouble(t.split("=")[1]);
            }

            if (w != null) {
                return new Vec3d(x, y, z, w);
            } else {
                return new Vec3d(x, y, z);
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
