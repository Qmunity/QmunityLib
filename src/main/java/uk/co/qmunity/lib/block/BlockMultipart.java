package uk.co.qmunity.lib.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
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
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.QLModInfo;
import uk.co.qmunity.lib.QmunityLib;
import uk.co.qmunity.lib.client.render.RenderMultipart;
import uk.co.qmunity.lib.helper.ItemHelper;
import uk.co.qmunity.lib.part.IQLPart;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.raytrace.RayTracer;
import uk.co.qmunity.lib.tile.TileMultipart;
import uk.co.qmunity.lib.vec.Cuboid;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMultipart extends BlockContainer {

    public BlockMultipart() {

        super(Material.ground);
        setBlockName(QLModInfo.MODID + ".multipart");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {

        return new TileMultipart();
    }

    public static TileMultipart findTile(IBlockAccess world, int x, int y, int z) {

        TileEntity te = world.getTileEntity(x, y, z);
        if (te == null || !(te instanceof TileMultipart))
            return null;
        return (TileMultipart) te;
    }

    @Override
    public boolean isNormalCube() {

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isBlockNormalCube() {

        return false;
    }

    @Override
    public boolean isOpaqueCube() {

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canRenderInPass(int pass) {

        RenderMultipart.pass = pass;

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {

        return 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {

        return RenderMultipart.RENDER_ID;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderAsNormalBlock() {

        return false;
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end) {

        return retrace(world, x, y, z, start, end);
    }

    private QMovingObjectPosition retrace(World world, int x, int y, int z, EntityPlayer player) {

        return retrace(world, x, y, z, RayTracer.getStartVec(player), RayTracer.getEndVec(player));
    }

    private QMovingObjectPosition retrace(World world, int x, int y, int z, Vec3 start, Vec3 end) {

        TileMultipart te = findTile(world, x, y, z);
        if (te == null)
            return null;
        QMovingObjectPosition mop = te.rayTrace(start, end);
        if (mop == null)
            return null;
        return mop;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {

        TileMultipart te = findTile(world, x, y, z);
        if (te == null)
            return true;
        QMovingObjectPosition mop = retrace(world, x, y, z, QmunityLib.proxy.getPlayer());
        if (mop == null || mop.part == null || mop.part.getParent() != te)
            return true;
        mop.part.addDestroyEffects(mop, effectRenderer);
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addHitEffects(World world, MovingObjectPosition target, EffectRenderer effectRenderer) {

        TileMultipart te = findTile(world, target.blockX, target.blockY, target.blockZ);
        if (te == null)
            return true;
        QMovingObjectPosition mop = retrace(world, target.blockX, target.blockY, target.blockZ, QmunityLib.proxy.getPlayer());
        if (mop == null || mop.part == null || mop.part.getParent() != te)
            return true;
        mop.part.addHitEffects(mop, effectRenderer);
        return true;
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {

        TileMultipart te = findTile(world, x, y, z);
        if (te == null)
            return 0;

        int light = 0;
        for (IQLPart p : te.getParts())
            light = Math.max(light, p.getLightValue());
        return light;
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {

        TileMultipart te = findTile(world, x, y, z);
        QMovingObjectPosition mop = retrace(world, x, y, z, player);

        if (te == null || mop == null) {
            if (world.isRemote)
                return true;
            for (ItemStack stack : getDrops(world, x, y, z, 0, 0))
                ItemHelper.dropItem(world, x, y, z, stack);
            world.setBlockToAir(x, y, z);
            world.removeTileEntity(x, y, z);
            return true;
        }

        if (!world.isRemote)
            mop.part.harvest(player, mop);

        return world.getTileEntity(x, y, z) == null;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB bounds, List l, Entity entity) {

        TileMultipart te = findTile(world, x, y, z);
        if (te == null)
            return;

        List<Cuboid> boxes = new ArrayList<Cuboid>();
        te.addCollisionBoxesToList(boxes, bounds);
        for (Cuboid c : boxes)
            l.add(c.toAABB());
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {

        TileMultipart te = findTile(world, x, y, z);
        if (te == null)
            return;
        for (IQLPart p : te.getParts())
            p.onNeighborBlockChange();
    }

    @Override
    public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {

        TileMultipart te = findTile(world, x, y, z);
        if (te == null)
            return;
        for (IQLPart p : te.getParts())
            p.onNeighborTileChange();
    }

    @Override
    public boolean getWeakChanges(IBlockAccess world, int x, int y, int z) {

        return true;
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {

        TileMultipart te = findTile(world, x, y, z);
        if (te == null)
            return false;

        return te.isSideSolid(side);
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {

        TileMultipart te = findTile(world, x, y, z);
        if (te == null)
            return false;
        return te.canConnectRedstone(ForgeDirection.getOrientation(side >= 0 && side < 4 ? Direction.directionToFacing[side] ^ 1 : 0));
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {

        TileMultipart te = findTile(world, x, y, z);
        if (te == null)
            return 0;
        return te.getWeakRedstoneOutput(ForgeDirection.getOrientation(side ^ 1));
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {

        TileMultipart te = findTile(world, x, y, z);
        if (te == null)
            return 0;
        return te.getStrongRedstoneOutput(ForgeDirection.getOrientation(side ^ 1));
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {

        QMovingObjectPosition mop = retrace(world, x, y, z, QmunityLib.proxy.getPlayer());
        if (mop == null || mop.part == null)
            return null;
        return mop.part.getPickBlock(QmunityLib.proxy.getPlayer(), mop);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float x_, float y_, float z_) {

        QMovingObjectPosition mop = retrace(world, x, y, z, QmunityLib.proxy.getPlayer());
        if (mop == null || mop.part == null)
            return false;
        return mop.part.onActivated(player, mop, player.getCurrentEquippedItem());
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {

        QMovingObjectPosition mop = retrace(world, x, y, z, QmunityLib.proxy.getPlayer());
        if (mop == null || mop.part == null)
            return;
        mop.part.onClicked(player, mop, player.getCurrentEquippedItem());
    }

    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z) {

        QMovingObjectPosition mop = retrace(world, x, y, z, player);
        if (mop == null || mop.part == null)
            return 1 / 100F;
        return 1F / mop.part.getHardness(player, mop);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {

        blockIcon = null;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {

        ArrayList<ItemStack> l = new ArrayList<ItemStack>();

        TileMultipart te = findTile(world, x, y, z);
        if (te != null)
            for (IQLPart p : te.getParts())
                l.addAll(p.getDrops());

        return l;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @SideOnly(Side.CLIENT)
    public void onDrawHighlight(DrawBlockHighlightEvent event) {

        try {
            if (!(event.player.worldObj.getBlock(event.target.blockX, event.target.blockY, event.target.blockZ) instanceof BlockMultipart))
                return;

            QMovingObjectPosition mop = event.target instanceof QMovingObjectPosition ? (QMovingObjectPosition) event.target : retrace(
                    event.player.worldObj, event.target.blockX, event.target.blockY, event.target.blockZ, event.player);
            if (mop == null || mop.part == null)
                return;
            if (mop.part.drawHighlight(mop, event.player, event.partialTicks))
                event.setCanceled(true);
        } catch (Exception ex) {
        }
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random rnd) {

        TileMultipart te = findTile(world, x, y, z);
        if (te != null)
            for (IQLPart part : te.getParts())
                part.randomDisplayTick(rnd);
    }

}
