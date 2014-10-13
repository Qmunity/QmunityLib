package com.qmunity.lib.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.qmunity.lib.misc.ForgeDirectionUtils;
import com.qmunity.lib.tileentity.IRotatable;
import com.qmunity.lib.tileentity.TileBase;

public abstract class BlockTileBase extends BlockBase implements ITileEntityProvider{

    private int guiId = -1;
    private Class<? extends TileBase> tileEntityClass;
    private boolean isRedstoneEmitter;

    public BlockTileBase(Material material, Class<? extends TileBase> tileEntityClass){
        super(material);
        isBlockContainer = true;
        setTileEntityClass(tileEntityClass);
    }

    public BlockTileBase setGuiId(int guiId){
        this.guiId = guiId;
        return this;
    }

    public int getGuiId(){
        return guiId;
    }

    public BlockTileBase setTileEntityClass(Class<? extends TileBase> tileEntityClass){
        if(tileEntityClass == null) throw new NullPointerException("Entity class can't be null!");
        this.tileEntityClass = tileEntityClass;
        return this;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata){

        try {
            return getTileEntity().newInstance();
        } catch(Exception e) {
            return null;
        }
    }

    /**
     * Fetches the TileEntity Class that goes with the block
     * 
     * @return a .class
     */
    protected Class<? extends TileEntity> getTileEntity(){

        return tileEntityClass;
    }

    public BlockTileBase emitsRedstone(){

        isRedstoneEmitter = true;
        return this;
    }

    @Override
    public boolean canProvidePower(){

        return isRedstoneEmitter;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block){

        super.onNeighborBlockChange(world, x, y, z, block);
        // Only do this on the server side.
        if(!world.isRemote) {
            TileBase tileEntity = (TileBase)world.getTileEntity(x, y, z);
            if(tileEntity != null) {
                tileEntity.onBlockNeighbourChanged();
            }
        }
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5){

        TileEntity te = par1IBlockAccess.getTileEntity(par2, par3, par4);
        if(te instanceof TileBase) {
            TileBase tileBase = (TileBase)te;
            return tileBase.getOutputtingRedstone();
        }
        return 0;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){

        if(player.isSneaking()) {
            return false;
        }

        TileEntity entity = world.getTileEntity(x, y, z);
        if(entity == null || !(entity instanceof TileBase)) {
            return false;
        }

        if(getGuiId() >= 0) {
            if(!world.isRemote) player.openGui(getModInstance(), getGuiId(), world, x, y, z);
            return true;
        }
        return false;
    }

    protected abstract Object getModInstance();

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta){

        if(shouldDropItems()) {
            TileBase tile = (TileBase)world.getTileEntity(x, y, z);
            if(tile != null) {
                for(ItemStack stack : tile.getDrops()) {
                    spawnItemInWorld(world, stack, x + 0.5, y + 0.5, z + 0.5);
                }
            }
        }
        super.breakBlock(world, x, y, z, block, meta);
        world.removeTileEntity(x, y, z);
    }

    private static void spawnItemInWorld(World world, ItemStack itemStack, double x, double y, double z){

        if(world.isRemote) return;
        float dX = world.rand.nextFloat() * 0.8F + 0.1F;
        float dY = world.rand.nextFloat() * 0.8F + 0.1F;
        float dZ = world.rand.nextFloat() * 0.8F + 0.1F;

        EntityItem entityItem = new EntityItem(world, x + dX, y + dY, z + dZ, new ItemStack(itemStack.getItem(), itemStack.stackSize, itemStack.getItemDamage()));

        if(itemStack.hasTagCompound()) {
            entityItem.getEntityItem().setTagCompound((NBTTagCompound)itemStack.getTagCompound().copy());
        }

        float factor = 0.05F;
        entityItem.motionX = world.rand.nextGaussian() * factor;
        entityItem.motionY = world.rand.nextGaussian() * factor + 0.2F;
        entityItem.motionZ = world.rand.nextGaussian() * factor;
        world.spawnEntityInWorld(entityItem);
        itemStack.stackSize = 0;
    }

    protected boolean shouldDropItems(){
        return true;
    }

    /**
     * Method to detect how the block was placed, and what way it's facing.
     */
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack iStack){
        TileEntity te = world.getTileEntity(x, y, z);
        if(te instanceof IRotatable) {
            ((IRotatable)te).setFacingDirection(ForgeDirectionUtils.getDirectionFacing(player, canRotateVertical()).getOpposite());
        }
    }

    protected boolean canRotateVertical(){

        return true;
    }

    @Override
    public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis){

        TileEntity te = worldObj.getTileEntity(x, y, z);
        if(te instanceof IRotatable) {
            IRotatable rotatable = (IRotatable)te;
            ForgeDirection dir = rotatable.getFacingDirection();
            dir = dir.getRotation(axis);
            if(dir != ForgeDirection.UP && dir != ForgeDirection.DOWN || canRotateVertical()) {
                rotatable.setFacingDirection(dir);
                return true;
            }
        }
        return false;
    }
}
