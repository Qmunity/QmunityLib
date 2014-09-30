package com.qmunity.lib.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Direction;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.qmunity.lib.QmunityLib;
import com.qmunity.lib.client.render.RenderMultipart;
import com.qmunity.lib.raytrace.QMovingObjectPosition;
import com.qmunity.lib.ref.Names;
import com.qmunity.lib.tile.TileMultipart;
import com.qmunity.lib.vec.Vec3d;
import com.qmunity.lib.vec.Vec3dCube;

public class BlockMultipart extends BlockContainer {

    public BlockMultipart() {

        super(Material.ground);

        setBlockName(Names.Unlocalized.Blocks.MULTIPART);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {

        return new TileMultipart();
    }

    @Override
    public boolean isNormalCube() {

        return false;
    }

    @Override
    public boolean isBlockNormalCube() {

        return false;
    }

    @Override
    public boolean isOpaqueCube() {

        return false;
    }

    @Override
    public boolean canRenderInPass(int pass) {

        RenderMultipart.PASS = pass;

        return true;
    }

    @Override
    public int getRenderBlockPass() {

        return 1;
    }

    @Override
    public int getRenderType() {

        return RenderMultipart.RENDER_ID;
    }

    public static TileMultipart get(IBlockAccess world, int x, int y, int z) {

        TileEntity te = world.getTileEntity(x, y, z);
        if (te == null)
            return null;
        if (!(te instanceof TileMultipart))
            return null;
        return (TileMultipart) te;
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end) {

        TileMultipart te = get(world, x, y, z);
        if (te == null)
            return null;

        QMovingObjectPosition mop = te.rayTrace(new Vec3d(start), new Vec3d(end));
        if (mop == null) {
            if (te.getParts().size() == 0)
                return super.collisionRayTrace(world, x, y, z, start, end);
            return null;
        }

        Vec3dCube c = mop.getCube();
        setBlockBounds((float) c.getMinX(), (float) c.getMinY(), (float) c.getMinZ(), (float) c.getMaxX(), (float) c.getMaxY(), (float) c.getMaxZ());

        return mop;
    }

    @Override
    public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {

        return true;
    }

    @Override
    public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer) {

        return true;
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {

        TileMultipart te = get(world, x, y, z);
        if (te == null)
            return 0;

        return te.getLightValue();
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {

        TileMultipart te = get(world, x, y, z);
        if (te == null || te.getParts().size() == 0) {
            world.removeTileEntity(x, y, z);
            world.setBlockToAir(x, y, z);
            return false;
        }

        return false;// super.removedByPlayer(world, player, x, y, z);
    }

    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer entity) {

        if (!world.isRemote) {
            TileMultipart te = get(world, x, y, z);
            if (te == null)
                return;
            te.removePart(entity);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB bounds, List l, Entity entity) {

        TileMultipart te = get(world, x, y, z);
        if (te == null)
            return;

        List<Vec3dCube> boxes = new ArrayList<Vec3dCube>();
        te.addCollisionBoxesToList(boxes, bounds, entity);
        for (Vec3dCube c : boxes)
            l.add(c.toAABB());
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {

        TileMultipart te = get(world, x, y, z);
        if (te == null)
            return;

        te.onNeighborBlockChange();
    }

    @Override
    public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {

        TileMultipart te = get(world, x, y, z);
        if (te == null)
            return;

        te.onNeighborChange();
    }

    @Override
    public boolean getWeakChanges(IBlockAccess world, int x, int y, int z) {

        return true;
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {

        TileMultipart te = get(world, x, y, z);
        if (te == null)
            return false;

        return te.isSideSolid(side);
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {

        TileMultipart te = get(world, x, y, z);
        if (te == null)
            return 0;

        return te.getStrongOutput(ForgeDirection.getOrientation(side).getOpposite());
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {

        TileMultipart te = get(world, x, y, z);
        if (te == null)
            return 0;

        return te.getWeakOutput(ForgeDirection.getOrientation(side).getOpposite());
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {

        TileMultipart te = get(world, x, y, z);
        if (te == null)
            return false;

        try {
            return te.canConnect(ForgeDirection.valueOf(Direction.directions[side]).getOpposite());
        } catch (Exception ex) {
        }
        return false;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {

        TileMultipart te = get(world, x, y, z);
        if (te == null)
            return null;

        return te.pickUp(QmunityLib.proxy.getPlayer());
    }
}
