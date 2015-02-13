package uk.co.qmunity.lib.block;

import java.util.ArrayList;
import java.util.List;

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
import uk.co.qmunity.lib.QmunityLib;
import uk.co.qmunity.lib.client.render.RenderMultipart;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartSelectableCustom;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.raytrace.RayTracer;
import uk.co.qmunity.lib.ref.Names;
import uk.co.qmunity.lib.tile.TileMultipart;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMultipart extends BlockContainer {

    public BlockMultipart() {

        super(Material.ground);

        setBlockName(Names.Unlocalized.Blocks.MULTIPART);

        MinecraftForge.EVENT_BUS.register(this);
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

        return retrace(world, x, y, z, new Vec3d(start), new Vec3d(end));
    }

    private QMovingObjectPosition retrace(World world, int x, int y, int z, Vec3d start, Vec3d end) {

        TileMultipart te = get(world, x, y, z);
        if (te == null)
            return null;

        QMovingObjectPosition mop = te.rayTrace(start, end);
        if (mop == null)
            return null;

        Vec3dCube c = mop.getCube().clone().expand(0.001);
        setBlockBounds((float) c.getMinX(), (float) c.getMinY(), (float) c.getMinZ(), (float) c.getMaxX(), (float) c.getMaxY(),
                (float) c.getMaxZ());

        return mop;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
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

        return false;
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

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float x_, float y_, float z_) {

        TileMultipart te = get(world, x, y, z);
        if (te == null)
            return false;

        return te.onActivated(player);
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {

        TileMultipart te = get(world, x, y, z);
        if (te == null)
            return;

        te.onClicked(player);
    }

    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z) {

        QMovingObjectPosition mop = retrace(world, x, y, z, RayTracer.instance().getStartVector(player),
                RayTracer.instance().getEndVector(player));

        if (mop == null || mop.getPart() == null)
            return 1F;

        return (float) mop.getPart().getHardness(player, mop);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {

        blockIcon = null;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {

        ArrayList<ItemStack> l = new ArrayList<ItemStack>();

        TileMultipart te = get(world, x, y, z);
        if (te != null) {
            for (IPart p : te.getParts()) {
                List<ItemStack> d = p.getDrops();
                if (d != null)
                    l.addAll(d);
            }
        }

        return l;
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onDrawHighlight(DrawBlockHighlightEvent event) {

        try {
            if (!(event.player.worldObj.getBlock(event.target.blockX, event.target.blockY, event.target.blockZ) instanceof BlockMultipart))
                return;

            QMovingObjectPosition mop = retrace(event.player.worldObj, event.target.blockX, event.target.blockY, event.target.blockZ,
                    RayTracer.instance().getStartVector(event.player), RayTracer.instance().getEndVector(event.player));
            if (mop == null)
                return;
            if (mop.getPart() == null || !(mop.getPart() instanceof IPartSelectableCustom))
                return;
            if (((IPartSelectableCustom) mop.getPart()).drawHighlight(mop, event.player, event.partialTicks))
                event.setCanceled(true);
        } catch (Exception ex) {
        }
    }
}
