package uk.co.qmunity.lib.part;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.vec.IWorldLocation;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Implement this interface on any multipart you want to make. It's recommended to extend {@link PartBase} right away, as it already handles most
 * things but you can implement this interface if you need.
 *
 * @author amadornes
 */
public interface IPart extends IWorldLocation {

    /**
     * Gets the parent {@link ITilePartHolder}, which contains this part and maybe some others, including microblocks.
     */
    public ITilePartHolder getParent();

    /**
     * Sets the parent {@link ITilePartHolder} of this part.
     */
    public void setParent(ITilePartHolder parent);

    /**
     * Gets the unique type identifier for this part.
     */
    public String getType();

    /**
     * Writes the part's data to an NBT tag, which is saved with the game data.
     */
    public void writeToNBT(NBTTagCompound tag);

    /**
     * Reads the part's data from an NBT tag, which was stored in the game data.
     */
    public void readFromNBT(NBTTagCompound tag);

    /**
     * Writes the part's data to an NBT tag, which will be sent to the clients around it.
     */
    public void writeUpdateData(DataOutput buffer, int channel) throws IOException;

    /**
     * Reads the part's data from an NBT tag, which has been sent by the server to the client.
     */
    public void readUpdateData(DataInput buffer, int channel) throws IOException;

    /**
     * Sends a part update to all the surrounding clients.
     */
    public void sendUpdatePacket(int channel);

    /**
     * Gets the itemstack that places this part.
     */
    public ItemStack getItem();

    /**
     * Gets the item that will be picked by the player when looking at the specified {@link QMovingObjectPosition}.
     */
    public ItemStack getPickedItem(QMovingObjectPosition mop);

    /**
     * Gets a list of items this part should drop when broken.
     */
    public List<ItemStack> getDrops();

    /**
     * Breaks this part and spawns its drops in the world.
     */
    public boolean breakAndDrop(EntityPlayer player, QMovingObjectPosition mop);

    /**
     * Gets the hardness of the part depending on the player that's breaking it and where they're looking at.
     */
    public double getHardness(EntityPlayer player, QMovingObjectPosition mop);

    /**
     * Gets the amount of light emitted by this part (0-15).
     */
    public int getLightValue();

    /**
     * Renders the breaking animation of this part at the specified position.
     */
    @SideOnly(Side.CLIENT)
    public boolean renderBreaking(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass, QMovingObjectPosition mop);

    /**
     * Renders this part statically. A tessellator has alredy started drawing. <br>
     * Only called when there's a block/lighting/render update in the chunk this part is in.
     */
    @SideOnly(Side.CLIENT)
    public boolean renderStatic(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass);

    /**
     * Renders this part dynamically (every render tick).
     */
    @SideOnly(Side.CLIENT)
    public void renderDynamic(Vec3d translation, double delta, int pass);

    /**
     * Checks if this part should be rendered statically on the specified pass.
     */
    @SideOnly(Side.CLIENT)
    public boolean shouldRenderOnPass(int pass);

    /**
     * Gets the render bounds for this part.
     */
    @SideOnly(Side.CLIENT)
    public Vec3dCube getRenderBounds();

    /**
     * Checks if the part passed as an argument occludes in any way this part. Return false if it does, true if it doesn't.
     */
    public boolean occlusionTest(IPart part);

}
